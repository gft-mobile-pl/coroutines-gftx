package com.gft.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

/**
 * Creates a [CoroutineScope] for fire-and-forget actions as an alternative to [kotlinx.coroutines.GlobalScope].
 *
 * ```kotlin
 * val scope = ErrorIgnoringScope()
 * scope.launch { recordClick() }
 * ```
 *
 * - Use [kotlinx.coroutines.launch] to respond to an action without waiting for its result.
 * - Logs uncaught exceptions with [println] instead of forwarding them to the default exception handler.
 * - Keeps the scope available for later actions after a failure.
 * - Cancel the scope with [kotlinx.coroutines.cancel] when its owner no longer needs it.
 *
 * @param dispatcher dispatcher for child coroutines; defaults to [Dispatchers.Default]
 */
@Suppress("FunctionName")
fun ErrorIgnoringScope(dispatcher: CoroutineDispatcher = Dispatchers.Default) = CoroutineScope(
    SupervisorJob() + dispatcher + CoroutineExceptionHandler { _, error ->
        println("Throwable caught in ErrorIgnoringScope: $error")
    }
)

