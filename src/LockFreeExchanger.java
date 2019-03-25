import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicStampedReference;

/*
    A LockFreeExchanger<T> object permits two threads to exchange values of type T.

    Figure 11.6
*/
public class LockFreeExchanger<T>{
    static final int EMPTY = 0;
    static final int WAITING = 1;
    static final int BUSY = 2;

    AtomicStampedReference<T> slot = new AtomicStampedReference<T>(null, 0);

    public T exchange(T myItem, long timeout, TimeUnit unit) throws TimeoutException {
        long nanos = unit.toNanos(timeout);
        long timeBound = System.nanoTime() + nanos;
        int[] stampHolder = {EMPTY};

        while(true){
            if(System.nanoTime() > timeBound){
                throw new TimeoutException();
            }

            T yrItem = slot.get(stampHolder);
            int stamp = stampHolder[0];

            switch (stamp){
                case EMPTY:
                    if(slot.compareAndSet(myItem, null, EMPTY, WAITING)){
                        while(System.nanoTime() < timeBound){
                            yrItem = slot.get(stampHolder);
                            if(stampHolder[0] == BUSY){
                                slot.set(null, EMPTY);
                                return yrItem;
                            }
                        }
                        if(slot.compareAndSet(myItem, null, WAITING, EMPTY)){
                            throw new TimeoutException();
                        }else{
                            yrItem = slot.get(stampHolder);
                            slot.set(null, EMPTY);
                            return yrItem;
                        }
                    }
                    break;

                case WAITING:
                    if(slot.compareAndSet(yrItem, myItem, WAITING, BUSY)){
                        return yrItem;
                    }
                    break;

                case BUSY:
                    break;

                default:
                    System.out.println("Should not happen");
                    break;

            }

        }
    }


}