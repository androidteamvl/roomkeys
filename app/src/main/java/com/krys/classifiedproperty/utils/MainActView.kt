package com.krys.classifiedproperty.activity

import com.krys.classifiedproperty.model.Postlist
import java.util.ArrayList

interface MainActView {

    fun updateData(products_arrayList: ArrayList<Postlist>)
}