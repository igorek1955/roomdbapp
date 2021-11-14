package com.example.advancedviews.cards.adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.widget.TextView
import com.example.roomdbapp.database.Note
import com.example.roomdbapp.R


class NoteAdapter(val list: List<Note>, val context: Context) :
    RecyclerView.Adapter<NoteAdapter.BeanHolder>() {
    var layoutInflater: LayoutInflater = LayoutInflater.from(context)
    var onNoteItemClick: OnNoteItemClick = context as OnNoteItemClick

    interface OnNoteItemClick {
        fun onNoteClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BeanHolder {
        val view: View = layoutInflater.inflate(R.layout.note_list_item, parent, false)
        return BeanHolder(view)
    }

    override fun onBindViewHolder(holder: BeanHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return list.size
    }


    inner class BeanHolder(private val itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val textViewContent: TextView
        private val textViewTitle: TextView
        init {
            itemView.setOnClickListener(this)
            textViewContent = itemView.findViewById(R.id.item_text)
            textViewTitle = itemView.findViewById(R.id.tv_title)
        }
        override fun onClick(v: View?) {
            onNoteItemClick.onNoteClick(adapterPosition)
        }

        fun bind(position: Int) {
            textViewTitle.text = list[position].getTitle()
            textViewContent.text = list[position].getContent()
        }
    }
}