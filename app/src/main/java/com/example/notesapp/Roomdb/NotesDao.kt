package com.example.notesapp.Roomdb
import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.notesapp.model.data.NotesDataClass
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

@Dao
interface NotesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertData(notes: NotesDataClass)

    @Query("SELECT * FROM notes_table ORDER BY id ASC")
    fun readData(): Flow<List<NotesDataClass>>

    @Delete
    suspend fun deleteData(notes: NotesDataClass)

    @Update
    suspend fun updatedata(notes: NotesDataClass)
}