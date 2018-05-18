package com.example.rafaelsavaris.noteapplicationmvpkotlin.utils

import java.util.concurrent.Executor
import java.util.concurrent.Executors

class DiskIOThreadExecutor : Executor {

    private val diskIO = Executors.newCachedThreadPool()

    override fun execute(command: Runnable?) {
        diskIO.execute(command)
    }

}