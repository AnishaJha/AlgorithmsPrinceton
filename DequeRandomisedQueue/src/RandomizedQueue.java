import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] rq;
    private int n;
    private int firstIndex;
    private  int lastIndex;
    // construct an empty randomized queue
    public RandomizedQueue() {
        rq = (Item[]) new Object[2];
        n = 0;
        firstIndex = -1;
        lastIndex = -1;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return  n == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return n;
    }
    private void resize(int capacity) {
        assert capacity >= n;
        Item[] copy = (Item[]) new Object[capacity];
        if (isEmpty())
            return;
        // Shuffle the queue so that it becomes random
        StdRandom.shuffle(rq, firstIndex, lastIndex+1);
        for (int i = 0; i < n; i++) {
                copy[i] = rq[firstIndex+i];
        }
        rq = copy;
        firstIndex = 0;
        lastIndex = n - 1;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Cannot add null item to Randomized Queue");
        }
        if (lastIndex == -1) {
            firstIndex = 0;
            lastIndex = 0;
            rq[lastIndex] = item;
        }
        else {
            if (lastIndex + 1 == rq.length)
                resize(n * 2);
            rq[++lastIndex] = item;
        }
        n++;
    }

    // remove and return a random item
    public Item dequeue() {
        if (size() == 0) {
            throw new NoSuchElementException("Cannot remove from empty queue");
        }
        n--;
        Item item = rq[firstIndex++];
        if (n == 0) {
            firstIndex = -1;
            lastIndex = -1;
            resize(2);
        }
        if (n < rq.length/4 && n > 2)
            resize(rq.length/4);
        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (size() == 0) {
            throw new NoSuchElementException("Cannot sample from empty queue");
        }
        int rnd = StdRandom.uniform(n);
        return rq[firstIndex + rnd];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    // an iterator, doesn't implement remove() since it's optional
    private class RandomizedQueueIterator implements Iterator<Item> {
        private final Item[] rqi;
        private int i;

        public RandomizedQueueIterator() {
            rqi = (Item[]) new Object[size()];
            System.arraycopy(rq, firstIndex, rqi, 0, n);
            StdRandom.shuffle(rqi);
            i = 0;
        }

        public boolean hasNext() {
            return i < rqi.length;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            return rqi[i++];
        }
    }


    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<Integer> intq = new RandomizedQueue<Integer>();
        for (int i = 0; i < 10; i++)
            intq.enqueue(i + 1);
        Iterator<Integer> it = intq.iterator();
        for (int i = 0; i < intq.size(); i++)
            System.out.println("1st Iterator " + it.next());
        Iterator<Integer> it1 = intq.iterator();
        for (int i = 0; i < intq.size(); i++)
            System.out.println("2ndt Iterator " + it1.next());
        System.out.println("Sample" + intq.sample());
        System.out.println("Deque" + intq.dequeue());
        System.out.println("Size" + intq.size());
        System.out.println("Deque" + intq.dequeue());
        intq.enqueue(6);
        System.out.println("Deque" + intq.dequeue());
        Iterator<Integer> it3 = intq.iterator();
        for (int i = 0; i < intq.size(); i++)
            System.out.println("3rd Iterator " + it3.next());

    }

}
