package com.dwi.crmjajananpasar.ui.adapter

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.dwi.crmjajananpasar.R
import com.dwi.crmjajananpasar.model.product.Product
import com.dwi.crmjajananpasar.util.Formatter.Companion.decimalFormat
import com.squareup.picasso.Picasso

class AdapterBanner : RecyclerView.Adapter<AdapterBanner.Holder> {

    lateinit var context: Context
    lateinit var list : ArrayList<Product>
    lateinit var onClick : (Product, Int) -> Unit

    constructor(context: Context, list: ArrayList<Product>, onClick: (Product, Int) -> Unit) : super() {
        this.context = context
        this.list = list
        this.onClick = onClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
       return Holder((context as Activity).layoutInflater.inflate(R.layout.adapter_banner,parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = list.get(position)

        holder.name.text = "${item.name}"
        holder.price.text = " ${decimalFormat(item.price)}"

        if (item.imageUrl != "") {
            Picasso.get()
                .load("${item.imageUrl}")
                .into(holder.image)
        }

        holder.layout.setOnClickListener{
            onClick.invoke(item,position)
        }
    }

    class Holder : RecyclerView.ViewHolder {
        lateinit var image : ImageView
        lateinit var name : TextView
        lateinit var price : TextView
        lateinit var layout : CardView
        constructor(itemView: View) : super(itemView) {
            this.image = itemView.findViewById(R.id.image_banner_imageview)
            this.name = itemView.findViewById(R.id.name_banner_textview)
            this.price = itemView.findViewById(R.id.price_banner_textview)
            this.layout = itemView.findViewById(R.id.adapter_layout)
        }
    }
}