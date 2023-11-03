package com.kobez.chatmodule.util

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/**
 * Converts a [StateFlow] to compose [State].
 *
 * @param coroutineScope The [CoroutineScope] in which to launch the coroutine in.
 */
internal fun <T> StateFlow<T>.asState(coroutineScope: CoroutineScope): State<T> {
    val state = mutableStateOf(this.value)
    onEach { state.value = it }.launchIn(coroutineScope)
    return state
}

/**
 * Converts a [Flow] to compose [State].
 *
 * @param coroutineScope The [CoroutineScope] in which to launch the coroutine in.
 */
internal fun <T> Flow<T>.asState(coroutineScope: CoroutineScope, defaultValue: T): State<T> {
    val state = mutableStateOf(defaultValue)
    onEach { state.value = it }.launchIn(coroutineScope)
    return state
}
