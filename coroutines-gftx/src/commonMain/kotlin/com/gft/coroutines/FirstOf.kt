package com.gft.coroutines

import kotlinx.coroutines.async
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.selects.select

/**
 * Returns the result of the action that finishes or throws first.
 * This operator is sometimes called `race`.
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
