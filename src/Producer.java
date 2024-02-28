public class Producer implements Runnable {

    int number;
    SynchronizedBoundedStack stack;
    int numCount;
    private static final int DELAY = 10; // delay between each attempt at a push (milliseconds)

    /**
     * Constructs the producer object.
     *
     * @param aNumber the initial number to insert into a stack
     * @param aStack the stack to insert the numbers into
     * @param count the number of numbers to produce
     */

    public Producer(int aNumber, SynchronizedBoundedStack aStack, int count) {
        number = aNumber;
        stack = aStack;
        numCount = count;
    }

    // What the Producer should do when started...push a bunch of elements to the stack
    public void run() {
        try {
            int i = 1;
            while (i <= numCount) {

                stack.push(i + number);
                i++;

                Thread.sleep((int) (Math.random() * DELAY)); // attempt to cause context switches by inserting random delays

            }

        }
        // Required catch statement since I am using the .sleep() method
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
