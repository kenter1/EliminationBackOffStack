import javax.management.Descriptor;
import javax.management.RuntimeOperationsException;

public class MyDescriptor implements Descriptor {

    private Assignment2LockFreeStack.Node head;
    private int numops;
    private int size;

    @Override
    public Object getFieldValue(String fieldName) throws RuntimeOperationsException {
        if (fieldName.equalsIgnoreCase("head")){
            return this.head;
        }else if(fieldName.equalsIgnoreCase("numops")){
            return this.numops;
        }else if(fieldName.equalsIgnoreCase("size")){
            return this.size;
        }else {
            return null;
        }
    }

    @Override
    public void setField(String fieldName, Object fieldValue) throws RuntimeOperationsException {
        if (fieldName.equalsIgnoreCase("head")){
            this.head = (Assignment2LockFreeStack.Node) fieldValue;
        }else if(fieldName.equalsIgnoreCase("numops")){
            this.numops = (Integer)fieldValue;
        }else if(fieldName.equalsIgnoreCase("size")){
            this.size = (Integer)fieldValue;
        }
    }

    @Override
    public String[] getFields() {
        return new String[0];
    }

    @Override
    public String[] getFieldNames() {
        return new String[0];
    }

    @Override
    public Object[] getFieldValues(String... fieldNames) {
        Object[] result = new Object[fieldNames.length];
        int j = 0;
        for(String i : fieldNames){
            result[j] = getFieldValue(i);
            j++;
        }
        return result;
    }

    @Override
    public void removeField(String fieldName) {

    }

    @Override
    public void setFields(String[] fieldNames, Object[] fieldValues) throws RuntimeOperationsException {
        for(int i = 0; i < fieldNames.length; i++){
            setField(fieldNames[i], fieldValues[i]);
        }
    }

    @Override
    public boolean isValid() throws RuntimeOperationsException {
        return false;
    }

    @Override
    public Object clone() throws RuntimeOperationsException {
        return null;
    }
}
