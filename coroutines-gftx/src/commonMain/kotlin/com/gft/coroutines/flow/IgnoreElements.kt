package com.gft.coroutines.flow

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

fun <T, R : Any?> Flow<T>.ignoreElements() = filter { false }.map<T, R> { throw IllegalStateException() }
