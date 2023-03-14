package manager.tasks;

import manager.history.*;
import manager.Managers;
import task.*;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

public class InMemoryTaskManager implements TaskManager {

    private int counterId = 1;
    protected HistoryManager viewsHistory = Managers.getDefaultHistory(); // храним историю просмотров

    protected HashMap<Integer, Task> tasks = new HashMap<>();
    protected HashMap<Integer, Subtask> subtasks = new HashMap<>();
    protected HashMap<Integer, Epic> epics = new HashMap<>();
    protected TreeSet<Task> prioritizedTasks = new TreeSet<>((t1, t2) -> {  // храним таски по приоритету
        if (t1.getStartTime() == null && t2.getStartTime() == null) {
            return 0;
        } else if (t1.getStartTime() == null) {
            return -1;
        } else if (t2.getStartTime() == null) {
            return 1;
        } else {
            return t1.getStartTime().compareTo(t2.getStartTime());
        }
    });

    public void saveTaskByPriority(Task task) { // сохраняем таски по приоритету
        Task oldTask = null;
        boolean isTimeCorrect = true;

        if (task instanceof Subtask) {
            oldTask = subtasks.get(task.getId());
        } else {
            oldTask = tasks.get(task.getId());
        }
        if (oldTask != null) {
            prioritizedTasks.remove(oldTask);
        }
        try {
            for (Task prioritizedTask : prioritizedTasks) { // ищем пересечение по времени с каждой таской
                if (task.getStartTime().isAfter(prioritizedTask.getStartTime()) && task.getStartTime().isBefore(prioritizedTask.getEndTime()) ||
                        task.getEndTime().isBefore(prioritizedTask.getEndTime()) && task.getEndTime().isAfter(prioritizedTask.getStartTime())) {
                    isTimeCorrect = false;
                    throw new IllegalArgumentException("Неверное время задачи");
                } else {
                    isTimeCorrect = true;
                }
            }
        } catch (IllegalArgumentException e) { // task пересекается с существующей задачей
            System.out.println(e.getMessage());
            isTimeCorrect = false;
        } catch (NullPointerException e) { // prioritizedTasks был пуст
            isTimeCorrect = true;
        }
        if (isTimeCorrect) {
            prioritizedTasks.add(task);
        }
    }

    public TreeSet<Task> getPrioritizedTasks() {
        return prioritizedTasks;
    }

    public void SaveAllTasksByPriority() { // формируем все задачи по приоритету
        for (Task task : tasks.values()) {
            saveTaskByPriority(task);
        }
        for (Subtask subtask : subtasks.values()) {
            saveTaskByPriority(subtask);
        }
    }

    @Override
    public List<Task> getHistory() { // метод возвращает список с просмотренными задачами
        return viewsHistory.getHistory();
    }

    @Override
    public ArrayList<Task> getTaskList() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Epic> getEpicList() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<Subtask> getSubtaskList() {
        return new ArrayList<>(subtasks.values());
    }

    public void setCounterId(int counterId) {
        this.counterId = counterId;
    }

    @Override
    public void deleteAllTasks() {
        if (!tasks.isEmpty()) { // чистим историю в случае удаления всех задач
            for (Integer id : tasks.keySet()) {
                viewsHistory.remove(id);
            }
        }
        if (!subtasks.isEmpty()) {
            for (Integer id : subtasks.keySet()) {
                viewsHistory.remove(id);
            }
        }
        if (!epics.isEmpty()) {
            for (Integer id : epics.keySet()) {
                viewsHistory.remove(id);
            }
        }
        tasks.clear();
        subtasks.clear();
        epics.clear();
        prioritizedTasks.clear();
    }

    @Override
    public Epic getEpicById(int id) {
        viewsHistory.add(epics.get(id)); //добавляем просмотренную задачу в историю просмотров
        return epics.get(id);
    }

    @Override
    public Subtask getSubtaskById(int id) {
        viewsHistory.add(subtasks.get(id));
        return subtasks.get(id);
    }

