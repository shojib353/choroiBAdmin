package com.cz.czadmin.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.cz.czadmin.R
import com.cz.czadmin.catagoryAdapter
import com.cz.czadmin.catdata
import com.cz.czadmin.databinding.FragmentAllProductBinding
import com.cz.czadmin.databinding.FragmentProductBinding
import com.cz.czadmin.editProductActivity
import com.cz.czadmin.productAdapter
import com.cz.czadmin.productDataModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class allProduct : Fragment() {

    lateinit var binding: FragmentAllProductBinding



    private fun getData() {
        val list =ArrayList<productDataModel>()
        Firebase.firestore.collection("products")
            .get().addOnSuccessListener {

                list.clear()
                for (doc in it.documents){
                    val data=doc.toObject(productDataModel::class.java)
                    list.add(data!!)
                }
                binding.productRecycle.layoutManager=LinearLayoutManager(requireContext())
                val adaptar=productAdapter(requireContext(), list)
                binding.productRecycle.adapter=adaptar

                adaptar.setonclickLsp(object :productAdapter.onClickListenerSp{
                    override fun onClick(position: Int, scmodel: productDataModel) {
                        val intent= Intent(context, editProductActivity::class.java)
                        intent.putExtra("id",list[position].productId)
                        context!!.startActivity(intent)

                    }

                    override fun onDelete(position: Int, scmodel: productDataModel) {

                        var id= scmodel.productId.toString()
                        var Did= scmodel.productDId.toString()

                        FirebaseStorage.getInstance().getReference("products").child(id).delete()

                        Firebase.firestore.collection("products").document(Did).delete().addOnSuccessListener {
                            list.removeAt(position)
                            adaptar.notifyItemRemoved(position)
                            Toast.makeText(requireContext(),"delete", Toast.LENGTH_SHORT).show()



                        }

                    }

                })




            }

            }





    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentAllProductBinding.inflate(layoutInflater)

        getData()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        getData()
    }


}