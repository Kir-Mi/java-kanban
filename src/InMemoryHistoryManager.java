import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final int numberOfViews = 10; // количество сохраняемых просмотров
    LinkedList<Task> viewsHistory = new LinkedList<>(); // список последних просмотров

    @Override
    public void add(Task task) {
        viewsHistory.add(task);
        if (viewsHistory.size() > numberOfViews) {
            viewsHistory.removeFirst();
        }
    }

    @Override
    public List<Task> getHistory() {
        return viewsHistory;
    }
}
