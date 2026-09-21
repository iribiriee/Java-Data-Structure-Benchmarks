package org;

import org.tuc.ObjectPool;

public class AObjectPool implements ObjectPool {
    MyElement[] elements;
    int tail;

    public AObjectPool(int maxSize) {
        elements = new MyElement[maxSize];
        tail = -1;
        Globals.numOfCommands += 2; // 2*Command
    }

    @Override
    public boolean hasFreeObject() {
        Globals.numOfCommands ++; // 1*If
        return elements[0] != null;  //if first element exists there are objects in objects pool
    }

    @Override
    public void addObject(Object object) {
        MyElement elem = (MyElement) object;
        tail++;
        elements[tail] = elem;
        Globals.numOfCommands += 3; // 3*Command
    }

    @Override
    public Object getObject() {
        Globals.numOfCommands ++; // 1*If
        if (hasFreeObject()) {   //if Object Pool has Objects, return first node
            MyElement elem = elements[tail];
            tail--;
            Globals.numOfCommands += 2; // 2*Command
            return elem;
            }
        return null;
    }

    @Override
    public int length() {
        return tail + 1;
    }
}
