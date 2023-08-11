package com.cz.czadmin.fragment

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.GridLayoutManager
import com.cz.czadmin.R
import com.cz.czadmin.catagoryAdapter
import com.cz.czadmin.catdata
import com.cz.czadmin.databinding.FragmentCatagoryBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID


class catagory : Fragment() {
    private lateinit var binding:FragmentCatagoryBinding
    private var imgurl: Uri?=null
    private lateinit var dialog: Dialog







    private var launchGalleryActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        if(it.resultCode==Activity.RESULT_OK){
            imgurl =it.data!!.data
            binding.imageView.setImageURI(imgurl)

        }
    }





    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



        binding=FragmentCatagoryBinding.inflate(layoutInflater)

        dialog=Dialog(requireContext())
        dialog.setContentView(R.layout.progress)
        dialog.setCancelable(false)

        getData()


        binding.apply {


            imageView.setOnClickListener {
                val intent=Intent("android.intent.action.GET_CONTENT")
                intent.type="image/*"
                launchGalleryActivity.launch(intent)

            }
            button6.setOnClickListener {

                validedData(binding.catName.text.toString())

            }
        }


        return binding.root
    }

    private fun getData() {
        val list =ArrayList<catdata>()
        Firebase.firestore.collection("catagory")
            .get().addOnSuccessListener {

                list.clear()
                for (x in it.documents){
                    val data=x.toObject(catdata::class.java)
                    list.add(data!!)
                }
                binding.recycle.layoutManager=GridLayoutManager(requireContext(),2)
                binding.recycle.adapter=catagoryAdapter(requireContext(), list)

            }
    }

    private fun validedData(catName: String) {
        if (catName.isEmpty()){
            Toast.makeText(requireContext(),"input name of product",Toast.LENGTH_SHORT).show()
        }

        else if (imgurl==null) {

            Toast.makeText(requireContext(),"input IMAGE of product",Toast.LENGTH_SHORT).show()

        }
        else{
            uploadimg(catName)
        }

    }

    private fun uploadimg(catName: String) {
        dialog.show()
        val fileName=UUID.randomUUID().toString()+".jpg"

        val refStore=FirebaseStorage.getInstance().reference.child("catagory/$fileName")
        refStore.putFile(imgurl!!)
            .addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener { image->
                    storeData(catName,image.toString())

                }
            }.addOnFailureListener{
                dialog.dismiss()
                Toast.makeText(requireContext(),"something wrong",Toast.LENGTH_SHORT).show()
            }
    }

    private fun storeData(catName: String, url: String) {
        val db=Firebase.firestore
        val data= hashMapOf<String, Any>(
            "productCatagory" to catName,
            "productCoverImage" to url
        )

        db.collection("catagory").document().set(data)
            .addOnSuccessListener {
                dialog.dismiss()
                binding.imageView.setImageDrawable(resources.getDrawable(R.drawable.img ))
                binding.catName.text= null


                getData()

                Toast.makeText(requireContext(),"catagory upload",Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                dialog.dismiss()
                Toast.makeText(requireContext(),"something Wrong",Toast.LENGTH_SHORT).show()
            }
    }


}