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

        //Firebase.firestore.collection("subcatagory").document().id







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
                val adapter=catagoryAdapter(requireContext(), list)
                binding.recycle.adapter=adapter
                adapter.setonclickLsp(object :catagoryAdapter.onClickListenerSp{
                    override fun onClick(position: Int, scmodel: catdata) {
                        Toast.makeText(requireContext(),"one click",Toast.LENGTH_SHORT).show()

                    }

                    override fun onDelete(position: Int, scmodel: catdata) {

                        var id= scmodel.productId.toString()
                        var Did= scmodel.productDId.toString()
                        FirebaseStorage.getInstance().getReference("catagory").child(id).delete()

                        Firebase.firestore.collection("catagory").document(Did).delete().addOnSuccessListener {
                            list.removeAt(position)
                            adapter.notifyItemRemoved(position)
                            Toast.makeText(requireContext(),"delete",Toast.LENGTH_SHORT).show()



                        }

                    }

                })

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
        var Key=UUID.randomUUID().toString()


        val refStores= FirebaseStorage.getInstance().getReference("catagory")
        refStores.child(Key).putFile(imgurl!!)
            .addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener { image->
                    storeData(catName,image.toString(),Key)

                }
            }.addOnFailureListener{
                dialog.dismiss()
                Toast.makeText(requireContext(),"something wrong",Toast.LENGTH_SHORT).show()
            }
    }

    private fun storeData(catName: String, url: String, Key: String) {
        val db=Firebase.firestore
        val Did=db.collection("catagory").document().id
        val data= catdata(
            catName,
            url,
            Key,
            Did

        )

        db.collection("catagory").document(Did).set(data)
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