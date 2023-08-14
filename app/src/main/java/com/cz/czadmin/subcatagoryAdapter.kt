package com.cz.czadmin

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cz.czadmin.databinding.ItemsubcatBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class subcatagoryAdapter(var context: Context, var list: ArrayList<subcatdata>):
    RecyclerView.Adapter<subcatagoryAdapter.subcatagoryviewholder>() {

    private var mcl:OnclickL?=null
    private var mdcl:OnclickL?=null
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
        holder.itemView.setOnClickListener {
            mcl!!.onClick(position,list[position])
        }
        holder.itemView.setOnLongClickListener {


            MaterialAlertDialogBuilder(holder.itemView.context)
                .setTitle("Delete item permanently")
                .setMessage("Are you sure you want to delete this item?")
                .setPositiveButton("Yes"){_,_->
                    mdcl!!.onDelete( position,list[position])

                }.setNegativeButton("No"){_,_ ->
                    Toast.makeText(holder.itemView.context,"canceled" , Toast.LENGTH_SHORT).show()
                }
                .show()


            true
        }
    }

    fun setonclickL(oncl:OnclickL){
        this.mcl = oncl
        this.mdcl=oncl

    }
    interface OnclickL{
        fun onClick(position: Int,scmodel:subcatdata)
        fun onDelete(position: Int,scmodel:subcatdata)
    }

}