package com.nectavox.nxcore.interfaces;

import java.util.Objects;
import java.util.function.Consumer;

public interface SchedulerAdapter {

    TaskHandle execute(Runnable task);
    TaskHandle executeAsync(Runnable task);

    TaskHandle runLater(Runnable task, long delayTicks);
    TaskHandle runLaterAsync(Runnable task, long delayTicks);

    TaskHandle runTimer(Runnable task, long delayTicks, long periodTicks);
    TaskHandle runTimerAsync(Runnable task, long delayTicks, long periodTicks);



    TaskHandle executeForEntity(Runnable task, Object entity);

    TaskHandle runLaterForEntity(Runnable task, long delayTicks, Object entity);

    TaskHandle runTimerForEntity(Runnable task, long delayTicks, long periodTicks, Object entity);
}