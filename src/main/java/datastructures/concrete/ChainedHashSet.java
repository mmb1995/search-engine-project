package datastructures.concrete;

import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.ISet;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author mmb1995
 * See ISet for more details on what each method is supposed to do.
 * Implements a ChainedHashSet that stores unique key-value pairs
 */
public class ChainedHashSet<T> implements ISet<T> {
    private IDictionary<T, Boolean> map;

    public ChainedHashSet() {
        this.map = new ChainedHashDictionary<>();
    }
    
    // Adds the given item to the ChainedHashSet if it doesn't already exist
    @Override
    public void add(T item) {
        if (!map.containsKey(item)) {
            map.put(item, true);
        }
    }
    
    // Removes the given item from the ChainedHashSet. Throws @NoSuchElementException if the item doesn't exist.
    @Override
    public void remove(T item) {
        if (!map.containsKey(item)) {
        		throw new NoSuchElementException();
        }
        map.remove(item);
    }
    
    // Returns True if the ChainedHashSet contains the given item and false otherwise
    @Override
    public boolean contains(T item) {
       return map.containsKey(item);
    }
    
    // Returns the number of elements in the ChainedHashSet
    @Override
    public int size() {
        return map.size();
    }
    
    // This class implements an Iterator for the ChainedHashSet
    @Override
    public Iterator<T> iterator() {
        return new SetIterator<>(this.map.iterator());
    }

    private static class SetIterator<T> implements Iterator<T> {
        // This should be the only field you need
        private Iterator<KVPair<T, Boolean>> iter;

        public SetIterator(Iterator<KVPair<T, Boolean>> iter) {
            this.iter = iter;
        }

        @Override
        public boolean hasNext() {
            return iter.hasNext();
        }

        @Override
        public T next() {
            if (!iter.hasNext()) {
    			throw new NoSuchElementException();
    		}
        return iter.next().getKey();
        }
    }
}
