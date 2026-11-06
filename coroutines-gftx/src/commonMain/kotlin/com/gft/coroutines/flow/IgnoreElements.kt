package com.gft.coroutines.flow

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

/**
 * Returns a [Flow] that collects upstream and discards every value.
 *
 * ```kotlin
 * val ignored: Flow<Unit> = flowOf(1, 2, 3).ignoreElements()
 * ignored.collect { error("Never called") }
 * ```
 *
 * - Starts collecting upstream when a collector collects the returned [Flow].
 * - Completes when upstream completes and propagates upstream exceptions and cancellation.
 *
 * @param T upstream value type
 * @param R output type; the returned [Flow] emits no values
 * @return a [Flow] that discards upstream values
 */
fun <T, R : Any?> Flow<T>.ignoreElements() = filter { false }.map<T, R> { throw IllegalStateException() }
