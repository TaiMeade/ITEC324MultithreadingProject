import java.util.Arrays;
import java.util.Scanner;

public class ThreadTesting {

    private static SynchronizedBoundedStack testStack = new SynchronizedBoundedStack(10); // The stack which some functions will be tested on

    public static void main(String[] args) {

        String userInput = "";

        Scanner input = new Scanner(System.in); // Create the scanner object used to get user input

        // Loop to allow the user to test things multiple times in one session/run of the program and test more than one thing.
        while (!userInput.equalsIgnoreCase("Q")) {

            // Divider
            System.out.println("\n--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

            // Reassign the user's input to an empty string.
            userInput = "";

            // Output the menu to the user
            System.out.println("\nEnter one of the numbers to select what to test ('Q' to exit the program): \n" +
                    "1. Test push(), peek(), and pop() methods (multithreading).\n" +
                    "   NOTES: There are 20 Producers and 10 Consumers in this section.  Only the Consumers (doing the popping) print anything to the terminal.\n" +
                    "          Also, the capacity of the stack they are pushing/popping to/from is only 10 which should open up for more race conditions/exceptions.\n" +
                    "          Both the Consumer and Producer class have random delays between each push/pop as well. This test takes about 1 minute to complete.\n" +
                    "\n2. Test equals method on two stacks with 10000000 elements (will conduct single threaded first...then multithreaded and compare the estimated runtimes). \n" +
                    "   NOTE: The timing mechanism DOES NOT factor in the pushing time...it only considers the time to determine if the stacks are equal.\n" +
                    "\n3. Test the stack works correctly in a single-threaded environment (most basic test).\n");
            userInput = input.nextLine(); // Get the user's input

            // Test the push(), peek(), and pop() methods in a multi-threaded environment by calling the testPushPeekPop() function with the global testStack Object as it's parameter
            if (userInput.equals("1")) {
                testPushPeekPop(testStack);
            }
            // Test/compare the equals method of the stack in a single-threaded environment as well as a multi-threaded environment, and compare the runtimes of each.
            else if (userInput.equals("2")) {

                // Gets an estimate of the time taken in seconds for the stacks to be compared in a single-threaded environment
                long timeTakenSingleThreaded = singleThreadEqualsExample();

                // Gets an estimate of the time taken in seconds for the stacks to be compared in a multi-threaded environment
                long timeTakenMultiThreaded = multiThreadEqualsExample();

                System.out.println("The single-threaded version took approximately: " + timeTakenSingleThreaded + " milliseconds");
                System.out.println("The multi-threaded version took approximately: " + timeTakenMultiThreaded + " milliseconds");
                System.out.println("Therefore, the multi-threaded version took " + (timeTakenSingleThreaded - timeTakenMultiThreaded) + " milliseconds less than the single-threaded version.");
            }
            // Run the basic single-threaded test...very quick test...tests the push(), pop(), and peek() methods in a single-threaded environment
            else if (userInput.equals("3")) {
                basicStackTest();
            }
            else if (userInput.equalsIgnoreCase("Q")) {
                System.out.println("Thank you for trying out my first ever multi-threaded program!");
            }
            // Inform the user that their selection/input was invalid.
            else {
                System.out.println("Invalid option.  Please try again.");
            }

        }

    }


    /**
     * -----------------------------------------------------------------------------------------MENU OPTION #1 FUNCTION-----------------------------------------------------------------------------------------
      */
    // This function is used to test the pushing, peeking, and popping methods of the SynchronizedBoundedStack class.
    public static void testPushPeekPop(SynchronizedBoundedStack stack) {

        final int NUM_COUNT = 10000; // the number of integers to push/pop onto the stack

        // Create 20 Producers (pushing numbers to the stack)
        Runnable run1 = new Producer(1, stack, NUM_COUNT);
        Runnable run2 = new Producer(50, stack, NUM_COUNT);
        Runnable run3 = new Producer(100, stack, NUM_COUNT);
        Runnable run4 = new Producer(150, stack, NUM_COUNT);
        Runnable run5 = new Producer(200, stack, NUM_COUNT);
        Runnable run6 = new Producer(250, stack, NUM_COUNT);
        Runnable run7 = new Producer(300, stack, NUM_COUNT);
        Runnable run8 = new Producer(350, stack, NUM_COUNT);
        Runnable run9 = new Producer(400, stack, NUM_COUNT);
        Runnable run10 = new Producer(450, stack, NUM_COUNT);
        Runnable run11 = new Producer(500, stack, NUM_COUNT);
        Runnable run12 = new Producer(550, stack, NUM_COUNT);
        Runnable run13 = new Producer(600, stack, NUM_COUNT);
        Runnable run14 = new Producer(650, stack, NUM_COUNT);
        Runnable run15 = new Producer(700, stack, NUM_COUNT);
        Runnable run16 = new Producer(750, stack, NUM_COUNT);
        Runnable run17 = new Producer(800, stack, NUM_COUNT);
        Runnable run18 = new Producer(850, stack, NUM_COUNT);
        Runnable run19 = new Producer(900, stack, NUM_COUNT);
        Runnable run20 = new Producer(950, stack, NUM_COUNT);

        // Create 10 Consumers (one per two Producer objects)
        Runnable run21 = new Consumer(stack, 2 * NUM_COUNT);
        Runnable run22 = new Consumer(stack, 2 * NUM_COUNT);
        Runnable run23 = new Consumer(stack, 2 * NUM_COUNT);
        Runnable run24 = new Consumer(stack, 2 * NUM_COUNT);
        Runnable run25 = new Consumer(stack, 2 * NUM_COUNT);
        Runnable run26 = new Consumer(stack, 2 * NUM_COUNT);
        Runnable run27 = new Consumer(stack, 2 * NUM_COUNT);
        Runnable run28 = new Consumer(stack, 2 * NUM_COUNT);
        Runnable run29 = new Consumer(stack, 2 * NUM_COUNT);
        Runnable run30 = new Consumer(stack, 2 * NUM_COUNT);

        // Create all the threads that will be running simultaneously
        Thread thread1 = new Thread(run1);
        Thread thread2 = new Thread(run2);
        Thread thread3 = new Thread(run3);
        Thread thread4 = new Thread(run4);
        Thread thread5 = new Thread(run5);
        Thread thread6 = new Thread(run6);
        Thread thread7 = new Thread(run7);
        Thread thread8 = new Thread(run8);
        Thread thread9 = new Thread(run9);
        Thread thread10 = new Thread(run10);
        Thread thread11 = new Thread(run11);
        Thread thread12 = new Thread(run12);
        Thread thread13 = new Thread(run13);
        Thread thread14 = new Thread(run14);
        Thread thread15 = new Thread(run15);
        Thread thread16 = new Thread(run16);
        Thread thread17 = new Thread(run17);
        Thread thread18 = new Thread(run18);
        Thread thread19 = new Thread(run19);
        Thread thread20 = new Thread(run20);
        Thread thread21 = new Thread(run21);
        Thread thread22 = new Thread(run22);
        Thread thread23 = new Thread(run23);
        Thread thread24 = new Thread(run24);
        Thread thread25 = new Thread(run25);
        Thread thread26 = new Thread(run26);
        Thread thread27 = new Thread(run27);
        Thread thread28 = new Thread(run28);
        Thread thread29 = new Thread(run29);
        Thread thread30 = new Thread(run30);

        // Start all of the threads
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        thread5.start();
        thread6.start();
        thread7.start();
        thread8.start();
        thread9.start();
        thread10.start();
        thread11.start();
        thread12.start();
        thread13.start();
        thread14.start();
        thread15.start();
        thread16.start();
        thread17.start();
        thread18.start();
        thread19.start();
        thread20.start();
        thread21.start();
        thread22.start();
        thread23.start();
        thread24.start();
        thread25.start();
        thread26.start();
        thread27.start();
        thread28.start();
        thread29.start();
        thread30.start();

        // This section peeks at the top element of the stack (only if the stack is not empty) every ten milliseconds...does so for around 45-50 seconds...which is around how long the consumers/producers run (during my tests)
        for (int i = 1; i < 4800; i++) {
            if (!stack.isEmpty()) {
                System.out.println("\nPeek: Top element = " + stack.peek() + "\n");
            }
            try {
                Thread.sleep(10);
            }
            // Handle exceptions that occur when peeking...during my tests there were none.
            catch (Exception e) {
                System.out.println();
                System.out.println("\n\n\n" + e + "-----------------------------------------ERROR------------------------------------------\n\n\n");
            }
        }
    }





    /**
     * -----------------------------------------------------------------------------------------MENU OPTION #2 FUNCTIONS-----------------------------------------------------------------------------------------
     */
    // Compares two stacks using a single thread (this thread/main thread)
    public static long singleThreadEqualsExample() {

        // Create the stacks that will be compared for equality
        SynchronizedBoundedStack testStack1 = new SynchronizedBoundedStack(100000000);
        SynchronizedBoundedStack testStack2 = new SynchronizedBoundedStack(100000000);

        // begins by assuming that they are equal
        boolean stacksEqual = true;

        // push integers from 1 to capacity (100000000) into both of the stacks
        for (int i = 1; i <= testStack1.capacity; i++) {
            testStack1.push(i);
            testStack2.push(i);
        }

        // Inform the user that the pushing of elements has completed
        System.out.println("\n\nSuccessfully pushed 100000000 elements to the stacks.\n" +
                "Comparing them now!");

        // Get the current time in milliseconds...used to estimate running time of the comparison
        long timeBegunComparing = System.currentTimeMillis();

        // Compare each element to the corresponding element in the other stack
        for (int i = 0; i < testStack1.getSize(); i++) {
            // If any element isn't equal then immediately break out of the for loop after assigning false to 'stacksEqual'...representing that the stacks are not equal
            if (testStack1.elements[i] != testStack2.elements[i]) {
                stacksEqual = false;
                break;
            }
        }

        // Gets the current time in milliseconds...used to help estimate the runtime of the comparison of the two stacks
        long timeEndComparing = System.currentTimeMillis();

        // Determines an estimate of the runtime of the equals method (single-threaded version)
        long timeTaken = (timeEndComparing - timeBegunComparing);

        // if the stacks are equal then return the timeTaken to complete the comparison and a small 'success' message
        if (stacksEqual) {
            System.out.println("Single-threaded version complete.");
            return timeTaken;
        }
        // ELSE print out a 'failure' message and return -1
        else {
            System.out.println("The stacks are NOT equal!"); // this line should never occur based on the current implementation of the method.
            return -1;
        }
    }

    // Utilizes my multi-threaded equals() method to compare two stacks much quicker
    public static long multiThreadEqualsExample() {

        // Create the stacks that will be compared for equality
        SynchronizedBoundedStack testStack1 = new SynchronizedBoundedStack(100000000);
        SynchronizedBoundedStack testStack2 = new SynchronizedBoundedStack(100000000);

        // begins by assuming that they are equal
        boolean stacksEqual = true;

        // push integers from 1 to capacity (100000000) into both of the stacks
        for (int i = 1; i <= testStack1.capacity; i++) {
            testStack1.push(i);
            testStack2.push(i);
        }

        // Output a success message to the user and inform them that the multi-threaded comparison has begun
        System.out.println("\n\nSuccessfully pushed 100000000 elements to the stacks.\n" +
                "Comparing them now!");

        // Get the current time in milliseconds...used to help estimate the amount of time the multi-threaded equals() method takes to run
        long timeBegunComparing = System.currentTimeMillis();

        // Officially begin the multi-threaded comparison of the two stacks
        testStack1.equals(testStack2);

        // Gets the current time in milliseconds...used to help estimate the amount of time the multi-threaded equals() method takes to complete
        long timeEndComparing = System.currentTimeMillis();

        // Calculates the time it took to compare all elements (multi-threaded)
        long timeTaken = (timeEndComparing - timeBegunComparing);

        // If the stacks are equal...print out a small 'success' message to the user and return the estimated runtime of
        if (stacksEqual) {
            System.out.println("Multi-threaded version complete.\n");
            return timeTaken;
        }
        // ELSE print out a 'failure' message and return -1...representing failure
        else {
            System.out.println("The stacks are NOT equal!"); // this line should never occur based on the current implementation of the method.
            return -1;
        }
    }





    /**
     * -----------------------------------------------------------------------------------------MENU OPTION #3 FUNCTION-----------------------------------------------------------------------------------------
     */
    public static void basicStackTest() {

        // Create the stack that things will be pushed/popped/peeked from
        SynchronizedBoundedStack tempStack = new SynchronizedBoundedStack(15);

        System.out.println("\nStack with a max capacity of " + tempStack.capacity + " elements created successfully.\n");
        System.out.println("The stack is empty? " + tempStack.isEmpty());
        System.out.println("Current size of the stack: " + tempStack.getSize());
        System.out.println("\nPushing 3 to the stack...");

        // Push one element to the stack
        tempStack.push(3);

        // Test peek()
        System.out.println("\nPeek at the top element: " + tempStack.peek());
        System.out.println("\nFilling up the stack...");

        // Push elements to the stack
        tempStack.push(14);
        tempStack.push(12);
        tempStack.push(16);
        tempStack.push(111);
        tempStack.push(101);
        tempStack.push(3);
        tempStack.push(5);
        tempStack.push(16);
        tempStack.push(47);
        tempStack.push(87);
        tempStack.push(95);
        tempStack.push(23);
        tempStack.push(17);
        tempStack.push(21);

        // Check to see if the isFull() works properly
        System.out.println("The stack is full? " + tempStack.isFull());
        System.out.println("Current size of the stack: " + tempStack.getSize());

        // Test pop() method
        System.out.println("\nPopping an element: " + tempStack.pop());
        System.out.println("The stack is full? " + tempStack.isFull());
        System.out.println("Current size of the stack: " + tempStack.getSize());

    }

}
