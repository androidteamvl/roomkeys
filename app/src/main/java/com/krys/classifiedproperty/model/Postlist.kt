package com.krys.classifiedproperty.model

data class Postlist(
    val post_id: String,
    val category_id: String,
    val subcategory_id: String,
    val child_category_id: String,
    val add_title: String,
    val description: String,
    val contact_mobile: String,
    val contact_name: String,
    val corresspondance_add: String,
    val landmark: String,
    val latitude: String,
    val longitude: String,
    val amount: String,
    val images: String,
    val wishliststatus: String
)