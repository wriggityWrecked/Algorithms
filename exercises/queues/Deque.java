
import java.util.Iterator;

/**
 *
 * @author devin
 * @param <Item>
 */
public class Deque<Item> implements Iterable<Item> {

    /**
     *
     */
    private class Node {

        Item item;
        Node previous;
        Node next;

        /**
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
        // add the item to the front

        //TODO case of size 0 or size 1 or size 2?
        Node oldFirst = first;
        first = new Node(item, null, oldFirst);
        if (oldFirst != null) {
            oldFirst.previous = first;
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
        // add the item to the end
        //TODO case of size 0 or size 1 or size 2?
        Node oldLast = last;
        last = new Node(item, oldLast, null);
        if (oldLast != null) {
            oldLast.next = last;
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
        first = first.next;
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
        last = last.previous;
        return lastItem;
    }

    @Override
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {

        Node current = first;

        @Override
        public boolean hasNext() {
            return first != null;
        }

        @Override
        public Item next() {

            if (current == null) {
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
    }
}
