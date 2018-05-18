package com.example.rafaelsavaris.noteapplicationmvpkotlin.notes.list

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.view.*
import android.widget.*
import com.example.rafaelsavaris.noteapplicationmvpkotlin.R
import com.example.rafaelsavaris.noteapplicationmvpkotlin.data.model.Note
import com.example.rafaelsavaris.noteapplicationmvpkotlin.notes.add.AddEditNoteActivity
import com.example.rafaelsavaris.noteapplicationmvpkotlin.notes.detail.DetailNoteActivity
import com.example.rafaelsavaris.noteapplicationmvpkotlin.utils.Constants
import com.example.rafaelsavaris.noteapplicationmvpkotlin.utils.showSnackBar
import kotlinx.android.synthetic.main.notes_fragment.*
import kotlinx.android.synthetic.main.notes_fragment.view.*
import java.util.ArrayList

class NotesFragment : Fragment(), NotesContract.View {

    override var isActive: Boolean = false
        get() = isAdded

    override lateinit var presenter: NotesContract.Presenter

    private lateinit var noNotesView: View
    private lateinit var noNotesIcon: ImageView
    private lateinit var noNotesMainView: TextView
    private lateinit var noNotesAddView: TextView
    private lateinit var notesView: LinearLayout
    private lateinit var filterLabelView: TextView

    internal var itemListener: NoteItemListener = object : NoteItemListener{

        override fun onNoteClick(note: Note) {

            val intent = Intent(context, DetailNoteActivity::class.java).apply {
                putExtra(Constants.NOTE_ID_PARAM, note.id)
            }

            startActivity(intent)

        }

        override fun onMarkNote(note: Note) {
            presenter.markNote(note)
        }

        override fun onUnMarkNote(note: Note) {
            presenter.unMarkNote(note)
        }

    }

    private val listAdapter = NotesAdapter(ArrayList(0), itemListener)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val root = inflater.inflate(R.layout.notes_fragment, container, false)

        // Set up tasks view
        with(root) {

            val listView = findViewById<ListView>(R.id.notes_list).apply { adapter = listAdapter }

            // Set up progress indicator
            findViewById<ScrollChilSwipeRefreshLayout>(R.id.refresh_layout).apply {
                setColorSchemeColors(
                        android.support.v4.content.ContextCompat.getColor(activity!!, R.color.colorPrimary),
                        android.support.v4.content.ContextCompat.getColor(activity!!, R.color.colorAccent),
                        android.support.v4.content.ContextCompat.getColor(activity!!, R.color.colorPrimaryDark)
                )
                // Set the scrolling view in the custom SwipeRefreshLayout.
                scrollUpChild = listView

                setOnRefreshListener { presenter.loadNotes(false) }

            }

            filterLabelView = findViewById(R.id.filteringLabel)
            notesView = findViewById(R.id.notesLL)

            // Set up  no tasks view
            noNotesView = findViewById(R.id.noNotes)
            noNotesIcon = findViewById(R.id.noNOtesIcon)
            noNotesMainView = findViewById(R.id.noNotesMain)
            noNotesAddView = (findViewById<TextView>(R.id.noNotesAdd)).also {
                it.setOnClickListener { addNewNote() }
            }
        }

        // Set up floating action button
        activity!!.findViewById<FloatingActionButton>(R.id.fab_add_note).apply {
            setImageResource(R.drawable.ic_add)
            setOnClickListener {
                addNewNote()
            }
        }
        setHasOptionsMenu(true)

