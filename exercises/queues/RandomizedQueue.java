
import java.util.Iterator;
import edu.princeton.cs.algs4.StdRandom;

/**
 * http://coursera.cs.princeton.edu/algs4/assignments/queues.html
 *
 * Implement a randomized queue.
 *
 * @author devin
 * @param <Item>
 */
public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] items;
    private int n;

    /**
     * Construct an empty randomized queue
     */
    public RandomizedQueue() {
        items = (Item[]) new Object[0];
        n = 0;
    }

    public boolean isEmpty() {
        // is the randomized queue empty?
        return n == 0;
    }

    public int size() {
        // return the number of items on the randomized queue
        return n;
    }

    /**
     *
     * @param item
     */
    public void enqueue(Item item) {

        if (item == null) {
            throw new java.lang.IllegalArgumentException();
        }

        // add the item
        //check the size to see if we need to double
        if (n == items.length) {
            //need to copy and double
            resize(n == 0 ? 1 : n * 2);
        }
        items[n++] = item;
    }

    /**
     *
     * @param size
     */
    private void resize(int size) {
        // copy array
        Item[] copy = (Item[]) new Object[size];
        int copyLength = size > items.length ? items.length : size;

        System.arraycopy(items, 0, copy, 0, copyLength);
        items = copy;
    }

    /**
     * Remove and return a random item
     *
     * @return
     */
    public Item dequeue() {

        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        }

        int sample = StdRandom.uniform(0, n);
        Item toReturn = items[sample];

        // replace with last item, no holes in array after remove!
        items[sample] = items[--n];
        items[n] = null; //don't loiter

        //resize if needed 
        if (n > 0 && n == items.length / 4) {
            resize(items.length / 2);
        }
        return toReturn;
    }

    /**
     * Return a random item (but do not remove it)
     *
     * @return
     */
    public Item sample() {

        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        }

        int sample = StdRandom.uniform(0, n);
        return items[sample];
    }

    @Override
    public Iterator<Item> iterator() {
        // return an independent iterator over items in random order
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item> {

        Item[] iteratorItems;
        int iteratorCount = n;

        public RandomizedQueueIterator() {
            iteratorItems = (Item[]) new Object[items.length];
            System.arraycopy(items, 0, iteratorItems, 0, items.length);
        }

        @Override
        public boolean hasNext() {
            return iteratorCount > 0;
        }

        @Override
        public Item next() {
            //check has next
            if (!hasNext()) {
                throw new java.util.NoSuchElementException();
            }

            int sample = StdRandom.uniform(0, iteratorCount);
            Item toReturn = iteratorItems[sample];
            // replace what was sampled with last item
            iteratorItems[sample] = iteratorItems[--iteratorCount];

            return toReturn;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public static void main(String[] args) {
        RandomizedQueue<Integer> intDeque = new RandomizedQueue<>();
        assert (intDeque.isEmpty());

        intDeque.enqueue(1);
        assert (!intDeque.isEmpty());
        int i = intDeque.sample();
        assert (i == 1);
        assert (!intDeque.isEmpty());
        i = intDeque.dequeue();
        assert (i == 1);
        assert (intDeque.isEmpty());

        intDeque.enqueue(1);
        assert (intDeque.size() == 1);
        intDeque.enqueue(2);
        assert (intDeque.size() == 2);
        intDeque.enqueue(3);
        assert (intDeque.size() == 3);
        intDeque.enqueue(4);
        assert (intDeque.size() == 4);

        i = intDeque.dequeue();
        System.out.println(i);
        assert (intDeque.size() == 3);
        i = intDeque.dequeue();
        System.out.println(i);
        assert (intDeque.size() == 2);
        i = intDeque.dequeue();
        System.out.println(i);
        assert (intDeque.size() == 1);
        i = intDeque.dequeue();
        System.out.println(i);
        assert (intDeque.isEmpty());

    }
}
