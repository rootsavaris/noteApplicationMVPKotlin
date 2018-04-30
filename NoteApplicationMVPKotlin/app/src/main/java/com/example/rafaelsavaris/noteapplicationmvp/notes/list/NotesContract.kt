package com.example.rafaelsavaris.noteapplicationmvp.notes.list

import com.example.rafaelsavaris.noteapplicationmvp.BasePresenter
import com.example.rafaelsavaris.noteapplicationmvp.BaseView

interface NotesContract {

    interface View: BaseView<Presenter>

    interface Presenter: BasePresenter

}