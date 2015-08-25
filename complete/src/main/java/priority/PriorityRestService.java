package priority;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created with IntelliJ IDEA.
 * User: amarinin
 * Date: 8/25/15
 * Time: 2:30 PM
 * To change this template use File | Settings | File Templates.
 */
class PriorityRestService
{
    private volatile static PriorityExecutorService priorityExecutorService = PriorityExecutors.newPriorityFixedThreadPool(2);

    public static <T> T execute(Callable<T> callable, int priority){
        Future<T> future = priorityExecutorService.submit(callable, priority);
        T result = null;
        try {
            result = future.get();

        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ExecutionException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return result;
    }
    
}
