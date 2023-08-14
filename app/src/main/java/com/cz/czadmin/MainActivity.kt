package com.cz.czadmin

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import com.cz.czadmin.fragment.home
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {
    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            onBackPressedMethod()
        }
    }


    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
       // onBackPressedDispatcher.addCallback(this,onBackPressedCallback)


    }
    private fun onBackPressedMethod() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Exit")
            .setMessage("Are you sure you want to exit?")
            .setCancelable(false)
            .setPositiveButton("Yes"){ _,_ ->
                moveTaskToBack(true)
                android.os.Process.killProcess(android.os.Process.myPid())
                exitProcess(1)
            }
            .setNegativeButton("No",null)
            .create()
            .show()
    }
}