import java.util.EmptyStackException;
import java.util.concurrent.locks.*;

public class SynchronizedBoundedStack extends BoundedStack {
   private Lock stackLock = new ReentrantLock(); // the lock to eliminate race conditions
   private Condition currentlyEmpty = stackLock.newCondition();  // this condition will be used for when the stack is empty
   private Condition currentlyFull = stackLock.newCondition(); // this condition will be used for when the stack is full
   private Condition checkingEquality = stackLock.newCondition(); // this condition will be used while the stack is checking for equality with another stack
   private boolean isEqual; // used to help determine if stacks are equal or not within the "equals()" method
   private int numThreads; // used to identify how many threads are still working during the "equals()" method

    /**
     * Constructor for a new SynchronizedBoundedStack object.
     *
     * @param capacity : The capacity of this stack.
     */

    public SynchronizedBoundedStack(int capacity) {
        super(capacity);
    }

    /**
     * Pushes the provided element onto the top of the stack.
     * @param t : The item to push.
     * @precondition : !isFull().
     */
   public void push(int t) {
        stackLock.lock(); // must lock before the try block
        try {
            // While the stack is full...tell the thread to wait until another thread calls pop() because then the stack will no longer be
            while (isFull()) {
                currentlyFull.await();
            }
            elements[top] = t; // assigns the top element to the value t
            top++; // increments the stack's top variable to ensure the next push is added to the element after the one that was just added by the last push.  This prevents elements from being overwritten

            currentlyEmpty.signal(); // once finished tell the thread that's been waiting the longest that the stack is no longer empty (since an element was just successfully pushed to the top)
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            stackLock.unlock(); // must ALWAYS unlock...so we place it into the finally block
        }
   }

    /**
     * Returns the value on top of the stack.
     * @precondition : !isEmpty().
     * @return : The value on top of the stack.
     */
   public int peek() {
       stackLock.lock(); // must lock before the try block
        try {
            // while the stack is empty...tell the thread to wait until it is signaled by another thread calling the push() method...since pushing an element will cause the stack to no longer be empty
            while (isEmpty()) {
                currentlyEmpty.await();
            }
            // the 'top' of the stack represents the next empty value on a stack....therefore the top - 1 is the top ELEMENT on the stack
            return elements[top - 1];
        }
        // Catch the exception and throw a RuntimeException to the terminal if there is one
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        finally {
            stackLock.unlock(); // must ALWAYS unlock the lock...that is why it is in the finally block
        }
   }

