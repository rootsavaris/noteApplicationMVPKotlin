package com.example.rafaelsavaris.noteapplicationmvp.notes.list

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.rafaelsavaris.noteapplicationmvp.R
import com.example.rafaelsavaris.noteapplicationmvp.utils.setupActionBar

class NotesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.notes_activity)

        setupActionBar(R.id.toolbar){

            setHomeAsUpIndicator(R.drawable.ic_menu)
            setDisplayHomeAsUpEnabled(true)

        }


    }
}