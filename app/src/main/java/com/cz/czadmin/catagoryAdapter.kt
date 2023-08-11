package com.cz.czadmin


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cz.czadmin.databinding.ItemBinding

class catagoryAdapter(var context: Context, var list: ArrayList<catdata>):RecyclerView

                .Adapter<catagoryAdapter.catagoryViewHolder>() {
    inner class catagoryViewHolder(view:View):RecyclerView.ViewHolder(view){

        var binding = ItemBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): catagoryViewHolder {
        return catagoryViewHolder(LayoutInflater.from(context).inflate(R.layout.item,parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: catagoryViewHolder, position: Int) {

        holder.binding.textView2.text=list[position].productCatagory
        Glide.with(context).load(list[position].productCoverImage).into(holder.binding.imageView2)

    }
}