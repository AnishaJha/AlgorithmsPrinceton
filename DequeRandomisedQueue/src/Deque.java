import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private Node<Item> first;
    private Node<Item> last;
    private int size;

    private static class Node<Item> {
        private Item item;
        private Node<Item> next;
        private Node<Item> prev;
    }

    // construct an empty deque
    public Deque() {
        first = null;
        last = null;
        size = 0;
    }

    private Node<Item> createNode(Item item) {
        Node<Item> newNode = new Node<Item>();
        newNode.item = item;
        newNode.next = null;
        newNode.prev = null;
        return newNode;
    }


    // is the deque empty?
    public boolean isEmpty() {
        return first == null;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Item to be added is null");
        }
        Node<Item> newNode = createNode(item);
        if (first != null) {
            newNode.next = first;
            first.prev = newNode;
            first = newNode;
        }
        else {
            first = newNode;
            last = newNode;
        }
        size = size+1;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Item to be added is null");
        }
        Node<Item> newNode = createNode(item);
        if (last != null) {
            last.next = newNode;
            newNode.prev = last;
            last = last.next;
        }
        else {
            first = newNode;
            last = newNode;
        }
        size = size + 1;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        Item item;
        Node<Item> temp;
        if (isEmpty()) {
            throw new NoSuchElementException("Cannot remove from queue. Queue is empty");
        }
        item = first.item;
        if (first == last) {
            first = null;
            last = null;
        }
        else {
            temp = first;
            first = first.next;
            first.prev = null;
            temp.next = null;
        }
        size = size-1;
        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        Item item;
        Node<Item> temp;
        if (isEmpty()) {
            throw new NoSuchElementException("Cannot remove from queue. Queue is empty");
        }
        item = last.item;
        if (first == last) {
            first = null;
            last = null;
        }
        else {
            temp = last;
            last = last.prev;
            last.next = null;
            temp.prev = null;
        }
        size = size-1;
        return item;

    }

    // return an iterator over items in order from front to back

    public Iterator<Item> iterator() {
        return new LinkedIterator(first);
    }

    // an iterator, doesn't implement remove() since it's optional
    private class LinkedIterator implements Iterator<Item> {
        private Node<Item> current;

        public LinkedIterator(Node<Item> first) {
            current = first;
        }

        public boolean hasNext() {
            return current != null;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<String> dq = new Deque<String>();
        dq.addFirst("hello");
        dq.addLast("hi");
        System.out.println("Remove First " + dq.removeFirst());
        System.out.println("Remove First" + dq.removeFirst());
        dq.addLast("hi");
        dq.addLast("hello");
        System.out.println("Remove First " + dq.removeFirst());
        System.out.println("Remove First" + dq.removeFirst());
        dq.addFirst("hello");
        dq.addFirst("hi");
        System.out.println("Remove Last" + dq.removeLast());
        System.out.println("Remove Last" + dq.removeLast());
        dq.addLast("hello");
        dq.addLast("hi");
        dq.addLast("welcome");
        Iterator<String> it = dq.iterator();
        System.out.println("Iterator next" + it.next());
        System.out.println("Iterator next" + it.next());
        System.out.println("Iterator next" + it.next());
    }

}
