package com.gft.coroutines.flow

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.transform
import kotlin.time.Duration

fun <T> Flow<T>.throttleLatest(samplingRate: Duration) = conflate().transform {
    emit(it)
    delay(samplingRate)
}
