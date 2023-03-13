package task;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subtaskId;
    Instant endTime;



    public Epic(String title, String description) {
        super(title, description);
        subtaskId = new ArrayList<>();
    }

    public Epic(int id, TaskStatus status, String title, String description) {
        super(id, status, title, description);
    }

    public Instant getEndTime(){
        return endTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public ArrayList<Integer> getSubtaskId() {
        return subtaskId;
    }

    public void setSubtaskId(ArrayList<Integer> subtaskId) {
        this.subtaskId = subtaskId;
    }

    @Override
    public String toString() {
        String result = "Task.Epic{" +
                "title='" + super.getTitle() + '\'' +
                ", description='" + super.getDescription() + '\'' +
                ", id=" + super.getId() +
                ", status='" + super.getStatus() + '\'' +
                ", subtaskId =" + subtaskId + '}';
        return result;
    }
}
