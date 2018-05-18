package com.example.rafaelsavaris.noteapplicationmvpkotlin.data.source.local

import com.example.rafaelsavaris.noteapplicationmvpkotlin.data.NotesDataSource
import com.example.rafaelsavaris.noteapplicationmvpkotlin.utils.AppExecutors
import com.example.rafaelsavaris.noteapplicationmvpkotlin.data.model.Note

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

    override fun refreshNotes() {}

    override fun markNote(note: Note) {
        appExecutors.diskIO.execute { notesDao.updateNoteMarked(note.id, true) }
    }

    override fun unMarkNote(note: Note) {
        appExecutors.diskIO.execute { notesDao.updateNoteMarked(note.id, false) }
    }

    override fun clearMarkedNotes() {
        appExecutors.diskIO.execute { notesDao.deleteMarkedNotes() }
    }

    override fun getNote(noteId: String, callback: NotesDataSource.GetNoteCallback) {
        appExecutors.diskIO.execute {

            val note = notesDao.getNoteById(noteId)

            if (note != null){
                callback.onNoteLoaded(note)
            } else {
                callback.onDataNotAvailable()
            }

        }
    }

    override fun deleteNote(noteId: String) {
        appExecutors.diskIO.execute { notesDao.deleteNote(noteId) }
    }

    override fun markNote(noteId: String) {}

    override fun unMarkNote(noteId: String) {}

    companion object {

        private var INSTANCE: NotesLocalDataSource? = null

        @JvmStatic
        fun getInstance(appExecutors: AppExecutors, notesDao: NotesDao): NotesLocalDataSource {

            if (INSTANCE == null){

                synchronized(NotesLocalDataSource::javaClass){
                    INSTANCE = NotesLocalDataSource(appExecutors, notesDao)
                }

            }

            return INSTANCE!!

        }

        fun clearInstance(){
            INSTANCE = null
        }

    }

}