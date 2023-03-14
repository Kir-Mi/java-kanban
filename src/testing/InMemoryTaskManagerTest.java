package testing;

import manager.Managers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Task;

import java.time.Instant;
import java.util.TreeSet;

public class InMemoryTaskManagerTest extends TaskManagerTest {

    @BeforeEach
    void setUp() {
        manager = Managers.getDefault();
    }

    @Test
    public void tasksByPriorityTest() {
        manager.createEpic(epic1);
        manager.createSubtask(subtask1, 1);
        manager.createTask(task1);
        final TreeSet<Task> actualTasksByPriority = manager.getPrioritizedTasks();

        Assertions.assertEquals(2, actualTasksByPriority.size(), "Неверное количество задач");
        Assertions.assertEquals(task1, actualTasksByPriority.first(), "Неверный порядок задач");


        task2.setStartTime(Instant.parse("2020-10-05T18:28:34Z"));
        manager.createTask(task2);

        Assertions.assertEquals(2, actualTasksByPriority.size(), "Добавилась задача с тем же временем");
        Assertions.assertEquals(task1, actualTasksByPriority.first(), "Неверный порядок задач");

        manager.createTask(task1);

        Assertions.assertEquals(2, actualTasksByPriority.size(), "Добавилась та же задача");
    }

    @Test
    public void toStringTest() {
        String expected = "Task.Task{title='задача1', description='описание задачи1', id=0, status='null'}";
        String actual = task1.toString();

        Assertions.assertEquals(expected, actual, "toString у тасков работает неверно");

        String expected2 = "Task.Epic{title='эпик1', description='описание эпика1', id=0, status='null', subtaskId =[]}";
        String actual2 = epic1.toString();

        Assertions.assertEquals(expected2, actual2, "toString  у эпиков работает неверно");

        String expected3 = "Task.Subtask{title='субтаск1', description='описание субтаска1', id=0, status='null', epicId =1}";
        String actual3 = subtask1.toString();

        Assertions.assertEquals(expected3, actual3, "toString у субтасков работает неверно");
    }
}