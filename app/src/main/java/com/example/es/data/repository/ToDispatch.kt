package com.example.es.data.repository

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

interface ToDispatch {

    fun launchUI(scope: CoroutineScope, block: suspend CoroutineScope.() -> Unit): Job
    fun launchIO(scope: CoroutineScope, block: suspend CoroutineScope.() -> Unit): Job

    class Base : ToDispatch {
        override fun launchUI(scope: CoroutineScope, block: suspend CoroutineScope.() -> Unit) =
            scope.launch(Dispatchers.Main, block = block)

        override fun launchIO(scope: CoroutineScope, block: suspend CoroutineScope.() -> Unit) =
            scope.launch(Dispatchers.IO, block = block)
    }
}