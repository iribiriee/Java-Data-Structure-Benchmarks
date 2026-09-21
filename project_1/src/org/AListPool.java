package org;

public class AListPool extends AList{
    protected AObjectPool pool;

    public AListPool(int maxSize) {
        super(maxSize);
        pool = new AObjectPool(maxSize);
    }

    @Override
    public boolean insert(int key, String data) {
        Globals.numOfCommands ++; // 1*If
        if (pool.hasFreeObject()) {  //if object pool has available elements
            MyElement element = (MyElement) pool.getObject(); /////////////////
            element.setKey(key);
            element.setData(data);
            int index = binarySearch(key); // Find insertion index
            Globals.numOfCommands += 2; // 1*Command and 1*If
            if (tail < elements.length - 1 && index <= tail + 1) {  //There is space left in the array AND the insertion index is within bounds

                for (int i = tail; i >= index; i--) {   // Shift elements to the right to make space for the new element
                    elements[i + 1] = elements[i];
                    Globals.numOfCommands ++; // 1*Command

                }

                elements[index] = element; // Insert the new element
                tail++;
                Globals.numOfCommands += 2; // 2*Command
                return true;
            }
        }
        else {
            MyElement element = new MyElement(key, data);
            Globals.numOfCommands ++ ; // 1*If and 1*Command
            int index = binarySearch(key); // Find insertion index
            Globals.numOfCommands ++; // 1*Command
            if (tail < elements.length - 1 && index <= tail + 1) {  //There is space left in the array AND the insertion index is within bounds
                Globals.numOfCommands++; // 1*If
                for (int i = tail; i >= index; i--) {   // Shift elements to the right to make space for the new element
                    elements[i + 1] = elements[i];
                    Globals.numOfCommands++; // 1*Command
                }

                elements[index] = element; // Insert the new element
                tail++;
                Globals.numOfCommands += 2; // 2*Command
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean delete(int key) {
        int index = binarySearch(key); // Find index of the element to delete
        Globals.numOfCommands += 2; // 1*Command and 1*If
        if (index <= tail && elements[index].getKey() == key) {
            pool.addObject(elements[index]);
            Globals.numOfCommands ++; // 1*Command
            for (int i = index; i < tail; i++) {    // Shift elements to the left to overwrite the deleted element
                elements[i] = elements[i + 1];
                Globals.numOfCommands ++; // 1*Command

            }
            elements[tail] = null; // Set the last element to null
            tail--;
            Globals.numOfCommands += 2; // 2*Command
            return true;
        }
        return false; // Element not found
    }

}
