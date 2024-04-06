package com.example.aibot.models

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.aibot.R

class GeminiAdapter : RecyclerView.Adapter<GeminiAdapter.ViewHolder>() {

    private val diffUtil = object : DiffUtil.ItemCallback<ModelClass>() {

        override fun areItemsTheSame(oldItem: ModelClass, newItem: ModelClass): Boolean {
            return oldItem.prompt == newItem.prompt
        }

        override fun areContentsTheSame(oldItem: ModelClass, newItem: ModelClass): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this,diffUtil)

    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view){

        val textView : TextView = view.findViewById(R.id.RtextView)
        val userText : TextView = view.findViewById(R.id.RcurrentUser)
        val image : ImageView = view.findViewById(R.id.RimageView)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GeminiAdapter.ViewHolder {
       val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_items, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: GeminiAdapter.ViewHolder, position: Int) {

        val pos = holder.adapterPosition
        val diffValue = differ.currentList[pos]

        holder.textView.text = diffValue.prompt
        holder.userText.text = if(diffValue.isUser) "You" else "ChatBot"

        if(diffValue.image != null){
            holder.image.setImageBitmap(diffValue.image)
            holder.image.visibility = View.VISIBLE
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

}