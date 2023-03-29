package manager.http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import manager.tasks.FileBackedTasksManager;
import manager.tasks.TaskManager;
import task.Epic;
import task.Subtask;
import task.Task;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

public class HttpTaskServer {

    private HttpServer httpServer;
    private TaskManager manager;
    private static final int PORT = 8080;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private final Gson gson;

    public HttpTaskServer() throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        manager = FileBackedTasksManager.loadFromFile(Path.of("src/resources/saveFile.csv"));
        httpServer.createContext("/tasks", this::handle);
        gson = new Gson();
    }

    public void start() {
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
        httpServer.start();
    }

    public void stop() {
        httpServer.stop(0);
        System.out.println("Остановили сервер на порту" + PORT);
    }

    private void writeResponse(HttpExchange exchange,
                               String responseString,
                               int responseCode) throws IOException {
        if (responseString.isBlank()) {
            exchange.sendResponseHeaders(responseCode, 0);
        } else {
            byte[] bytes = responseString.getBytes(DEFAULT_CHARSET);
            exchange.sendResponseHeaders(responseCode, bytes.length);
            exchange.getResponseHeaders().add("Content-Type", "application/json");
            exchange.getResponseBody().write(bytes);
        }
    }

    public void handle(HttpExchange exchange) {
        final String path = exchange.getRequestURI().getPath().replaceFirst("/tasks/", "");
        try {
            if (path.contains("task")) {
                taskHandler(exchange);
            } else if (path.contains("subtask")) {
                subtaskHandler(exchange);
            } else if (path.contains("epic")) {
                epicHandler(exchange);
            } else if (path.contains("history")) {
                historyHandler(exchange);
            } else if (path.isEmpty()) {
                tasksHandler(exchange);
            } else {
                System.out.println("Неверный запрос");
            }
        } catch (IOException e) {
            System.out.println("Ошибка запроса"); // логирование не проходили, а сам не разобрался(
        }
        exchange.close();
    }

    private void taskHandler(HttpExchange exchange) throws IOException {
        try {
            System.out.println("taskHandler works");
            String method = exchange.getRequestMethod();
            String query = exchange.getRequestURI().getQuery();
            String body = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
            switch (method) {
                case "GET":
                    if (query == null) {
                        String response = gson.toJson(manager.getTaskList());
                        writeResponse(exchange, response, 200);
                    } else {
                        String idString = query.substring(3);
                        int id = Integer.parseInt(idString);
                        String response = gson.toJson(manager.getTaskById(id));
                        writeResponse(exchange, response, 200);
                    }
                    break;
                case "PUT":
                    Task task = gson.fromJson(body, Task.class);
                    if (manager.getTaskById(task.getId()) == null) {
                        manager.createTask(task);
                        String response = "Задача успешно добавлена";
                        writeResponse(exchange, response, 201);
                    } else {
                        manager.updateTask(task);
                        String response = "Задача успешно обновлена";
                        writeResponse(exchange, response, 201);
                    }
                    break;
                case "DELETE":
                    if (query != null) {
                        String idString = query.substring(3); // ловить ошибки
                        int id = parseId(idString);
                        manager.deleteTask(id);
                        String response = "Задача успешно удалена";
                        writeResponse(exchange, response, 200);
                    }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            exchange.close();
        }
    }

    private void subtaskHandler(HttpExchange exchange) throws IOException {
        try {
            System.out.println("subtaskHandler works");
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();
            String query = exchange.getRequestURI().getQuery();
            String body = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
            switch (method) {
                case "GET":
                    if (query == null) {
                        String response = gson.toJson(manager.getSubtaskList());
                        writeResponse(exchange, response, 200);
                    } else if (path.contains("epic")) {
                        String idString = query.substring(3);
                        int id = parseId(idString);
                        String response = gson.toJson(manager.getAllSubtasksByEpic(id));
                        writeResponse(exchange, response, 200);
                    } else {
                        String idString = query.substring(3);
                        int id = parseId(idString);
                        String response = gson.toJson(manager.getSubtaskById(id));
                        writeResponse(exchange, response, 200);
                    }
                    break;
                case "PUT":
                    Subtask subtask = gson.fromJson(body, Subtask.class);
                    if (subtask.getId() == 0) {
                        manager.createSubtask(subtask, subtask.getEpicId());
                        String response = "Подзадача успешно добавлена";
                        writeResponse(exchange, response, 201);
                    } else {
                        manager.updateSubtask(subtask);
                        String response = "Подзадача успешно обновлена";
                        writeResponse(exchange, response, 201);
                    }
                    break;
                case "DELETE":
                    String idString = query.substring(3);
                    int id = parseId(idString);
                    manager.deleteSubtask(id);
                    String response = "Задача успешно удалена";
                    writeResponse(exchange, response, 200);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            exchange.close();
        }
    }

    private void epicHandler(HttpExchange exchange) throws IOException {
        try {
            System.out.println("epicHandler works");
            String method = exchange.getRequestMethod();
            String query = exchange.getRequestURI().getQuery();
            String body = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
            switch (method) {
                case "GET":
                    if (query == null) {
                        String response = gson.toJson(manager.getEpicList());
                        writeResponse(exchange, response, 200);
                    } else {
                        String idString = query.substring(3);
                        int id = parseId(idString);
                        String response = gson.toJson(manager.getEpicById(id));
                        writeResponse(exchange, response, 200);
                    }
                    break;
                case "PUT":
                    Epic epic = gson.fromJson(body, Epic.class);
                    if (epic.getId() == 0) {
                        manager.createEpic(epic);
                        String response = "Эпик успешно добавлен";
                        writeResponse(exchange, response, 201);
                    } else {
                        manager.updateEpic(epic);
                        String response = "Эпик успешно обновлен";
                        writeResponse(exchange, response, 201);
                    }
                    break;
                case "DELETE":
                    String idString = query.substring(3);
                    int id = parseId(idString);
                    manager.deleteEpic(id);
                    String response = "Эпик успешно удален";
                    writeResponse(exchange, response, 200);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            exchange.close();
        }
    }

    private void historyHandler(HttpExchange exchange) throws IOException {
        try {
            System.out.println("historyHandler works");
            String method = exchange.getRequestMethod();
            if (method.equals("GET")) {
                String response = gson.toJson(manager.getHistory());
                writeResponse(exchange, response, 201);
            } else {
                System.out.println("Ожидался метод GET");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void tasksHandler(HttpExchange exchange) throws IOException {
        try {
            System.out.println("tasksHandler works");
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();
            switch (method) {
                case "DELETE":
                    if (path.equals("tasks/delete")) {
                        manager.deleteAllTasks();
                        String response = "Все задачи удалены";
                        writeResponse(exchange, response, 200);
                    } else {
                        String response = "Указан неверный путь";
                        writeResponse(exchange, response, 400);
                    }
                    break;
                case "GET":
                    if (path.equals("tasks/prioritized")) {
                        String response = gson.toJson(manager.getPrioritizedTasks());
                        writeResponse(exchange, response, 200);
                    } else {
                        String response = "Указан неверный путь";
                        writeResponse(exchange, response, 400);
                    }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            exchange.close();
        }
    }

    private int parseId(String path) {
        try {
            return Integer.parseInt(path);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
