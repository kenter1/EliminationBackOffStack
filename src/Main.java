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


public class Main {

    public static void main(String[] args) {
        EliminationBackoffStack<Integer> a = new EliminationBackoffStack<>();

        a.push(12);
        a.push(14);

        System.out.println(a.pop());
        System.out.println(a.pop());
    }






    
}
