package com.example.rafaelsavaris.noteapplicationmvp.data.source.remote

import android.os.Handler
import com.example.rafaelsavaris.noteapplicationmvp.data.NotesDataSource
import com.example.rafaelsavaris.noteapplicationmvp.data.model.Note
import com.google.common.collect.Lists

object NotesRemoteDataSource : NotesDataSource{

    private const val SERVICE_LATENCY = 5000L

    private var NOTES_SERVICE_DATA = LinkedHashMap<String, Note>(2)

    init {
        addNote("NOTE1", "TEXT1")
        addNote("NOTE2", "TEXT2")
    }

    override fun getNotes(callback: NotesDataSource.LoadNotesCallback) {

        val notes = Lists.newArrayList(NOTES_SERVICE_DATA.values)

        Handler().postDelayed({callback.onNotesLoaded(notes)}, SERVICE_LATENCY)

    }

    override fun saveNote(note: Note) {
        NOTES_SERVICE_DATA[note.id] = note
    }

    override fun deleteAllNotes() {
        NOTES_SERVICE_DATA.clear()
    }

    private fun addNote(title: String, text: String){
        val note = Note(title, text)
        NOTES_SERVICE_DATA[note.id] = note
    }

}