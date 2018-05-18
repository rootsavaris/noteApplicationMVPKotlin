package com.example.rafaelsavaris.noteapplicationmvpkotlin

import android.content.Context
import com.example.rafaelsavaris.noteapplicationmvpkotlin.dara.source.remote.MockRemoteDataSource
import com.example.rafaelsavaris.noteapplicationmvpkotlin.data.source.NotesRepository
import com.example.rafaelsavaris.noteapplicationmvpkotlin.data.source.local.NotesDatabase
import com.example.rafaelsavaris.noteapplicationmvpkotlin.data.source.local.NotesLocalDataSource
import com.example.rafaelsavaris.noteapplicationmvpkotlin.utils.AppExecutors

object Injection {

    fun provideNotesRepository(context: Context): NotesRepository {

        val database = NotesDatabase.getInstance(context)

        return NotesRepository.getInstance(MockRemoteDataSource.getInstance(), NotesLocalDataSource.getInstance(AppExecutors(), database.notesDao()))

    }

}