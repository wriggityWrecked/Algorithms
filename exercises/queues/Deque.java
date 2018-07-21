
import java.util.Iterator;

/**
 * http://coursera.cs.princeton.edu/algs4/assignments/queues.html
 *
 * Implement a deque.
 *
 * @author devin
 * @param <Item>
 */
public class Deque<Item> implements Iterable<Item> {

    /**
     * Inner class to track nodes.
     */
    private class Node {

        private final Item item;
        private Node previous;
        private Node next;

        /**
         * Construct a node.
         *
         * @param item
         * @param previous
         * @param next
         */
        public Node(Item item, Node previous, Node next) {
            this.item = item;
            this.previous = previous;
            this.next = next;
        }
    }

    private Node first;
    private Node last;
    private int size;

    // construct an empty deque 
    public Deque() {
        first = null;
        last = null;
        size = 0;
    }

    /**
     *
     * @return
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     *
     * @return
     */
    public int size() {
        return size;
    }

    /**
     *
     * @param item
     */
    public void addFirst(Item item) {

        if (item == null) {
            throw new java.lang.IllegalArgumentException();
        }

        size++;

        if (size() == 1) {
            // last equals first....
            first = new Node(item, null, null);
            last = first;
        } else {
            // add the item to the end
            Node oldFirst = first;
            first = new Node(item, null, oldFirst);
            oldFirst.previous = first;
            if (size() == 2) {
                last = oldFirst;
            }
        }
    }

    /**
     *
     * @param item
     */
    public void addLast(Item item) {

        if (item == null) {
            throw new java.lang.IllegalArgumentException();
        }

        size++;

        if (size() == 1) {
            // first equals last....
            last = new Node(item, null, null);
            first = last;
        } else {
            Node oldLast = last;
            last = new Node(item, oldLast, null);
            oldLast.next = last;
            if (size() == 2) {
                first = oldLast;
            }
        }
    }

    /**
     * Remove and return the item from the front
     *
     * @return
     */
    public Item removeFirst() {

        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        }

        size--;
        Item firstItem = first.item;

        if (first.next != null) {
            first = first.next;
            first.previous = null; //don't loiter
        }
        if (isEmpty()) {
            first = null; //don't loiter
            last = null; //don't loiter
        }
        return firstItem;
    }

    /**
     * Remove and return the item from the end
     *
     * @return
     */
    public Item removeLast() {

        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        }

        size--;
        Item lastItem = last.item;

        if (last.previous != null) {
            last = last.previous;
            last.next = null; //don't loiter
        }
        if (isEmpty()) {
            first = null; //don't loiter
            last = null; //don't loiter
        }
        return lastItem;
    }

    @Override
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {

        private Node current = first;

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public Item next() {

            if (!hasNext()) {
                throw new java.util.NoSuchElementException();
            }

            Item item = current.item;
            current = current.next;
            return item;
        }

        @Override
        public void remove() {
            throw new java.lang.UnsupportedOperationException();
        }
    }

    public static void main(String[] args) {
        // unit testing (optional)
        Deque<Integer> intDeque = new Deque<>();
        assert (intDeque.isEmpty());

        intDeque.addFirst(1);
        assert (!intDeque.isEmpty());
        assert (intDeque.size() == 1);
        int i = intDeque.removeFirst();
        assert (i == 1);
        assert (intDeque.isEmpty());

        intDeque.addLast(1);
        assert (!intDeque.isEmpty());
        i = intDeque.removeLast();
        assert (i == 1);
        assert (intDeque.isEmpty());

        intDeque.addLast(1);
        assert (intDeque.size() == 1);
        intDeque.addLast(2);
        assert (intDeque.size() == 2);
        intDeque.addLast(3);
        assert (intDeque.size() == 3);
        intDeque.addLast(4);
        assert (intDeque.size() == 4);

        i = intDeque.removeLast();
        assert (i == 4);
        assert (intDeque.size() == 3);
        i = intDeque.removeLast();
        assert (i == 3);
        assert (intDeque.size() == 2);
        i = intDeque.removeLast();
        assert (i == 2);
        assert (intDeque.size() == 1);
        i = intDeque.removeLast();
        assert (i == 1);
        assert (intDeque.size() == 0);

        intDeque.addFirst(1);
        i = intDeque.removeLast();
        assert (i == 1);

        intDeque.addLast(1);
        i = intDeque.removeFirst();
        assert (i == 1);

        intDeque.addFirst(1);
        assert (intDeque.size() == 1);
        intDeque.addFirst(2);
        assert (intDeque.size() == 2);
        intDeque.addFirst(3);
        assert (intDeque.size() == 3);
        intDeque.addFirst(4);
        assert (intDeque.size() == 4);

        Iterator<Integer> itr = intDeque.iterator();
        while (itr.hasNext()) {
            System.out.println(itr.next());
        }
        i = intDeque.removeFirst();
        i = intDeque.removeFirst();
        i = intDeque.removeFirst();
        i = intDeque.removeFirst();

        intDeque.addLast(1);
        assert (intDeque.size() == 1);
        intDeque.addLast(2);
        assert (intDeque.size() == 2);
        intDeque.addLast(3);
        assert (intDeque.size() == 3);
        intDeque.addLast(4);
        assert (intDeque.size() == 4);

        itr = intDeque.iterator();
        while (itr.hasNext()) {
            System.out.println(itr.next());
        }
    }
}
