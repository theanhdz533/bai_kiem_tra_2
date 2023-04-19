package com.example.study_firebase.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.study_firebase.R
import com.example.study_firebase.model.ProductModel



class ItemDataAdapter(private val list: List<ProductModel>):RecyclerView.Adapter<ItemDataAdapter.ViewHolder>() {

    // item listener
    private lateinit var listener: onItemClickListener

    interface  onItemClickListener{
        fun onItemClick(position: Int)
    }
    fun setOnItemClickListener(clickListener: onItemClickListener){
        listener = clickListener
    }

    /*------------------------------------------------------------------------------------------------------*/
    class ViewHolder(view: View, clickListener: onItemClickListener):RecyclerView.ViewHolder(view){
        val data : TextView = view.findViewById(R.id.txtDataItem)
        val img : ImageView = view.findViewById(R.id.imageView)
        init {
            view.setOnClickListener{
                clickListener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_item_show_data,parent,false)
        return ViewHolder(itemView,listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val activity = holder.itemView.context as Activity
        holder.data.setText("Tên sản phẩm: "+list[position].name)
        Glide.with(activity)
            .load(list[position].linkImg.toString())
            .into(holder.img)


    }

    override fun getItemCount(): Int {
       return list.size
    }
}