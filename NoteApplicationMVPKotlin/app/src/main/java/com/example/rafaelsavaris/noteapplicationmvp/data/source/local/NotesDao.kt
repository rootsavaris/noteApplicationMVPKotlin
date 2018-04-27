package com.example.rafaelsavaris.noteapplicationmvp.data.source.local

import android.arch.persistence.room.*
import com.example.rafaelsavaris.noteapplicationmvp.data.model.Note

@Dao interface NotesDao {

    @Query("SELECT * FROM notes") fun getNotes(): List<Note>

    @Query("SELECT * FROM notes WHERE id = :noteId") fun getNoteById(noteId: String): Note?

    @Insert(onConflict = OnConflictStrategy.REPLACE) fun insertNote(note: Note)

    @Update fun updateNote(note: Note): Int

    @Query("UPDATE notes SET marked = :marked WHERE id = :noteId") fun updateNoteMarked(noteId: String, marked: Boolean)

    @Query("DELETE FROM notes WHERE id = :noteId") fun deleteNote(noteId: String): Int

    @Query("DELETE FROM notes") fun deleteNotes()

    @Query("DELETE FROM notes WHERE marked = 1") fun deleteMarkedNotes(): Int

}


