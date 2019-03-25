/*
    The EliminationBackoffStack is a subclass of LockFreeStack that overrides the push() and pop() methods,
    and adds an EliminationArray field.

    Upon failure of a tryPush() or tryPop() attempt, instead of simply backing off, these methods
    try to use the EliminationArray to exchange values.

    Figure 11.8 and Figure 11.9
*/

import java.util.EmptyStackException;
import java.util.concurrent.TimeoutException;

public class EliminationBackoffStack<T> extends LockFreeStack<T> {

    static final int capacity = 10;
    EliminationArray<T> eliminationArray = new EliminationArray<T>(capacity);
    static ThreadLocal<RangePolicy> policy = new ThreadLocal<>(){
        protected synchronized RangePolicy initialValue(){
            return new RangePolicy(capacity);
        }
    };

    public void push(T value){
        RangePolicy rangePolicy = policy.get();
        Node node = new Node(value);

        while(true){
            if(tryPush(node)){
                return;
            }else try {
                T otherValue = eliminationArray.visit(value, rangePolicy.getRange());

                if(otherValue == null){
                    rangePolicy.recordEliminationSuccess();
                    return;
                }
            }catch (TimeoutException ex){
                rangePolicy.recordEliminationTimeout();
            }
        }
    }

    public T pop() throws EmptyStackException{
        RangePolicy rangePolicy = policy.get();
        while(true){
            Node returnNode = tryPop();
            if(returnNode != null){
                return returnNode.value;
            }else try{
                T otherValue = eliminationArray.visit(null, rangePolicy.getRange());
                if(otherValue != null){
                    rangePolicy.recordEliminationSuccess();
                    return otherValue;
                }
            }catch (TimeoutException ex){
                rangePolicy.recordEliminationTimeout();
            }
        }
    }


}
