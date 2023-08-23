package com.dbucci.chronostasis

import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CountListAdapter(private val countList: Array<Timer>) :
    RecyclerView.Adapter<CountListAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_count, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = countList[position]
        val drawable = holder.tag.background as GradientDrawable

        holder.icon.setImageResource(currentItem.getIcon())
        holder.time.text = currentItem.getTime()
        holder.tag.text = currentItem.getTag()

        drawable.setColor(currentItem.getColor())
    }

    override fun getItemCount(): Int {
        return countList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val icon: ImageView = itemView.findViewById(R.id.image_view_icon)
        val time: TextView = itemView.findViewById(R.id.text_view_timer_value)
        val tag: TextView = itemView.findViewById(R.id.text_view_tag)
    }
}