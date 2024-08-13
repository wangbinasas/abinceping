package com.abin.app.micro_app.common.util;

/**
 * @Author: Wangbin02
 * @Date: 2024/8/13
 * @Desc:
 */
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RateLimiter {

    private Map<String, Long> lastAccessMap = new HashMap<>();
    private ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

    public RateLimiter(int expirySeconds) {
        executorService.scheduleAtFixedRate(() -> {
            lastAccessMap.clear();
        }, expirySeconds, expirySeconds, TimeUnit.SECONDS);
    }

    public boolean isAllowed(String key) {
        Long lastAccessTime = lastAccessMap.get(key);
        if (lastAccessTime == null || System.currentTimeMillis() - lastAccessTime > TimeUnit.SECONDS.toMillis(10)) {
            lastAccessMap.put(key, System.currentTimeMillis());
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        RateLimiter rateLimiter = new RateLimiter(4);
        System.out.println(rateLimiter.isAllowed("key1"));
        System.out.println(rateLimiter.isAllowed("key1"));
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(rateLimiter.isAllowed("key1"));
    }
}
