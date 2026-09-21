package org;

import org.tuc.Element;
import org.tuc.List;

public class AList implements List {
    MyElement[] elements;
    int tail;

    public AList(int maxSize) {
        elements = new MyElement[maxSize];
        tail = -1;
        Globals.numOfCommands += 2; // 2*Command
    }

    @Override
    public boolean insert(int key, String data) {
        MyElement element = new MyElement(key, data);
        Globals.numOfCommands ++ ; // 1*If and 1*Command
        int index = binarySearch(key); // Find insertion index
        Globals.numOfCommands ++; // 1*Command
        if (tail < elements.length - 1 && index <= tail + 1) {  //There is space left in the array AND the insertion index is within bounds
            Globals.numOfCommands ++; // 1*If
            for (int i = tail; i >= index; i--) {   // Shift elements to the right to make space for the new element
                elements[i + 1] = elements[i];
                Globals.numOfCommands ++; // 1*Command
            }

            elements[index] = element; // Insert the new element
            tail++;
            Globals.numOfCommands += 2; // 2*Command
            return true;
        }
        return false; // List is full or insertion index is out of bounds
    }

    @Override
    public boolean delete(int key) {
        int index = binarySearch(key); // Find index of the element to delete
        Globals.numOfCommands ++; // 1*Command
        if (index <= tail && elements[index].getKey() == key) {
            Globals.numOfCommands ++;
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

    @Override
    public Element search(int key) {
        int index = binarySearch(key); // Find index of the element to search
        Globals.numOfCommands ++; // 1*Command
        if (index <= tail && elements[index].getKey() == key) {
            Globals.numOfCommands ++; // 1*If
            return elements[index]; // Return the found element
        }
        return null; // Element not found
    }

    protected int binarySearch(int key) {
        int low = 0;
        int high = tail;
        Globals.numOfCommands += 2; // 2*Command

        while (low <= high) {
            int mid = low + (high - low)/2;
            //int mid = (high + low)/2;
            Globals.numOfCommands ++; // 1*Command
            Globals.numOfCommands += 2; // 1*If + 1*ElseIf
            if (elements[mid].getKey() == key) {
                Globals.numOfCommands --; // -1*ElseIf
                return mid; // Key found, insert at this position
            } else if (elements[mid].getKey() < key) {
                low = mid + 1; // Continue search in the right half
                Globals.numOfCommands ++;
            } else {
                high = mid - 1; // Continue search in the left half
                Globals.numOfCommands ++;
            }
        }
        return low; // Key not found, return the insertion position
    }
}
