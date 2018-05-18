package com.example.rafaelsavaris.noteapplicationmvpkotlin.notes.add

import com.example.rafaelsavaris.noteapplicationmvpkotlin.data.NotesDataSource
import com.example.rafaelsavaris.noteapplicationmvpkotlin.data.source.NotesRepository
import com.example.rafaelsavaris.noteapplicationmvpkotlin.data.model.Note

class AddEditNotePresenter(val noteId: String?,
                           val notesRepository: NotesRepository,
                           val view: AddEditNoteContract.View,
                           override var isDataMissing: Boolean): AddEditNoteContract.Presenter, NotesDataSource.GetNoteCallback {

    init {
        view.presenter = this
    }

    override fun start() {

        if (noteId != null && isDataMissing){
            populateNote()
        }

    }

    private fun populateNote() {
        notesRepository.getNote(noteId!!, this)
    }

    override fun onNoteLoaded(note: Note) {

        if (view.isActive){
            view.setTitle(note.title)
            view.setText(note.text)
        }

        isDataMissing = false

    }

    override fun onDataNotAvailable() {

        if (view.isActive){
            view.showEmptyNoteError()
        }

    }

    override fun saveNote(title: String, text: String) {

        if (noteId == null){
            createNote(title, text)
        } else {
            updateNote(title, text)
        }

    }

    private fun createNote(title: String, text: String){

        val note = Note(title, text)

        if (note.isEmpty){
            view.showEmptyNoteError()
        } else{
            notesRepository.saveNote(note)
            view.showNoteList()
        }

    }

    private fun updateNote(title: String, text: String){

        val note = Note(title, text, noteId!!)

        if (note.isEmpty){
            view.showEmptyNoteError()
        } else{
            notesRepository.saveNote(note)
            view.showNoteList()
        }

    }

}