package com.gft.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * Starts [block] immediately in the calling thread until its first suspension.
 *
 * ```kotlin
 * scope.launchUndispatched { loadData() }
 * ```
 *
 * - Uses [CoroutineStart.UNDISPATCHED] with [launch].
 * - Resumes after suspension using the dispatcher in the resulting coroutine context.
 * - Inherits this [CoroutineScope]'s context and applies [context] overrides.
 * - Starts even if the coroutine's [kotlinx.coroutines.Job] already has cancellation requested.
 *
 * @param context context elements to add or override; defaults to [EmptyCoroutineContext]
 * @param block work to run in the new coroutine
 * @return [kotlinx.coroutines.Job] for the launched coroutine
 */
fun CoroutineScope.launchUndispatched(context: CoroutineContext = EmptyCoroutineContext, block: suspend CoroutineScope.() -> Unit) =
    launch(start = CoroutineStart.UNDISPATCHED, block = block, context = context)

/**
 * Starts collecting this [Flow] in [scope] immediately until the first suspension.
 *
 * ```kotlin
 * events.onEach { handle(it) }.launchUndispatchedIn(scope)
 * ```
 *
 * - Uses [launchUndispatched] and [collect]; discards emitted values.
 * - Runs upstream side effects such as [kotlinx.coroutines.flow.onEach].
 * - Follows [scope]'s lifetime and exception handling.
 *
 * @param scope scope in which to collect this [Flow]
 * @return [kotlinx.coroutines.Job] for the collection coroutine
 */
fun <T> Flow<T>.launchUndispatchedIn(scope: CoroutineScope) = scope.launchUndispatched { collect() }
