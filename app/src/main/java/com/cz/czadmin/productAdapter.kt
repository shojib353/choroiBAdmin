package com.cz.czadmin

import android.content.Context
import android.content.Intent
import android.location.GnssAntennaInfo.Listener
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cz.czadmin.databinding.ProductItemBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class productAdapter(var context: Context, var list: ArrayList<productDataModel>):
    RecyclerView.Adapter<productAdapter.productAdapterViewHolder>(){
    private var mcl: productAdapter.onClickListenerSp?=null
    private var mdcl: productAdapter.onClickListenerSp?=null
    inner class productAdapterViewHolder (view:View):RecyclerView.ViewHolder(view){

        var binding= ProductItemBinding.bind(view)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): productAdapterViewHolder {

        return productAdapterViewHolder(LayoutInflater.from(context)

            .inflate(R.layout.product_item,parent,false)

        )

    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: productAdapterViewHolder, position: Int) {
        holder.binding.ProductNAme.text=list[position].productName
        holder.binding.mrp.text=list[position].productMrp
        holder.binding.sp.text=list[position].productSp
        Glide.with(context).load(list[position].productCoverImage).into(holder.binding.ProductImage)

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
    fun setonclickLsp(oncl: productAdapter.onClickListenerSp){
        this.mcl = oncl
        this.mdcl=oncl

    }
    interface onClickListenerSp{
        fun onClick(position: Int,scmodel:productDataModel)
        fun onDelete(position: Int,scmodel:productDataModel)

    }


}