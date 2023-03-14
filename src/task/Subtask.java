package task;

import java.time.Duration;
import java.time.Instant;

public class Subtask extends Task {
    private int epicId;

    public Subtask(String title, String description, int epicId, Instant startTime, Duration duration) {
        super(title, description, startTime, duration);
        this.epicId = epicId;
    }

    public Subtask(int id, TaskStatus status, String title, String description, Instant startTime, Duration duration, int epicId) {
        super(id, status, title, description, startTime, duration);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        String result = "Task.Subtask{" +
                "title='" + super.getTitle() + '\'' +
                ", description='" + super.getDescription() + '\'' +
                ", id=" + super.getId() +
                ", status='" + super.getStatus() + '\'' +
                ", epicId =" + epicId + '}';
        return result;
    }
}