import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private Item[] arrLast;
    private Item[] arrFirst;
    private int lastStartIndex;
    private int lastEndIndex;
    private int firstStartIndex;
    private int firstEndIndex;
    private int n;
    // construct an empty deque
    public Deque(){
        arrLast = (Item[]) new Object[2];
        arrFirst = (Item[]) new Object[2];
        n=0;
        lastStartIndex=-1;
        lastEndIndex=-1;
        firstEndIndex=-1;
        firstStartIndex=-1;
    }
    public void print_arrays(){
        System.out.println("arrFirst");
        for (Item item : arrFirst) System.out.println(item);
        System.out.println("arrLast");
        for (Item item : arrLast) System.out.println(item);
    }

    // is the deque empty?
    public boolean isEmpty(){
        return n==0;
    }

    private boolean arrEmpty(boolean fl){
        if(fl){
            return firstStartIndex==-1;
        }
        return lastStartIndex==-1;
    }
    private int arrFirstSize(){
        if(firstEndIndex == -1)
                return 0;
        return firstEndIndex-firstStartIndex+1;
    }
    private int arrLastSize(){
        if(lastStartIndex==-1)
            return 0;
        return lastEndIndex-lastStartIndex+1;
    }

    // return the number of items on the deque
    public int size(){
        return n;
    }

    //Resize the array
    private void resize(boolean fl, int capacity){
        assert capacity>=2;
        Item[] copy = (Item[]) new Object[capacity];
        int i;
        int temp;
        if(fl){
            temp=arrFirstSize()-1;
            for(i=firstStartIndex;i<=firstEndIndex;i++){
                copy[i-firstStartIndex]=arrFirst[i];
            }
            arrFirst=copy;
            firstStartIndex=0;
            firstEndIndex=temp;
        }
        else{
            temp=arrLastSize()-1;
            for(i=lastStartIndex;i<=lastEndIndex;i++){
                copy[i-lastStartIndex]=arrLast[i];
            }
            arrLast=copy;
            lastStartIndex=0;
            lastEndIndex=temp;
        }
    }

    // add the item to the front
    public void addFirst(Item item){
        if(item==null)
            throw new IllegalArgumentException("Cannot add null item to Queue");
        int l=arrFirst.length;
        if(arrFirstSize()==0){
            firstStartIndex=0;
            firstEndIndex=0;
            arrFirst[firstEndIndex]=item;
        }
        else {
            if (firstEndIndex == l - 1)
                resize(true, (firstEndIndex - firstStartIndex+1) * 2);
            arrFirst[++firstEndIndex] = item;
        }
        n=n+1;
    }

    // add the item to the back
    public void addLast(Item item){
        if(item==null)
            throw new IllegalArgumentException("Cannot add null item to Queue");
        int l=arrLast.length;
        if(arrLastSize()==0){
            lastStartIndex=0;
            lastEndIndex=0;
            arrLast[lastEndIndex]=item;
        }
        else {
            if (lastEndIndex == l - 1)
                resize(false, (lastEndIndex - lastStartIndex+1) * 2);
            arrLast[++lastEndIndex] = item;
        }
        n=n+1;
    }

    // remove and return the item from the front
    public Item removeFirst(){
        Item item;
        if(isEmpty())
            throw new java.util.NoSuchElementException("Removing from empty Queue");
        if(arrFirstSize()==0) {
            item=arrLast[lastStartIndex];
            if (lastStartIndex != lastEndIndex) {
                lastStartIndex = lastStartIndex + 1;
            } else {
                lastStartIndex = -1;
                lastEndIndex = -1;
            }
        }
        else {
            item=arrFirst[firstEndIndex];
            if (firstStartIndex != firstEndIndex) {
                firstEndIndex = firstEndIndex - 1;
            } else {
                firstStartIndex = -1;
                firstEndIndex = -1;
            }
        }
        if(arrLastSize()<(arrLast.length/4))
            resize(false, Math.max((arrLast.length / 4), 2));
        if(arrFirstSize()<(arrFirst.length/4))
            resize(true, Math.max((arrFirst.length / 4), 2));

        n=n-1;
        return item;
    }

    // remove and return the item from the back
    public Item removeLast(){
        Item item;
        if(isEmpty())
            throw new java.util.NoSuchElementException("Removing from empty Queue");
        if(arrLastSize()==0) {
            item=arrFirst[firstStartIndex];
            if (firstStartIndex != firstEndIndex) {
                firstStartIndex = firstStartIndex + 1;
            } else {
                firstStartIndex = -1;
                firstEndIndex = -1;
            }
        }
        else {
            item=arrLast[lastEndIndex];
            if (lastStartIndex != lastEndIndex) {
                lastEndIndex = lastEndIndex - 1;
            } else {
                lastStartIndex = -1;
                lastEndIndex = -1;
            }
        }
        if(arrLastSize()<(arrLast.length/4))
            resize(false, Math.max((arrLast.length / 4), 2));
        if(arrFirstSize()<(arrFirst.length/4))
            resize(true, Math.max((arrFirst.length / 4), 2));
        n=n-1;
        return item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new ArrayIterator();
    }

    // an iterator, doesn't implement remove() since it's optional
    private class ArrayIterator implements Iterator<Item> {
        private int i = 0;
        public boolean hasNext()  { return i < n;                               }
        public void remove()      { throw new UnsupportedOperationException("Cannot call remove in iterator");  }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item;
            if(i>=arrFirstSize()) {
                item = arrLast[i-arrFirstSize() + lastStartIndex];
                i=i+1;
                return item;
            }
            return arrFirst[firstEndIndex-i++];
        }
    }

    // unit testing (required)
    public static void main(String[] args){
        Deque<String> dq= new Deque<String>();
        dq.addFirst("hi");
        System.out.println("First start Index"+dq.firstStartIndex);
        System.out.println("First end Index"+dq.firstEndIndex);
        System.out.println("Last start Index"+dq.lastStartIndex);
        System.out.println("Last end Index"+dq.lastEndIndex);
        dq.print_arrays();
        /*
        System.out.println("First start Index"+dq.firstStartIndex);
        System.out.println("First end Index"+dq.firstEndIndex);
        System.out.println("Last start Index"+dq.lastStartIndex);
        System.out.println("Last end Index"+dq.lastEndIndex);
        */
        dq.addFirst("hello");
        System.out.println("First start Index"+dq.firstStartIndex);
        System.out.println("First end Index"+dq.firstEndIndex);
        System.out.println("Last start Index"+dq.lastStartIndex);
        System.out.println("Last end Index"+dq.lastEndIndex);
        dq.print_arrays();
        dq.addLast("welcome");
        System.out.println("First start Index"+dq.firstStartIndex);
        System.out.println("First end Index"+dq.firstEndIndex);
        System.out.println("Last start Index"+dq.lastStartIndex);
        System.out.println("Last end Index"+dq.lastEndIndex);
        dq.print_arrays();
        dq.addFirst("come");
        System.out.println("First start Index"+dq.firstStartIndex);
        System.out.println("First end Index"+dq.firstEndIndex);
        System.out.println("Last start Index"+dq.lastStartIndex);
        System.out.println("Last end Index"+dq.lastEndIndex);
        dq.print_arrays();
        /*
        System.out.println("Remove" + dq.removeLast());
        System.out.println("First start Index"+dq.firstStartIndex);
        System.out.println("First end Index"+dq.firstEndIndex);
        System.out.println("Last start Index"+dq.lastStartIndex);
        System.out.println("Last end Index"+dq.lastEndIndex);
        System.out.println("Size" + dq.n);
        System.out.println("Remove" + dq.removeLast());
        System.out.println("First start Index"+dq.firstStartIndex);
        System.out.println("First end Index"+dq.firstEndIndex);
        System.out.println("Last start Index"+dq.lastStartIndex);
        System.out.println("Last end Index"+dq.lastEndIndex);
        System.out.println("Size" + dq.n);
        System.out.println("Remove" + dq.removeLast());
        System.out.println("First start Index"+dq.firstStartIndex);
        System.out.println("First end Index"+dq.firstEndIndex);
        System.out.println("Last start Index"+dq.lastStartIndex);
        System.out.println("Last end Index"+dq.lastEndIndex);
        System.out.println("Size" + dq.n);
         */
        dq.addFirst("home");
        dq.addFirst("dear");
        // dq.addFirst("welcome1");
        // dq.addFirst("home1");
        System.out.println("First start Index"+dq.firstStartIndex);
        System.out.println("First end Index"+dq.firstEndIndex);
        System.out.println("Last start Index"+dq.lastStartIndex);
        System.out.println("Last end Index"+dq.lastEndIndex);
        System.out.println("Size" + dq.n);
        dq.print_arrays();

        Iterator<String> it = dq.iterator();
        System.out.println("It1 "+it.next());
        System.out.println("It2 "+it.next());
        System.out.println("It3 " + it.next());
        System.out.println("Remove" + dq.removeFirst());
        System.out.println("Remove" + dq.removeFirst());
        System.out.println("Remove" + dq.removeFirst());
        System.out.println("Remove" + dq.removeFirst());
        System.out.println("First start Index"+dq.firstStartIndex);
        System.out.println("First end Index"+dq.firstEndIndex);
        System.out.println("Last start Index"+dq.lastStartIndex);
        System.out.println("Last end Index"+dq.lastEndIndex);
        System.out.println("Size" + dq.n);
        dq.print_arrays();

        it = dq.iterator();
        System.out.println("It1 " + it.next());
        System.out.println("It2 " + it.next());
        System.out.println("It3 " + it.next());
    }

}
