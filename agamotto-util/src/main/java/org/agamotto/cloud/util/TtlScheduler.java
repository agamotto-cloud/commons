/*
 * Copyright 2013-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.agamotto.cloud.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;

@Slf4j
public class TtlScheduler {

    private final TaskScheduler scheduler = new ConcurrentTaskScheduler(Executors.newSingleThreadScheduledExecutor());

    private final Map<String, ScheduledFuture<?>> serviceHeartbeats = new ConcurrentHashMap<>();



    public void add(String runnableName, Runnable runnable) {
        ScheduledFuture<?> task = this.scheduler.scheduleAtFixedRate(runnable, Duration.ofSeconds(30));
        ScheduledFuture<?> previousTask = this.serviceHeartbeats.put(runnableName, task);
        if (previousTask != null) {
            previousTask.cancel(true);
        }
    }

    public void remove(String runnableName) {
        ScheduledFuture<?> task = this.serviceHeartbeats.get(runnableName);
        if (task != null) {
            task.cancel(true);
        }
        this.serviceHeartbeats.remove(runnableName);
    }
}
