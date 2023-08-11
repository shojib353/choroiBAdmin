package com.cz.czadmin

import android.content.Context
import android.content.Intent
import android.location.GnssAntennaInfo.Listener
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cz.czadmin.databinding.ProductItemBinding

class productAdapter(var context: Context, var list: ArrayList<productDataModel>):
    RecyclerView.Adapter<productAdapter.productAdapterViewHolder>(){
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
            val intent=Intent(context,editProductActivity::class.java)
            intent.putExtra("id",list[position].productId)
            context.startActivity(intent)
        }

    }


}