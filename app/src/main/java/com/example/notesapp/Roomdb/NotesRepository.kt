package com.example.notesapp.Roomdb

import androidx.lifecycle.LiveData
import com.example.notesapp.model.data.NotesDataClass
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

class NotesRepository(val notesDao: NotesDao) {

    val readData: Flow<List<NotesDataClass>> = notesDao.readData()

    suspend fun deleteData(notes:NotesDataClass) = notesDao.deleteData(notes)

    suspend fun insertData(notes: NotesDataClass) = notesDao.insertData(notes)

    suspend fun upDateData(notes: NotesDataClass){
         notesDao.updatedata(notes)
    }
}