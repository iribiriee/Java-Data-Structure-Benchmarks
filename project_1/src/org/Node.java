package org;

public class Node {
    MyElement element;
    Node next;

    Node(MyElement element) {
        this.element = element;
        this.next = null;
        Globals.numOfCommands += 2; // 2*Command
    }

    public Node getNext() {
        return next;
    }

    public MyElement getElement() {
        return element;
    }

    public void setElement(MyElement element) {
        this.element = element;
    }
}
