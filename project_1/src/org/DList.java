package org;

import org.tuc.Element;
import org.tuc.List;

public class DList implements List {
    protected Node head;
    protected Node tail;

    public DList() {
        this.head = null;
        this.tail = null;
        Globals.numOfCommands += 2; // 2*Command
    }

    @Override
    public boolean insert(int key, String data) {
        MyElement element = new MyElement(key, data);
        Globals.numOfCommands ++ ; // 1*Command

        Node newNode = new Node(element);
        if (head == null) { // Case: if List is empty
            head = newNode;
        }
        else {            // Case: if list already has elements inside
            tail.next = newNode;
        }
        tail = newNode;
        Globals.numOfCommands += 4;   // 2*if + 2*(Commands In If)
        return true;
    }

    @Override
    public boolean delete(int key) {
        if (head == null) {return false;}    // Case: if List is empty
        Globals.numOfCommands ++;

        if (head.element.getKey() == key) { // Case: Deleting Element == Head
            head = head.next;   // Making the next Element the new Head
            Globals.numOfCommands += 3; // 3*If + 1*Command
            if (head == null) { // Case: The new Head is null, The List is empty (set tail to null as well)
                tail = null;
                Globals.numOfCommands ++;
            }
            return true;
        }

        Node current = head;    // Starting from the Head
        Globals.numOfCommands ++;
        while (current.next != null) {  // Looping for (n - 1) Elements in the org.tuc.List (not the Tail)
            Globals.numOfCommands ++; // 1*If
            if (current.next.element.getKey() == key) { // If the next Element is the one we want Deleted
                Globals.numOfCommands ++; // 1*If
                if (current.next == tail) { // If the element to delete is the tail, update tail to the previous node
                    tail = current;
                    Globals.numOfCommands ++; // 1*Command
                }
                current.next = current.next.next;   // Change the next Element with the next.next Element
                Globals.numOfCommands ++; // 1*Command
                return true;
            }
            current = current.next; // Going to the next Element for the loop
            Globals.numOfCommands ++;
        }
        return false;
    }

    @Override
    public Element search(int key) {
        Node current = head;
        Globals.numOfCommands ++; // 1*Command
        while (current != null) {
            Globals.numOfCommands ++; // 1*if
            if (current.element.getKey() == key) {
                return current.element;
            }
            current = current.next;
            Globals.numOfCommands ++;
        }
        return null;
    }
}
