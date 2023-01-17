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
        Subtask testSubtask2 = new Subtask("субтаск2", "описание субтаска2", epicId2);
        int subId2 = taskManager.createSubtask(testSubtask2, epicId2);
        Subtask testSubtask3 = new Subtask("субтаск3", "описание субтаска3", epicId2);
        int subId3 = taskManager.createSubtask(testSubtask3, epicId2);

        taskManager.getTaskById(taskId1);  // проверяем как формируется список
        System.out.println(taskManager.getHistory());
        taskManager.getTaskById(taskId2);
        System.out.println(taskManager.getHistory());
        taskManager.deleteTask(taskId1); // удаляем таск, проверяем чтоб он остался в истории
        System.out.println(taskManager.getHistory());
        taskManager.getEpicById(epicId1);
        System.out.println(taskManager.getHistory());
        taskManager.getEpicById(epicId2);
        System.out.println(taskManager.getHistory());
        taskManager.getTaskById(taskId1);
        System.out.println(taskManager.getHistory());
        taskManager.getSubtaskById(subId1);
        System.out.println(taskManager.getHistory());
        taskManager.getSubtaskById(subId2);
        System.out.println(taskManager.getHistory());
        taskManager.getSubtaskById(subId3);
        System.out.println(taskManager.getHistory());
        taskManager.getSubtaskById(subId1);
        System.out.println(taskManager.getHistory());
        taskManager.getTaskById(taskId1);
        System.out.println(taskManager.getHistory());
        taskManager.getTaskById(taskId2);
        System.out.println(taskManager.getHistory());
        taskManager.getSubtaskById(subId2);
        System.out.println(taskManager.getHistory());
    }
}