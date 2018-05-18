package com.example.rafaelsavaris.noteapplicationmvpkotlin.notes.list

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.example.rafaelsavaris.noteapplicationmvpkotlin.Injection
import com.example.rafaelsavaris.noteapplicationmvpkotlin.R
import com.example.rafaelsavaris.noteapplicationmvpkotlin.utils.replaceFragmentInActivity
import com.example.rafaelsavaris.noteapplicationmvpkotlin.utils.setupActionBar

class NotesActivity : AppCompatActivity() {

    private val CURRENT_FILTER_KEY = "CURRENT_FILTER_KEY"

    private lateinit var presenter: NotesPresenter

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.notes_activity)

        setupActionBar(R.id.toolbar){

            setHomeAsUpIndicator(R.drawable.ic_menu)
            setDisplayHomeAsUpEnabled(true)

        }

        val notesFragment = supportFragmentManager.findFragmentById(R.id.contentFrame) as NotesFragment? ?: NotesFragment.newInstance().also {
            replaceFragmentInActivity(it, R.id.contentFrame)
        }

        presenter = NotesPresenter(Injection.provideNotesRepository(applicationContext), notesFragment).apply {

            if (savedInstanceState != null){
                currentFilter = savedInstanceState.getSerializable(CURRENT_FILTER_KEY) as NotesFilterType
            }

        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState.apply {
            putSerializable(CURRENT_FILTER_KEY, presenter.currentFilter)})
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home){
            return true
        }

        return super.onOptionsItemSelected(item)

    }
}