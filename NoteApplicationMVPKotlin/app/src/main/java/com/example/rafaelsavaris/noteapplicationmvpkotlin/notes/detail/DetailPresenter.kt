package com.example.rafaelsavaris.noteapplicationmvpkotlin.notes.detail

import com.example.rafaelsavaris.noteapplicationmvpkotlin.data.NotesDataSource
import com.example.rafaelsavaris.noteapplicationmvpkotlin.data.model.Note
import com.example.rafaelsavaris.noteapplicationmvpkotlin.data.source.NotesRepository

class DetailPresenter(val noteId: String, val notesRepository: NotesRepository, val view: DetailNoteContract.View) : DetailNoteContract.Presenter {

    init {
        view.presenter = this
    }

    override fun start() {

        if (noteId.isEmpty()){
            view.showMissingNote()
            return
        }

        view.setLoadingIndicator(true)

        notesRepository.getNote(noteId, object : NotesDataSource.GetNoteCallback{

            override fun onNoteLoaded(note: Note) {

                with(view){

                    if (!isActive){
                        return@onNoteLoaded
                    }

                    setLoadingIndicator(false)

                }

                showNote(note)

            }

            override fun onDataNotAvailable() {

                with(view){

                    if (!isActive){
                        return@onDataNotAvailable
                    }

                    showMissingNote()

                }

            }

        })

    }

    override fun deleteNote() {

        if (noteId.isEmpty()){
            view.showMissingNote()
            return
        }

        notesRepository.deleteNote(noteId)

        view.showNoteDeleted()

    }

    override fun markNote() {

        if (noteId.isEmpty()){
            view.showMissingNote()
            return
        }

        notesRepository.markNote(noteId)

        view.showNoteMarked()

    }

    override fun unMarkNote() {

        if (noteId.isEmpty()){
            view.showMissingNote()
            return
        }

        notesRepository.unMarkNote(noteId)

        view.showNoteUnMarked()

    }

    override fun showEditNote() {

        if (noteId.isEmpty()){
            view.showMissingNote()
            return
        }

        view.toEditNote(noteId)

    }

    private fun showNote(note: Note){

        with(view){

            if (noteId.isEmpty()){
                hideTitle()
                hideText()
            } else {
                showTitle(note.title)
                showText(note.text)
            }

            showMarked(note.isMarked)

        }

    }

}