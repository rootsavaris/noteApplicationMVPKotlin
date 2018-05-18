package com.example.rafaelsavaris.noteapplicationmvpkotlin.dara.source.remote

import com.example.rafaelsavaris.noteapplicationmvpkotlin.data.NotesDataSource
import com.example.rafaelsavaris.noteapplicationmvpkotlin.data.model.Note
import com.google.common.collect.Lists

class MockRemoteDataSource private constructor(): NotesDataSource {

    private val NOTES_DATA = LinkedHashMap<String, Note>()

    override fun getNotes(callback: NotesDataSource.LoadNotesCallback) {
        callback.onNotesLoaded(Lists.newArrayList(NOTES_DATA.values))
    }

    override fun deleteAllNotes() {
        NOTES_DATA.clear()
    }

    override fun saveNote(note: Note) {
        NOTES_DATA[note.id] = note
    }

    override fun refreshNotes() {}

    override fun markNote(note: Note) {

        val newNote = Note(note.title, note.text, note.id).apply {
            isMarked = true
        }

        NOTES_DATA[note.id] = newNote

    }

    override fun unMarkNote(note: Note) {

        val newNote = Note(note.title, note.text, note.id).apply {
            isMarked = false
        }

        NOTES_DATA[note.id] = newNote

    }

    override fun clearMarkedNotes() {

        with(NOTES_DATA.iterator()){

            while (hasNext()){
                if (next().value.isMarked){
                    remove()
                }
            }

        }

    }

    override fun getNote(noteId: String, callback: NotesDataSource.GetNoteCallback) {

        val note = NOTES_DATA[noteId]

        if (note != null){
            callback.onNoteLoaded(note)
        } else {
            callback.onDataNotAvailable()
        }

    }

    override fun deleteNote(noteId: String) {
        NOTES_DATA.remove(noteId)
    }

    override fun markNote(noteId: String) {
    }

    override fun unMarkNote(noteId: String) {
    }

    companion object {

        private var INSTANCE: MockRemoteDataSource? = null

        @JvmStatic fun getInstance(): MockRemoteDataSource {

            if (INSTANCE == null) {
                INSTANCE = MockRemoteDataSource()
            }

            return INSTANCE!!

        }

        fun clearInstance(){
            INSTANCE = null
        }

    }


}