package main;

import manager.http.HttpTaskManager;
import manager.tasks.TaskManager;
import task.*;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

public class Main {

    public static void main(String[] args) throws IOException {

        TaskManager taskManager = new HttpTaskManager("http://localhost:8078");

        //((HttpTaskManager) taskManager).load();

        System.out.println(taskManager.getTaskList());



        /* код для тестирования оставил прежний. На гитхабе файл для восстановления, загружается корректно.
        По итогу запуска программы наполнение файла меняется. Первоначальный запуск без файла тоже работает ок.
         */
        Task testTask1 = new Task("задача1", "описание задачи1", Instant.parse("2020-10-05T19:28:34Z"), Duration.parse("PT16H35M")); // создаем задачи
        int taskId1 = taskManager.createTask(testTask1);
        Task testTask2 = new Task("задача2", "описание задачи2", Instant.parse("2020-10-04T19:28:34Z"), Duration.parse("PT16H35M"));
        int taskId2 = taskManager.createTask(testTask2);

        Epic testEpic1 = new Epic("эпик1", "описание эпика1");
        int epicId1 = taskManager.createEpic(testEpic1);
        Epic testEpic2 = new Epic("эпик2", "описание эпика2");
        int epicId2 = taskManager.createEpic(testEpic2);

        Subtask testSubtask1 = new Subtask("субтаск1", "описание субтаска1", epicId1, Instant.parse("2020-10-06T19:28:34Z"), Duration.parse("PT16H35M"));
        int subId1 = taskManager.createSubtask(testSubtask1, epicId1);
        Subtask testSubtask2 = new Subtask("субтаск2", "описание субтаска2", epicId1, Instant.parse("2020-10-08T19:28:34Z"), Duration.parse("PT16H35M"));
        int subId2 = taskManager.createSubtask(testSubtask2, epicId1);
        Subtask testSubtask3 = new Subtask("субтаск3", "описание субтаска3", epicId1, Instant.parse("2020-10-07T19:28:34Z"), Duration.parse("PT16H35M"));
        int subId3 = taskManager.createSubtask(testSubtask3, epicId1);

        taskManager.getTaskById(taskId1);
        System.out.println("Задача 1"); // так обозначил вызов задач
        System.out.println(taskManager.getHistory());
        taskManager.getTaskById(taskId2);
        System.out.println("Задача 2");
        System.out.println(taskManager.getHistory());
        taskManager.getTaskById(taskId1);
        System.out.println("Задача 1");
        System.out.println(taskManager.getHistory());
        taskManager.getEpicById(epicId1);
        System.out.println("Эпик 1");
        System.out.println(taskManager.getHistory());
        taskManager.getEpicById(epicId2);
        System.out.println("Эпик 2");
        System.out.println(taskManager.getHistory());
        taskManager.getTaskById(taskId1);
        System.out.println("Задача 1");
        System.out.println(taskManager.getHistory());
        taskManager.getSubtaskById(subId1);
        System.out.println("Субтаск 1");
        System.out.println(taskManager.getHistory());
        taskManager.getSubtaskById(subId2);
        System.out.println("Субтаск 2");
        System.out.println(taskManager.getHistory());
        taskManager.getSubtaskById(subId3);
        System.out.println("Субтаск 3");
        System.out.println(taskManager.getHistory());
        taskManager.getSubtaskById(subId1);
        System.out.println("Субтаск 1");
        System.out.println(taskManager.getHistory());
        taskManager.getTaskById(taskId1);
        System.out.println("Задача 1");
        System.out.println(taskManager.getHistory());
        taskManager.getTaskById(taskId2);
        System.out.println("Задача 2");
        System.out.println(taskManager.getHistory());
        taskManager.getSubtaskById(subId2);
        System.out.println("Субтаск 2");
        System.out.println(taskManager.getHistory());
       /* taskManager.deleteTask(taskId2);
        System.out.println("Удалить задачу 2"); // смотрим, чтобы из истории тоже удалилась
        System.out.println(taskManager.getHistory());
        taskManager.deleteEpic(epicId1);
        System.out.println("Удалить эпик 1");*/
        System.out.println(taskManager.getHistory());
        System.out.println("Задачи по приоритету");
        System.out.println(taskManager.getPrioritizedTasks());
        //((HttpTaskManager) taskManager).load();
    }
}