    /**
     * Removes the top value from the stack and returns it.
     * @precondition : !isEmpty().
     * @return : The item popped from the stack.
     */
   public int pop() {
        stackLock.lock(); // must lock before the try block
        try {
            // while the stack is empty this thread should await until it is told that the stack is no longer empty by another thread performing the push() function
            while (isEmpty()) {
                currentlyEmpty.await();
            }
            // Should only ever return an int
            int returnInt = elements[top - 1]; // top - 1 is at the top of the stack (that contains an element)
            elements[top - 1] = 0;
            top--; // decrements the top variable because the top element was removed

            // I think signal() instead of signalAll() because if it was full, then it only has one space open now...because only one element was removed via pop()
            currentlyFull.signal(); // tell the thread waiting for the longest that the stack is no longer full (since pop() removes an element from the stack)

            return returnInt;
        }
        // Catch the exception and throw a RuntimeException to the terminal if there is one
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            stackLock.unlock(); // must ALWAYS unlock...so we place it into the finally block

        }
   }

    /**
     * Returns true if the stack is empty and false otherwise.
     * @return : Whether the stack is empty.
     */
   public boolean isEmpty() {
      if (getSize() == 0) {
         return true; // returns true if the size of the stack is 0...meaning nothing is on the stack at the moment
      }
      return false; // otherwise returns false meaning at least 1 element is on the stack
   }

    /**
     * Returns true if the stack is full, and false otherwise.
     * @return : Whether or not the stack is full.
     */
   public boolean isFull() {
        // Compares the value from the size, to the length of the array (which is its capacity since all elements are initialized to 0)
      if (getSize() == elements.length) {
          return true; // If the size is equal ot the length/capacity...return true because this means that all the elements have a value
      }
      return false; // otherwise return false because this means there is at least one element remaining that has not been assigned
   }

    /**
     * Returns the number of elements currently on the stack.
     * @return : The size of the stack.
     */
   public int getSize() {
      return top; // top represents the next unoccupied index AKA the size of the current size of the stack
   }

    /**
     * Checks to see if this Stack's contents are identical to
     * another stack's contents; this is done by invoking
     * .equals() on the corresponding elements of both stacks.
     * @param other : The stack to compare this stack with.
     * @return : True if this stack's contents are equal to the
     * other stack's contents, and false otherwise.
     */
   public boolean equals(BoundedStack other) {

        //  If the two stacks are not equivalent in size, then it will immediately return false.  This is because they cannot have corresponding equal elements if they are not the same size to begin with.
        if (!(this.getSize() == other.getSize())) {
            return false;
        }

        // Defaults to assuming the stacks are equal
        isEqual = true;

        // The number of threads that this method uses...this variable represents the number of threads that are still "working"
        numThreads = 4;

        // Dividing the stacks into 4 equal groups
        int stackSize = this.getSize();
        int stackGroupSize = Math.floorDiv(stackSize, 4);

        // Checks the first n // 4 elements (Example using a stack with 16 elements...indices: 0 - 3 (first 4 elements)
       Thread thread1 = new Thread(() -> {
           // For each element in this 'chunk'...check that it is equal to the other stack's element at that particular index.  If it is not, then set isEqual to false
            for (int i = 0; i < stackGroupSize; i++) {
                if (elements[i] != other.elements[i]) {
                    isEqual = false;
                }
            }
           stackLock.lock(); // obtain the lock
           try {

               numThreads--; // Decrement the number of threads (this represents the number of threads still working)

               // IF the number of threads still working is 0...then signal to all threads waiting on the checkingEquality condition (should just be the main thread that called the function)
               if (numThreads == 0) {
                   checkingEquality.signalAll();
               }
           }
           finally {
               stackLock.unlock(); // must ALWAYS unlock the lock...which is why this is in the finally block
           }
       });

       // Checks the second group of elements (Example using a stack with 16 elements...indices: 4 - 7)
       Thread thread2 = new Thread(() -> {
           for (int i = stackGroupSize; i < stackGroupSize * 2; i++) {
               if (elements[i] != other.elements[i]) {
                   isEqual = false;
               }
           }
           stackLock.lock(); // obtain the lock
           try {

               numThreads--; // Decrement the number of threads (this represents the number of threads still working)

               // IF the number of threads still working is 0...then signal to all threads waiting on the checkingEquality condition (should just be the main thread that called the function)
               if (numThreads == 0) {
                   checkingEquality.signalAll();
               }
           }
           finally {
               stackLock.unlock(); // must ALWAYS unlock the lock...which is why this is in the finally block
           }
       });

       // Checks the third group of elements (Example using a stack with 16 elements...indices: 8 - 11)
       Thread thread3 = new Thread(() -> {
           for (int i = stackGroupSize * 2; i < stackGroupSize * 3; i++) {
               if (elements[i] != other.elements[i]) {
                   isEqual = false;
               }
           }
           stackLock.lock(); // obtain the lock
           try {

               numThreads--; // Decrements the number of threads by 1 since 1 thread has finished working

               // IF the number of threads still working is 0...then signal to all threads waiting on the checkingEquality condition (should just be the main thread that called the function)
               if (numThreads == 0) {
                   checkingEquality.signalAll();
               }
           }
           finally {
               stackLock.unlock(); // must ALWAYS unlock the lock...which is why this is in the finally block
           }
       });

       // Checks the final/remaining elements (Example using a stack with 16 elements...indices: 12 - 15)
       Thread thread4 = new Thread(() -> {
           for (int i = stackGroupSize * 3; i < getSize(); i++) {
               if (elements[i] != other.elements[i]) {
                   isEqual = false;
               }
           }
           stackLock.lock(); // obtain the lock
           try {

               numThreads--; // Decrements the number of threads "working" by 1

               // IF the number of threads still working is 0...then signal to all threads waiting on the checkingEquality condition (should just be the main thread that called the function)
               if (numThreads == 0) {
                   checkingEquality.signalAll();
               }
           }
           finally {
               stackLock.unlock(); // must ALWAYS unlock the lock...which is why this is in the finally block
           }
       });


       stackLock.lock(); // obtain the lock
       try {
           while (numThreads > 0) {
               // Start all 4 threads.
               thread1.start();
               thread2.start();
               thread3.start();
               thread4.start();
               checkingEquality.await(); // wait until all 4 threads finish
           }
       } catch (InterruptedException e) {
           throw new RuntimeException(e);
       }
       finally {
           stackLock.unlock(); // must ALWAYS unlock the lock...which is why this is in the finally block
       }



/*      ----------------------------------------------------------------------------This is the single-thread implementation----------------------------------------------------------------------------
        // Loops through and compares each element to the other stacks elements...for single threaded environment
        for (int i = 0; i < this.getSize(); i++) {
            if (this.elements[i] != other.elements[i]) {
                return false;
            }
        }
        */

       // If the stacks are not equal then return false...otherwise return true.
       if (isEqual == false) {
           return false;
       }
       return true;
   }
}
