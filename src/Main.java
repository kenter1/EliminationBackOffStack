/*
    Name: Mark Alano
    Due Date: April 3, 2019

    Problem 1 (100 points)

    Modify the lock-free stack from Homework Assignment 1 so that it uses the Elimination Back-Off
    array detailed in Chapter 11 of The Art of Multiprocessor Programming.

    This implementation must remain linearizable and lock-free.
    In your documentation, reason about the correctness and liveness of the new stack. Be sure to
    include detailed analysis of the various linearization points for each stack operation.

    Additionally, perform an experimental evaluation of your approach and compare it against the
    lock-free stack you designed in Programming Assignment 2. Include the results of both stacks in
    your performance graphs. Discuss the results in your documentation. If one version of the stack
    performs better than the other, be sure to explain why.

    Additional Instructions:
    ● In your benchmark tests, vary the number of threads from 1 to 32 and produce graphs where
    you map the total execution time on the y-axis and the number of threads on the x-axis.
    Produce at least 3 different graphs representing different ratios of the invocation of push,
    pop, and size.

    ● In your benchmark tests, the total execution time should begin prior to spawning threads
    and end after all threads complete. All nodes and random bits should be generated before
    the total execution time begins (if necessary).
* */


import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    public static void main(String[] args) {

        //Mode 0 all push, 1 all pop, 2 1:1 push and pop

        int executions = 100000;
        Long start = 0L;
        Long stop = 0L;
        int mode = -1;
        int threadCount = -1;

        AtomicInteger numops = new AtomicInteger();
        numops.set(0);

        try {
            mode = Integer.parseInt(args[0]);
            threadCount = Integer.parseInt(args[1]);
        }catch (Exception e){
            e.printStackTrace();
        }

        if(mode == -1 || threadCount == -1){
            return;
        }

        int stackMode = -1;

        try {
            stackMode =  Integer.parseInt(args[2]);
        }catch (Exception e){
            stackMode = 0;
        }

        if(stackMode != 0 && stackMode != 1){
            System.out.println("Should not happen");
            return;
        }
        Thread[] threads = new Thread[threadCount];

        if (stackMode == 0) {
            EliminationBackoffStack<Integer> a = new EliminationBackoffStack<>();
            switch (mode) {
                case 0:

                    //System.out.println("Start");
                    start = System.nanoTime();
                    for (int x = 0; x < threadCount; x++) {
                        threads[x] = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                while (numops.incrementAndGet() < executions + 1) {
                                    a.push(1);
                                }
                            }
                        });
                        threads[x].start();
                    }

                    for (int x = 0; x < threadCount; x++) {
                        try {
                            threads[x].join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    stop = System.nanoTime();
                    System.out.println(TimeUnit.MILLISECONDS.convert(stop - start, TimeUnit.NANOSECONDS));
                    //System.out.println("Done");
                    break;
                case 1:
                    //Initialize stack with items
                    for (int i = 0; i < executions; i++) {
                        a.push(1);
                    }

                    //System.out.println("Start");
                    start = System.nanoTime();
                    for (int x = 0; x < threadCount; x++) {
                        threads[x] = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                while (numops.incrementAndGet() < executions) {
                                    a.pop();
                                }
                            }
                        });
                        threads[x].start();
                    }

                    for (int x = 0; x < threadCount; x++) {
                        try {
                            threads[x].join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    stop = System.nanoTime();
                    System.out.println(TimeUnit.MILLISECONDS.convert(stop - start, TimeUnit.NANOSECONDS));
                    //System.out.println("Done");

                    break;
                case 2:
                    //Initialize stack with items
                    for (int i = 0; i < executions; i++) {
                        a.push(1);
                    }
                    //Initialize Random values
                    Random random = new Random(System.nanoTime());
                    int[] randomList = new int[executions];
                    for (int i = 0; i < executions; i++) {
                        randomList[i] = random.nextInt(100);
                    }

                    //System.out.println("Start");
                    start = System.nanoTime();
                    for (int x = 0; x < threadCount; x++) {
                        threads[x] = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                while (numops.incrementAndGet() < executions) {
                                    if (randomList[numops.get()] < 50) {
                                        a.push(1);
                                    } else {
                                        a.pop();
                                    }
                                }
                            }
                        });
                        threads[x].start();
                    }

                    for (int x = 0; x < threadCount; x++) {
                        try {
                            threads[x].join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    stop = System.nanoTime();
                    System.out.println(TimeUnit.MILLISECONDS.convert(stop - start, TimeUnit.NANOSECONDS));
                    //System.out.println("Done");

                    break;
            }
        }else {
            Assignment2LockFreeStack<Integer> a = new Assignment2LockFreeStack<>();
            switch (mode) {
                case 0:

                    //System.out.println("Start");
                    start = System.nanoTime();
                    for (int x = 0; x < threadCount; x++) {
                        threads[x] = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                while (numops.incrementAndGet() < executions + 1) {
                                    a.push(1);
                                }
                            }
                        });
                        threads[x].start();
                    }

                    for (int x = 0; x < threadCount; x++) {
                        try {
                            threads[x].join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    stop = System.nanoTime();
                    System.out.println(TimeUnit.MILLISECONDS.convert(stop - start, TimeUnit.NANOSECONDS));
                    //System.out.println("Done");
                    break;
                case 1:
                    //Initialize stack with items
                    for (int i = 0; i < executions; i++) {
                        a.push(1);
                    }

                    //System.out.println("Start");
                    start = System.nanoTime();
                    for (int x = 0; x < threadCount; x++) {
                        threads[x] = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                while (numops.incrementAndGet() < executions) {
                                    a.pop();
                                }
                            }
                        });
                        threads[x].start();
                    }

                    for (int x = 0; x < threadCount; x++) {
                        try {
                            threads[x].join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    stop = System.nanoTime();
                    System.out.println(TimeUnit.MILLISECONDS.convert(stop - start, TimeUnit.NANOSECONDS));
                    //System.out.println("Done");

                    break;
                case 2:
                    //Initialize stack with items
                    for (int i = 0; i < executions; i++) {
                        a.push(1);
                    }
                    //Initialize Random values
                    Random random = new Random(System.nanoTime());
                    int[] randomList = new int[executions + 1];
                    for (int i = 0; i < executions + 1; i++) {
                        randomList[i] = random.nextInt(100);
                    }

                    //System.out.println("Start");
                    start = System.nanoTime();
                    for (int x = 0; x < threadCount; x++) {
                        threads[x] = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                while (numops.incrementAndGet() < executions) {
                                    if (randomList[numops.get()] < 50) {
                                        a.push(1);
                                    } else {
                                        a.pop();
                                    }
                                }
                            }
                        });
                        threads[x].start();
                    }

                    for (int x = 0; x < threadCount; x++) {
                        try {
                            threads[x].join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    stop = System.nanoTime();
                    System.out.println(TimeUnit.MILLISECONDS.convert(stop - start, TimeUnit.NANOSECONDS));
                    //System.out.println("Done");

                    break;
            }
        }



    }






    
}
