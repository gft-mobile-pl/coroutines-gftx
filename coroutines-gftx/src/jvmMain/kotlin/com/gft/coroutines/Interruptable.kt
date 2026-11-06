package com.gft.coroutines

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.Channel
import kotlin.concurrent.thread

/**
 * Interrupts the thread running [block] when the enclosing coroutine is cancelled.
 *
 * Especially useful for non-coroutine code, such as third-party libraries, that supports thread
 * interruption but does not support coroutine cancellation.
 *
 * ```kotlin
 * val job = launch {
 *    interruptable {
 *        while (!Thread.interrupted()) {
 *             processNextChunk() // One bounded unit of non-suspending work.
 *        }
 *     }
 * }
 *
 * ...
 *
 * job.cancelAndJoin()
 * ```
 *
 * - Runs [block] on a separate daemon thread and suspends until its result is available.
 * - Calls [java.lang.Thread.interrupt] on that thread when the enclosing coroutine is cancelled.
 * - Use [java.lang.Thread.interrupted] inside [block] to check and clear the interruption flag.
 * - Requires [block] to respond to interruption for its thread to stop.
 *
 * @param block blocking work to run on a separate thread
 * @return result received from [block]
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
