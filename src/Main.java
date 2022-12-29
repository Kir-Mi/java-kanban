public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

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
        Subtask testSubtask2 = new Subtask("субтаск2", "описание субтаска2", epicId2);
        int subId2 = taskManager.createSubtask(testSubtask2, epicId2);
        Subtask testSubtask3 = new Subtask("субтаск3", "описание субтаска3", epicId2);
        int subId3 = taskManager.createSubtask(testSubtask3, epicId2);

        System.out.println("Печатаем списки задач");
        System.out.println(taskManager.getTaskList());
        System.out.println(taskManager.getEpicList());
        System.out.println(taskManager.getSubtaskList());

        Task testTask11 = new Task("задача1", "новое описание задачи1"); // обновляем задачи
        testTask11.setStatus("DONE");
        testTask11.setId(taskId1);
        taskManager.updateTask(testTask11);

        Subtask testSubtask11 = new Subtask("субтаск1", "новое описание субтаска1", epicId1);
        testSubtask11.setStatus("DONE");
        testSubtask11.setId(subId1);
        taskManager.updateSubtask(testSubtask11);
        Subtask testSubtask22 = new Subtask("субтаск2", "новое описание субтаска2", epicId2);
        testSubtask22.setStatus("DONE");
        testSubtask22.setId(subId2);
        taskManager.updateSubtask(testSubtask22);

        System.out.println("Печатаем задачи после обновления");
        System.out.println(taskManager.getTaskList());
        System.out.println(taskManager.getEpicList());
        System.out.println(taskManager.getSubtaskList());
        System.out.println("Печатаем подзадачи эпика");
        System.out.println(taskManager.getAllSubtasksByEpic(epicId2));

        taskManager.deleteTask(taskId2); // удаляем задачи
        taskManager.deleteEpic(epicId1);
        taskManager.deleteSubtask(subId3);

        System.out.println("Печатаем задачи после удаления некоторых");
        System.out.println(taskManager.getTaskList());
        System.out.println(taskManager.getEpicList());
        System.out.println(taskManager.getSubtaskList());
    }
}