package com.cz.czadmin.fragment

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.cz.czadmin.R
import com.cz.czadmin.catagoryAdapter
import com.cz.czadmin.catdata
import com.cz.czadmin.databinding.FragmentAllProductBinding
import com.cz.czadmin.databinding.FragmentProductBinding
import com.cz.czadmin.productAdapter
import com.cz.czadmin.productDataModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

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
                binding.productRecycle.adapter=productAdapter(requireContext(), list)




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


}