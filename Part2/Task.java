package Part2;

import java.util.Comparator;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class Task extends FutureTask implements Comparable<FutureTask> {
    TaskType priority;

    public Task(Callable mission, TaskType priority) {
        super(mission);
        this.priority = priority;
    }
    public Task(Runnable mission, TaskType priority) {
        super(mission, null);
        this.priority = priority;
    }

    public static Task createTask(Callable c, TaskType priority) {
        return new Task(c, priority);
    }
    public static Task createTask(Runnable r, TaskType priority) {
        return new Task(r, priority);
    }
    public static Task createTask(Callable c) {
        return new Task(c, TaskType.OTHER);
    }
    public static Task createTask(Runnable r) {
        return new Task(r, TaskType.OTHER);
    }

    @Override
    public int compareTo(FutureTask o) {
        if (o.getClass() == this.getClass())
            return Integer.compare(this.priority.getPriorityValue(), ((Task) o).priority.getPriorityValue());
        else
            throw new RuntimeException("Comparing a task to a non task!");
    }
}