package main;

import manager.Managers;
import manager.tasks.TaskManager;
import task.*;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();

        // код ниже оставил для удобства тестирования
        Task testTask1 = new Task("задача1", "описание задачи1"); // создаем задачи
        int taskId1 = taskManager.createTask(testTask1);
        Task testTask2 = new Task("задача2", "описание задачи2");
        int taskId2 = taskManager.createTask(testTask2);

        Epic testEpic1 = new Epic("эпик1", "описание эпика1");
        int epicId1 = taskManager.createEpic(testEpic1);
        Epic testEpic2 = new Epic("эпик2", "описание эпика2");
        int epicId2 = taskManager.createEpic(testEpic2);

        Subtask testSubtask1 = new Subtask("субтаск1", "описание субтаска1", epicId1);
        int subId1 = taskManager.createSubtask(testSubtask1, epicId1);
        Subtask testSubtask2 = new Subtask("субтаск2", "описание субтаска2", epicId1);
        int subId2 = taskManager.createSubtask(testSubtask2, epicId1);
        Subtask testSubtask3 = new Subtask("субтаск3", "описание субтаска3", epicId1);
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
        taskManager.deleteTask(taskId2);
        System.out.println("Удалить задачу 2"); // смотрим, чтобы из истории тоже удалилась
        System.out.println(taskManager.getHistory());
        taskManager.deleteEpic(epicId1);
        System.out.println("Удалить эпик 1");
        System.out.println(taskManager.getHistory());
    }
}