package com.example.rafaelsavaris.noteapplicationmvp.notes.list

import com.example.rafaelsavaris.noteapplicationmvp.data.NotesRepository

class NotesPresenter (val notesRepository: NotesRepository, val notesView: NotesContract.View) : NotesContract.Presenter{

    override fun start() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}