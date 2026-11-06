package com.gft.coroutines

import kotlinx.coroutines.async
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.selects.select

/**
 * Returns the result of the action that finishes or throws first.
 * This operator is sometimes called `race`.
 *
 * ```kotlin
 * val response = firstOf({ primary.fetch() }, { backup.fetch() })
 * ```
 *
 * - Starts each action in a child coroutine within [coroutineScope].
 * - Uses [select] to choose a completed action. When several clauses are ready, [select] favors
 *   the first in registration order: [action1], [action2], then [otherActions] in array order.
 * - Cancels remaining children after selecting a successful result. Cancellation is cooperative.
 * - Propagates the selected action's exception. A failure other than
 *   [kotlinx.coroutines.CancellationException] in any child cancels the scope and fails [firstOf].
 * - Propagates caller cancellation to all children.
 * - Waits for all children to finish, including cancellation cleanup, before returning or throwing.
 *
 * @param action1 first action to race
 * @param action2 second action to race
 * @param otherActions additional actions to race
 * @return result chosen by [select] if the scope completes successfully
 */
suspend fun <T : Any?> firstOf(
    action1: suspend () -> T,
    action2: suspend () -> T,
    vararg otherActions: suspend () -> T,
): T = coroutineScope {
    select {
        async { action1() }.onAwait { it }
        async { action2() }.onAwait { it }
        otherActions.forEach { action ->
            async { action() }.onAwait { it }
        }
    }.also {
        coroutineContext.cancelChildren()
    }
}
