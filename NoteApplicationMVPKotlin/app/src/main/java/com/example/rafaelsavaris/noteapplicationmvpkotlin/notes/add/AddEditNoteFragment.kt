package com.example.rafaelsavaris.noteapplicationmvpkotlin.notes.add

import android.app.Activity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.rafaelsavaris.noteapplicationmvpkotlin.R
import com.example.rafaelsavaris.noteapplicationmvpkotlin.utils.showSnackBar

class AddEditNoteFragment: Fragment(), AddEditNoteContract.View {

    override lateinit var presenter: AddEditNoteContract.Presenter

    override var isActive: Boolean = false
        get() = isAdded

    private lateinit var title: TextView
    private lateinit var text: TextView

    override fun onResume() {
        super.onResume()
        presenter.start()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val root = inflater.inflate(R.layout.add_edit_note_frag, container, false)

        with(root){

            title = findViewById(R.id.add_edit_note_title)
            text = findViewById(R.id.add_edit_note_text)

        }

        setHasOptionsMenu(true)

        return root

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {

        super.onActivityCreated(savedInstanceState)

        with(activity!!.findViewById<FloatingActionButton>(R.id.fab_add_note_done)){
            setImageResource(R.drawable.ic_done)
            setOnClickListener {
                presenter.saveNote(title.text.toString(), text.text.toString())
            }
        }

    }

    override fun setTitle(title: String) {
        this.title.text = title
    }

    override fun setText(text: String) {
        this.text.text = text
    }

    override fun showEmptyNoteError() {
        title.showSnackBar(getString(R.string.empty_note_text), Snackbar.LENGTH_SHORT)
    }

    override fun showNoteList() {

        with(activity!!){
            setResult(Activity.RESULT_OK)
            finish()
        }

    }

    companion object {

        fun newInstance() = AddEditNoteFragment()

    }

}