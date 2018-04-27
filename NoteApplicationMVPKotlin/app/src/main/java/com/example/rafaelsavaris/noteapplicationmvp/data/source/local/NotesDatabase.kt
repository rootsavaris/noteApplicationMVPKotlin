package com.example.rafaelsavaris.noteapplicationmvp.data.source.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.example.rafaelsavaris.noteapplicationmvp.data.model.Note


@Database(entities = arrayOf(Note::class), version = 1)
abstract class NotesDatabase : RoomDatabase() {

    abstract fun notesDao(): NotesDao

    companion object {

        private var INSTANCE: NotesDatabase? = null

        private val lock = Any()

        fun getInstance(context: Context): NotesDatabase{

            synchronized(lock){

                if (INSTANCE == null){

                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                            NotesDatabase::class.java,
                            "Notes.db").build()

                }

                return INSTANCE!!

            }

        }

    }

}