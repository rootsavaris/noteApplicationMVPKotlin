package com.example.rafaelsavaris.noteapplicationmvp.data

import com.example.rafaelsavaris.noteapplicationmvp.data.model.Note

interface NotesDataSource{

    interface LoadNotesCallback{

        fun onNotesLoaded(notes: List<Note>)

        fun onDataNotAvailable()

    }

    fun getNotes(callback: LoadNotesCallback)

    fun deleteAllNotes()

    fun saveNote(note: Note)

}