package com.cz.czadmin

data class Adproductmodel(
    val productName:String?="",
    val productDescription:String?="",
    val productCoverImage:String?="",
    val productCatagory:String?="",
    val productsubCatagory:String?="",
    val productId:String?="",
    val productMrp:String?="",
    val productSp:String?="",
    val productImage:ArrayList<String>
)
