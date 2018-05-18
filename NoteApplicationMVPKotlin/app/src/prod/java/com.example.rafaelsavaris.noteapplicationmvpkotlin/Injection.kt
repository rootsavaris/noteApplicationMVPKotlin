package com.example.rafaelsavaris.noteapplicationmvpkotlin

import android.content.Context
import com.example.rafaelsavaris.noteapplicationmvpkotlin.data.NotesRepository
import com.example.rafaelsavaris.noteapplicationmvpkotlin.data.source.local.NotesDatabase
import com.example.rafaelsavaris.noteapplicationmvpkotlin.data.source.local.NotesLocalDataSource
import com.example.rafaelsavaris.noteapplicationmvpkotlin.data.source.remote.NotesRemoteDataSource
import com.example.rafaelsavaris.noteapplicationmvpkotlin.utils.AppExecutors

object Injection {

    fun provideNotesRepository(context: Context): NotesRepository {

        val database = NotesDatabase.getInstance(context)

        return NotesRepository.getInstance(NotesRemoteDataSource, NotesLocalDataSource.getInstance(AppExecutors(), database.notesDao()))

    }

}