package com.example.rafaelsavaris.noteapplicationmvpkotlin.notes.list

import android.app.Activity
import com.example.rafaelsavaris.noteapplicationmvpkotlin.data.NotesDataSource
import com.example.rafaelsavaris.noteapplicationmvpkotlin.data.source.NotesRepository
import com.example.rafaelsavaris.noteapplicationmvpkotlin.data.model.Note
import com.example.rafaelsavaris.noteapplicationmvpkotlin.utils.Constants

class NotesPresenter (val notesRepository: NotesRepository, val notesView: NotesContract.View) : NotesContract.Presenter{

    override var currentFilter = NotesFilterType.ALL_NOTES

    private var firstLoad: Boolean = true

    init {
        notesView.presenter = this
    }

    override fun start() {
        loadNotes(false)
    }

    override fun loadNotes(forceUpdate: Boolean){
        loadNotes(forceUpdate || firstLoad, true )
        firstLoad = false
    }

    override fun markNote(note: Note) {
        notesRepository.markNote(note)
        notesView.showNoteMarked()
        loadNotes(false, false)
    }

    override fun unMarkNote(note: Note) {
        notesRepository.unMarkNote(note)
        notesView.showNoteUnMarked()
        loadNotes(false, false)
    }

    override fun clearMarkedNotes() {
        notesRepository.clearMarkedNotes()
        notesView.showCompleteNotesCleared()
        loadNotes(false, false)
    }

    override fun onResult(requestCode: Int, resultCode: Int) {

        if (Constants.REQUEST_ADD_NOTE == requestCode && Activity.RESULT_OK == resultCode){
            notesView.showSaveSuccessMessage()
        }

    }

    private fun loadNotes(forceUpdate: Boolean, showLoadindUI: Boolean){

        if (showLoadindUI){
            notesView.setLoadingIndicator(true)
        }

        if (forceUpdate){
            notesRepository.refreshNotes()
        }

        notesRepository.getNotes(object : NotesDataSource.LoadNotesCallback {

            override fun onNotesLoaded(notes: List<Note>) {

                val notesToShow = ArrayList<Note>()

                for(note in notes){

                    when(currentFilter){

                        NotesFilterType.ALL_NOTES -> notesToShow.add(note)

                        NotesFilterType.MARKED_NOTES -> if (note.isMarked){
                            notesToShow.add(note)
                        }

                    }

                }

                if (!notesView.isActive){
                    return
                }

                if (showLoadindUI){
                    notesView.setLoadingIndicator(false)
                }

                processNotes(notesToShow)


            }

            override fun onDataNotAvailable() {

                if (!notesView.isActive){
                    return
                }

                notesView.showLoadingNotesError()

            }

        })

    }

    private fun processNotes(notes: List<Note>){

        if (notes.isEmpty()){
            processEmptyNotes()
        } else {
            notesView.showNotes(notes)
            showFilterLabel()
        }

    }

    private fun processEmptyNotes(){

        when (currentFilter){

            NotesFilterType.MARKED_NOTES -> notesView.showNoMarkedNotes()
            else -> notesView.showNoNotes()

        }

    }

    private fun showFilterLabel(){

        when(currentFilter){

            NotesFilterType.MARKED_NOTES -> notesView.showMarkedFilterLabel()
            else -> notesView.showAllFilterLabel()

        }

    }

}