package com.example.rafaelsavaris.noteapplicationmvpkotlin.data.source

import com.example.rafaelsavaris.noteapplicationmvpkotlin.data.NotesDataSource
import com.example.rafaelsavaris.noteapplicationmvpkotlin.data.model.Note
import kotlin.collections.ArrayList

class NotesRepository (
        val notesRemoteDataSource: NotesDataSource,
        val notesLocalDataSource: NotesDataSource
) : NotesDataSource {

    var cachedNotes: LinkedHashMap<String, Note> = LinkedHashMap()

    var cacheIsDirty = false

    override fun getNotes(callback: NotesDataSource.LoadNotesCallback) {

        if (cachedNotes.isNotEmpty() && !cacheIsDirty){
            callback.onNotesLoaded(ArrayList(cachedNotes.values))
            return
        }

        if (cacheIsDirty){
            getNotesFromRemoteDataSource(callback)
        } else {

            notesLocalDataSource.getNotes(object : NotesDataSource.LoadNotesCallback {

                override fun onNotesLoaded(notes: List<Note>) {

                    refreshCache(notes)
                    callback.onNotesLoaded(ArrayList(cachedNotes.values))

                }

                override fun onDataNotAvailable() {
                    getNotesFromRemoteDataSource(callback)
                }

            })

        }

    }

    override fun deleteAllNotes() {
        notesLocalDataSource.deleteAllNotes()
        notesRemoteDataSource.deleteAllNotes()
        cachedNotes.clear()
    }

    override fun saveNote(note: Note) {

        cacheAndPerform(note, {
            notesRemoteDataSource.saveNote(it)
            notesLocalDataSource.saveNote(it)
        })

    }

    override fun refreshNotes() {
        cacheIsDirty = true
    }

    override fun markNote(note: Note) {

        cacheAndPerform(note){
            it.isMarked = true
            notesRemoteDataSource.markNote(it)
            notesLocalDataSource.markNote(it)
        }

    }

    override fun unMarkNote(note: Note) {

        cacheAndPerform(note){
            it.isMarked = false
            notesRemoteDataSource.unMarkNote(it)
            notesLocalDataSource.unMarkNote(it)
        }

    }

    override fun clearMarkedNotes() {

        notesRemoteDataSource.clearMarkedNotes()
        notesLocalDataSource.clearMarkedNotes()

        cachedNotes = cachedNotes.filterValues {
            !it.isMarked
        } as LinkedHashMap<String, Note>

    }

    override fun getNote(noteId: String, callback: NotesDataSource.GetNoteCallback) {

        val note = getNoteFromId(noteId)

        if (note != null){
            callback.onNoteLoaded(note)
            return
        }

        notesLocalDataSource.getNote(noteId, object : NotesDataSource.GetNoteCallback {

            override fun onNoteLoaded(note: Note) {

                cacheAndPerform(note){
                    callback.onNoteLoaded(note)
                }

            }

            override fun onDataNotAvailable() {

                notesRemoteDataSource.getNote(noteId, object : NotesDataSource.GetNoteCallback {

                    override fun onNoteLoaded(note: Note) {

                        cacheAndPerform(note, {
                            callback.onNoteLoaded(note)
                        })

                    }

                    override fun onDataNotAvailable() {
                        callback.onDataNotAvailable()
                    }

                })

            }

        })

    }

    override fun deleteNote(noteId: String) {
        notesRemoteDataSource.deleteNote(noteId)
        notesLocalDataSource.deleteNote(noteId)
        cachedNotes.remove(noteId)
    }

    override fun markNote(noteId: String) {
        getNoteWithId(noteId)?.let {
            markNote(it)
        }
    }

    override fun unMarkNote(noteId: String) {
        getNoteWithId(noteId)?.let {
            unMarkNote(it)
        }
    }

    private fun getNoteFromId(noteId: String) = cachedNotes[noteId]

    private fun getNotesFromRemoteDataSource(callback: NotesDataSource.LoadNotesCallback){

        notesRemoteDataSource.getNotes(object : NotesDataSource.LoadNotesCallback {

            override fun onNotesLoaded(notes: List<Note>) {
                refreshCache(notes)
                refreshLocalDatasource(notes)
                callback.onNotesLoaded(ArrayList(cachedNotes.values))
            }

            override fun onDataNotAvailable() {
                callback.onDataNotAvailable()
            }

        })

    }

    private fun refreshCache(notes: List<Note>){
        cachedNotes.clear()

        notes.forEach {
            cacheAndPerform(it, {})
        }

        cacheIsDirty = false

    }

    private fun refreshLocalDatasource(notes: List<Note>){

        notesLocalDataSource.deleteAllNotes()

        for (note in notes){
            notesLocalDataSource.saveNote(note)
        }

    }

    private inline fun cacheAndPerform(note: Note, perform: (Note) -> Unit){

        val cachedNote = Note(note.title, note.text, note.id).apply {
            isMarked = note.isMarked
        }

        cachedNotes[cachedNote.id] = cachedNote

        perform(cachedNote)

    }

    private fun getNoteWithId(noteId: String) = cachedNotes[noteId]

    companion object {

        private var INSTANCE: NotesRepository? = null

        @JvmStatic fun getInstance(notesRemoteDataSource: NotesDataSource, notesLocalDataSource: NotesDataSource): NotesRepository {
            return INSTANCE ?: NotesRepository(notesRemoteDataSource, notesLocalDataSource).apply { INSTANCE = this }
        }

        @JvmStatic fun destroyInstance(){
            INSTANCE = null
        }


    }

}