package com.example.rafaelsavaris.noteapplicationmvp;

import android.content.Context;

import com.example.rafaelsavaris.noteapplicationmvp.data.source.remote.MockRemoteDataSource;

/**
 * Created by rafael.savaris on 18/10/2017.
 */

public class Injection {

    public static NotesRepository providesNotesRepository(Context context){

        NoteDatabase database = NoteDatabase.getInstance(context);

        return NotesRepository.getInstance(MockRemoteDataSource.getInstance(), NotesLocalDataSource.getInstance(new AppExecutors(), database.noteDao()));

    }

}
