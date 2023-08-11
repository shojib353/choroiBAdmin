package com.cz.czadmin

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.cz.czadmin.databinding.ActivityEditProductBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class editProductActivity : AppCompatActivity() {
    lateinit var binding: ActivityEditProductBinding


    lateinit var adaptarp:productAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityEditProductBinding.inflate(layoutInflater)
        var pid=intent.getStringExtra("id")

        getProductDetails(pid)
        binding.updateProductBtn.setOnClickListener {

            var UPname=binding.productName.text.toString()
            var UPmrp=binding.productmrp.text.toString()
            var UPdes=binding.productdes.text.toString()
            var UPsp=binding.productsp.text.toString()

            var update= mapOf(

                "productName" to UPname,
                "productMrp" to UPmrp,
                "productDescription" to UPdes,
                "productSp" to UPsp

            )
            Firebase.firestore.collection("products").document(pid!!).update(update)
                .addOnSuccessListener {
                    Toast.makeText(this,"UPDATE DONE",Toast.LENGTH_SHORT).show()
                    adaptarp.notifyDataSetChanged()








                }

        }



        setContentView(binding.root)
    }

    private fun getProductDetails(proid: String?) {

        Firebase.firestore.collection("products").document(proid!!).get()
            .addOnSuccessListener {


            binding.productName.setText(it.getString("productName"))
                binding.productmrp.setText(it.getString("productMrp"))
                binding.productsp.setText(it.getString("productSp"))
                binding.productdes.setText(it.getString("productDescription"))






        }
    }
}