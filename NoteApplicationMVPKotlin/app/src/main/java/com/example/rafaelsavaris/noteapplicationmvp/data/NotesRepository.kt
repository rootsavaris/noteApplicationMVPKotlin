package com.example.rafaelsavaris.noteapplicationmvp.data

import com.example.rafaelsavaris.noteapplicationmvp.data.model.Note
import kotlin.collections.ArrayList

class NotesRepository (
        val notesRemoteDataSource: NotesDataSource,
        val notesLocalDataSource: NotesDataSource
) : NotesDataSource{

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

            notesLocalDataSource.getNotes(object : NotesDataSource.LoadNotesCallback{

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

    private fun getNotesFromRemoteDataSource(callback: NotesDataSource.LoadNotesCallback){

        notesRemoteDataSource.getNotes(object : NotesDataSource.LoadNotesCallback{

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

    companion object {

        private var INSTANCE: NotesRepository? = null

        @JvmStatic fun getInstance(notesRemoteDataSource: NotesDataSource, notesLocalDataSource: NotesDataSource): NotesRepository{
            return INSTANCE ?: NotesRepository(notesRemoteDataSource, notesLocalDataSource).apply { INSTANCE = this }
        }

        @JvmStatic fun destroyInstance(){
            INSTANCE = null
        }


    }

}