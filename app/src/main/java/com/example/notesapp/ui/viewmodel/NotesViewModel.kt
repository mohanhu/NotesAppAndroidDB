package com.example.notesapp.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.notesapp.Roomdb.NotesDataBase
import com.example.notesapp.Roomdb.NotesRepository
import com.example.notesapp.model.data.NotesDataClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NotesViewModel(application: Application):AndroidViewModel(application) {

    var notesLiveData: Flow<List<NotesDataClass>>
    private var notesRepository:NotesRepository
    val liveSearch:MutableLiveData<String> by lazy { MutableLiveData<String>() }

    init {
         val userDao = NotesDataBase.getDatabase(application).notesDao()
         notesRepository = NotesRepository(userDao)
         notesLiveData=notesRepository.readData
    }

    fun setMessage(searchVal:String){
        viewModelScope.launch ( Dispatchers.Main){
            liveSearch.value=searchVal
        }
    }
    fun insertData(notes:NotesDataClass){
        viewModelScope.launch(Dispatchers.IO){
            notesRepository.insertData(notes)
        }
    }

    fun deleteData(notes: NotesDataClass){
        viewModelScope.launch(Dispatchers.IO){
            notesRepository.deleteData(notes)
        }
    }

    fun updateData(notes: NotesDataClass){
        viewModelScope.launch(Dispatchers.IO) {
            notesRepository.upDateData(notes)
        }
    }

}