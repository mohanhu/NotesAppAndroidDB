package com.example.notesapp.ui.view.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.notesapp.databinding.AddNotesBinding
import com.example.notesapp.model.data.NotesDataClass

class AddNotes : AppCompatActivity() {
    private lateinit var binding: AddNotesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = AddNotesBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        addDataFunction()
    }



    private fun addDataFunction() {

        binding.change.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            val addTitle = binding.title.text
            val addDesc = binding.description.text
            if(addTitle.toString().isEmpty() || addDesc.toString().isEmpty()){
                Toast.makeText(this, "Input Failed", Toast.LENGTH_SHORT).show()
            }
            else{
                intent.putExtra("titlePass", addTitle.toString())
                intent.putExtra("descPass", addDesc.toString())
                setResult(RESULT_OK,intent)
                finish()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}