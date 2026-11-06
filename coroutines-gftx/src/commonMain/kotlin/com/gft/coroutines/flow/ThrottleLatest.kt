package com.gft.coroutines.flow

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.transform
import kotlin.time.Duration

/**
 * Emits the first value, then the latest value after each pause.
 *
 * ```kotlin
 * flowOf(1, 2, 3)
 *     .throttleLatest(Duration.parse("100ms"))
 *     .collect { println(it) }
 * ```
 *
 * - Emits the first available value immediately.
 * - Uses [conflate] to keep only the latest pending value while downstream processes a value
 *   and during each pause.
 * - Calls [delay] with [samplingRate] after each downstream emission returns, so downstream
 *   processing adds to the time between emissions.
 * - Emits any pending value after the pause when upstream completes normally.
 *
 * @param samplingRate pause between emissions; zero or negative durations skip the pause
 * @return a [Flow] that emits values with a pause after each emission
 */
fun <T> Flow<T>.throttleLatest(samplingRate: Duration) = conflate().transform {
    emit(it)
    delay(samplingRate)
}
