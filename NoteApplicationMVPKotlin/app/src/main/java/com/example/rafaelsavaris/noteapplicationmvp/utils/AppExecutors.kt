package com.example.rafaelsavaris.noteapplicationmvp.utils

import java.util.concurrent.Executor
import java.util.concurrent.Executors

const val THREAD_COUNT = 3

open class AppExecutors constructor(
        val diskIO: Executor = DiskIOThreadExecutor(),
        val networkIO: Executor = Executors.newFixedThreadPool(THREAD_COUNT),
        val mainThread: Executor = MainThreadExecutor()
)
