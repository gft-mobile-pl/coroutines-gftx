package com.gft.coroutines.flow

import kotlinx.coroutines.flow.flow
import kotlin.jvm.JvmName

/**
 * Creates a cold [kotlinx.coroutines.flow.Flow] that emits the result of [producer].
 *
 * ```kotlin
 * val greeting = flowFrom { "Hello" }
 * greeting.collect { println(it) }
 * ```
 *
 * - Runs [producer] once per collection, in the collecting coroutine.
 * - Emits one value and completes when [producer] returns successfully.
 * - Propagates exceptions from [producer] to the collector.
 *
 * @param producer suspending function that supplies the value for each collection
 * @return a flow that runs [producer] and emits its result on each collection
 */
@JvmName("flowFromItem")
fun <T> flowFrom(producer: suspend () -> T) = flow { emit(producer()) }
