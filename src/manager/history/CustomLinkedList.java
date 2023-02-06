package manager.history;

import java.util.ArrayList;
import java.util.List;

public class CustomLinkedList<T> {

    class Node<E> {

        public E data;
        public Node<E> next;
        public Node<E> prev;

        public Node(Node<E> prev, E data, Node<E> next) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }
    }

    private Node<T> tail;
    private Node<T> head;
    private int size = 0;

    public void removeNode(Node<T> node) {
        Node<T> oldNext = node.next;
        Node<T> oldPrev = node.prev;
        if (node.prev == null && node.next == null) { // если нода единственная
            node.data = null;
            head = tail = null;
            size--;
        } else if (node.prev == null) { // если удаляем голову
            oldNext.prev = null;
            head = node.next;
            node.data = null;
            node.next = null;
            size--;
        } else if (node.next == null) { // если удаляем хвост
            oldPrev.next = null;
            tail = node.prev;
            node.data = null;
            node.prev = null;
            size--;
        } else {
            oldNext.prev = node.prev;
            oldPrev.next = node.next;
            node.next = node.prev = null;
            node.data = null;
            size--;
        }
    }

    public Node<T> linkLast(T task) {
        final Node<T> oldTail = tail;
        final Node<T> newNode = new Node<>(oldTail, task, null);
        tail = newNode;
        if (oldTail == null)
            head = newNode;
        else
            oldTail.next = newNode;
        size++;
        return newNode;
    }

    public List<T> getTasks() {
        List<T> history = new ArrayList<>();
        Node<T> element = head;

        while (element != null) {
            history.add(element.data);
            element = element.next;
        }
        return history;
    }
}