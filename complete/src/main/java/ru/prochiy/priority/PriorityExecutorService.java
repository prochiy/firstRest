package ru.prochiy.priority;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * Created with IntelliJ IDEA.
 * User: amarinin
 * Date: 8/25/15
 * Time: 11:49 AM
 * To change this template use File | Settings | File Templates.
 */
public interface PriorityExecutorService extends ExecutorService
{
    public Future<?> submit(Runnable task, int priority);
    public <T> Future<T> submit(Runnable task, T result, int priority);
    public <T> Future<T> submit(Callable<T> task, int priority);
    public int getLeastPriority();
    public int getHighestPriority();
    public <T> void changePriorities(int fromPriority, int toPriority);
}

