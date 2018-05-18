package com.example.rafaelsavaris.noteapplicationmvpkotlin.data

import com.example.rafaelsavaris.noteapplicationmvpkotlin.data.model.Note


interface NotesDataSource{

    interface LoadNotesCallback{

        fun onNotesLoaded(notes: List<Note>)

        fun onDataNotAvailable()

    }

    interface GetNoteCallback{

        fun onNoteLoaded(note: Note)

        fun onDataNotAvailable()

    }

    fun getNotes(callback: LoadNotesCallback)

    fun getNote(noteId: String, callback: GetNoteCallback)

    fun refreshNotes()

    fun deleteAllNotes()

    fun saveNote(note: Note)

    fun markNote(note: Note)

    fun markNote(noteId: String)

    fun unMarkNote(note: Note)

    fun unMarkNote(noteId: String)

    fun clearMarkedNotes()

    fun deleteNote(noteId: String)

}