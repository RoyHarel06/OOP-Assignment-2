package Part2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class CustomExecutor extends ThreadPoolExecutor{
    List<Runnable> active;
    int max_priority;

    public CustomExecutor () {
        super(Runtime.getRuntime().availableProcessors() / 2, Runtime.getRuntime().availableProcessors() - 1,
                300, TimeUnit.MILLISECONDS,
                new PriorityBlockingQueue());

        this.max_priority = TaskType.OTHER.getPriorityValue();
    }

    public Future submit(Task task) {
        if (task == null)
            throw new NullPointerException();

        execute(task);
        //updateMaxPriority();
        return task;
    }
    @Override
    public Future submit(Runnable r) {
        Task task = Task.createTask(r);
        return this.submit(task);
    }
    public Future submit(Runnable r, TaskType priority) {
        Task task = Task.createTask(r, priority);
        return this.submit(task);
    }
    @Override
    public Future submit(Callable c) {
        Task task = Task.createTask(c);
        return this.submit(task);
    }
    public Future submit(Callable c, TaskType priority) {
        Task task = Task.createTask(c, priority);
        return this.submit(task);
    }

    @Override
    public void beforeExecute(Thread t, Runnable r)  {
        updateMaxPriority();
        super.beforeExecute(t, r);
    }

    public void updateMaxPriority() {
        var a = this.getQueue().peek();
        if (a != null)
            this.max_priority = ((Task) a).priority.getPriorityValue();
        else
            this.max_priority = -1;
    }

    public int getCurrentMax() {
        return this.max_priority;
    }

    public void gracefullyTerminate() {
        this.shutdown();
        try {
            if (!this.awaitTermination(300, TimeUnit.MILLISECONDS)) {
                this.shutdownNow();
                if (!this.awaitTermination(300, TimeUnit.MILLISECONDS)) {
                    throw new RuntimeException("Couldn't terminate executor!");
                }
            }

        } catch (InterruptedException e) {
            this.shutdownNow();
            throw new RuntimeException(e);
        }
    }
}
