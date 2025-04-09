package com.example.androidassessmenttest.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidassessmenttest.R
import com.example.androidassessmenttest.data.model.ListItem

class LabelAdapter(var labels: List<ListItem>) :
    RecyclerView.Adapter<LabelAdapter.LabelViewHolder>() {

    inner class LabelViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title = view.findViewById<TextView>(R.id.title)
        val subtitle = view.findViewById<TextView>(R.id.subtitle)
        val image = view.findViewById<ImageView>(R.id.image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LabelViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        return LabelViewHolder(view)
    }

    override fun onBindViewHolder(holder: LabelViewHolder, position: Int) {
        holder.title.text = labels[position].title
        holder.subtitle.text = labels[position].subtitle
        holder.image.setImageResource(labels[position].imageRes) // Static for simplicity
    }

    override fun getItemCount(): Int = labels.size

    fun update(newList: List<ListItem>) {
        labels = newList
        notifyDataSetChanged()
    }
}
