package com.cz.czadmin.fragment

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.cz.czadmin.R
import com.cz.czadmin.databinding.FragmentHomeBinding

import com.google.android.material.dialog.MaterialAlertDialogBuilder
import javax.xml.datatype.DatatypeFactory.newInstance
import kotlin.system.exitProcess


class home : Fragment() {
    private lateinit var binding: FragmentHomeBinding

private fun onBackPressedMethod() {
    MaterialAlertDialogBuilder(requireContext())
        .setTitle("Exit")
        .setMessage("Are you sure you want to exit?")
        .setCancelable(false)
        .setPositiveButton("Yes"){ _,_ ->

            android.os.Process.killProcess(android.os.Process.myPid())
            exitProcess(1)
        }
        .setNegativeButton("No",null)
        .create()
        .show()
}


    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback = object : OnBackPressedCallback(
            true // default to enabled
        ) {
            override fun handleOnBackPressed() {
                onBackPressedMethod()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            this, // LifecycleOwner
            callback
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {






        binding = FragmentHomeBinding.inflate(layoutInflater)
        //

       binding.btnsubcatagory.setOnClickListener {
           findNavController().navigate(R.id.action_home2_to_subcatagory)
        }
        binding.btncatagory.setOnClickListener {
            findNavController().navigate(R.id.action_home2_to_catagory)
        }

         binding.btnslider.setOnClickListener {
             findNavController().navigate(R.id.action_home2_to_slider)
         }
        binding.btnproduct.setOnClickListener {
           findNavController().navigate(R.id.action_home2_to_product)
        }




        return binding.root
    }




}