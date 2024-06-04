# Coroutines GFTX

A set of tools, add-ons, and extension functions that enhance capabilities and streamline working with Kotlin Coroutines.<br />
This library is usually required by other GFT libraries.

## Content

### Coroutines
- **launchUndispatched**<br />
  ```kotlin
  fun CoroutineScope.launchUndispatched(
      context: CoroutineContext = EmptyCoroutineContext, 
      block: suspend CoroutineScope.() -> Unit
  ): Job
  ```
- **interruptable**<br />
  ```kotlin
  suspend fun <T> interruptable(
    block: () -> T
  ): T
  ```
- **firstOf**<br />
  ```kotlin
  suspend fun <T : Any?> firstOf(
    action1: suspend () -> T, 
    action2: suspend () -> T, 
    vararg otherActions: suspend () -> T, 
  ): T
  ```
- **ErrorIgnoringScope**<br />
  ```kotlin
  fun ErrorIgnoringScope(
    dispatcher: CoroutineDispatcher = Dispatchers.Default
  ): CoroutineScope
  ```

### Flows
- **flowFrom**<br />
  ```kotlin
  fun <T> flowFrom(
    producer: suspend () -> T
  ): Flow<T>
  ```
- **ignoreElements**<br />
  ```kotlin
  fun <T, R : Any?> Flow<T>.ignoreElements(): Flow<R>
  ```
- **throttleLatest**<br />
  ```kotlin
  fun <T> Flow<T>.throttleLatest(
    samplingRate: Duration
  ): Flow<T>
  ```
- **toStateFlow**<br />
  ```kotlin
  fun <T> Flow<T>.toStateFlow(
    initialValue: T,
    scope: CoroutineScope,
    updateValueOnGet: Boolean = false,
  ): StateFlow<T>
  ```
