package com.example.rafaelsavaris.noteapplicationmvpkotlin.notes.list

import com.example.rafaelsavaris.noteapplicationmvp.BasePresenter
import com.example.rafaelsavaris.noteapplicationmvp.BaseView
import com.example.rafaelsavaris.noteapplicationmvpkotlin.data.model.Note

interface NotesContract {

    interface View: BaseView<Presenter>{

        var isActive: Boolean

        fun setLoadingIndicator(show: Boolean)

        fun showNoNotes()

        fun showNoMarkedNotes()

        fun showNotes(notes: List<Note>)

        fun showAllFilterLabel()

        fun showMarkedFilterLabel()

        fun showLoadingNotesError()

        fun showNoteMarked()

        fun showNoteUnMarked()

        fun showCompleteNotesCleared()

        fun addNewNote()

        fun showSaveSuccessMessage()

    }

    interface Presenter: BasePresenter{

        var currentFilter: NotesFilterType

        fun loadNotes(forceUpdate: Boolean)

        fun markNote(note: Note)

        fun unMarkNote(note: Note)

        fun clearMarkedNotes()

        fun onResult(requestCode: Int, resultCode: Int)

    }

}