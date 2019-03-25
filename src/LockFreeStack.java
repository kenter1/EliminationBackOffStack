/*
    11.2 Unbound Lock-Free Stack
*/
import java.util.EmptyStackException;
import java.util.concurrent.atomic.AtomicReference;

public class LockFreeStack<T> {
    AtomicReference<Node> top = new AtomicReference<Node>(null);

    protected boolean tryPush(Node node){
        Node oldTop = top.get();
        node.next = oldTop;
        return (top.compareAndSet(oldTop, node));
    }

    public void push(T value){
        Node node = new Node(value);

        while(true){
            if(tryPush(node)){
                return;
            }
        }

    }



    protected Node tryPop() throws EmptyStackException{
        Node oldTop = top.get();
        if(oldTop == null){
            throw new EmptyStackException();
        }

        Node newTop = oldTop.next;
        if(top.compareAndSet(oldTop, newTop)){
            return oldTop;
        }else{
            return null;
        }
    }

    public T pop() throws EmptyStackException {
        while(true){
            Node returnNode = tryPop();
            if(returnNode != null){
                return returnNode.value;
            }
        }
    }

    public class Node{
        public T value;
        public Node next;

        public Node(T value){
            this.value = value;
            this.next = null;
        }
    }


}
