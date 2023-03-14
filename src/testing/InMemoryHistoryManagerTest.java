package testing;

import manager.history.InMemoryHistoryManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Task;
import task.TaskStatus;

import java.util.List;

public class InMemoryHistoryManagerTest {

    InMemoryHistoryManager manager;
    Task task1 = new Task(1, TaskStatus.NEW, "задача1", "описание задачи1");
    Task task2 = new Task(2, TaskStatus.NEW, "задача2", "описание задачи2");
    Task task3 = new Task(3, TaskStatus.NEW, "задача3", "описание задачи3");

    @BeforeEach
    public void setUp() {
        manager = new InMemoryHistoryManager();
    }

    @Test
    public void addTest() {
        manager.add(task1);
        manager.add(task2);
        final List<Task> actualHistory = manager.getHistory();

        Assertions.assertTrue(actualHistory.contains(task1), "Таска отсутствует в истории");
        Assertions.assertEquals(2, actualHistory.size(), "Неверный размер истории");
        Assertions.assertEquals(task1, actualHistory.get(0), "Неверный размер истории");

        manager.add(task3);
        manager.remove(2);
        final List<Task> actualHistory2 = manager.getHistory();

        Assertions.assertEquals(task3, actualHistory2.get(1), "История отображается неверно");
        Assertions.assertEquals(2, actualHistory2.size(), "Размер истории не верный");
        Assertions.assertFalse(actualHistory2.contains(task2), "Таска осталась в истории");
    }
}