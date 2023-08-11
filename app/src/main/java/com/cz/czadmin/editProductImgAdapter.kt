package com.cz.czadmin

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cz.czadmin.databinding.ImageItemBinding


class editProductImgAdapter(val list:ArrayList<Uri>): RecyclerView.Adapter<editProductImgAdapter
.editProductImgViewHolder>() {
    inner class editProductImgViewHolder(val binding: ImageItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): editProductImgViewHolder {
        val binding=ImageItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return editProductImgViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: editProductImgViewHolder, position: Int) {

        holder.binding.itemImage.setImageURI(list[position])

    }


}