package com.example.rafaelsavaris.noteapplicationmvpkotlin.data.source

import any
import capture
import com.example.rafaelsavaris.noteapplicationmvpkotlin.data.NotesDataSource
import com.example.rafaelsavaris.noteapplicationmvpkotlin.data.model.Note
import com.google.common.collect.Lists
import eq
import org.hamcrest.Matchers.`is`
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.*
import org.mockito.Mockito.*

class NotesRepositoryTest {

    private val TITLE1 = "title1"
    private val TITLE2 = "title2"
    private val TITLE3 = "title3"
    private val TEXT = "text"
    private val NOTES = Lists.newArrayList(Note(TITLE1, TEXT), Note(TITLE2, TEXT), Note(TITLE3, TEXT))
    private lateinit var notesRepository: NotesRepository

    @Mock private lateinit var notesRemoteDataSource: NotesDataSource
    @Mock private lateinit var notesLocalDataSource: NotesDataSource
    @Mock private lateinit var getNoteCallback: NotesDataSource.GetNoteCallback
    @Mock private lateinit var loadNotesCallback: NotesDataSource.LoadNotesCallback

    @Captor private lateinit var notesCallbackCaptor: ArgumentCaptor<NotesDataSource.LoadNotesCallback>
    @Captor private lateinit var noteCallbackCaptor: ArgumentCaptor<NotesDataSource.GetNoteCallback>

    @Before fun setup(){

        MockitoAnnotations.initMocks(this)

        notesRepository = NotesRepository.getInstance(notesRemoteDataSource, notesLocalDataSource)

    }

    @After fun destroy(){
        NotesRepository.destroyInstance()
    }


    @Test fun getNotes_repositoryCachesAfterFirstApiCall(){

        twoNotesLoadCallsToRepository(loadNotesCallback)

        verify(notesRemoteDataSource).getNotes(any<NotesDataSource.LoadNotesCallback>())

    }

    @Test fun getNotes_requestAllNotesFromLocalDataSource(){

        notesRepository.getNotes(loadNotesCallback)

        verify(notesLocalDataSource).getNotes(any<NotesDataSource.LoadNotesCallback>())

    }

    @Test fun saveNote_savesNoteToServiceApi(){

        val note = Note(TITLE1, TEXT)

        notesRepository.saveNote(note)

        verify(notesRemoteDataSource).saveNote(note)
        verify(notesLocalDataSource).saveNote(note)

        assertThat(notesRepository.cachedNotes.size, `is`(1))

    }

    @Test fun markNote_marksNoteServiceApiUpdatesCache(){

        val note = Note(TITLE1, TEXT)

        notesRepository.saveNote(note)

        notesRepository.markNote(note)

        verify(notesRemoteDataSource).markNote(note)

        verify(notesLocalDataSource).markNote(note)

        assertThat(notesRepository.cachedNotes.size, `is`(1))

        val cachedNote = notesRepository.cachedNotes[note.id]

        assertNotNull(cachedNote)

        assertThat(cachedNote!!.isMarked, `is`(true))

    }

    @Test fun markNoteId_marksNoteServiceApiUpdatesCache(){

        val note = Note(TITLE1, TEXT)

        notesRepository.saveNote(note)

        notesRepository.markNote(note.id)

        verify(notesRemoteDataSource).markNote(note)

        verify(notesLocalDataSource).markNote(note)

        assertThat(notesRepository.cachedNotes.size, `is`(1))

        val cachedNote = notesRepository.cachedNotes[note.id]

        assertThat(cachedNote!!.isMarked, `is`(true))

    }

    @Test fun unMarkNote_unMmarksNoteServiceApiUpdatesCache(){

        val note = Note(TITLE1, TEXT).apply {
            isMarked = true
        }

        with(notesRepository){

            saveNote(note)

            unMarkNote(note)

            verify(notesRemoteDataSource).unMarkNote(note)

            verify(notesLocalDataSource).unMarkNote(note)

            assertThat(cachedNotes.size, `is`(1))

            assertThat(cachedNotes[note.id]!!.isMarked, `is`(false))

        }

    }

    @Test fun unMarkNoteId_unMarksNoteServiceApiUpdatesCache(){

        val note = Note(TITLE1, TEXT).apply {
            isMarked = true
        }

        with(notesRepository){

            saveNote(note)

            unMarkNote(note.id)

            verify(notesRemoteDataSource).unMarkNote(note)

            verify(notesLocalDataSource).unMarkNote(note)

            assertThat(cachedNotes.size, `is`(1))

            assertThat(cachedNotes[note.id]!!.isMarked, `is`(false))

        }

    }

    @Test fun getNote_requestsSingleNoteFromLocalDatasource(){

        notesRepository.getNote(TITLE1, getNoteCallback)

        verify(notesLocalDataSource).getNote(eq(TITLE1), any<NotesDataSource.GetNoteCallback>())

    }

