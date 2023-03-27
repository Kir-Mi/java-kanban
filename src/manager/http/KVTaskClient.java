package manager.http;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    public String url;
    public String apiToken;
    public HttpClient httpClient;

    public KVTaskClient (String url){
        this.url = url;
        this.httpClient = HttpClient.newHttpClient();
        register();
    }

    public void register(){
        URI uri = URI.create(url + "/register");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                apiToken = response.body();
            } else System.out.println("Не удалось получить API_TOKEN");
        } catch (InterruptedException e) {
            System.out.println("Регистрация не удалась: InterruptedException");
        } catch (IOException e){
            System.out.println("Регистрация не удалась: IOException");
        }
    }

    public void put(String key, String json){
        if (apiToken == null) {
            System.out.println("Регистрация не пройдена");
            return;
        }
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "/save/" + key + "?API_TOKEN=" + apiToken))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.statusCode());
        } catch (IOException | InterruptedException e) {
            System.out.println("Сохранить состояние менеджера не удалось");
        }
    }

    public String load(String key){
        if (apiToken == null) {
            return "Регистрация не пройдена";
        }
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "/load/" + key + "?API_TOKEN=" + apiToken))
                .GET()
                .header("Content-Type", "application/json")
                .build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.statusCode());
            return response.body();
        } catch (InterruptedException e) {
            System.out.println("Восстановить состояние менеджера не удалось: InterruptedException");
        } catch (IOException e){
            System.out.println("Восстановить состояние менеджера не удалось: IOException");
        }
        return "Ошибка восстановления менеджера";
    }
}
