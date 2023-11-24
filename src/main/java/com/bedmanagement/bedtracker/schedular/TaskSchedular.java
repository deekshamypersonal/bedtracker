package com.bedmanagement.bedtracker.schedular;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.concurrent.ScheduledFuture;

@Component
public class TaskSchedular {

    @Autowired
    TaskScheduler taskScheduler;

    public void scheduleATask(LocalDateTime dateTime,String email,String reason,Boolean deleteFromCloud) {
        Runnable runnable=new BookingCancel(email,reason,deleteFromCloud);
        Instant t=dateTime.atZone(ZoneId.systemDefault()).toInstant();
        ScheduledFuture<?> scheduledTask = taskScheduler.schedule(runnable,t);
    }
}
