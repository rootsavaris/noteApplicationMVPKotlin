package com.example.rafaelsavaris.noteapplicationmvp.data.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.*

@Entity(tableName = "notes")
data class Note @JvmOverloads constructor(
    @ColumnInfo(name = "title") var title: String = "",
    @ColumnInfo(name = "text") var text: String = "",
    @PrimaryKey @ColumnInfo(name = "id") var id: String = UUID.randomUUID().toString()
){

    @ColumnInfo(name = "marked") var isMarked = false

    val titleForList: String
        get() = if (title.isNullOrEmpty()) title else text

    val isEmpty
        get() = title.isEmpty() && text.isEmpty()

}