    @Test fun deleteMarkedNotes_deleteMarkedNotesToServiceApiUpdatesCache(){

        val note = Note(TITLE1, TEXT).apply {
            isMarked = true
        }

        val note2 = Note(TITLE2, TEXT)

        val note3 = Note(TITLE3, TEXT).apply {
            isMarked = true
        }

        with(notesRepository){

            saveNote(note)
            saveNote(note2)
            saveNote(note3)

            clearMarkedNotes()

            verify(notesRemoteDataSource).clearMarkedNotes()

            verify(notesLocalDataSource).clearMarkedNotes()

            assertThat(cachedNotes.size, `is`(1))

            assertThat(cachedNotes[note2.id]!!.isMarked, `is`(false))

            assertThat(cachedNotes[note2.id]!!.title, `is`(TITLE2))

        }

    }

    @Test fun deleteAllNotes_deleteNotesToServiceApiUpdatesCache(){

        val note = Note(TITLE1, TEXT).apply {
            isMarked = true
        }

        val note2 = Note(TITLE2, TEXT)

        val note3 = Note(TITLE3, TEXT).apply {
            isMarked = true
        }

        with(notesRepository) {

            saveNote(note)
            saveNote(note2)
            saveNote(note3)

            deleteAllNotes()

            verify(notesRemoteDataSource).deleteAllNotes()
            verify(notesLocalDataSource).deleteAllNotes()

            assertThat(cachedNotes.size, `is`(0))

        }

    }

    @Test fun deleteNote_deleteNoteToServiceApiRemovedFromCache(){

        val note = Note(TITLE1,TEXT).apply {
            isMarked = true
        }

        with(notesRepository){

            saveNote(note)

            assertThat(cachedNotes.containsKey(note.id), `is`(true))

            deleteNote(note.id)

            verify(notesRemoteDataSource).deleteNote(note.id)
            verify(notesLocalDataSource).deleteNote(note.id)

            assertThat(cachedNotes.containsKey(note.id), `is`(false))

        }

    }

    @Test fun getNotesWithDirtyCache_notesAreRetrievedFromRemote(){

        notesRepository.refreshNotes()

        notesRepository.getNotes(loadNotesCallback)

        setNotesAvailable(notesRemoteDataSource, NOTES)

        verify(notesLocalDataSource, never()).getNotes(loadNotesCallback)

        verify(loadNotesCallback).onNotesLoaded(NOTES)

    }

    @Test fun getNotesWithLocalDatasourceUnavailable_notesAreRetrieveFromRemote(){

        notesRepository.getNotes(loadNotesCallback)

        setNotesUnavailable(notesLocalDataSource)

        setNotesAvailable(notesRemoteDataSource, NOTES)

        verify(loadNotesCallback).onNotesLoaded(NOTES)

    }

    @Test fun getNotesWithBothDataSourceUnavailable_firesOnDataUnavailable(){

        notesRepository.getNotes(loadNotesCallback)

        setNotesUnavailable(notesLocalDataSource)

        setNotesUnavailable(notesRemoteDataSource)

        verify(loadNotesCallback).onDataNotAvailable()

    }

    @Test fun getNoteWithBothDataSourceUnavailable_firesOnDataUnavailable(){

        val id = "111"

        notesRepository.getNote(id, getNoteCallback)

        setNoteUnavailable(notesLocalDataSource, id)

        setNoteUnavailable(notesRemoteDataSource, id)

        verify(getNoteCallback).onDataNotAvailable()

    }

    @Test fun getNotes_refreshesLocalDataSource(){

        notesRepository.refreshNotes()

        notesRepository.getNotes(loadNotesCallback)

        setNotesAvailable(notesRemoteDataSource, NOTES)

        verify(notesLocalDataSource, times(NOTES.size)).saveNote(any<Note>())

    }

    private fun twoNotesLoadCallsToRepository(loadNotesCallBack: NotesDataSource.LoadNotesCallback){

        notesRepository.getNotes(loadNotesCallBack)

        verify(notesLocalDataSource).getNotes(capture(notesCallbackCaptor))

        notesCallbackCaptor.value.onDataNotAvailable()

        verify(notesRemoteDataSource).getNotes(capture(notesCallbackCaptor))

        notesCallbackCaptor.value.onNotesLoaded(NOTES)

        notesRepository.getNotes(loadNotesCallBack)

    }

    private fun setNotesAvailable(notesDatasource: NotesDataSource, notes: List<Note>){
        verify(notesDatasource).getNotes(capture(notesCallbackCaptor))
        notesCallbackCaptor.value.onNotesLoaded(notes)
    }

    private fun setNotesUnavailable(notesDatasource: NotesDataSource){
        verify(notesDatasource).getNotes(capture(notesCallbackCaptor))
        notesCallbackCaptor.value.onDataNotAvailable()
    }

    private fun setNoteUnavailable(notesDatasource: NotesDataSource, noteId: String){
        verify(notesDatasource).getNote(eq(noteId), capture(noteCallbackCaptor))
        noteCallbackCaptor.value.onDataNotAvailable()
    }


}