package com.example.notesapp.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "notes_table")
data class NotesDataClass(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val title:String,
    val desc:String,
    val date:String
):Serializable
