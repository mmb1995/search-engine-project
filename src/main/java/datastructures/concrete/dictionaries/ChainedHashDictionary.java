package datastructures.concrete.dictionaries;

import datastructures.concrete.KVPair;
import datastructures.interfaces.IDictionary;
import java.util.Iterator;
import java.util.NoSuchElementException;
import misc.exceptions.NoSuchKeyException;

/**
 * @author mmb1995
 * This class implements a ChainedHashDictionary for storing key-value pairs.
 * This class uses a simple hash function and resolves collisions through chaining.
 * This class contains public methods for inserting and removing key-value pairs,
 * and obtaining values by referencing their keys.
 */
public class ChainedHashDictionary<K, V> implements IDictionary<K, V> {
    // You may not change or rename this field: we will be inspecting
    // it using our private tests.
    private IDictionary<K, V>[] chains;
    private int size; 
    
    public ChainedHashDictionary() {
        this.chains = this.makeArrayOfChains(100);
        size = 0;
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain IDictionary<K, V> objects.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private IDictionary<K, V>[] makeArrayOfChains(int size) {
        // Note: You do not need to modify this method.
        // See ArrayDictionary's makeArrayOfPairs(...) method for
        // more background on why we need this method.
        return (IDictionary<K, V>[]) new IDictionary[size];
    }

    /**
     * Returns the value corresponding to the given key.
     *
     * @throws NoSuchKeyException if the dictionary does not contain the given key.
     */
    @Override
    public V get(K key) {
        int index = getHashCode(key);
        if (chains[index] == null) {
    	        throw new NoSuchKeyException();
        }
        return chains[index].get(key);
    }
    
    /**
     * Adds the key-value pair to the dictionary. If the key already exists in the dictionary,
     * replaces its value with the given value.
     */
    @Override
    public void put(K key, V value) {
	// Resizes the ChainedHashDictionary if it gets too full
	if (getLoadFactor() >= .75) {
		resizeArray();
	}
	int index = getHashCode(key);
	if (chains[index] == null) {
		IDictionary<K,V> newDictionary = new ArrayDictionary<K,V>();
		newDictionary.put(key, value);
		chains[index] = newDictionary;
		size++;
	} else { 
		int arraySize = chains[index].size();
		chains[index].put(key, value);
		if (arraySize < chains[index].size()) {
			size++;
		}      		
	}
    }
    
    /**
     * Resizes the array when it becomes too full
     */
    private void resizeArray() {
		IDictionary<K,V>[] largerArray = makeArrayOfChains(chains.length * 2);
		int newSize = 0;
		for (KVPair<K,V> pair : this) {
			int index = Math.abs(pair.getKey().hashCode() % largerArray.length);
			if (largerArray[index] == null) {
				IDictionary<K,V> newDictionary = new ArrayDictionary<K,V>();
				newDictionary.put(pair.getKey(), pair.getValue());
				largerArray[index] = newDictionary;
				newSize++;	
			} else {
				int arraySize = largerArray[index].size();
				largerArray[index].put(pair.getKey(), pair.getValue());
				if (arraySize < largerArray[index].size()) {
					newSize++;
				}
			}		
		}
		this.chains = largerArray;
		this.size = newSize;  		
    }

    /**
     * returns a double representing the amount of elements in the array
     * divided by the length of the array. This is used to determine when the array needs to be resized.
     */
    private double getLoadFactor() {
		return (double)this.size() / chains.length;
    }
    
    /**
     * returns an int representing the index to place the given key
     */
    private int getHashCode(K key) {
		if (key == null) {
			return 0;
		}
		return Math.abs(key.hashCode() % chains.length);
    }

    /**
     * Removes the key-value pair corresponding to the given key from the dictionary.
     * Returns the removed value.
     * @throws NoSuchKeyException if the dictionary does not contain the given key.
     */
    @Override
    public V remove(K key) {
        int index = getHashCode(key);
        if (chains[index]== null) {
    		throw new NoSuchKeyException();
        }
        V value = chains[index].remove(key);
        size--;
        return value;        
    }

    /**
     * Returns 'true' if the dictionary contains the given key and 'false' otherwise.
     */
    @Override
    public boolean containsKey(K key) {
        int index = getHashCode(key);
	    if (chains[index] == null) {
	        return false;
	    } else {
	        return chains[index].containsKey(key);
	    }
    }
    
    /**
     * Returns the number of key-value pairs stored in this dictionary.
     * Note: dictionaries found at each index have their own size variable
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Returns a list of all key-value pairs contained within this dict.
     */
    @Override
    public Iterator<KVPair<K, V>> iterator() {
        // Note: you do not need to change this method
        return new ChainedIterator<>(this.chains);
    }

    /**
     * Implements an iterator for the ChainedHashDictionary
     */
    private static class ChainedIterator<K, V> implements Iterator<KVPair<K, V>> {
        private IDictionary<K, V>[] chains;
        private int index;
        private Iterator<KVPair<K,V>> current;
        private int size;
        
        // flag to indicate when the iterator has no elements left to access
        private boolean finished;

        public ChainedIterator(IDictionary<K, V>[] chains) {
		this.chains = chains;
		this.size = 0;
		this.finished = false;
		for (int i = 0; i < chains.length; i++) {
			if (chains[i] != null) {
				size++;
			}
		}
		if (size == 0) {
			this.finished = true;
		} else {
			// gets reference to first value in the dictionary
			while (chains[index] == null) {
				index++;
			}
			current = chains[index].iterator();
			size--;
		}
        }
        
        /**
         * Indicates if the Dictionary has more elements in it
         */
        @Override
        public boolean hasNext() {
	    return !this.finished;
	}

        /**
        * Returns the next KvPair<K,V> in the dictionary
        * Throws an @NoSuchElementException if there are no
        * elements left in the dictionary.
        */
        @Override 
        public KVPair<K, V> next() { 
		if (!this.hasNext()) {
			throw new NoSuchElementException();
		}
		if (current.hasNext()) {
			KVPair<K,V> result = current.next();
			if (size == 0 && !current.hasNext()) {
				this.finished = true;
			}
			return result;
		} else {
			index++;

			// gets reference to next element in the dictionary
			while (chains[index] == null) {
				this.index++;
			}

			// calls the iterator for the element contained at this index
			this.current = chains[index].iterator();
			this.size--;
			KVPair<K,V> result = current.next();
			if (size == 0 && !this.current.hasNext()) {
				this.finished = true;
			}
			return result;     
            	}
	}
	}
}

