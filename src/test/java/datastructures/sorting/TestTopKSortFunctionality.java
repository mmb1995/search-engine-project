package datastructures.sorting;

import misc.BaseTest;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import misc.Searcher;

import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestTopKSortFunctionality extends BaseTest {
    @Test(timeout=SECOND)
    public void testSimpleUsage() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }

        IList<Integer> top = Searcher.topKSort(5, list);
        assertEquals(5, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(15 + i, top.get(i));
        }
    }
    
    @Test(timeout=SECOND)
    public void testReverseSort() {
    		IList<Integer> list = new DoubleLinkedList<>();
    		for (int i = 20; i > 0; i--) {
    			list.add(i);
    		}
    	
    		IList<Integer> top = Searcher.topKSort(5, list);
    		assertEquals(5, top.size());
    		for (int i = 0; i < top.size(); i++) {
    			assertEquals(16 + i, top.get(i));
    		}
	}
    	
    	@Test(timeout=SECOND)
    	public void testKLargerThanList() {
    		IList<Integer> list = new DoubleLinkedList<>();
    		for (int i = 0; i < 10; i++) {
    			list.add(i);
    		}
    		
    		IList<Integer> top = Searcher.topKSort(20, list);
    		assertEquals(10, top.size());
    		for (int i = 0; i < top.size(); i++) {
    			assertEquals(i, top.get(i));
    		}
    		IList<Integer> list2 = new DoubleLinkedList<>();
    		list2.add(6);
    		IList<Integer> top2 = Searcher.topKSort(5, list2);
    		assertEquals(1, top2.size());
    		
    		IList<Integer> list3 = new DoubleLinkedList<>();
    		for (int i = 20; i > 0; i--) {
    			list3.add(i);
    		}
    		IList<Integer> top3 = Searcher.topKSort(24, list3);
    		assertEquals(20, list3.size());
    		for (int i = 0; i < top3.size(); i++) {
    			assertEquals(i + 1, top3.get(i));
    		}
    }
    	
    	@Test(timeout=SECOND)
    	public void testKSameAsList() {
    		IList<Integer> list = new DoubleLinkedList<>();
    		for (int i = 0; i < 10; i++) {
    			list.add(i);
    		}
    		IList<Integer> top = Searcher.topKSort(10, list);
    		assertEquals(10, top.size());
    		for (int i = 0; i < top.size(); i++) {
    			assertEquals(i, top.get(i));
    		}
    	} 
    	
    	@Test(timeout=SECOND)
    	public void testNegativeKThrowsException() {
    		IList<Integer> list = new DoubleLinkedList<>();
    		list.add(0);
    		
    		try {
    			IList<Integer> top = Searcher.topKSort(-1, list);
    			fail("Expected IllegalArgumentException");
    		} catch(IllegalArgumentException ex) {
    			// Do nothing this is expected
    		}
    	}
    	
    	@Test(timeout=SECOND)
    	public void testTopKSortOnEmptyList() {
    		IList<Integer> list = new DoubleLinkedList<>();
    		IList<Integer> top = Searcher.topKSort(2, list);
    		assertEquals(0, top.size());
    	}
    	
    	@Test(timeout=SECOND)
    	public void testTopKEqualsZero() {
    		IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }

        IList<Integer> top = Searcher.topKSort(0, list);
        assertEquals(0, top.size());
    	}
    	
    	@Test(timeout=SECOND)
    	public void testDuplicates() {
    		IList<Integer> list = new DoubleLinkedList<>();
    		for (int i = 0; i < 5; i++) {
    			for (int j = 0; j < 2; j++) {
    				list.add(i);
    			}
    		}
    		IList<Integer> top = Searcher.topKSort(3, list);
    		assertEquals(3, top.size());
    		assertEquals(3, top.get(0));
    		assertEquals(4, top.get(1));
    		assertEquals(4, top.get(2));
	}	
}
    	

