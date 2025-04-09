package com.aslmmovic.bosta_task.common

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

// Simplified debounced click listener extension
@OptIn(FlowPreview::class)
fun View.debouncedClick(
    debounceTime: Long = 100,
    scope: CoroutineScope = CoroutineScope(Dispatchers.Main.immediate),
    onClickListener: suspend (View) -> Unit
) {
    val clickFlow = MutableSharedFlow<View>()
    val job = scope.launch {
        clickFlow
            .debounce(debounceTime)
            .collectLatest { onClickListener(it) }
    }
    setOnClickListener { scope.launch { clickFlow.emit(this@debouncedClick) } } // Emit 'this' view

    // Cleanup
    this.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
        override fun onViewAttachedToWindow(v: View) {}
        override fun onViewDetachedFromWindow(v: View) { job.cancel() }
    })
}

