package com.example.rafaelsavaris.noteapplicationmvpkotlin.notes.add

import com.example.rafaelsavaris.noteapplicationmvp.BasePresenter
import com.example.rafaelsavaris.noteapplicationmvp.BaseView
import com.example.rafaelsavaris.noteapplicationmvpkotlin.data.model.Note

interface AddEditNoteContract {

    interface View: BaseView<Presenter>{

        var isActive: Boolean

        fun setTitle(title: String)

        fun setText(text: String)

        fun showEmptyNoteError()

        fun showNoteList()

    }

    interface Presenter: BasePresenter{

        var isDataMissing: Boolean

        fun saveNote(title: String, text: String)

    }

}