package com.micro.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledFuture;

@Service
@EnableAsync
@EnableScheduling
@RequiredArgsConstructor
public class DynamicSchedulerService {

    private final ThreadPoolTaskScheduler threadPoolTaskScheduler;
    private Map<String, ScheduledFuture<?>> scheduledTasks = new HashMap<>();
    private final ConnectionService connectionService;
    private final MicroControllerService microControllerService;

    @Async
    public synchronized CompletableFuture<Void> startTask(String taskName, long updateTime) {
        return CompletableFuture.runAsync(() -> {
            if(scheduledTasks.get(taskName) != null) {
                stopTask(taskName);
            }
            ScheduledFuture<?> futureTask = null;

            switch (taskName) {
                case "temperatureTask":
                    futureTask = threadPoolTaskScheduler.scheduleWithFixedDelay(this::temperatureTask, updateTime);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown task name: " + taskName);
            }

            scheduledTasks.put(taskName, futureTask);
        });
    }

    public synchronized void stopTask(String taskName) {
        ScheduledFuture<?> existingTask = scheduledTasks.get(taskName);
        if (existingTask != null) {
            existingTask.cancel(true);
            scheduledTasks.remove(taskName);
        } else {
            throw new IllegalArgumentException("Unknown task name: " + taskName);
        }
    }

    private void temperatureTask() {
        microControllerService.sensor("patric", "temperature");
    }

    public void scheduleOneTimeTask() {
        Calendar currentDate = Calendar.getInstance();
        currentDate.add(Calendar.MINUTE, 2);
        Date twoHoursLater = currentDate.getTime();

        threadPoolTaskScheduler.schedule(this::executeOneTimeTask, twoHoursLater);
    }

    private void executeOneTimeTask() {
        System.out.println("Задача выполнена!");
    }
}
