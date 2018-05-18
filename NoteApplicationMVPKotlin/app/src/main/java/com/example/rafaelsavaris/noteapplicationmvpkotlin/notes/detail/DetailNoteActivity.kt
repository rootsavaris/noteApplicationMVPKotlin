package com.example.rafaelsavaris.noteapplicationmvpkotlin.notes.detail

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.rafaelsavaris.noteapplicationmvpkotlin.Injection
import com.example.rafaelsavaris.noteapplicationmvpkotlin.R
import com.example.rafaelsavaris.noteapplicationmvpkotlin.utils.Constants.Companion.NOTE_ID_PARAM
import com.example.rafaelsavaris.noteapplicationmvpkotlin.utils.replaceFragmentInActivity
import com.example.rafaelsavaris.noteapplicationmvpkotlin.utils.setupActionBar

class DetailNoteActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.detail_note_activity)

        setupActionBar(R.id.toolbar){
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        val noteId = intent.getStringExtra(NOTE_ID_PARAM)

        val detailNoteFragment = supportFragmentManager.findFragmentById(R.id.contentFrame) as DetailNoteFragment? ?: DetailNoteFragment.newInstance().also {
            replaceFragmentInActivity(it, R.id.contentFrame)
        }

        DetailPresenter(noteId, Injection.provideNotesRepository(this), detailNoteFragment)

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}