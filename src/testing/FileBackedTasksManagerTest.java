package testing;

import manager.tasks.FileBackedTasksManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Subtask;
import task.Task;

import java.nio.file.Path;
import java.util.List;
import java.util.TreeSet;

public class FileBackedTasksManagerTest extends TaskManagerTest {

    @BeforeEach
    public void setUp() {
        manager = new FileBackedTasksManager(Path.of("src/resources/test.csv"));
    }

    @Test
    public void loadFromFileTest() {
        manager = FileBackedTasksManager.loadFromFile(Path.of("src/resources/testFile.csv"));
        manager.getHistory();
        final List<Task> actualHistory = manager.getHistory();

        Assertions.assertEquals(5, actualHistory.get(0).getId(), "История восстановлено неверно");
        Assertions.assertEquals(7, actualHistory.size(), "Неверный размер истории");

        final TreeSet<Task> actualPriorityTasks = manager.getPrioritizedTasks();
        task2 = manager.getTaskById(3);

        Assertions.assertEquals(task2, actualPriorityTasks.first(), "Неверно восстановлены задачи по приоритету");

        final List<Task> actualTaskList = manager.getTaskList();
        final List<Epic> actualEpicList = manager.getEpicList();
        final List<Subtask> actualSubtaskList = manager.getSubtaskList();
        epic1 = manager.getEpicById(4);
        subtask1 = manager.getSubtaskById(6);

        Assertions.assertTrue(actualEpicList.contains(epic1), "Эпики неверно восстановлены");
        Assertions.assertTrue(actualSubtaskList.contains(subtask1), "Сабтаски неверно восстановлены");
        Assertions.assertTrue(actualTaskList.contains(task2), "Таски неверно восстановлены");
    }
}