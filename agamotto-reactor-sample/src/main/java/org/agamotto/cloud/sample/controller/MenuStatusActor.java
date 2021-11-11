package org.agamotto.cloud.sample.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;
import org.springframework.util.unit.DataSize;

@Slf4j
@Service
public class MenuStatusActor implements ApplicationRunner, DisposableBean {

    private volatile boolean runFlag = true;
    private Thread xx;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        xx = new Thread(() -> {
            while (runFlag) {
                log.info("内存:maxMemory:{}MB freeMemory:{}MB totalMemory:{}MB", DataSize.ofBytes(Runtime.getRuntime().maxMemory()).toMegabytes(),
                        DataSize.ofBytes(Runtime.getRuntime().freeMemory()).toMegabytes(),
                        DataSize.ofBytes(Runtime.getRuntime().totalMemory()).toMegabytes());
                ThreadGroup parentThread;
                for (parentThread = Thread.currentThread().getThreadGroup(); parentThread
                        .getParent() != null; parentThread = parentThread.getParent())
                    ;
                log.info("线程总数：{}", parentThread.activeCount());
               // Runtime.getRuntime().gc();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            log.info("关闭");
        });
        xx.start();
    }


    @Override
    public void destroy() {
        runFlag = false;
        xx.interrupt();

    }
}
