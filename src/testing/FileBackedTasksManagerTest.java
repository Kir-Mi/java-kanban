package testing;

import manager.tasks.FileBackedTasksManager;
import manager.tasks.InMemoryTaskManager;
import manager.tasks.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

public class FileBackedTasksManagerTest extends TaskManagerTest<InMemoryTaskManager>{


    @BeforeEach
    public void setUp(){
        manager = new FileBackedTasksManager(Path.of("src/resources/saveFile.csv"));
    }

}
