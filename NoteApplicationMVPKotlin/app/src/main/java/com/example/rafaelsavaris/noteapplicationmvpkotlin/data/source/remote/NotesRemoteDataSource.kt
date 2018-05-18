package com.example.rafaelsavaris.noteapplicationmvpkotlin.data.source.remote

import android.os.Handler
import com.example.rafaelsavaris.noteapplicationmvpkotlin.data.NotesDataSource
import com.example.rafaelsavaris.noteapplicationmvpkotlin.data.model.Note
import com.google.common.collect.Lists

object NotesRemoteDataSource : NotesDataSource {

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

    override fun refreshNotes() {}

    private fun addNote(title: String, text: String){
        val note = Note(title, text)
        NOTES_SERVICE_DATA[note.id] = note
    }

    override fun markNote(note: Note) {

        val newNote = Note(note.title, note.text, note.id).apply {
            isMarked = true
        }

        NOTES_SERVICE_DATA[note.id] = newNote

    }

    override fun unMarkNote(note: Note) {

        val newNote = Note(note.title, note.text, note.id).apply {
            isMarked = false
        }

        NOTES_SERVICE_DATA[note.id] = newNote

    }

    override fun clearMarkedNotes() {

        with(NOTES_SERVICE_DATA.iterator()){

            while (hasNext()){
                if (next().value.isMarked){
                    remove()
                }
            }

        }

    }

    override fun getNote(noteId: String, callback: NotesDataSource.GetNoteCallback) {

        val note = NOTES_SERVICE_DATA[noteId]

        with(Handler()){

            if (note != null){
                postDelayed({callback.onNoteLoaded(note)}, SERVICE_LATENCY)
            } else {
                postDelayed({callback.onDataNotAvailable()}, SERVICE_LATENCY)
            }

        }

    }

    override fun markNote(noteId: String) {}

    override fun unMarkNote(noteId: String) {}

    override fun deleteNote(noteId: String) {
        NOTES_SERVICE_DATA.remove(noteId)
    }

}