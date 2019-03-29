import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class Assignment2LockFreeStack<T> extends  LockFreeStack<T>{


    private static int MAXNODE = 10000000;
    private AtomicReference<MyDescriptor> topDescriptor = new AtomicReference<>();
    private ArrayList<Node> nodes = new ArrayList<>();
    private ArrayList<MyDescriptor> descriptors = new ArrayList<>();
    private AtomicInteger used = new AtomicInteger();

    public Assignment2LockFreeStack(){

        for(int i = 0; i < MAXNODE; i++){
            nodes.add(new Node());
            descriptors.add(new MyDescriptor());
        }
        this.used.set(0);
        MyDescriptor temp = new MyDescriptor();
        temp.setField("head", null);
        temp.setField("numops", 0);
        temp.setField("size", 0);
        topDescriptor.set(temp);
    }

    public void push(T value){
        int pos = used.getAndIncrement();

        while (true){
            MyDescriptor temp = topDescriptor.get();
            MyDescriptor newItem = descriptors.get(pos);

            Node node = nodes.get(pos);
            node.setValue(value);
            node.next = (Node) temp.getFieldValue("head");

            newItem.setField("head", node);
            newItem.setField("numops", (Integer)temp.getFieldValue("numops") + 1);
            newItem.setField("size", (Integer)temp.getFieldValue("size") + 1);

            if(topDescriptor.compareAndSet(temp, newItem)){
                return;
            }

        }
    }

    public T pop() throws EmptyStackException {
        if(topDescriptor.get() == null){
            return null;
        }

        int pos = used.getAndIncrement();

        while (true){
            MyDescriptor temp = topDescriptor.get();
            MyDescriptor newItem = descriptors.get(pos);

            Node tempHead = ((Node) temp.getFieldValue("head")).next;

            newItem.setField("head", tempHead);
            newItem.setField("numops", (Integer)temp.getFieldValue("numops") + 1);
            newItem.setField("size", (Integer)temp.getFieldValue("size") - 1);

            if(topDescriptor.compareAndSet(temp, newItem)){
                return ((Node) temp.getFieldValue("head")).getValue();
            }

        }
    }

    public int getNumOps(){
        return (Integer) topDescriptor.get().getFieldValue("numops");
    }

    public int getSize(){
        return (Integer) topDescriptor.get().getFieldValue("size");
    }


    public class Node{
        private T value;
        Node next;

        public Node(T value){
            this.value = value;
            this.next = null;
        }

        public Node(){

        }

        public T getValue() {
            return value;
        }

        public void setValue(T value) {
            this.value = value;
        }

        public Node getNext() {
            return next;
        }

        public void setNext(Node next) {
            this.next = next;
        }
    }



}
