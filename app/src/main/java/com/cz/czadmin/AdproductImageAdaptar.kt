package com.cz.czadmin

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cz.czadmin.databinding.ImageItemBinding

class AdproductImageAdaptar(val list:ArrayList<Uri>):RecyclerView.Adapter<AdproductImageAdaptar
.AdproductImageViewHolder>() {
    inner class AdproductImageViewHolder( val binding:ImageItemBinding )
        :RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdproductImageViewHolder {
        val binding=ImageItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return AdproductImageViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: AdproductImageViewHolder, position: Int) {
        holder.binding.itemImage.setImageURI(list[position])
    }


}