package datastructures.concrete;

import datastructures.interfaces.IPriorityQueue;
import misc.exceptions.EmptyContainerException;


/**
 * @author mmb1995
 * See IPriorityQueue for details on what each method must do.
 * Implements an ArrayHeap. This class is a min 4-heap.
 * This class implements public methods for inserting and removing elements along with
 * peeking at the minimum element in the heap.
 */
public class ArrayHeap<T extends Comparable<T>> implements IPriorityQueue<T> {
    // See spec: you must implement a 4-heap.
    // This heap is not currently set up to be an n-heap, so changing this value will lead to errors
    // and unpredictable behavior.
    private static final int NUM_CHILDREN = 4;

    // You MUST use this field to store the contents of your heap.
    // You may NOT rename this field: we will be inspecting it within
    // our private tests.
    private T[] heap;

    // Feel free to add more fields and constants.
    private int size;

    public ArrayHeap() {
        this.size = 0;
        this.heap = this.makeArrayOfT(20);
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain elements of type T.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private T[] makeArrayOfT(int arraySize) {
        // This helper method is basically the same one we gave you
        // in ArrayDictionary and ChainedHashDictionary.
        //
        // As before, you do not need to understand how this method
        // works, and should not modify it in any way.
        return (T[]) (new Comparable[arraySize]);
    }

    /**
     * Removes and return the smallest element in the queue.
     *
     * If two elements within the queue are considered "equal"
     * according to their compareTo method, this method may break
     * the tie arbitrarily and return either one.
     *
     * @throws EmptyContainerException  if the queue is empty
     */
    @Override
    public T removeMin() {
        if (this.size == 0) {
            throw new EmptyContainerException();
		} else if (this.size == 1) {
			T result = heap[1];
			size--;
			return result;
		} else {
			T result = heap[1];
			heap[1] = heap[size];
			size--;
			percolateDown();
			return result;
		}	
    }
  
    /**
     * Returns, but does not remove, the smallest element in the queue.
     *
     * This method must break ties in the same way the removeMin
     * method breaks ties.
     *
     * @throws EmptyContainerException  if the queue is empty
     */
    @Override
    public T peekMin() {
        if (this.size == 0) {
			throw new EmptyContainerException();
		}
        return heap[1];
    }
    
    /**
     * Inserts the given item into the queue.
     *
     * @throws IllegalArgumentException  if the item is null
     */
    @Override
    public void insert(T item) {
		if (item == null) {
			throw new IllegalArgumentException();
		} else if (this.size == 0) {
			heap[1] = item;
			this.size++;
		} else {
			if (this.size == heap.length - 1) {
				resizeHeap();
			}
			this.size++;
			int parent = (this.size + 2)/4;
			if (item.compareTo(heap[parent]) >= 0) {
				heap[size] = item;		
			} else {
				percolateUp(item, parent, size);
			}
		}
    }
    
    // Helper method for maintaining heap structure during remove operations
    // "down-heap"
    private void percolateDown() {
		int parent = 1;
		int indexSmallestChild = findSmallestChild(parent);
		while (heap[parent].compareTo(heap[indexSmallestChild]) > 0) {
			T item = heap[parent];
			heap[parent] = heap[indexSmallestChild];
			heap[indexSmallestChild] = item;
			parent = indexSmallestChild;
			indexSmallestChild = findSmallestChild(parent);
		}
    }
    
    // Helper method to locate index of given parents smallest child
    private int findSmallestChild(int parent) {
		int firstChild = parent * 4 - 2;
		if (firstChild > size) {
			return parent;
		}
		int minIndex = firstChild;
		int lastChild = parent * 4 + 1;
	   	if (lastChild > size) {
			lastChild = this.size;
		}
		for (int i = firstChild + 1; i <= lastChild; i++) {
			if (heap[i].compareTo(heap[minIndex]) <= 0) {
				 minIndex = i;
			}
		}
		return minIndex;
    }
     
    // Resizes the ArrayHeap
    private void resizeHeap() {
		T[] newHeap = makeArrayOfT(this.size() * 2);
		for (int i = 1; i < heap.length; i++) {
			newHeap[i] = heap[i];
		}
		heap = newHeap;
    }
    
    // Helper method for maintaining heap structure during insertion operations
    // "up-heap"
    private void percolateUp(T item, int parent, int child) {
		while (parent != 0) {
			if (item.compareTo(heap[parent]) < parent) {
				T copy = heap[parent];
				heap[parent] = item;
				heap[child] = copy;
			}
			child = parent;
			parent = (parent + 2) / 4;
		} 			  		
    }
 
    /**
     * Returns the number of elements contained within this heap.
     */
    @Override
    public int size() {
        return this.size;
    }
}
