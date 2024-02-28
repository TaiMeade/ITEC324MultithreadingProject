/**
 * An action that repeatedly removes a greeting from a queue.
 */

public class Consumer implements Runnable {

    int numCount;
    SynchronizedBoundedStack stack;
    private static final int DELAY = 10; // delay between each attempt at a push (milliseconds)

    /**
     * Constructs the consumer object.
     *
     * @param aStack the stack from which to pop greetings
     * @param count the number of elements to pop
      */

    public Consumer(SynchronizedBoundedStack aStack, int count) {
        stack = aStack;
        numCount = count;
    }

    // What the Producer should do when started...pop a bunch of elements from the stack
    public void run() {
        try {
            int i = 1;
            while (i <= numCount) {

                int number = stack.pop();
                System.out.println("Popped: " + number);
                i++;

                Thread.sleep((int) Math.random() * DELAY); // attempt to cause context switches by inserting random delays

            }
        }
        // Required catch statement since I am using the .sleep() method
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
