package manager.history;

import task.Task;

import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    CustomLinkedList<Task> orderOfViews = new CustomLinkedList<>(); // список последних просмотров
    HashMap<Integer, CustomLinkedList<Task>.Node<Task>> nodeToDelete = new HashMap<>(); // храним просмотренные таски

    @Override
    public void remove(int id) {
        if (nodeToDelete.get(id) != null) {
            orderOfViews.removeNode(nodeToDelete.get(id));
            nodeToDelete.remove(id);
        }
    }

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
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