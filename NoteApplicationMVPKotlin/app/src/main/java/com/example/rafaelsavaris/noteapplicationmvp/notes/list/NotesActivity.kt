package com.example.rafaelsavaris.noteapplicationmvp.notes.list

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.rafaelsavaris.noteapplicationmvp.R
import com.example.rafaelsavaris.noteapplicationmvp.utils.replaceFragmentInActivity
import com.example.rafaelsavaris.noteapplicationmvp.utils.setupActionBar

class NotesActivity : AppCompatActivity() {

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

        presenter = NotesPresenter()




    }
}