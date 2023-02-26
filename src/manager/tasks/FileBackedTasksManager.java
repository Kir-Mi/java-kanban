package manager.tasks;

import exceptions.ManagerSaveException;
import manager.history.HistoryManager;
import task.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private final Path saveFile;

    public FileBackedTasksManager(Path saveFile) {
        this.saveFile = saveFile;
    }

    public static FileBackedTasksManager loadFromFile(Path saveFile) {
        FileBackedTasksManager recovery = new FileBackedTasksManager(saveFile);
        HashMap<Integer, Task> restoredTasks = new HashMap<>();
        HashMap<Integer, Subtask> restoredSubtasks = new HashMap<>();
        HashMap<Integer, Epic> restoredEpics = new HashMap<>();
        List<Integer> history = new ArrayList<>();
        int restoredCounterId = 1;

        try {
            try {
                List<String> lines = Files.readAllLines(saveFile); // сохраняем таски по промежуточным мапам
                for (int i = 1; i < lines.size() - 2; i++) {
                    if (lines.get(i) == null && lines.get(i - 1) != null && lines.get(i + 1) != null) {
                        history = historyFromString(lines.get(i + 1));
                        break;
                    }
                    Task restoredTask = taskFromString(lines.get(i));
                    if (restoredTask instanceof Epic) {
                        restoredEpics.put(restoredTask.getId(), (Epic) restoredTask);
                    } else if (restoredTask instanceof Subtask) {
                        restoredSubtasks.put(restoredTask.getId(), (Subtask) restoredTask);
                    } else {
                        restoredTasks.put(restoredTask.getId(), restoredTask);
                    }
                }
                for (Epic epic : restoredEpics.values()) { // формируем список SubtaskId у эпиков
                    ArrayList<Integer> idList = new ArrayList<>();
                    for (Subtask subtask : restoredSubtasks.values()) {
                        if (epic.getId() == subtask.getEpicId()) {
                            idList.add(subtask.getId());
                        }
                    }
                    epic.setSubtaskId(idList);
                    recovery.epics.put(epic.getId(), epic);
                }
                recovery.tasks = restoredTasks;
                recovery.subtasks = restoredSubtasks;

                for (Integer id : history) { // восстанавливаем историю просмотров
                    for (Task task : recovery.tasks.values()) {
                        if (id == task.getId()) {
                            recovery.viewsHistory.add(task);
                        }
                        if (task.getId() > restoredCounterId) { // вычисляем максимальный из имеющихся id
                            restoredCounterId = task.getId();
                        }
                    }
                    for (Subtask subtask : recovery.subtasks.values()) {
                        if (id == subtask.getId()) {
                            recovery.viewsHistory.add(subtask);
                        }
                        if (subtask.getId() > restoredCounterId) {
                            restoredCounterId = subtask.getId();
                        }
                    }
                    for (Epic epic : recovery.epics.values()) {
                        if (id == epic.getId()) {
                            recovery.viewsHistory.add(epic);
                        }
                        if (epic.getId() > restoredCounterId) {
                            restoredCounterId = epic.getId();
                        }
                    }
                }
            } catch (IOException exception) {
                throw new ManagerSaveException("Ошибка чтения файла");
            }
        } catch (ManagerSaveException exception) {
            System.out.println(exception.getMessage());
        }
        recovery.setCounterId(restoredCounterId + 1); // восстанавливаем значение CounterId
        return recovery;
    }

    public void save() {
        try {
            try {
                if (!Files.exists(saveFile)) {
                    Files.createFile(saveFile);
                }
            } catch (IOException exception) {
                throw new ManagerSaveException("Ошибка создания файла");
            }
            try (Writer fileWriter = new FileWriter(saveFile.toFile());) {
                fileWriter.write("id,type,name,status,description,epic\n");
                for (Epic epic : epics.values()) {
                    fileWriter.write(taskToString(epic) + "\n");
                    for (int subId : epic.getSubtaskId()) {
                        fileWriter.write(taskToString(subtasks.get(subId)) + "\n");
                    }
                }
                for (Task task : tasks.values()) {
                    fileWriter.write(taskToString(task) + "\n");
                }
                fileWriter.write(historyToString(viewsHistory) + "\n");
            } catch (IOException exception) {
                throw new ManagerSaveException("Ошибка записи в файл");
            }
        } catch (ManagerSaveException exception) {
            System.out.println(exception.getMessage());
        }
    }

    public String taskToString(Task task) {
        if (task instanceof Subtask) {
            Subtask subtask = (Subtask) task;
            return String.join(",", Integer.toString(subtask.getId()),
                    TaskTypes.SUBTASK.toString(), subtask.getTitle(), subtask.getStatus().toString(),
                    subtask.getDescription(), Integer.toString(subtask.getEpicId()));
        } else if (task instanceof Epic) {
            return String.join(",", Integer.toString(task.getId()), TaskTypes.EPIC.toString(),
                    task.getTitle(), task.getStatus().toString(), task.getDescription());
        } else {
            return String.join(",", Integer.toString(task.getId()), TaskTypes.TASK.toString(),
                    task.getTitle(), task.getStatus().toString(), task.getDescription());
        }
    }

    public static Task taskFromString(String value) {
        String[] split = value.split(",");

        String taskId = split[0];
        String taskType = split[1];
        String taskTitle = split[2];
        String taskStatus = split[3];
        String taskDescription = split[4];

        if (taskType.equals(TaskTypes.TASK.toString())) {
            return new Task(Integer.parseInt(taskId), TaskStatus.valueOf(taskStatus), taskTitle, taskDescription);
        } else if (taskType.equals(TaskTypes.SUBTASK.toString())) {
            String epicId = split[5];
            return new Subtask(Integer.parseInt(taskId), TaskStatus.valueOf(taskStatus),
                    taskTitle, taskDescription, Integer.parseInt(epicId));
        } else {
            return new Epic(Integer.parseInt(taskId), TaskStatus.valueOf(taskStatus), taskTitle, taskDescription);
        }
    }

    public static String historyToString(HistoryManager manager) {
        List<Task> historyList = manager.getHistory();
        String[] idHistory = new String[historyList.size()];
        for (int i = 0; i < historyList.size(); i++) {
            idHistory[i] = Integer.toString(historyList.get(i).getId());
        }
        return String.join(",", idHistory);
    }

    public static List<Integer> historyFromString(String value) {
        String[] split = value.split(",");
        List<Integer> idHistory = new ArrayList<>();
        for (String id : split) {
            idHistory.add(Integer.parseInt(id));
        }
        return idHistory;
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = super.getEpicById(id);
        save();
        return epic;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = super.getSubtaskById(id);
        save();
        return subtask;
    }

    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public int createTask(Task task) {
        int id = super.createTask(task);
        save();
        return id;
    }

    @Override
    public int createEpic(Epic epic) {
        int id = super.createEpic(epic);
        save();
        return id;
    }

    @Override
    public int createSubtask(Subtask subtask, int epicId) {
        int id = super.createSubtask(subtask, epicId);
        save();
        return id;  // Как вернуть ИД из супера?
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public void deleteSubtask(Integer id) {
        super.deleteSubtask(id);
        save();
    }
}