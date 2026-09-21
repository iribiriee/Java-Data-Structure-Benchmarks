package org;

public class DListPool extends DList {
    protected DObjectPool pool;

    public DListPool() {
        super();
        pool = new DObjectPool();
    }

    @Override
    public boolean insert(int key, String data) {
        Globals.numOfCommands ++; // 1*If
        if (pool.hasFreeObject()) {  //if object pool has available nodes
            Node newNode = (Node) pool.getObject();
            newNode.element.setKey(key);
            newNode.element.setData(data);
            Globals.numOfCommands += 2; // 2*Command
            if (head == null) { // Case: if List is empty
                head = newNode;
            }
            else {            // Case: if list already has elements inside
                tail.next = newNode;
            }
            tail = newNode;
        }
        else {
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
        }
        Globals.numOfCommands += 4;   // 2*if + 2*(Commands In If)
        return true;
    }

    @Override
    public boolean delete(int key) {
        if (head == null) {return false;}    // Case: if List is empty
        Globals.numOfCommands += 2; // 2*If

        if (head.element.getKey() == key) { // Case: Deleting Element == Head
            pool.addObject(head);
            head = head.next;   // Making the next Element the new Head
            Globals.numOfCommands += 3; // 2*Command and 1*If
            if (head == null) { // Case: The new Head is null, The List is empty (set tail to null as well)
                tail = null;
                Globals.numOfCommands ++;
            }
            return true;
        }

        Node current = head;    // Starting from the Head
        Globals.numOfCommands ++;

        while (current.next != null) {  // Looping for (n - 1) Elements in the org.tuc.List (not the Tail)
            Globals.numOfCommands += 2; //1*Command while and 1*If

            if (current.next.element.getKey() == key) { // If the next Element is the one we want Deleted
                pool.addObject(current.next);   //current - current.next//
                Globals.numOfCommands += 2; //1*Command and 1*If
                if (current.next == tail) { // If the element to delete is the tail, update tail to the previous node
                    tail = current;
                    Globals.numOfCommands ++;

                }
                current.next = current.next.next;   // Change the next Element with the next.next Element
                Globals.numOfCommands ++;

                return true;
            }
            current = current.next; // Going to the next Element for the loop
            Globals.numOfCommands ++;
        }
        return false;
    }

}
