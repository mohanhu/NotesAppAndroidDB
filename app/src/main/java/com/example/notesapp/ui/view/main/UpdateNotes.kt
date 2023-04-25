package com.example.notesapp.ui.view.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.notesapp.R
import com.example.notesapp.databinding.ActivityUpdateNotesBinding
import com.example.notesapp.model.data.NotesDataClass
import com.example.notesapp.ui.viewmodel.NotesViewModel
import java.text.SimpleDateFormat
import java.util.*

class UpdateNotes : AppCompatActivity() {
    private lateinit var binding:ActivityUpdateNotesBinding
    private val userViewModel: NotesViewModel by lazy { ViewModelProvider(this).get(NotesViewModel::class.java) }
    override fun onCreate(savedInstanceState: Bundle?) {
        binding=ActivityUpdateNotesBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        updateDataFunction()
    }
    private fun updateDataFunction() {
        val bundle: Bundle? = intent.extras
        val receivedData: NotesDataClass = bundle?.getSerializable("titles") as NotesDataClass
        Toast.makeText(this, receivedData.id.toString(), Toast.LENGTH_SHORT).show()
        binding.uptitle.setText(receivedData.title)
        binding.updescription.setText(receivedData.desc.toString())

        binding.upchange.setOnClickListener {
            if (binding.uptitle.text.toString().isEmpty() || binding.updescription.text.toString().isEmpty()){
                Toast.makeText(this,"input Failed", Toast.LENGTH_SHORT).show()
            }
            else{
                val notes = NotesDataClass(
                    receivedData.id,
                    binding.uptitle.text.toString(),
                    binding.updescription.text.toString(),
                    currentDateAndTimes().toString()
                )
                userViewModel.updateData(notes)
                Intent(this, MainActivity::class.java).apply {
                    startActivity(this)
                    finish()
                }
                onBackPressed()
            }
        }
    }
    fun currentDateAndTimes(): CharSequence? {
        val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm:ss z")
        return sdf.format(Date())
    }
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}