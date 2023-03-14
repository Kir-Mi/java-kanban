package testing;

import manager.tasks.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Subtask;
import task.Task;
import task.TaskStatus;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.TreeSet;

abstract class TaskManagerTest<T extends TaskManager> {

    protected T manager;
    Epic epic1 = new Epic("эпик1", "описание эпика1"); // id всегда должен быть 1
    Epic epic2 = new Epic("эпик2", "описание эпика2");
    Task task1 = new Task("задача1", "описание задачи1", Instant.parse("2020-10-05T19:28:34Z"), Duration.parse("PT16H35M"));
    Task task2 = new Task("задача2", "описание задачи2", Instant.parse("2020-10-04T19:28:34Z"), Duration.parse("PT16H35M"));
    Subtask subtask1 = new Subtask("субтаск1", "описание субтаска1", 1, Instant.parse("2020-10-08T19:28:34Z"), Duration.parse("PT16H35M"));
    Subtask subtask2 = new Subtask("субтаск2", "описание субтаска2", 1, Instant.parse("2020-10-06T19:28:34Z"), Duration.parse("PT16H35M"));

    @Test
    public void getHistoryTest() {
        manager.createTask(task1);
        manager.getTaskById(1);
        List<Task> actualHistory = manager.getHistory();

        Assertions.assertEquals(task1, actualHistory.get(0), "Задачи не совпадают");
        Assertions.assertEquals(1, actualHistory.size(), "Количество задач не совпадает");
    }

    @Test
    public void getTaskListTest() {
        manager.createTask(task1);
        final List<Task> taskList = manager.getTaskList();

        Assertions.assertTrue(taskList.contains(task1), "Задача отсутствует в списке");
        Assertions.assertEquals(1, taskList.size(), "Неверное количество задач");
        Assertions.assertEquals(task1, taskList.get(0), "Задачи не совпадают");
    }

    @Test
    public void getEpicListTest() {
        manager.createEpic(epic1);
        final List<Epic> epicList = manager.getEpicList();

        Assertions.assertTrue(epicList.contains(epic1), "Задача отсутствует в списке");
        Assertions.assertEquals(1, epicList.size(), "Неверное количество задач");
        Assertions.assertEquals(epic1, epicList.get(0), "Задачи не совпадают");
    }

    @Test
    public void getSubtaskListTest() {
        manager.createEpic(epic1);
        manager.createSubtask(subtask1, 1);
        final List<Subtask> subtaskList = manager.getSubtaskList();

        Assertions.assertTrue(subtaskList.contains(subtask1), "Задача отсутствует в списке");
        Assertions.assertEquals(1, subtaskList.size(), "Неверное количество задач");
        Assertions.assertEquals(subtask1, subtaskList.get(0), "Задачи не совпадают");
    }

    @Test
    public void deleteAllTasksTest() {
        manager.createEpic(epic1);
        manager.createSubtask(subtask1, 1);
        manager.createTask(task1);
        manager.deleteAllTasks();

        Assertions.assertTrue(manager.getTaskList().isEmpty());
        Assertions.assertTrue(manager.getSubtaskList().isEmpty());
        Assertions.assertTrue(manager.getEpicList().isEmpty());
        Assertions.assertTrue(manager.getPrioritizedTasks().isEmpty());
    }

    @Test
    public void getEpicByIdTest() {
        manager.createEpic(epic1);

        final Epic actualEpic = manager.getEpicById(1);

        Assertions.assertEquals(epic1, actualEpic, "Эпики не совпадают");
    }

    @Test
    public void getTaskByIdTest() {
        manager.createTask(task1);

        final Task actualTask = manager.getTaskById(1);

        Assertions.assertEquals(task1, actualTask, "Таски не совпадают");
    }

    @Test
    public void getSubtaskByIdTest() {
        manager.createEpic(epic1);
        manager.createSubtask(subtask1, 1);

        final Subtask actualSubtask = manager.getSubtaskById(2);

        Assertions.assertEquals(subtask1, actualSubtask, "Таски не совпадают");
    }

