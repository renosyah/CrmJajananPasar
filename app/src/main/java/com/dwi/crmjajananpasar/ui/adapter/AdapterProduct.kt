package com.dwi.crmjajananpasar.ui.adapter

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dwi.crmjajananpasar.R
import com.dwi.crmjajananpasar.model.product.Product
import com.dwi.crmjajananpasar.util.Formatter.Companion.decimalFormat
import com.dwi.crmjajananpasar.util.RoundedImage
import com.squareup.picasso.Picasso

class AdapterProduct : RecyclerView.Adapter<AdapterProduct.Holder> {

    lateinit var context: Context
    lateinit var list : ArrayList<Product>
    lateinit var onClick : (Product, Int) -> Unit

    constructor(context: Context, list: ArrayList<Product>, onClick: (Product, Int) -> Unit) : super() {
        this.context = context
        this.list = list
        this.onClick = onClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
       return Holder((context as Activity).layoutInflater.inflate(R.layout.adapter_product,parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = list.get(position)

        holder.name.text = "${item.name}"
        holder.price.text = "${decimalFormat(item.price)}"
        holder.detail.text = "${item.detail}"

        if (item.imageUrl != "") {
            Picasso.get()
                .load("${item.imageUrl}")
                .transform(RoundedImage())
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
        lateinit var detail : TextView
        lateinit var layout : LinearLayout
        constructor(itemView: View) : super(itemView) {
            this.image = itemView.findViewById(R.id.image)
            this.name = itemView.findViewById(R.id.name)
            this.price = itemView.findViewById(R.id.price)
            this.detail = itemView.findViewById(R.id.detail)
            this.layout = itemView.findViewById(R.id.adapter_layout)
        }
    }
}