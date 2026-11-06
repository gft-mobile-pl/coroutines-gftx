package com.gft.coroutines.flow

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Creates a [StateFlow] backed by this [Flow].
 *
 * ```kotlin
 * val state = source.toStateFlow(initialValue = 0, scope = scope, updateValueOnGet = true)
 * val latestValue = state.value
 * ```
 *
 * - Collects the source only while subscribers observe the returned [StateFlow], like [stateIn]
 *   with [SharingStarted.WhileSubscribed].
 * - When [updateValueOnGet] is `true`, reading [StateFlow.value] without subscribers also tries
 *   to fetch the latest source value immediately, unlike [stateIn].
 * - Retains the last value received from the source or fetched by reading [StateFlow.value]
 *   across subscriptions.
 *
 * @param initialValue initial value of the returned [StateFlow]
 * @param scope scope for collecting the source [Flow]
 * @param updateValueOnGet whether to try to fetch the latest source value when reading
 *   [StateFlow.value] without subscribers
 * @return a [StateFlow] that retains the latest received or fetched value
 */
fun <T> Flow<T>.toStateFlow(
    initialValue: T,
    scope: CoroutineScope,
    updateValueOnGet: Boolean = false,
): StateFlow<T> = MutableStateFlowWithSource(
    initialValue = initialValue,
    source = this,
    scope = scope,
    updateValueOnGet = updateValueOnGet
)

private class MutableStateFlowWithSource<T>(
    initialValue: T,
    private val source: Flow<T>,
    private val scope: CoroutineScope,
    private val dispatcher: MutableStateFlow<T> = MutableStateFlow(initialValue),
    private val updateValueOnGet: Boolean,
) : MutableStateFlow<T> by dispatcher {
    private var subscription: Job? = null

    private val sharedSource by lazy {
        dispatcher
            .onSubscription {
                subscription = scope.launch(start = CoroutineStart.UNDISPATCHED) {
                    source.collect { item -> value = item }
                }
            }
            .onCompletion {
                val subscriptionToCancel = subscription
                subscription = null
                subscriptionToCancel?.cancel()

            }
            .shareIn(scope, SharingStarted.WhileSubscribed())
    }

    override var value: T
        get() {
            if (updateValueOnGet && subscription == null) {
                // We try to get the latest item from the source - some flows (e.g. StateFlows, buffered flows) are able to provide last item immediately.
                // This is very important during State creation (that is, when `collectAsStateWithLifecycle` is called).
                val latestValue = dispatcher.value
                var newValue = latestValue
                scope
                    .launch(start = CoroutineStart.UNDISPATCHED, context = Dispatchers.Unconfined) {
                        runCatching {
                            newValue = source.first()
                        }
                    }
                    .cancel()
                dispatcher.compareAndSet(latestValue, newValue)
            }

            return dispatcher.value
        }
        set(value) {
            dispatcher.value = value
        }

    override suspend fun collect(collector: FlowCollector<T>): Nothing = sharedSource.collect(collector)

    override val subscriptionCount: StateFlow<Int>
        get() = throw NotImplementedError("subscriptionCount is not supported!")
}
