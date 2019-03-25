/*
    An EliminationArray is implemented as an array of Exchanger objects of
    maximal size capacity.

    Figure 11.7
*/

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class EliminationArray<T> {
    private static final int duration = 1;
    LockFreeExchanger<T>[] exchanger;
    Random random;

    public EliminationArray(int capacity){
        exchanger = (LockFreeExchanger<T>[]) new LockFreeExchanger[capacity];
        for(int i = 0; i < capacity; i++){
            exchanger[i] = new LockFreeExchanger<T>();
        }
        random = new Random();
    }

    public T visit(T value, int range) throws TimeoutException{
        int slot = random.nextInt(range);
        return (exchanger[slot].exchange(value, duration, TimeUnit.MILLISECONDS));
    }

}
