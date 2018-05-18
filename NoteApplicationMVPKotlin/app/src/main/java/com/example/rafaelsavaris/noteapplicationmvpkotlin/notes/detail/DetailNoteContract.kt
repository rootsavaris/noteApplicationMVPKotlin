package com.example.rafaelsavaris.noteapplicationmvpkotlin.notes.detail

import com.example.rafaelsavaris.noteapplicationmvp.BasePresenter
import com.example.rafaelsavaris.noteapplicationmvp.BaseView

class DetailNoteContract {

    interface View : BaseView<Presenter>{

        var isActive : Boolean

        fun showMissingNote()

        fun setLoadingIndicator(show: Boolean)

        fun hideTitle()

        fun hideText()

        fun showTitle(title: String)

        fun showText(text: String)

        fun showMarked(marked: Boolean)

        fun showNoteDeleted()

        fun showNoteMarked()

        fun showNoteUnMarked()

        fun toEditNote(noteId: String)

    }

    interface Presenter : BasePresenter {

        fun deleteNote()

        fun markNote()

        fun unMarkNote()

        fun showEditNote()

    }

}