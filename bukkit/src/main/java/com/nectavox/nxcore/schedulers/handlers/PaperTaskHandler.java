package com.nectavox.nxcore.schedulers.handlers;

import com.nectavox.nxcore.interfaces.TaskHandle;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

@RequiredArgsConstructor
public class PaperTaskHandler implements TaskHandle {
    private final ScheduledTask task;

    @Override
    public void cancel() {
        task.cancel();
    }

    @Override
    public boolean isCancelled() {
        return task.isCancelled();
    }
}
