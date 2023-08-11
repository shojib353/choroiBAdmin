package com.cz.czadmin.fragment

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.cz.czadmin.AdproductImageAdaptar
import com.cz.czadmin.Adproductmodel
import com.cz.czadmin.R
import com.cz.czadmin.catdata
import com.cz.czadmin.databinding.FragmentProductBinding
import com.cz.czadmin.subcatdata
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID


class product : Fragment() {

    private lateinit var binding:FragmentProductBinding
    private lateinit var listimages:ArrayList<String>
    private lateinit var list:ArrayList<Uri>
    private lateinit var adaptar:AdproductImageAdaptar
    private var coverimage:Uri?=null
    private lateinit var dialog:Dialog
    private var coverImageUrl:String?=""
    private lateinit var subCatagoryList:ArrayList<String>



    private var launchGalleryActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        if(it.resultCode== Activity.RESULT_OK){
            coverimage =it.data!!.data
            binding.productCImage.setImageURI(coverimage)
            binding.productCImage.visibility=View.VISIBLE
        }
    }


    private var launchProductActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        if(it.resultCode==Activity.RESULT_OK){
          val imgurl =it.data!!.data
           list.add(imgurl!!)
            adaptar.notifyDataSetChanged()

        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentProductBinding.inflate(layoutInflater)


        swif()
        setProductsubCatagory()

        list= ArrayList()
        listimages= ArrayList()
        dialog= Dialog(requireContext())
        dialog.setContentView(R.layout.progress)
        dialog.setCancelable(false)

        binding.productCImgbtn.setOnClickListener {
            val intent= Intent("android.intent.action.GET_CONTENT")
            intent.type="image/*"
            launchGalleryActivity.launch(intent)

        }

        binding.productImgBtn.setOnClickListener {
            val intent= Intent("android.intent.action.GET_CONTENT")
            intent.type="image/*"
            launchProductActivity.launch(intent)
            binding.productImgRecycler.visibility=View.VISIBLE

        }
        adaptar= AdproductImageAdaptar(list)
        binding.productImgRecycler.adapter = adaptar
        binding.submitProductBtn.setOnClickListener {
            validateData()
        }
        binding.SeeProductBtn.setOnClickListener {

            findNavController().navigate(R.id.action_product_to_allProduct)

        }



        return binding.root
    }

    private fun validateData() {
        if (binding.productname.text.toString().isEmpty()){

            binding.productname.requestFocus()
            binding.productname.error="Empty"
        }else if (binding.productsp.text.toString().isEmpty()){
            binding.productname.requestFocus()
            binding.productname.error="Empty"


        }else if (coverimage==null){
            Toast.makeText(requireContext(),"selecet cover image",Toast.LENGTH_SHORT).show()
        }else if (list.size<1){
            Toast.makeText(requireContext(),"selecet product images",Toast.LENGTH_SHORT).show()
        }else{
            uploadImage()
        }
    }

    private fun uploadImage() {
        dialog.show()
        val fileName= UUID.randomUUID().toString()+".jpg"

        val refStore= FirebaseStorage.getInstance().reference.child("products/$fileName")
        refStore.putFile(coverimage!!)
            .addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener { image->
                    coverImageUrl=image.toString()
                    uploadProductImage()

                }
            }.addOnFailureListener{
                dialog.dismiss()
                Toast.makeText(requireContext(),"something wrong",Toast.LENGTH_SHORT).show()
            }
    }
    private var i=0
    private fun uploadProductImage() {
        dialog.show()
        val fileName=UUID.randomUUID().toString()+".jpg"

        val refStore=FirebaseStorage.getInstance().reference.child("products/$fileName")
        refStore.putFile(list[i]!!)
            .addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener { image->

                    listimages.add(image!!.toString())
                    binding.productCImage.setImageURI(null)

                    if (list.size==listimages.size){
                    storeData()
                    }else{
                        i+=1
                        uploadProductImage()
                    }

                }
            }.addOnFailureListener{
                dialog.dismiss()
                Toast.makeText(requireContext(),"something wrong",Toast.LENGTH_SHORT).show()
            }
    }

    private fun storeData() {
        val db=Firebase.firestore.collection("products")
        val key=db.document().id
        val data=Adproductmodel(
            binding.productname.text.toString(),
            binding.productdes.text.toString(),
            coverImageUrl.toString(),
            null,
            subCatagoryList[binding.productCatDrop.selectedItemPosition],
            key,
            binding.productmrp.text.toString(),
            binding.productsp.text.toString(),
            listimages

        )
        db.document(key).set(data).addOnSuccessListener {
            binding.productCImage.setImageURI(null)
            binding.productname.text=null
            binding.productdes.text=null
            binding.productmrp.text=null
            binding.productsp.text=null
            list.clear()
            listimages.clear()

            dialog.dismiss()
            Toast.makeText(requireContext(),"product Added",Toast.LENGTH_SHORT).show()
            /*binding.productname.text=null
            binding.productdes.text=null
            binding.productmrp.text=null
            binding.productsp.text=null
            list.clear()
            listimages.clear()*/
          //  refresh(context)







        }.addOnFailureListener {
            dialog.dismiss()
            Toast.makeText(requireContext(),"error",Toast.LENGTH_SHORT).show()

        }
    }

    private fun setProductsubCatagory(){

        subCatagoryList= ArrayList()
        Firebase.firestore.collection("subcatagory").get().addOnSuccessListener {
            subCatagoryList.clear()

            for (doc in it.documents){
                val data=doc.toObject(subcatdata::class.java)
                subCatagoryList.add(data!!.subCatagoryName!!)


            }
            subCatagoryList.add(0,"Select Sub Catagory")

            val arrayAdaptar=ArrayAdapter(requireContext(),R.layout.drwop_item,subCatagoryList)
            binding.productCatDrop.adapter=arrayAdaptar

        }
    }
   /*private fun refresh(context: Context?){
        context?.let {
            val fragmentManager= (context as? AppCompatActivity)?.supportFragmentManager
            fragmentManager?.let {
                val currentFragment=fragmentManager.findFragmentById(R.id.frame)

                currentFragment?.let {
                    val fragmentTransaction=fragmentManager.beginTransaction()

                    fragmentTransaction.detach(it)
                    fragmentTransaction.attach(it)
                    fragmentTransaction.commit()
                }
            }
        }
    }*/

    fun swif(){

        binding.swipeToRefresh.setOnRefreshListener {
            setProductsubCatagory()


            listimages.clear()
            binding.productCImage.setImageURI(null)
            binding.productCImage.visibility=View.GONE
            binding.productname.text=null
            binding.productdes.text=null
            binding.productmrp.text=null
            binding.productsp.text=null
            binding.productImgRecycler.visibility=View.GONE
            list.clear()


            Toast.makeText(requireContext(),"Refresh",Toast.LENGTH_SHORT).show()
            binding.swipeToRefresh.isRefreshing=false
        }
    }

}