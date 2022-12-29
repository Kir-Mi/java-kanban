import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private int counterId = 1;

    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, Subtask> subtasks = new HashMap<>();
    HashMap<Integer, Epic> epics = new HashMap<>();

    public ArrayList<Task> getTaskList() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Epic> getEpicList() {
        return new ArrayList<>(epics.values());
    }

    public ArrayList<Subtask> getSubtaskList() {
        return new ArrayList<>(subtasks.values());
    }

    public void deleteAllTasks() {
        tasks.clear();
        subtasks.clear();
        epics.clear();
    }

    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    public Subtask getSubtaskById(int id) {
        return subtasks.get(id);
    }

    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public int createTask(Task task) {
        task.setId(counterId);
        task.setStatus("NEW");
        tasks.put(counterId, task);
        counterId++;
        return task.getId(); //
    }

    public int createEpic(Epic epic) {
        epic.setId(counterId);
        epic.setStatus("NEW");
        epics.put(counterId, epic);
        counterId++;
        return epic.getId(); //
    }

    public int createSubtask(Subtask subtask, int epicId) {
        subtask.setId(counterId);
        subtask.setStatus("NEW");
        subtasks.put(counterId, subtask);

        Epic epic = epics.get(epicId);
        if (epic.getStatus().equals("DONE")) { // статус epic меняется только в случае, если он был "DONE"
            epic.setStatus("IN_PROGRESS");
        }
        ArrayList<Integer> idList = epic.getSubtaskId();
        idList.add(counterId);
        epic.setSubtaskId(idList);
        epics.put(epicId, epic);

        counterId++;
        return subtask.getId(); //
    }

    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public void updateEpic(Epic epic) {
        epic.setStatus(epics.get(epic.getId()).getStatus()); // Сохраняем в новом эпике статус из старого
        epics.put(epic.getId(), epic);                       // чтобы юзер не мог менять статус эпика напрямую
    }

    public void updateSubtask(Subtask subtask) {
        String oldStatus = subtasks.get(subtask.getId()).getStatus();
        if (oldStatus.equals(subtask.getStatus())) { // если статус не поменялся - просто записываем новый субтаск
            subtasks.put(subtask.getId(), subtask);
            return;
        }
        subtasks.put(subtask.getId(), subtask);  // если статус поменялся - пересчитываем статус эпика
        Epic epic = epics.get(subtask.getEpicId());
        ArrayList<Integer> subId = epic.getSubtaskId();
        int statusNew = 0;
        int statusDone = 0;
        for (Integer id : subId) {
            String status = subtasks.get(id).getStatus();
            if (status.equals("NEW")) {
                statusNew++;
            } else {
                statusDone++;
            }
        }
        if (statusNew != 0 && statusDone != 0) {
            epic.setStatus("IN_PROGRESS");
        } else if (statusDone != 0) {
            epic.setStatus("DONE");
        } else {
            epic.setStatus("NEW");
        }
        epics.put(epic.getId(), epic);
    }

    public void deleteTask(int id) {
        tasks.remove(id);
    }

    public void deleteEpic(int id) {
        ArrayList<Integer> idList = epics.get(id).getSubtaskId(); // сначала удаляем подзадачи эпика
        for (Integer idSub : idList) {
            subtasks.remove(idSub);
        }
        epics.remove(id);
    }

    public void deleteSubtask(Integer id) {
        Epic epic = epics.get(subtasks.get(id).getEpicId());
        subtasks.remove(id);
        ArrayList<Integer> idList = epic.getSubtaskId();
        idList.remove(id);//чтобы сработал как удаление по значению, а не по индексу, передаем в метод Integer, а не int
        int statusNew = 0;
        int statusDone = 0;
        for (Integer subId : idList) {
            String status = subtasks.get(subId).getStatus();
            if (status.equals("NEW")) {
                statusNew++;
            } else {
                statusDone++;
            }
        }
        if (statusNew != 0 && statusDone != 0) {
            epic.setStatus("IN_PROGRESS");
        } else if (statusDone != 0) {
            epic.setStatus("DONE");
        } else {
            epic.setStatus("NEW");
        }
        epics.put(epic.getId(), epic);
    }

    public ArrayList<Subtask> getAllSubtasksByEpic(int id) {
        ArrayList<Subtask> subtaskList = new ArrayList<>();
        Epic epic = epics.get(id);
        ArrayList<Integer> subtaskId = epic.getSubtaskId();
        for (Integer subId : subtaskId) {
            subtaskList.add(subtasks.get(subId));
        }
        return subtaskList;
    }
}
