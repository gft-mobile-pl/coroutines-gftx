# Coroutines GFTX

A set of tools, add-ons, and extension functions that enhance capabilities and streamline working with Kotlin Coroutines.
This library is usually required by other GFT libraries.

## Content

### Coroutines
- `CoroutineScope.launchUndispatched(context: CoroutineContext = EmptyCoroutineContext, block: suspend CoroutineScope.() -> Unit)`
- `suspend fun <T> interruptable(block: () -> T): T`
- `suspend fun <T : Any?> firstOf(action1: suspend () -> T, action2: suspend () -> T, vararg otherActions: suspend () -> T, ): T`
- `ErrorIgnoringScope(dispatcher: CoroutineDispatcher = Dispatchers.Default): CoroutineScope`

### Flows
- `flowFrom(producer: suspend () -> T): Flow<T>`
- `Flow<T>.ignoreElements()`
- `Flow<T>.throttleLatest(samplingRate: Duration): Flow<T>`
- `Flow<T>.toStateFlow(initialValue: T, scope: CoroutineScope, updateValueOnGet: Boolean = false): StateFlow<T>`
