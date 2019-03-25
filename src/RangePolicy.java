
/*
    RangePolicy object that records both successful exchanges (as in Line 37) and
    timeout failures.

    We ignore exchanges that fail because the operations do not match (such as push() with push()),
    because they account for a fixed fraction of the exchanges for any given distribution of push() and pop() calls.

    One simple policy is to shrink the range as the number of failures increases and vice versa
*/
class RangePolicy{

    private int range;
    private int currRange;

    public RangePolicy(int range){
        this.range = range;
        this.currRange = range;
    }

    public int getRange(){
        return this.currRange;
    }

    public void recordEliminationSuccess(){
        //Prevent accessing array out of range
        if(currRange < range){
            this.currRange++;
        }
    }

    public void recordEliminationTimeout(){
        //minimum range is 1
        if(currRange > 1){
            this.currRange--;
        }
    }

}