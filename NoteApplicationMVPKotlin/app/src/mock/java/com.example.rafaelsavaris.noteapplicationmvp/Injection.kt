package com.example.rafaelsavaris.noteapplicationmvp

import android.content.Context
import com.example.rafaelsavaris.noteapplicationmvp.data.NotesRepository
import com.example.rafaelsavaris.noteapplicationmvp.data.source.local.NotesDatabase
import com.example.rafaelsavaris.noteapplicationmvp.data.source.local.NotesLocalDataSource
import com.example.rafaelsavaris.noteapplicationmvp.data.source.remote.MockRemoteDataSource
import com.example.rafaelsavaris.noteapplicationmvp.utils.AppExecutors

object Injection {

    fun provideNotesRepository(context: Context): NotesRepository {

        val database = NotesDatabase.getInstance(context)

        return NotesRepository.getInstance(MockRemoteDataSource.getInstance(), NotesLocalDataSource.getInstance(AppExecutors(), database.notesDao()))

    }

}