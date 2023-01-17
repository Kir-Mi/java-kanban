import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    int numberOfViews = 10; // количество сохраняемых просмотров
    List<Task> viewsHistory = new ArrayList<>(numberOfViews); // список последний просмотров

    @Override
    public void add(Task task) {
        if (viewsHistory.size() < numberOfViews) { // проверяем заполнен ли список
            viewsHistory.add(task);
        } else {
            ArrayList<Task> copyViewsHistory = new ArrayList<>(viewsHistory);
            viewsHistory.clear();
            for (int i = 1; i < copyViewsHistory.size(); i++) {
                viewsHistory.add(copyViewsHistory.get(i));
            }
            viewsHistory.add(task);
        }
    }

    @Override
    public List<Task> getHistory() {
        return viewsHistory;
    }
}
