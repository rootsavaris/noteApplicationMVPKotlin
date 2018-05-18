package com.example.rafaelsavaris.noteapplicationmvpkotlin.notes.detail

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.*
import android.widget.CheckBox
import android.widget.TextView
import com.example.rafaelsavaris.noteapplicationmvpkotlin.R
import com.example.rafaelsavaris.noteapplicationmvpkotlin.notes.add.AddEditNoteActivity
import com.example.rafaelsavaris.noteapplicationmvpkotlin.utils.Constants
import com.example.rafaelsavaris.noteapplicationmvpkotlin.utils.showSnackBar

class DetailNoteFragment: Fragment(), DetailNoteContract.View {

    private lateinit var title: TextView
    private lateinit var text: TextView
    private lateinit var marked: CheckBox

    override lateinit var presenter: DetailNoteContract.Presenter

    override var isActive: Boolean = false
        get() = isAdded

    override fun onResume() {
        super.onResume()
        presenter.start()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val root = inflater.inflate(R.layout.detail_note_fragment, container, false)

        setHasOptionsMenu(true)

        with(root){
            title = findViewById(R.id.note_detail_title)
            text = findViewById(R.id.note_detail_text)
            marked = findViewById(R.id.note_detail_marked)
        }

        activity!!.findViewById<FloatingActionButton>(R.id.fab_edit_note).setOnClickListener {
            presenter.showEditNote()
        }

        return root

    }

    override fun showMissingNote() {
        title.text = ""
        text.text = getString(R.string.no_data)
    }

    override fun setLoadingIndicator(show: Boolean) {

        if (show){
            title.text = ""
            text.text = getString(R.string.loading)
        }

    }

    override fun hideTitle() {
        title.visibility = View.GONE
    }

    override fun hideText() {
        text.visibility = View.GONE
    }

    override fun showTitle(title: String) {
        this.title.text = title
    }

    override fun showText(text: String) {
        this.text.text = text
    }

    override fun showMarked(marked: Boolean) {

        with(this.marked){

            isChecked = marked

            setOnCheckedChangeListener { _, isChecked ->

                if (isChecked){
                    presenter.markNote()
                } else {
                    presenter.unMarkNote()
                }

            }

        }

    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
        inflater.inflate(R.menu.detail_note_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        with(item){

            when(itemId){

                R.id.menu_delete -> presenter.deleteNote()
                else -> null

            }


        }

        return super.onOptionsItemSelected(item)
    }

    override fun showNoteDeleted() {
        activity!!.finish()
    }

    override fun showNoteMarked() {
        view?.showSnackBar(getString(R.string.note_marked), Snackbar.LENGTH_SHORT)
    }

    override fun showNoteUnMarked() {
        view?.showSnackBar(getString(R.string.note_unmarked), Snackbar.LENGTH_SHORT)
    }

    override fun toEditNote(noteId: String) {

        val intent = Intent(context, AddEditNoteActivity::class.java)

        intent.putExtra(Constants.NOTE_ID_PARAM, noteId)

        startActivityForResult(intent, Constants.REQUEST_EDIT_NOTE)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == Constants.REQUEST_EDIT_NOTE && resultCode == Activity.RESULT_OK){
            activity!!.finish()
        }

    }

    companion object {

        fun newInstance(): DetailNoteFragment{
            return DetailNoteFragment()
        }

    }

}