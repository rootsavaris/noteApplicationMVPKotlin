package com.example.rafaelsavaris.noteapplicationmvp.data.source.local

import com.example.rafaelsavaris.noteapplicationmvp.data.NotesDataSource
import com.example.rafaelsavaris.noteapplicationmvp.data.model.Note
import com.example.rafaelsavaris.noteapplicationmvp.utils.AppExecutors

class NotesLocalDataSource private constructor(
        val appExecutors: AppExecutors,
        val notesDao: NotesDao
) : NotesDataSource{

    override fun getNotes(callback: NotesDataSource.LoadNotesCallback) {

        appExecutors.diskIO.execute{

            val notes = notesDao.getNotes()

            appExecutors.mainThread.execute {

                if (notes.isEmpty()){
                    callback.onDataNotAvailable()
                } else{
                    callback.onNotesLoaded(notes)
                }

            }

        }

    }

    override fun saveNote(note: Note) {
        appExecutors.diskIO.execute { notesDao.insertNote(note) }
    }

    override fun deleteAllNotes() {
        appExecutors.diskIO.execute { notesDao.deleteNotes() }
    }

}