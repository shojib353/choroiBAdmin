package com.cz.czadmin


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cz.czadmin.databinding.ItemBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class catagoryAdapter(var context: Context, var list: ArrayList<catdata>):RecyclerView

                .Adapter<catagoryAdapter.catagoryViewHolder>() {

    private var mcl:onClickListenerSp?=null
    private var mdcl: onClickListenerSp?=null
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
    fun setonclickLsp(oncl: catagoryAdapter.onClickListenerSp){
        this.mcl = oncl
        this.mdcl=oncl

    }
    interface onClickListenerSp{
        fun onClick(position: Int,scmodel:catdata)
        fun onDelete(position: Int,scmodel:catdata)

    }
}