    @Test
    public void createTask() {
        manager.createTask(task1);

        final Task actualTask = manager.getTaskById(1);

        Assertions.assertNotNull(actualTask, "Задача не найдена.");
        Assertions.assertEquals(task1, actualTask, "Задачи не совпадают.");
    }

    @Test
    public void createEpic() {
        manager.createEpic(epic1);

        final Task actualEpic = manager.getEpicById(1);

        Assertions.assertNotNull(actualEpic, "Задача не найдена.");
        Assertions.assertEquals(epic1, actualEpic, "Задачи не совпадают.");
    }

    @Test
    public void createSubtask() {
        manager.createEpic(epic1);
        manager.createSubtask(subtask1, 1);

        final Subtask actualSubtask = manager.getSubtaskById(2);

        Assertions.assertNotNull(actualSubtask, "Задача не найдена.");
        Assertions.assertEquals(subtask1, actualSubtask, "Задачи не совпадают.");
    }

    @Test
    public void updateTaskTest() {
        manager.createTask(task1);
        task1.setStatus(TaskStatus.DONE);
        manager.updateTask(task1);
        final Task actualTask = manager.getTaskById(1);

        Assertions.assertEquals(actualTask.getStatus(), TaskStatus.DONE, "Статус не обновлён");
    }

    @Test
    public void updateEpicTest() {
        manager.createEpic(epic1);
        epic1.setTitle("эпик9");
        manager.updateEpic(epic1);
        final Epic actualEpic = manager.getEpicById(1);

        Assertions.assertEquals(actualEpic.getTitle(), "эпик9", "Заголовок не обновлён");
    }

    @Test
    public void updateSubtaskTest() {
        manager.createEpic(epic1);
        manager.createSubtask(subtask1, 1);
        subtask1.setStatus(TaskStatus.DONE);
        manager.updateSubtask(subtask1);
        final Subtask actualSubtask = manager.getSubtaskById(2);

        Assertions.assertEquals(TaskStatus.DONE, actualSubtask.getStatus(), "Статус субтаска не обновлён");
    }

    @Test
    public void deleteTaskTest() {
        manager.createTask(task1);
        manager.deleteTask(1);

        Assertions.assertTrue(manager.getTaskList().isEmpty(), "Список задач не пуст");
    }

    @Test
    public void deleteEpicTest() {
        manager.createEpic(epic1);
        manager.deleteEpic(1);

        Assertions.assertTrue(manager.getEpicList().isEmpty(), "Список эпиков не пуст");
    }

    @Test
    public void deleteSubtaskTest() {
        manager.createEpic(epic1);
        manager.createSubtask(subtask1, 1);
        manager.deleteSubtask(2);

        Assertions.assertFalse(manager.getSubtaskList().contains(subtask1), "субтаск не удален");
    }

    @Test
    public void getAllSubtasksByEpicTest() {
        manager.createEpic(epic1);
        manager.createSubtask(subtask1, 1);
        final List<Subtask> actualSubList = manager.getAllSubtasksByEpic(1);

        Assertions.assertTrue(actualSubList.contains(subtask1), "Список не содержит подзадачу");
        Assertions.assertEquals(1, actualSubList.size(), "Размер списка подзадач не верный");
    }

    @Test
    public void changeEpicStatusTest() {
        manager.createEpic(epic1);
        manager.createSubtask(subtask1, 1);
        manager.createSubtask(subtask2, 1);
        subtask1.setStatus(TaskStatus.DONE);
        manager.updateSubtask(subtask1);

        Assertions.assertEquals(TaskStatus.IN_PROGRESS, epic1.getStatus(), "Статус не обновился");

        subtask2.setStatus(TaskStatus.DONE);
        manager.updateSubtask(subtask2);

        Assertions.assertEquals(TaskStatus.DONE, epic1.getStatus(), "Статус не обновился");
    }

    @Test
    public void getPrioritizedTasksTest() {
        manager.createTask(task1);
        final TreeSet<Task> actualTreeSet = manager.getPrioritizedTasks();

        Assertions.assertEquals(task1, actualTreeSet.first(), "Задача не добавлена в список");
    }
}