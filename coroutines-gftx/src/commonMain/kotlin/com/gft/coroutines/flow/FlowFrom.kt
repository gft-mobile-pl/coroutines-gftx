package com.gft.coroutines.flow

import kotlinx.coroutines.flow.flow
import kotlin.jvm.JvmName

@JvmName("flowFromItem")
fun <T> flowFrom(producer: suspend () -> T) = flow { emit(producer()) }
