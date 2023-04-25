package com.example.notesapp.ui.view.main

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.notesapp.R
import com.example.notesapp.Roomdb.NotesDataBase
import com.example.notesapp.databinding.ActivityMainBinding
import com.example.notesapp.model.data.NotesDataClass
import com.example.notesapp.ui.adapter.NotesAdapter
import com.example.notesapp.ui.viewmodel.NotesViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val notesAdapter: NotesAdapter by lazy { NotesAdapter() }
    private lateinit var layoutManager: StaggeredGridLayoutManager
    private val userViewModel: NotesViewModel by lazy { ViewModelProvider(this).get(NotesViewModel::class.java) }
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setAdapter()
        launchAddDataReceived()
        observeData()
        deleteData()
        updatedata()
        searchListenText()
        searchData()
    }

    private fun searchData() {
        userViewModel.liveSearch.observe(this, androidx.lifecycle.Observer {
            if (it.toString().isNotEmpty()) {
                filterArray(it.toString())
            }
            else {
                lifecycleScope.launchWhenCreated {
                    userViewModel.notesLiveData.collect() {
                        notesAdapter.differ.submitList(it){
                            binding.notesCycle.scrollToPosition(0)
                        }
                    }
                }
            }
        })
    }

    private fun filterArray(searchInput: String?) {
            val tempArray = ArrayList<NotesDataClass>()
            tempArray.clear()
        lifecycleScope.launchWhenCreated {
            userViewModel.notesLiveData.collect() {
                if (searchInput?.isNotEmpty() == true) {
                    for (item in it) {
                        if (item.title.lowercase().toString().trim()
                                .contains(
                                    searchInput.toString().lowercase()
                                ) || item.desc.lowercase().trim()
                                .toString().contains(searchInput.toString().lowercase())
                        ) {
                            tempArray.add(item)
                        }
                    }
                }
                if (tempArray.isEmpty()) {
                    notesAdapter.differ.submitList(tempArray)
                } else {
                    notesAdapter.differ.submitList(tempArray)
                }
            }
        }
    }

    private fun searchListenText() {
        binding.searchNotes.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
               try {
                   userViewModel.setMessage(s.toString())
               }
               catch (e:Exception){
                   Toast.makeText(this@MainActivity, e.toString(), Toast.LENGTH_SHORT).show()
               }

            }
            override fun afterTextChanged(s: Editable?) {

            }
        })
    }

    private fun updatedata() {
        notesAdapter.updatePos {
            val intent = Intent(this, UpdateNotes::class.java)
            intent.putExtra("titles", it)
            startActivity(intent)
            finish()
        }
    }

    private fun deleteData() {
        notesAdapter.passData { data, card ->
            val menuButton = PopupMenu(this, card)
            menuButton.menuInflater.inflate(R.menu.popmenu, menuButton.menu)
            menuButton.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.action_del -> {
                        userViewModel.deleteData(data)
                    }
                    else -> {
                    }
                }
                true
            }
            menuButton.show()
        }
    }

    private fun observeData() {
        lifecycleScope.launchWhenCreated {
            userViewModel.notesLiveData.collect(){
                Log.d("notesrespo", it.size.toString())
                notesAdapter.differ.submitList(it)
                binding.notesCycle.layoutManager?.scrollToPosition(0)
            }
        }
    }

    private fun launchAddDataReceived() {
        val getLaunch =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    val intent = result.data
                    val title = intent?.getStringExtra("titlePass")
                    val desc = intent?.getStringExtra("descPass")
                    val notesList = NotesDataClass(
                        0,
                        title.toString(),
                        desc.toString(),
                        currentDateAndTimes().toString()
                    )
                    userViewModel.insertData(notesList)
                }
            }
        binding.addFloat.setOnClickListener {
            val intentMain = Intent(this, AddNotes::class.java)
            getLaunch.launch(intentMain)
        }
    }

    private fun currentDateAndTimes(): CharSequence? {
        val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm:ss z")
        return sdf.format(Date())
    }

    private fun setAdapter() {
        binding.notesCycle.adapter = notesAdapter
        layoutManager = StaggeredGridLayoutManager(2, LinearLayout.VERTICAL)
        binding.notesCycle.layoutManager = layoutManager
    }
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}