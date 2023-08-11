package com.cz.czadmin.fragment

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.cz.czadmin.R
import com.cz.czadmin.databinding.FragmentSliderBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID


class slider : Fragment() {
    private lateinit var binding:FragmentSliderBinding
    private var imgurl: Uri?=null

    private lateinit var dialog: Dialog

    private var launchGalleryActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        if(it.resultCode== Activity.RESULT_OK){
            imgurl =it.data!!.data
            binding.sliderimg.setImageURI(imgurl)

        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding=FragmentSliderBinding.inflate(layoutInflater)

        dialog= Dialog(requireContext())
        dialog.setContentView(R.layout.progress)
        dialog.setCancelable(false)

        binding.apply {
            sliderimg.setOnClickListener {
                val intent= Intent("android.intent.action.GET_CONTENT")
                intent.type="image/*"
                launchGalleryActivity.launch(intent)
            }

            sliderbtn.setOnClickListener {

                 if (imgurl==null) {

                Toast.makeText(requireContext(),"input IMAGE of product", Toast.LENGTH_SHORT).show()

            }
            else{
                uploadimg(imgurl!!)
            }

            }
        }



        return binding.root
    }

    private fun uploadimg(uri: Uri) {

        dialog.show()
        val fileName= UUID.randomUUID().toString()+".jpg"

        val refStore= FirebaseStorage.getInstance().reference.child("slider/$fileName")
        refStore.putFile(uri)
            .addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener { image->
                    storeData(image.toString())

                }
            }.addOnFailureListener{
                dialog.dismiss()
                Toast.makeText(requireContext(),"uploding wrong",Toast.LENGTH_SHORT).show()
            }

    }

    private fun storeData(image:String) {

        val db= Firebase.firestore
        val data= hashMapOf<String, Any>(

            "image" to image
        )

        db.collection("slider").document("item").set(data)
            .addOnSuccessListener {
                dialog.dismiss()
               Toast.makeText(requireContext(),"slider upload",Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                dialog.dismiss()
                Toast.makeText(requireContext(),"something Wrong",Toast.LENGTH_SHORT).show()
            }

    }


}