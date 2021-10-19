package com.example.flickrbrowserapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.flickrbrowserapp.databinding.ItemRowBinding

class myAdapter(val Activity:MainActivity, val ListOfphotos:ArrayList<image>):RecyclerView.Adapter<myAdapter.ItemViewHolder>(){
    class ItemViewHolder( val binding: ItemRowBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
       return ItemViewHolder(ItemRowBinding.inflate(LayoutInflater.from(parent.context),parent
           ,false))

    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val ListOfphoto= ListOfphotos[position]
        holder.binding.apply {
            textvR.text=ListOfphoto.Tital
            Glide.with(Activity).load(ListOfphoto.Link).into(imgrv)
            consID.setOnClickListener { Activity.openItem(ListOfphoto.Link) }

        }

    }

    override fun getItemCount() = ListOfphotos.size

}