        return root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        presenter.onResult(requestCode, resultCode)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){

            R.id.menu_clear -> presenter.clearMarkedNotes()
            R.id.menu_filter -> showFilterPopup()
            R.id.menu_refresh -> presenter.loadNotes(true)

        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.notes_fragment_menu, menu)
    }

    private class NotesAdapter(notes: List<Note>, private val itemListener: NoteItemListener)
        : BaseAdapter() {

        var notes: List<Note> = notes
            set(notes) {
                field = notes
                notifyDataSetChanged()
            }

        override fun getCount() = notes.size

        override fun getItem(i: Int) = notes[i]

        override fun getItemId(i: Int) = i.toLong()

        override fun getView(i: Int, view: View?, viewGroup: ViewGroup): View {

            val task = getItem(i)

            val rowView = view ?: LayoutInflater.from(viewGroup.context)
                    .inflate(R.layout.note_item, viewGroup, false)

            with(rowView.findViewById<TextView>(R.id.title)) {
                text = task.titleForList
            }

            with(rowView.findViewById<CheckBox>(R.id.mark)) {

                isChecked = task.isMarked

                val rowViewBackground =
                        if (task.isMarked) R.drawable.list_completed_touch_feedback
                        else R.drawable.touch_feedback

                rowView.setBackgroundResource(rowViewBackground)

                setOnClickListener {
                    if (!task.isMarked) {
                        itemListener.onMarkNote(task)
                    } else {
                        itemListener.onUnMarkNote(task)
                    }
                }
            }

            rowView.setOnClickListener { itemListener.onNoteClick(task) }

            return rowView

        }
    }

    override fun onResume() {
        super.onResume()
        presenter.start()
    }

    override fun setLoadingIndicator(show: Boolean) {

        val root = view ?: return

        with(root.findViewById<SwipeRefreshLayout>(R.id.refresh_layout)){
            post { isRefreshing = show }
        }

    }

    override fun showNoNotes() {
        showNoNotesViews(resources.getString(R.string.no_notes_all), R.drawable.ic_assignment_turned_in_24dp, true)
    }

    override fun showNoMarkedNotes() {
        showNoNotesViews(resources.getString(R.string.no_notes_marked), R.drawable.ic_verified_user_24dp, false)
    }

    override fun showNotes(notes: List<Note>) {
        listAdapter.notes = notes
        notesView.visibility = View.VISIBLE
        noNotesView.visibility = View.GONE
    }

    override fun showAllFilterLabel() {
        filteringLabel.text = resources.getString(R.string.label_all)
    }

    override fun showMarkedFilterLabel() {
        filteringLabel.text = resources.getString(R.string.label_marked)
    }

    override fun showLoadingNotesError() {
        showMessage(resources.getString(R.string.loading_notes_error))
    }

    override fun showNoteMarked() {
        showMessage(resources.getString(R.string.note_marked))
    }

    override fun showNoteUnMarked() {
        showMessage(resources.getString(R.string.note_unmarked))
    }

    override fun showCompleteNotesCleared() {
        showMessage(resources.getString(R.string.marked_notes_cleared))
    }

    override fun addNewNote() {
        val intent = Intent(context, AddEditNoteActivity::class.java)
        startActivityForResult(intent, Constants.REQUEST_ADD_NOTE)
    }

    override fun showSaveSuccessMessage() {
        showMessage(resources.getString(R.string.successfully_saved_note_message))
    }

    private fun showMessage(message: String){
        view?.showSnackBar(message, Snackbar.LENGTH_LONG)
    }

    private fun showNoNotesViews(mainText: String, iconRest: Int, showAddView: Boolean){

        notesView.visibility = View.GONE
        noNotesView.visibility = View.VISIBLE

        noNotesMainView.text = mainText
        noNotesIcon.setImageResource(iconRest)
        noNotesAddView.visibility = if (showAddView) View.VISIBLE else View.GONE

    }

    private fun showFilterPopup(){

        PopupMenu(context, activity!!.findViewById(R.id.menu_filter)).apply {

            menuInflater.inflate(R.menu.filter_notes, menu)

            setOnMenuItemClickListener { item ->

                when (item.itemId){
                    R.id.marked -> presenter.currentFilter = NotesFilterType.MARKED_NOTES
                    else -> presenter.currentFilter = NotesFilterType.ALL_NOTES
                }

                presenter.loadNotes(false)

                true

            }

            show()

        }

    }

    interface NoteItemListener{

        fun onNoteClick(note: Note)

        fun onMarkNote(note: Note)

        fun onUnMarkNote(note: Note)

    }

    companion object {

        fun newInstance(): NotesFragment {
            return NotesFragment()
        }

    }

}