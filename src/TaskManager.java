import java.util.ArrayList;
import java.util.List;

public interface TaskManager {

    public List<Task> getHistory();

    ArrayList<Task> getTaskList();

    ArrayList<Epic> getEpicList();

    ArrayList<Subtask> getSubtaskList();

    void deleteAllTasks();

    Epic getEpicById(int id);

    Subtask getSubtaskById(int id);

    Task getTaskById(int id);

    int createTask(Task task);

    int createEpic(Epic epic);

    int createSubtask(Subtask subtask, int epicId);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubtask(Subtask subtask);

    void deleteTask(int id);

    void deleteEpic(int id);

    void deleteSubtask(Integer id);

    ArrayList<Subtask> getAllSubtasksByEpic(int id);

    void changeEpicStatus(Epic epic);
}