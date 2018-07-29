package ru.prochiy.services;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.prochiy.priority.PriorityExecutorService;
import ru.prochiy.priority.PriorityExecutors;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: amarinin
 * Date: 8/25/15
 * Time: 2:30 PM
 * To change this template use File | Settings | File Templates.
 */
@Configuration
class PriorityRestService
{
    private static Logger log = Logger.getLogger(PriorityRestService.class.getName());
    private PriorityExecutorService priorityExecutorService = PriorityExecutors.newPriorityFixedThreadPool(2);


    @Bean(name = "restHighPriority")
    public ExecutorService geHighPriorityExecutor(){
        return getPriorityExecutor(Thread.MAX_PRIORITY);
    }

    @Bean(name = "restLowPriority")
    public ExecutorService getLowPriorityExecutor(){
        return getPriorityExecutor(Thread.MIN_PRIORITY);
    }

    public ExecutorService getPriorityExecutor(final int priority){
        //return priorityExecutorService;
        return new ExecutorService() {
            @Override
            public void execute(Runnable command) {
                log.info("Executor getExecutorHighPriority()");
                priorityExecutorService.execute(command);
            }

            @Override
            public void shutdown() {
                priorityExecutorService.shutdown();
            }

            @Override
            public List<Runnable> shutdownNow() {
                return priorityExecutorService.shutdownNow();
            }

            @Override
            public boolean isShutdown() {
                return priorityExecutorService.isShutdown();
            }

            @Override
            public boolean isTerminated() {
                return priorityExecutorService.isTerminated();
            }

            @Override
            public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
                return priorityExecutorService.awaitTermination(timeout, unit);
            }

            @Override
            public <T> Future<T> submit(Callable<T> task) {
                return priorityExecutorService.submit(task, priority);
            }

            @Override
            public <T> Future<T> submit(Runnable task, T result) {
                return priorityExecutorService.submit(task, result, priority);
            }

            @Override
            public Future<?> submit(Runnable task) {
                return priorityExecutorService.submit(task, priority);
            }

            @Override
            public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
                return priorityExecutorService.invokeAll(tasks);
            }

            @Override
            public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
                return invokeAll(tasks, timeout, unit);
            }

            @Override
            public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
                return invokeAny(tasks);
            }

            @Override
            public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
                return invokeAny(tasks, timeout, unit);
            }
        };
    }
    
}
