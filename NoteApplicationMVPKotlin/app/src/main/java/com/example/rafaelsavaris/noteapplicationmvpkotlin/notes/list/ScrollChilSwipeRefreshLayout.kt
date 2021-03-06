package com.example.rafaelsavaris.noteapplicationmvpkotlin.notes.list

import android.content.Context
import android.support.v4.widget.SwipeRefreshLayout
import android.util.AttributeSet
import android.view.View

class ScrollChilSwipeRefreshLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : SwipeRefreshLayout(context, attrs)
{

    var scrollUpChild: View? = null

    override fun canChildScrollUp() = scrollUpChild?.canScrollVertically(-1) ?:  super.canChildScrollUp()

}