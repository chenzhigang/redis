package org.czg.redis.center.biz.list.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author czg
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class QueueListService {

    private final StringRedisTemplate stringRedisTemplate;

    private final static String TASK_KEY = "task:test";

    public void push(String taskId) {
        stringRedisTemplate.opsForList().leftPush(TASK_KEY, taskId);
    }

    /**
     * 使用定时任务定期检查队列是否有新任务。可以根据需要调整间隔时间
     */
    @Scheduled(fixedDelay = 1000)
    public void processTasks() {
        // 使用RPOP操作从队列尾部移除并返回一个任务。如果队列为空，则返回空列表。
        List<String> tasks = stringRedisTemplate.opsForList().rightPop(TASK_KEY, 1);
        // 如果队列中有任务，则处理它。这里只是简单地打印出来，实际应用中你需要根据业务逻辑来处理这些任务。
        if (ObjectUtils.isNotEmpty(tasks)) {
            // 假设任务的唯一标识是List中的第一个元素。根据实际情况可能需要调整这部分代码。
            String taskId = tasks.getFirst();
            // 处理任务的逻辑...例如：处理Task对象等。

            log.info("Processing task with ID: {}", taskId);
        } else {
            // 如果队列为空，可以记录日志或做其他处理。
            log.info("暂未发现任务……");
        }
    }

}
