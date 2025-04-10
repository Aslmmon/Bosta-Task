package com.aslmmovic.bosta_task.common

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*

@OptIn(FlowPreview::class)
fun View.debouncedClick(delay: Long = 100L, action: () -> Unit) {
    callbackFlow {
        setOnClickListener {
            trySend(Unit)
        }
        awaitClose { setOnClickListener(null) }
    }
        .debounce(delay)
        .onEach { action() }
        .launchIn(CoroutineScope(Dispatchers.Main.immediate))
}
