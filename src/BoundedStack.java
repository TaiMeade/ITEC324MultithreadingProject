/*
 * You are to implement this abstract class with a Thread safe
 * stack-based implementation. You are to use an array
 * of bounded size to implement your stack.
 *
 * You will be implementing a thread safe implementation
 * of this interface. In particular:
 *
 * - If multiple threads call some combination of push(), pop()
 * and/or peek() at the same time, there should be no race conditions.
 * Use the ReentrantLock class to resolve any race conditions.
 * - If the client checks for a pre-condition prior to calling a
 * method (such as push, peek, or pop), then you must re-check
 * this pre-condition inside of your method using a while loop and the
 * await() method of a Condition object.
 * - If a method completes, and it's completion could benefit waiting
 * threads, then the method should invoke the notifyAll() method of
 * the appropriate Condition object.
 *
 * Refer to the BoundedQueue implementation from class and the textbook
 * for a similar example.
 *
 * Additionally, your implementation of the equals() method must
 * create 4 additional threads each time it is invoked. You should divide the
 * elements of the stack into 4 roughly equal-sized groups, and assign
 * each "chunk" to one of the 4 threads. Each thread will ensure all the items in
 * its assigned chunk are equal. The initial thread on which equals() was invoked
 * should wait for these 4 "worker" threads to finish before compiling the
 * results.
 * Your implementation of equals() should also be efficient (at least in theory).
 * If 4 cores were dedicated towards your program, and the stacks have many items,
 * your implementation of equals() should be substantially faster than a
 * corresponding single-threaded version (for the worst-case inputs).
 */
/**
 * An abstract class that represents a stack of integers.
 * All methods should be implemented in a thread-safe manner.
 */
/*
 * You must extend this class and implement the abstract methods in
 * such a fashion that all race conditions are avoided.
 */
public abstract class BoundedStack {

    /*
    Protected instance fields. Use these in your subclass's implementation.
     */

    /**
     * The maximum number of elements the stack can hold at a time.
     * Initialized in the constructor.
     */
    protected final int capacity;

    /**
     * An array holding the items on the stack.
     * Note that not every slot of elements is used at any given time.
     * Unused elements could have arbitrary values, but this
     * should not matter to the client.
     */
    protected final int[] elements;

    /**
     * Index of the next unoccupied location on the stack.
     */
    protected int top;

    /**
     * Constructor for a new BoundedStack object.
     * @param capacity : The capacity of this stack.
     */
    /*
     Override this constructor, and invoke super(), passing in the
     capacity.
     */
    public BoundedStack(int capacity) {
        this.capacity = capacity;
        this.elements = new int[capacity]; // Defaults to 0 for each element
        this.top = 0;
    }

    /**
     * Pushes the provided element onto the top of the stack.
     * @param t : The item to push.
     * @precondition : !isFull().
     */
    public abstract void push(int t);

    /**
     * Returns the value on top of the stack.
     * @precondition : !isEmpty().
     * @return : The value on top of the stack.
     */
    public abstract int peek();

    /**
     * Removes the top value from the stack and returns it.
     * @precondition : !isEmpty().
     * @return : The item popped from the stack.
     */
    public abstract int pop();

    /**
     * Returns true if the stack is empty and false otherwise.
     * @return : Whether the stack is empty.
     */
    public abstract boolean isEmpty();

    /**
     * Returns true if the stack is full, and false otherwise.
     * @return : Whether or not the stack is full.
     */
    public abstract boolean isFull();

    /**
     * Returns the number of elements currently on the stack.
     * @return : The size of the stack.
     */
    public abstract int getSize();

    /**
     * Checks to see if this Stack's contents are identical to
     * another stack's contents; this is done by invoking
     * .equals() on the corresponding elements of both stacks.
     * @param other : The stack to compare this stack with.
     * @return : True if this stack's contents are equal to the
     * other stack's contents, and false otherwise.
     */
    public abstract boolean equals(BoundedStack other);
}
