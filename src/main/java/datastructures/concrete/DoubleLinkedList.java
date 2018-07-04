package datastructures.concrete;

import datastructures.interfaces.IList;
import java.util.NoSuchElementException;
import misc.exceptions.EmptyContainerException;
import java.util.Iterator;

/**
 * @author mmb1995
* Note: For more info on the expected behavior of your methods, see
* the source code for IList.
* This class implements a DoubleLinkedList which is a variation on a linked list that maintains
* a list of sequentially related nodes that store references to the previous and next node in the sequence of nodes.
* This class contains public methods for adding to the end of the list, inserting elements, removing elements,
* and getting elements from a specified index.
*/
public class DoubleLinkedList<T> implements IList<T> {
  // You may not rename these fields or change their types.
  // We will be inspecting these in our private tests.
  // You also may not add any additional fields.
  private Node<T> front;
  private Node<T> back;
  private int size;
  
  public DoubleLinkedList() {
      this.front = null;
      this.back = null;
      this.size = 0;
  }
  
  /**
  * Adds the given item to the *end* of this DoubleLinkedList.
  */
  @Override
  public void add(T item) {
      if (front == null) {
          front = new Node<T>(item);
          back = front;
    } else {
          Node<T> newNode = new Node<T>(back, item, null);
          back.next = newNode;
          back = newNode;
    }
      this.size++;
  }
  
  /**
  * Removes and returns the item from the *end* of this DoubleLinkedList.
  *
  * @throws EmptyContainerException if the container is empty and there is no element to remove.
  */
  @Override
  public T remove() {
      if (front == null) {
          throw new EmptyContainerException();
      }
      T data = null;
      if (front.next == null) {
          data = front.data;
          front = null;
      } else {
          data = back.data;
          back = back.prev;
          back.next = null;
      }
      this.size--;
      return data;
  }
  
  /**
  * Returns the item located at the given index.
  *
  * @throws IndexOutOfBoundsException if the index < 0 or index >= this.size()
  */
  @Override
  public T get(int index) {
      if (index < 0 || index > size - 1) {
          throw new IndexOutOfBoundsException();
      }
      Node<T> current = findNode(index);
      return current.data;
  }
  
  /**
  * Overwrites the element located at the given index with the new item.
  *
  * @throws IndexOutOfBoundsException if the index < 0 or index >= this.size()
  */
  @Override
  public void set(int index, T item) {
      if (index < 0 || index >= this.size) {
          throw new IndexOutOfBoundsException();
      }
      insert(index, item);
      delete(index + 1);
  }
  
  /**
  * Inserts the given item at the given index. If there already exists an element
  * at that index, shift over that element and any subsequent elements one index
  * higher.
  *
  * @throws IndexOutOfBoundsException if the index < 0 or index >= this.size() + 1
  */
  @Override
  public void insert(int index, T item) {
      if (index < 0 || index >= this.size() + 1) {
          throw new IndexOutOfBoundsException();
      }
      if (index == size) {
          add(item);
      } else if (index == 0) {
          Node<T> newNode = new Node<T>(null, item, front);
          front.prev = newNode;
          front = newNode;
          size++;
      } else {
          Node<T> current = findNode(index);
          Node<T> previous = current.prev;
          Node<T> newNode = new Node<T>(previous, item, current);
          previous.next = newNode;
          current.prev = newNode;
          size++;
      }
  }
  
  
  /**
  * Deletes the item at the given index. If there are any elements located at a higher
  * index, shift them all down by one.
  *
  * @throws IndexOutOfBoundsException if the index < 0 or index >= this.size()
  */
  @Override
  public T delete(int index) {
      if (size == 0) {
          throw new EmptyContainerException();
      }
      if (index < 0 || index >= this.size()) {
          throw new IndexOutOfBoundsException();
      }
      T data = null;
      if (index == size - 1) {
          return this.remove();
      } else if (index == 0) {
          data = front.data;
          front = front.next;
          front.prev = null;
          size--;
      } else {
          Node<T> current = findNode(index);
          Node<T> previous = current.prev;
          Node<T> nextNode = current.next;
          data = current.data;
          previous.next = current.next;
          nextNode.prev = previous;
          size--;
      }
      return data;
  }
  
  /**
  * Returns the index corresponding to the first occurrence of the given item
  * in the list.
  *
  * If the item does not exist in the list, return -1.
  */
  @Override
  public int indexOf(T item) {
      Node<T> current = front;
      int index = 0;
      while (index < this.size) {
          T data = current.data;
          if (data == item || data.equals(item)) {
            return index;
          }
          current = current.next;
          index++;
      }
      return -1;
  }
  
  /**
  * Returns the number of elements in the DoubleLinkedList.
  */
  @Override
  public int size() {
    return size;
  }
  
  /**
  * Returns 'true' if this container contains the given element, and 'false' otherwise.
  */
  @Override
  public boolean contains(T other) {
    return this.indexOf(other) != -1;
  }
  
  /**
  * Returns the Node at the given index.
  */
  private Node<T> findNode(int index) {
    Node<T> current = new Node<T>(null, null, null);
    if (size - 1 - index >= size - 1 / 2) {
      current = getNodeFromFront(index);
    } else {
      current = getNodeFromBack(size - 1 - index);
    }
    return current;
  }
  
  /**
  * returns the Node located the given amount of steps away from the front of the list.
  */
  private Node<T> getNodeFromFront(int steps) {
    Node<T> current = front;
    int count = 0;
    while (count != steps) {
      current = current.next;
      count++;
    }
    return current;
  }
  
  /**
  * Returns the Node located the given amount of steps from the back of the list.
  */
  private Node<T> getNodeFromBack(int steps) {
    Node<T> current = back;
    int count = 0;
    while (count != steps) {
      current = current.prev;
      count++;
    }
    return current;
  }
  
  @Override
  public Iterator<T> iterator() {
    // Note: we have provided a part of the implementation of
    // an iterator for you. You should complete the methods stubs
    // in the DoubleLinkedListIterator inner class at the bottom
    // of this file. You do not need to change this method.
    return new DoubleLinkedListIterator<>(this.front);
  }
  
  private static class Node<E> {
    // You may not change the fields in this node or add any new fields.
    public final E data;
    public Node<E> prev;
    public Node<E> next;
    
    public Node(Node<E> prev, E data, Node<E> next) {
      this.data = data;
      this.prev = prev;
      this.next = next;
    }
    
    public Node(E data) {
      this(null, data, null);
    }
    
  }
  
  private static class DoubleLinkedListIterator<T> implements Iterator<T> {
    // You should not need to change this field, or add any new fields.
    private Node<T> current;
    
    public DoubleLinkedListIterator(Node<T> current) {
      // You do not need to make any changes to this constructor.
      this.current = current;
    }
    
    /**
    * Returns 'true' if the iterator still has elements to look at;
    * returns 'false' otherwise.
    */
    public boolean hasNext() {
      return current != null;
    }
    
    /**
    * Returns the next item in the iteration and internally updates the
    * iterator to advance one element forward.
    *
    * @throws NoSuchElementException if we have reached the end of the iteration and
    *         there are no more elements to look at.
    */
    public T next() {
      if (current == null) {
        throw new NoSuchElementException();
      }
      T data = current.data;
      current = current.next;
      return data;
    }
  }
}