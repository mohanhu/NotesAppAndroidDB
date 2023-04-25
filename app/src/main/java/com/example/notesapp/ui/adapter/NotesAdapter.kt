package com.example.notesapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.R
import com.example.notesapp.databinding.CardListBinding
import com.example.notesapp.model.data.NotesDataClass
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random

class NotesAdapter : RecyclerView.Adapter<NotesAdapter.ViewHolderOne>() {
    private lateinit var passpos: ((NotesDataClass,CardView) -> Unit)
    private lateinit var updatepos: ((NotesDataClass) -> Unit)

    inner class ViewHolderOne(private val binding: CardListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItem(data: NotesDataClass) {
            binding.titleDis.text = data.title
            binding.descriptiondisp.text = data.desc
            binding.dateDispl.text = data.date
            binding.card.setCardBackgroundColor(randomColor())
            binding.layout.setBackgroundResource(randomColor())

            binding.card.setOnLongClickListener {
                passpos.invoke(data,binding.card)
                true
            }
            binding.card.setOnClickListener {
                updatepos.invoke(data)
            }
        }
    }

    fun randomColor(): Int {
        val colorList = ArrayList<Int>()
        colorList.add(R.color.lime)
        colorList.add(R.color.cadetblue)
        colorList.add(R.color.tan)
        colorList.add(R.color.mediumorchid)
        colorList.add(R.color.paleturquoise)
        colorList.add(R.color.blue)
        colorList.add(R.color.magenta)

        val seed = System.currentTimeMillis().toInt()
        val randomIndex = Random(seed).nextInt(colorList.size)
        return colorList[randomIndex]
    }

    val differcallback = object : DiffUtil.ItemCallback<NotesDataClass>() {
        override fun areItemsTheSame(
            oldItem: NotesDataClass,
            newItem: NotesDataClass
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: NotesDataClass,
            newItem: NotesDataClass
        ): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, differcallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderOne {
        return ViewHolderOne(
            binding = CardListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolderOne, position: Int) {
        holder.bindItem(differ.currentList[position])
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
    fun passData(listen:((NotesDataClass,CardView)->Unit)){
        passpos=listen
    }
    fun updatePos(listener:((NotesDataClass)->Unit)){
        updatepos=listener
    }
}