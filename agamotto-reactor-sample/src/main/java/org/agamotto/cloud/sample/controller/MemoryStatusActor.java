package org.agamotto.cloud.sample.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;
import org.springframework.util.unit.DataSize;

@Slf4j
@Component
public class MemoryStatusActor implements SmartLifecycle {

    private volatile boolean runFlag = false;
    private Thread xx;


    @Override
    public void start() {
        runFlag = true;
        xx = new Thread(() -> {
            while (runFlag) {
                ThreadGroup parentThread;
                for (parentThread = Thread.currentThread().getThreadGroup(); parentThread
                        .getParent() != null; parentThread = parentThread.getParent()) {
                }
                log.info("线程总数：{},内存:maxMemory:{}MB freeMemory:{}MB totalMemory:{}MB", parentThread.activeCount(),
                        DataSize.ofBytes(Runtime.getRuntime().maxMemory()).toMegabytes(),
                        DataSize.ofBytes(Runtime.getRuntime().freeMemory()).toMegabytes(),
                        DataSize.ofBytes(Runtime.getRuntime().totalMemory()).toMegabytes());

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ignored) {
                }
            }

            log.info("关闭");
        });
        xx.start();
    }

    @Override
    public void stop() {
        log.info("结束");
        this.runFlag = false;
        try {
            xx.interrupt();
        } catch (Exception ignored) {
        }
    }


    @Override
    public boolean isRunning() {
        return runFlag;
    }
}
