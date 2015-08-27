package priority;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: amarinin
 * Date: 8/25/15
 * Time: 11:33 AM
 * To change this template use File | Settings | File Templates.
 */
public class PriorityExecutors
{
    private static Logger log = Logger.getLogger(PriorityExecutors.class.getName());
    public static PriorityExecutorService newPriorityFixedThreadPool(int nThreads)
    {
        log.info("PriorityExecutorService newPriorityFixedThreadPool(int nThreads)");
        return new PriorityThreadPoolExecutor(nThreads, nThreads, 0L, TimeUnit.MILLISECONDS);
    }

    public static PriorityExecutorService newPriorityFixedThreadPool(int nThreads, ThreadFactory threadFactory)
    {
        return new PriorityThreadPoolExecutor(nThreads, nThreads, 0L, TimeUnit.MILLISECONDS, threadFactory);
    }

    public static PriorityExecutorService newPrioritySingleThreadPool()
    {
        return new PriorityThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS);
    }

    public static PriorityExecutorService newPrioritySingleThreadPool(ThreadFactory threadFactory)
    {
        return new PriorityThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, threadFactory);
    }

    public static PriorityExecutorService newPriorityCachedThreadPool()
    {
        return new PriorityThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS);
    }

    public static PriorityExecutorService newPriorityCachedThreadPool(ThreadFactory threadFactory)
    {
        return new PriorityThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, threadFactory);
    }
}
