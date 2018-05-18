package com.example.rafaelsavaris.noteapplicationmvpkotlin.data.source.local

import android.arch.persistence.room.*
import com.example.rafaelsavaris.noteapplicationmvpkotlin.data.model.Note

@Dao interface NotesDao {

    @Query("SELECT * FROM note") fun getNotes(): List<Note>

    @Query("SELECT * FROM note WHERE id = :noteId") fun getNoteById(noteId: String): Note?

    @Insert(onConflict = OnConflictStrategy.REPLACE) fun insertNote(note: Note)

    @Update fun updateNote(note: Note): Int

    @Query("UPDATE note SET marked = :marked WHERE id = :noteId") fun updateNoteMarked(noteId: String, marked: Boolean)

    @Query("DELETE FROM note WHERE id = :noteId") fun deleteNote(noteId: String): Int

    @Query("DELETE FROM note") fun deleteNotes()

    @Query("DELETE FROM note WHERE marked = 1") fun deleteMarkedNotes(): Int

}


