package priority;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * Created with IntelliJ IDEA.
 * User: amarinin
 * Date: 8/25/15
 * Time: 1:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class PriorityFutureTask<T> extends FutureTask<T>
{
    private int priority;

    public PriorityFutureTask(Callable<T> callable, int priority)
    {
        super(callable);
        this.validatePriority(priority);
        this.priority = priority;
    }

    public PriorityFutureTask(Runnable runnable, T result, int priority)
    {
        super(runnable, result);
        this.validatePriority(priority);
        this.priority = priority;
    }

    public int getPriority()
    {
        return this.priority;
    }

    public void setPriority(int priority)
    {
        this.validatePriority(priority);
        this.priority = priority;
    }

    private void validatePriority(int priority)
    {
        if(priority<Thread.MIN_PRIORITY || priority>Thread.MAX_PRIORITY)
            throw new IllegalArgumentException("Priority should be between Thread.MIN_PRIORITY and Thread.MAX_PRIORITY");
    }

    @Override
    public void run()
    {
        int originalPriority = Thread.currentThread().getPriority();
        Thread.currentThread().setPriority(priority);
        super.run();
        Thread.currentThread().setPriority(originalPriority);
    }

    public String toString()
    {
        return "Priority: "+this.priority;
    }
}