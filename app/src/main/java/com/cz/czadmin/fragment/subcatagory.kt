package com.cz.czadmin.fragment

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.cz.czadmin.R
import com.cz.czadmin.catdata
import com.cz.czadmin.databinding.FragmentSubcatagoryBinding
import com.cz.czadmin.subcatagoryAdapter
import com.cz.czadmin.subcatdata
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage


@Suppress("Deprecated")
class subcatagory : Fragment() {
    lateinit var binding:FragmentSubcatagoryBinding
    private lateinit var catagoryList:ArrayList<String>
    private var imgurl: Uri?=null
    private lateinit var dialog: Dialog
    var SubCatagoryCoverImageUrl:String?=""
    var Key=Firebase.firestore.collection("subcatagory").document().id


    private var launchGalleryActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        if(it.resultCode== Activity.RESULT_OK){
            imgurl =it.data!!.data
            binding.subcatimg.setImageURI(imgurl)

        }
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setProductCatagory()
        getData()



        binding=FragmentSubcatagoryBinding.inflate(layoutInflater)
        dialog=Dialog(requireContext())
        dialog.setContentView(R.layout.progress)
        dialog.setCancelable(false)



        binding.apply {


            subcatimg.setOnClickListener {
                val intent = Intent("android.intent.action.GET_CONTENT")
                intent.type = "image/*"
                launchGalleryActivity.launch(intent)

            }

            subcataddbtn.setOnClickListener {

                validedData(binding.subcatName.text.toString())



            }
        }




        return binding.root
    }



    private fun validedData(subCatName: String) {
        if (subCatName.isEmpty()){

            Toast.makeText(requireContext(),"input name of product", Toast.LENGTH_SHORT).show()
        }

        else if (imgurl==null) {


            Toast.makeText(requireContext(),"input IMAGE of product", Toast.LENGTH_SHORT).show()

        }
        else{
            uploadimg()
        }

    }
    private fun uploadimg() {
        dialog.show()


        //val refStore= FirebaseStorage.getInstance().reference.child("subcatagory/$fileName")
        val refStores= FirebaseStorage.getInstance().getReference("subcatagory")
        refStores.child(Key).putFile(imgurl!!)
            .addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener { image->
                    SubCatagoryCoverImageUrl=image.toString()
                    storeData()

                }
            }.addOnFailureListener{
                dialog.dismiss()
                Toast.makeText(requireContext(),"something wrong",Toast.LENGTH_SHORT).show()
            }
    }
    private fun storeData() {
        val db=Firebase.firestore



        val data= subcatdata(
            catagoryList[binding.productSubCatDrop.selectedItemPosition] ,
            SubCatagoryCoverImageUrl.toString(),
            binding.subcatName.text.toString(),
            Key


        )


        db.collection("subcatagory").document(Key).set(data)
            .addOnSuccessListener {
                dialog.dismiss()
                binding.subcatimg.setImageDrawable(resources.getDrawable(R.drawable.img ))
                binding.subcatName.text=null

                Toast.makeText(requireContext(),"sub catagory upload",Toast.LENGTH_SHORT).show()
                getData()



            }
            .addOnFailureListener {
                dialog.dismiss()
                Toast.makeText(requireContext(),"something Wrong",Toast.LENGTH_SHORT).show()
            }
    }
    private fun getData() {
        val list =ArrayList<subcatdata>()
        Firebase.firestore.collection("subcatagory")
            .get().addOnSuccessListener {

                list.clear()
                for (x in it.documents){
                    val data=x.toObject(subcatdata::class.java)
                    list.add(data!!)
                }
                val adapter=subcatagoryAdapter(requireContext(),list)
                binding.subcatrecycle.adapter=adapter



                adapter.setonclickL(object:subcatagoryAdapter.OnclickL{
                    override fun onClick(position: Int, scmodel: subcatdata) {
                        Toast.makeText(requireContext(),"one click",Toast.LENGTH_SHORT).show()
                    }

                    override fun onDelete(position: Int, scmodel: subcatdata) {
                       var id= scmodel.productId.toString()
                        FirebaseStorage.getInstance().getReference("subcatagory").child(id).delete()

                        Firebase.firestore.collection("subcatagory").document(id).delete().addOnSuccessListener {
                            list.removeAt(position)
                            adapter.notifyItemRemoved(position)
                            Toast.makeText(requireContext(),"delete",Toast.LENGTH_SHORT).show()



                        }



                    }


                })


            }
    }



    private fun setProductCatagory(){

        catagoryList= ArrayList()
        Firebase.firestore.collection("catagory").get().addOnSuccessListener {
            catagoryList.clear()

            for (doc in it.documents){
                val data=doc.toObject(catdata::class.java)
                catagoryList.add(data!!.productCatagory!!)


            }
            catagoryList.add(0,"select catagory")

            val arrayAdaptar= ArrayAdapter(requireContext(),R.layout.drwop_item2,catagoryList)
            binding.productSubCatDrop.adapter=arrayAdaptar

        }
    }




}