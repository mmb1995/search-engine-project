package datastructures.sorting;

import misc.BaseTest;
import misc.Searcher;

import org.junit.Test;

import datastructures.concrete.ArrayHeap;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import datastructures.interfaces.IPriorityQueue;


/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestSortingStress extends BaseTest {
	
    @Test(timeout=10*SECOND)
    public void testSortingStress() {
        IList<Integer> list = new DoubleLinkedList<Integer>();
        for (int i = 0; i < 500000; i++) {
        		list.add(i);
        }
        
        IList<Integer> top = Searcher.topKSort(250000, list);
        assertEquals(250000, top.size());
    }
    
    protected <T extends Comparable<T>> IPriorityQueue<T> makeInstance() {
        return new ArrayHeap<>();
    }
    
    @Test(timeout=10*SECOND)
    public void arrayHeapInsertMany() {
    		IPriorityQueue<Integer> heap = this.makeInstance();
    		for (int i = 500000; i > 0; i--) {
    			heap.insert(i);
    			assertEquals(i, heap.peekMin());
    		}
    }
    
    @Test(timeout=5*SECOND)
    public void testInsertisEfficient() {
    		IPriorityQueue<Integer> heap = this.makeInstance();
    		for (int i = 0; i < 1000000; i++) {
    			heap.insert(i);
    		}
    		assertEquals(1000000, heap.size());
    		
    		IPriorityQueue<Integer> heap2 = this.makeInstance();
    		for (int i = 1000000; i > 0; i--) {
    			heap2.insert(i);
    			assertEquals(i, heap2.peekMin());
    		}
    		assertEquals(1000000, heap2.size());
    }  
    
    @Test(timeout=5*SECOND)
    public void testStressKLargerThanN() {
    		IList<Integer> list = new DoubleLinkedList<Integer>();
    		for (int i = 0; i < 100000; i++) {
    			list.add(i);
    		}
    		IList<Integer> top = Searcher.topKSort(1000000, list);
    		assertEquals(100000, top.size());
    }
    
	@Test(timeout=10*SECOND)
	public void arrayHeapStressTest() {
		IPriorityQueue<Integer> heap = this.makeInstance();
		for (int i = 500000; i > 0; i--) {
			heap.insert(i);
			assertEquals(i, heap.peekMin());
		}
		assertEquals(500000, heap.size());
        for (int i = 1; i <= 500000; i++) {
			assertEquals(i, heap.removeMin());
		}
		assertEquals(0, heap.size());
	}
}
