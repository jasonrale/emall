package com.emall.utils;

import lombok.Data;

import java.util.concurrent.Future;

/**
 * 可以存储Future的Runnable
 */
@Data
public abstract class FutureRunnable implements Runnable {
    private Future<?> future;
}