    @Override
    public Task getTaskById(int id) {
        viewsHistory.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public int createTask(Task task) {
        task.setId(counterId);
        task.setStatus(TaskStatus.NEW);
        tasks.put(counterId, task);
        saveTaskByPriority(task); // сохраняем в список по приоритету
        counterId++;
        return task.getId();
    }

    @Override
    public int createEpic(Epic epic) {
        epic.setId(counterId);
        epic.setStatus(TaskStatus.NEW);
        epics.put(counterId, epic);
        counterId++;
        return epic.getId();
    }

    @Override
    public int createSubtask(Subtask subtask, int epicId) {
        subtask.setId(counterId);
        subtask.setStatus(TaskStatus.NEW);
        subtasks.put(counterId, subtask);

        Epic epic = epics.get(epicId);
        if (epic.getStatus().equals(TaskStatus.DONE)) { // статус epic меняется только в случае, если он был "DONE"
            epic.setStatus(TaskStatus.IN_PROGRESS);
        }
        ArrayList<Integer> idList = epic.getSubtaskId();
        idList.add(counterId);
        epic.setSubtaskId(idList);
        epics.put(epicId, epic);
        calculateEpicDuration(epicId);// пересчитываем временные рамки эпика
        saveTaskByPriority(subtask); // сохраняем в список по приоритету

        counterId++;
        return subtask.getId();
    }

    public void calculateEpicDuration(int epicId) { // вычисляем временные рамки эпика
        Epic epic = epics.get(epicId);
        List<Integer> subId = epic.getSubtaskId();
        Duration durationOfEpic = Duration.ofHours(0);
        Instant earliestStartTime = Instant.MIN;
        Instant latestEndTime = Instant.MAX;
        for (Integer id : subId) {
            Subtask subtask = subtasks.get(id);
            if (earliestStartTime == Instant.MIN && latestEndTime == Instant.MAX) {
                earliestStartTime = subtask.getStartTime();
                latestEndTime = subtask.getEndTime();
            }
            if (subtask.getStartTime().isBefore(earliestStartTime)) {
                earliestStartTime = subtask.getStartTime();
            }
            if (subtask.getEndTime().isAfter(latestEndTime)) {
                latestEndTime = subtask.getEndTime();
            }
        }
        durationOfEpic = Duration.between(earliestStartTime, latestEndTime);
        epic.setStartTime(earliestStartTime);
        epic.setEndTime(latestEndTime);
        epic.setDuration(durationOfEpic);
        epics.put(epicId, epic);
    }

    @Override
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
        saveTaskByPriority(task); // сохраняем заново в список по приоритету
    }

    @Override
    public void updateEpic(Epic epic) {
        epic.setStatus(epics.get(epic.getId()).getStatus()); // Сохраняем в новом эпике статус из старого
        epics.put(epic.getId(), epic);                       // чтобы юзер не мог менять статус эпика напрямую
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);  // если статус поменялся - пересчитываем статус эпика
        Epic epic = epics.get(subtask.getEpicId());
        changeEpicStatus(epic);
        calculateEpicDuration(epic.getId()); // пересчитываем временные рамки эпика
        saveTaskByPriority(subtask); // сохраняем заново в список по приоритету
    }

    @Override
    public void deleteTask(int id) {
        prioritizedTasks.remove(tasks.get(id));
        viewsHistory.remove(id);
        tasks.remove(id);
    }

    @Override
    public void deleteEpic(int id) {
        ArrayList<Integer> idList = epics.get(id).getSubtaskId(); // сначала удаляем подзадачи эпика
        for (Integer idSub : idList) {
            prioritizedTasks.remove(subtasks.get(idSub));
            viewsHistory.remove(idSub);
            subtasks.remove(idSub);
        }
        viewsHistory.remove(id);
        epics.remove(id);
    }

    @Override
    public void deleteSubtask(Integer id) {
        prioritizedTasks.remove(subtasks.get(id));
        Epic epic = epics.get(subtasks.get(id).getEpicId());
        viewsHistory.remove(id);
        subtasks.remove(id);
        ArrayList<Integer> idList = epic.getSubtaskId();
        idList.remove(id);//чтобы сработал как удаление по значению, а не по индексу, передаем в метод Integer, а не int
        changeEpicStatus(epic);
        calculateEpicDuration(epic.getId());
    }

    @Override
    public ArrayList<Subtask> getAllSubtasksByEpic(int id) {
        ArrayList<Subtask> subtaskList = new ArrayList<>();
        Epic epic = epics.get(id);
        ArrayList<Integer> subtaskId = epic.getSubtaskId();
        for (Integer subId : subtaskId) {
            subtaskList.add(subtasks.get(subId));
        }
        return subtaskList;
    }

    @Override
    public void changeEpicStatus(Epic epic) {
        List<Integer> idList = epic.getSubtaskId();
        int statusNew = 0;
        int statusDone = 0;
        for (Integer subId : idList) {
            TaskStatus status = subtasks.get(subId).getStatus();
            if (status.equals(TaskStatus.NEW)) {
                statusNew++;
            } else {
                statusDone++;
            }
        }
        if (statusNew != 0 && statusDone != 0) {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        } else if (statusDone != 0) {
            epic.setStatus(TaskStatus.DONE);
        } else {
            epic.setStatus(TaskStatus.NEW);
        }
        epics.put(epic.getId(), epic);
    }
}