package datastructures.sorting;

import static org.junit.Assert.fail;

import misc.BaseTest;
import datastructures.concrete.ArrayHeap;
import datastructures.interfaces.IPriorityQueue;
import misc.exceptions.EmptyContainerException;
import org.junit.Test;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestArrayHeapFunctionality extends BaseTest {
    protected <T extends Comparable<T>> IPriorityQueue<T> makeInstance() {
        return new ArrayHeap<>();
    }

    @Test(timeout=SECOND)
    public void testBasicSize() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.insert(3);
        assertEquals(1, heap.size());
    }
    
    @Test(timeout=SECOND)
    public void testBasicPeek() {
    		IPriorityQueue<Integer> heap = this.makeInstance();
    		heap.insert(3);
    		int result = heap.peekMin();
    		assertEquals(3, result);
    		heap.insert(5);
    		result = heap.peekMin();
    		assertEquals(3, result);
    		assertEquals(2, heap.size());
    		heap.insert(2);
    		result = heap.peekMin();
    		assertEquals(2, result);
    		assertEquals(3, heap.size());
    } 
    
    @Test(timeout=SECOND)
    public void testInsertBasic() {
    		IPriorityQueue<Integer> heap = this.makeInstance();
    		heap.insert(2);
    		heap.insert(5);
    		heap.insert(4);
    		assertEquals(2, heap.peekMin());
    		heap.insert(6);
    		assertEquals(2, heap.peekMin()); 
    		heap.insert(1);
    		assertEquals(1, heap.peekMin());
    		assertEquals(5, heap.size());
    }
    
    @Test(timeout=SECOND) 
    public void testInsertNullThrowsException() {
    		IPriorityQueue<Integer> heap = this.makeInstance();
    		try {
    			heap.insert(null);
    			fail("Expected IllegalArgumentException");
    		} catch (IllegalArgumentException ex) {
    			// do nothing, this is expected behavior
    		}
    }
    
    @Test(timeout=SECOND)
    public void testRemoveMinBasic() {
    		IPriorityQueue<Integer> heap = this.makeInstance();
    		heap.insert(7);
    		heap.insert(6);
    		heap.insert(9);
    		int result = heap.removeMin();
    		assertEquals(6, result);
    		assertEquals(2, heap.size());
    		result = heap.removeMin();
    		assertEquals(7, result);
    		assertEquals(1, heap.size());
    }
    
    @Test(timeout = SECOND)
    public void testInsertMany() {
    		IPriorityQueue<Integer> heap = this.makeInstance();
    		for (int i = 500; i > 0; i--) {
    			heap.insert(i);
    			assertEquals(i, heap.peekMin());
    		}
    		assertEquals(500, heap.size());
    }
    
    @Test(timeout = SECOND)
    public void testInsertAndRemoveMany() {
    		IPriorityQueue<Integer> heap = this.makeInstance();
    		for (int i = 0; i < 500; i++) {
    			heap.insert(i);
    		}
    		assertEquals(500, heap.size());
    		for (int i = 0; i < 500; i++) {
    			int result = heap.removeMin();
    			assertEquals(i, result);
    		}
    		assertEquals(0, heap.size());
    }
    
    @Test(timeout=SECOND)
    public void testRemoveMinOnEmptyThrowsException() {
    		IPriorityQueue<Integer> heap = this.makeInstance();
    		try {
    			heap.removeMin();
    			fail("Expected EmptyContainerException");
    		} catch (EmptyContainerException ex) {
    			// do nothing this is expected 
    		}
    		heap.insert(5);
    		heap.removeMin();
    		try {
    			heap.removeMin();
    			fail("Expected EmptyContainerException");
    		} catch (EmptyContainerException ex) {
    			// do nothing this is expected
    		}
    }
    
    @Test(timeout=SECOND)
    public void testRemoveSingleElement() {
    		IPriorityQueue<Integer> heap = this.makeInstance();
    		heap.insert(7);
    		int result = heap.removeMin();
    		assertEquals(7, result);
    		assertEquals(0, heap.size());
    }
    
    @Test(timeout=SECOND)
    public void testInsertandRemoveNonInts() {
    		IPriorityQueue<Character> heap = this.makeInstance();
    		heap.insert('e');
    		heap.insert('a');
    		heap.insert('f');
    		assertEquals('a', heap.removeMin());
    		assertEquals('e', heap.removeMin());   		
    }
    
    @Test(timeout=SECOND)
    public void testPeekMinOnEmptyThrowsException() {
		IPriorityQueue<Integer> heap = this.makeInstance();
		try {
			heap.peekMin();
			fail("Expected EmptyContainerException");
		} catch (EmptyContainerException ex) {
			// do nothing this is expected 
		}
		heap.insert(5);
		heap.removeMin();
		try {
			heap.peekMin();
			fail("Expected EmptyContainerException");
		} catch (EmptyContainerException ex) {
			// do nothing this is expected
		}
    }
    
    @Test(timeout=SECOND)
    public void testRemoveWithDuplitcates() {
    		IPriorityQueue<Integer> heap = this.makeInstance();
    		for (int i = 5; i > 0; i--) {
    			for (int j = 0; j < 2; j++) {
    				heap.insert(i);
    			}
    		}
    		assertEquals(10, heap.size());
    		for (int i = 1; i <= 5; i++) {
    			for (int j = 0; j < 2; j++) {
    				assertEquals(i, heap.removeMin());
    			}
    		}
    }
    
    @Test(timeout=SECOND)
    public void testNegativeIntegers() {
    		IPriorityQueue<Integer> heap = this.makeInstance();
    		for (int i = -10; i < 0; i++) {
    			heap.insert(i);
    		}
    		for (int i = -10; i < 0; i++) {
    			assertEquals(heap.removeMin(), i);
    		}
    }
    
    @Test(timeout=SECOND)
    public void testInsertandRemoveMixed() {
    		IPriorityQueue<Integer> heap = this.makeInstance();
    		for (int i = 10; i > 0; i--) {
    			heap.insert(i);
    		}
    		heap.removeMin();
    		heap.removeMin();
    		for (int i = 0; i < 5; i++) {
    			heap.insert(i + 10);
    		}
    		assertEquals(heap.removeMin(), 3);
    		heap.insert(2);
    		assertEquals(heap.removeMin(), 2);
    }
}
    
