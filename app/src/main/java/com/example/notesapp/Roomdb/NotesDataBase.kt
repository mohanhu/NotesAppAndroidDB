package com.example.notesapp.Roomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.notesapp.model.data.NotesDataClass

@Database(entities = [NotesDataClass::class], version = 1, exportSchema = false)
abstract class NotesDataBase:RoomDatabase() {

    abstract fun notesDao():NotesDao

    companion object{
        @Volatile
        var INSTANCE:NotesDataBase?=null
        fun getDatabase(context: Context):NotesDataBase{
            val tempInstance= INSTANCE
            if (tempInstance!=null)
            {
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(context.applicationContext,NotesDataBase::class.java,
                    "notes_table"
                ).build()
                INSTANCE=instance
                return instance
            }
        }
    }
}