package com.cz.czadmin

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cz.czadmin.databinding.ItemsubcatBinding


class subcatagoryAdapter(var context: Context, var list: ArrayList<subcatdata>):
    RecyclerView.Adapter<subcatagoryAdapter.subcatagoryviewholder>() {
  inner class subcatagoryviewholder(view: View):RecyclerView.ViewHolder(view) {
      var binding = ItemsubcatBinding.bind(view)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): subcatagoryviewholder {
        return subcatagoryviewholder(
            LayoutInflater.from(context).inflate(R.layout.itemsubcat,parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: subcatagoryviewholder, position: Int) {

        Glide.with(context).load(list[position].subCatagoryCoverImage).into(holder.binding.subimg)
        holder.binding.subname.text=list[position].subCatagoryName
        holder.binding.maincatagoryname.text=list[position].productCatagory
    }
}