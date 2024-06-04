package com.gft.coroutines

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.Channel
import kotlin.concurrent.thread

/**
 * Launches the provided block on a separate thread and suspends until the result is provided.
 * If the coroutine is cancelled, the underlying thread is interrupted. You may use `Thread.interrupted()` inside the scope of the provided block
 * to check whether the coroutine has been cancelled.
 * This method is especially useful when dealing with non-coroutine code (e.g., from third-party libraries),
 * which does not abide by the rules of coroutine cancellation.
 */
suspend fun <T> interruptable(block: () -> T): T {
    val result = Channel<T>()
    val workerThread = thread(
        isDaemon = true
    ) {
        try {
            result.trySend(block())
        } catch (error: Throwable) {
            result.close(error)
        }
    }
    return try {
        result.receive()
    } catch (error: Throwable) {
        if (error is CancellationException) {
            workerThread.interrupt()
        }
        throw error
    }
}
