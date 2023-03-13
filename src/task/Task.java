package task;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

public class Task {
    private int id;
    private TaskStatus status;
    private String title;
    private String description;
    private Instant startTime;
    private Duration duration;

    public Task(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public Task(String title, String description, Instant startTime, Duration duration) {
        this.title = title;
        this.description = description;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Task(int id, TaskStatus status, String title, String description) {
        this.id = id;
        this.status = status;
        this.title = title;
        this.description = description;
    }

    public Task(int id, TaskStatus status, String title, String description, Instant startTime, Duration duration) {
        this.id = id;
        this.status = status;
        this.title = title;
        this.description = description;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Instant getEndTime(){
        return startTime.plus(duration);
    }

    public Instant getStartTime() {
        return startTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        String result = "Task.Task{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' + '}';
        return result;
    }
}
