package Manager;

public class Node<T> {

    public T data;
    public Node next;
    public Node prev;

    public Node(Node prev, T data, Node next) {
        this.data = data;
        this.next = next;
        this.prev = prev;
    }
}