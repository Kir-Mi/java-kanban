package manager.http;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import manager.tasks.FileBackedTasksManager;
import task.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Objects;

public class HttpTaskManager extends FileBackedTasksManager {
    private String url;
    private KVTaskClient kvTaskClient;
    private Gson gson;

    public HttpTaskManager(String url) {
        super(url);
        this.url = url;
        gson = new Gson();
        kvTaskClient = new KVTaskClient(url);
    }

    @Override
    public void save() {
        kvTaskClient.put("tasks", gson.toJson(getTaskList()));
        kvTaskClient.put("epics", gson.toJson(getEpicList()));
        kvTaskClient.put("subtasks", gson.toJson(getSubtaskList()));
        kvTaskClient.put("history", gson.toJson(getHistory()));
    }

    public void load() {
        Type tasksType = new TypeToken<ArrayList<Task>>() {
        }.getType();

        ArrayList<Task> recoveryTasks = gson.fromJson(kvTaskClient.load("tasks"), tasksType);
        if (Objects.nonNull(recoveryTasks)) {
            recoveryTasks.stream()
                    .filter(Objects::nonNull)
                    .forEach(task -> {
                        int id = task.getId();
                        tasks.put(id, task);
                        prioritizedTasks.add(task);
                        if (id > counterId) {
                            counterId = id;
                        }
                    });
        }

        Type subtasksType = new TypeToken<ArrayList<Subtask>>() {
        }.getType();
        ArrayList<Subtask> recoverySubtasks = gson.fromJson(kvTaskClient.load("subtasks"), subtasksType);
        if (Objects.nonNull(recoverySubtasks)) {
            recoverySubtasks.stream()
                    .filter(Objects::nonNull)
                    .forEach(subtask -> {
                        int id = subtask.getId();
                        subtasks.put(id, subtask);
                        prioritizedTasks.add(subtask);
                        if (id > counterId) {
                            counterId = id;
                        }
                    });
        }

        Type epicsType = new TypeToken<ArrayList<Epic>>() {
        }.getType();
        ArrayList<Epic> recoveryEpics = gson.fromJson(kvTaskClient.load("epics"), epicsType);
        if (Objects.nonNull(recoveryEpics)) {
            recoveryEpics.stream()
                    .filter(Objects::nonNull)
                    .forEach(epic -> {
                        int id = epic.getId();
                        epics.put(id, epic);
                        if (id > counterId) {
                            counterId = id;
                        }
                    });
        }

        Type historyType = new TypeToken<ArrayList<Task>>() {
        }.getType();
        ArrayList<Task> history = gson.fromJson(kvTaskClient.load("history"), historyType);
        if (Objects.nonNull(history)) {
            for (Task task : history) {
                viewsHistory.add(task);
            }
        }
    }
}
