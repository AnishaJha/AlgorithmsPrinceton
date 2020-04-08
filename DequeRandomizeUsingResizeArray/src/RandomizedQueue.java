import java.util.Iterator;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] arrLast;

    // construct an empty randomized queue
    public RandomizedQueue(){

    }

    // is the randomized queue empty?
    public boolean isEmpty(){
        return true;
    }

    // return the number of items on the randomized queue
    public int size(){
        return -1;
    }

    // add the item
    public void enqueue(Item item){

    }

    // remove and return a random item
    public Item dequeue(){
        return arrLast[0];
    }

    // return a random item (but do not remove it)
    public Item sample(){
        return  arrLast[0];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator(){
        return new ArrayIterator();
    }
    private class ArrayIterator implements Iterator<Item> {
        private int i = 0;
        public boolean hasNext()  { return i < 5;                               }
        public void remove()      { throw new UnsupportedOperationException();  }

        public Item next() {return arrLast[0];}
    }

    // unit testing (required)
    public static void main(String[] args){

    }

}
