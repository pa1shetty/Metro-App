package com.example.nammametromvvm.utility

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
@Suppress("unused")
object Coroutines {
    fun main(work: suspend (()->Unit))=
        CoroutineScope(Dispatchers.Main).launch {
            work()
        }
    fun io(work: suspend () -> Unit)= CoroutineScope(Dispatchers.IO).launch {
        work()
    }
}