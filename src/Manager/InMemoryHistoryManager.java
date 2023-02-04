package Manager;

import Task.Task;

import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    OrderOfViews<Task> orderOfViews = new OrderOfViews<>(); // список последних просмотров
    HashMap<Integer, Node<Task>> nodeToDelete = new HashMap<>(); // запоминаем какие таски уже смотрели

    @Override
    public void remove(int id) {
        if (nodeToDelete.get(id) != null) {
            orderOfViews.removeNode(nodeToDelete.get(id));
            nodeToDelete.remove(id);
        }
    }

    @Override
    public void add(Task task) {
        if (nodeToDelete.get(task.getId()) == null) {
            nodeToDelete.put(task.getId(), orderOfViews.linkLast(task));
        } else {
            orderOfViews.removeNode(nodeToDelete.get(task.getId()));
            nodeToDelete.put(task.getId(), orderOfViews.linkLast(task));
        }
    }

    @Override
    public List<Task> getHistory() {
        return orderOfViews.getTasks();
    }
}