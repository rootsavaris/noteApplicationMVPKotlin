package com.example.rafaelsavaris.noteapplicationmvpkotlin.notes.add

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.rafaelsavaris.noteapplicationmvpkotlin.Injection
import com.example.rafaelsavaris.noteapplicationmvpkotlin.R
import com.example.rafaelsavaris.noteapplicationmvpkotlin.utils.Constants
import com.example.rafaelsavaris.noteapplicationmvpkotlin.utils.Constants.Companion.NOTE_ID_PARAM
import com.example.rafaelsavaris.noteapplicationmvpkotlin.utils.replaceFragmentInActivity
import com.example.rafaelsavaris.noteapplicationmvpkotlin.utils.setupActionBar

class AddEditNoteActivity : AppCompatActivity() {

    private lateinit var addEditNotePresenter: AddEditNotePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_edit_note_activity)

        val noteId: String? = intent.getStringExtra(NOTE_ID_PARAM)

        setupActionBar(R.id.toolbar){
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)

            title = if (noteId == null) resources.getString(R.string.add_note) else resources.getString(R.string.edit_note)

        }

        var addEditNoteFragment = supportFragmentManager.findFragmentById(R.id.contentFrame) as AddEditNoteFragment? ?: AddEditNoteFragment.newInstance().also {
            replaceFragmentInActivity(it, R.id.contentFrame)
        }

        val shouldLoadDataFromRepo = savedInstanceState?.getBoolean(Constants.SHOULD_DATA_FROM_REPO) ?: true

        addEditNotePresenter = AddEditNotePresenter(noteId, Injection.provideNotesRepository(this), addEditNoteFragment, shouldLoadDataFromRepo)

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState.apply {
            putBoolean(Constants.SHOULD_DATA_FROM_REPO, addEditNotePresenter.isDataMissing)
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}