package com.gft.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

fun CoroutineScope.launchUndispatched(context: CoroutineContext = EmptyCoroutineContext, block: suspend CoroutineScope.() -> Unit) =
    launch(start = CoroutineStart.UNDISPATCHED, block = block, context = context)

fun <T> Flow<T>.launchUndispatchedIn(scope: CoroutineScope) = scope.launchUndispatched { collect() }
