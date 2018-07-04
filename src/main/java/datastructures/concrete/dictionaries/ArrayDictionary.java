package datastructures.concrete.dictionaries;

import datastructures.concrete.KVPair;
import datastructures.interfaces.IDictionary;
import java.util.Iterator;
import java.util.NoSuchElementException;
import misc.exceptions.NoSuchKeyException;

/**
 * @author mmb1995
* A data structure for storing a bunch of key-pair values. All keys are unique
*/
public class ArrayDictionary<K, V> implements IDictionary<K, V> {
  private Pair<K, V>[] pairs;
  private int size; // number of elements in the ArrayDictionary
  
  // You're encouraged to add extra fields (and helper methods) though!
  
  public ArrayDictionary() {
      this.pairs = this.makeArrayOfPairs(20); // initial length is arbitrary
      this.size = 0;
  }
  
  /**
  * This method will return a new, empty array of the given size
  * that can contain Pair<K, V> objects.
  *
  * Note that each element in the array will initially be null.
  */
  @SuppressWarnings("unchecked")
  private Pair<K, V>[] makeArrayOfPairs(int arraySize) {
    // It turns out that creating arrays of generic objects in Java
    // is complicated due to something known as 'type erasure'.
    //
    // We've given you this helper method to help simplify this part of
    // your assignment. Use this helper method as appropriate when
    // implementing the rest of this class.
    //
    // You are not required to understand how this method works, what
    // type erasure is, or how arrays and generics interact. Do not
    // modify this method in any way.
    return (Pair<K, V>[]) (new Pair[arraySize]);
    
  }
  
  /**
  * Returns the value corresponding to the given key.
  *
  * @throws NoSuchKeyException if the dictionary does not contain the given key.
  */
  @Override
  public V get(K key) {
      int index = findIndex(key);
      if (index == -1) {
          throw new NoSuchKeyException();
      }
      return pairs[index].value;
  }
  
  /**
  * Adds the key-value pair to the dictionary. If the key already exists in the dictionary,
  * replace its value with the given one.
  */
  @Override
  public void put(K key, V value) {
	  int index = findIndex(key);
	  if (index != -1) {
		  pairs[index].value = value;
	  } else {
	      // checks if array needs to be resized
		  if (this.size == pairs.length) {
			  Pair<K, V>[] copy = pairs;
			  pairs = this.makeArrayOfPairs(size * 2);
			  for (int i = 0; i < size; i++) {
				  pairs[i] = copy[i];
			  }
		  }
      pairs[size] = new Pair<K, V>(key, value);
      size++;
    }
  }
  
  /**
  * Remove the key-value pair corresponding to the given key from the dictionary.
  *
  * @throws NoSuchKeyException if the dictionary does not contain the given key.
  */
  @Override
  public V remove(K key) {
      int index = findIndex(key);
      if (index == -1) {
          throw new NoSuchKeyException();
      }
      V value = pairs[index].value;
      for (int i = index; i < size - 1; i++) {
          pairs[i] = pairs[i + 1];
      }
      size--;
      return value;
  }
  
  /**
  * Returns 'true' if the dictionary contains the given key and 'false' otherwise.
  */
  @Override
  public boolean containsKey(K key) {
      return findIndex(key) != -1;
  }
  
  /**
  * Returns the number of key-value pairs stored in this dictionary.
  */
  @Override
  public int size() {
      return size;
  }
  
  /**
  * returns the index of the given key. Returns -1 if key is not found in the dictionary.
  */
  private int findIndex(K key) {
      for (int i = 0; i < this.size; i++) {
          
          // Null is considered a valid potential key value 
          if (key == null || pairs[i].key == null) {
              if (pairs[i].key == key) {
                  return i;
              }
          
          // returns the index if the key exists
          } else if (pairs[i].key == key || pairs[i].key.equals(key)) {
              return i;
          }
      }
      return -1;
  }
  

  /**
   * Returns a list of all key-value pairs contained within this dict.
   */
  public Iterator<KVPair<K, V>> iterator() {
	  return new ArrayIterator<K,V>(this.pairs);
  }
  
  
  /**
  *  An object for storing a key and its paired value
  */
  private static class Pair<K, V> {
      public K key;
      public V value;
    
    // You may add constructors and methods to this class as necessary.
      public Pair(K key, V value) {
          this.key = key;
          this.value = value;
      }
    
      /**
      * Returns a string representation of the key-value pair.
      */
      @Override
      public String toString() {
        return this.key + "=" + this.value;
      }
  }
  
  private static class ArrayIterator<K, V> implements Iterator<KVPair<K,V>> {
	  private Pair<K,V>[] pairs;
	  private int index;
	  
	  public ArrayIterator(Pair<K,V>[] pairs) {
		  this.pairs = pairs;
		  index = 0;
	  }
	  
	  /**
	   * Indicates if the dictionary has another element
	   */
	  public boolean hasNext() {
		  return pairs[index] != null;
	  }
	  
	  /** 
	   * Returns the next KVPair in the dictionary
	   * throws an @NoSuchElementException if there are no
	   * more elements in the dictionary.
	   */
	  public KVPair<K,V> next() {
		  if (pairs[index] == null) {
			  throw new NoSuchElementException();
		  }
		  KVPair<K, V> result = new KVPair<K,V>(pairs[index].key, pairs[index].value);
		  index++;
		  return result;
	  }
  }
}
