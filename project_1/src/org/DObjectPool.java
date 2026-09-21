package org;

import org.tuc.ObjectPool;

public class DObjectPool implements ObjectPool {
    protected Node head;
    protected Node tail;
    protected int size;

    public DObjectPool() {
        this.head = null;
        this.tail = null;
        this.size = 0;
        Globals.numOfCommands += 3; // 3*Assignments
    }

    @Override
    public boolean hasFreeObject() {
        Globals.numOfCommands ++; // 1*If
        return head != null;  //if head of Node is null and no Nodes exist in the Object Pool, return false
    }

    @Override
    public void addObject(Object object) {
        Node newNode = (Node) object;  //casting object as Node
        Globals.numOfCommands ++; // 1*NewObject
        if (head == null) { // Case: if Object Pool is empty
            head = newNode;
        }
        else {            // Case: if Object Pool already has Nodes inside
            tail.next = newNode;
        }
        tail = newNode;
        size++;
        Globals.numOfCommands += 5;   // 2*if + 1*(Command In If) + 2*Commands
    }

    @Override
    public Object getObject() {
        Globals.numOfCommands ++; // 1*If
        if (hasFreeObject()) {   //if Object Pool has Objects, return first node
            Node current = head;    // Starting from the Head
            head = head.next;   // Making the next Element the new Head
            Globals.numOfCommands += 3; // 2*Command + 1*If
            if (head == null) { // Case: The new Head is null, The List is empty (set tail to null as well)
                tail = null;
                Globals.numOfCommands ++; // 1*Command
            }
            size--;
            Globals.numOfCommands ++; // 1*Command
            return current;
        }
        return null;
    }

    @Override
    public int length() {
        return size;
    }
}
