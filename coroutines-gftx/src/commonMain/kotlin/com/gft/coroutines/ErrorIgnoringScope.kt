package com.gft.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

@Suppress("FunctionName")
fun ErrorIgnoringScope(dispatcher: CoroutineDispatcher = Dispatchers.Default) = CoroutineScope(
    SupervisorJob() + dispatcher + CoroutineExceptionHandler { _, error ->
        println("Throwable caught in ErrorIgnoringScope: $error")
    }
)

