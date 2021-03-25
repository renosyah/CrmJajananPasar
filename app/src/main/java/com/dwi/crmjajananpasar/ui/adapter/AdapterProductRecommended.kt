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

class AdapterProductRecommended : RecyclerView.Adapter<AdapterProductRecommended.Holder> {

    lateinit var context: Context
    lateinit var list : ArrayList<Product>
    lateinit var onClick : (Product, Int) -> Unit

    constructor(context: Context, list: ArrayList<Product>, onClick: (Product, Int) -> Unit) : super() {
        this.context = context
        this.list = list
        this.onClick = onClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
       return Holder((context as Activity).layoutInflater.inflate(R.layout.adapter_product_recomended,parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = list.get(position)

        holder.name.text = "${item.name}\n${decimalFormat(item.price)}"

        if (item.imageUrl != "") {
            Picasso.get()
                .load("${item.imageUrl}")
                .transform(RoundedImage())
                .into(holder.image)
        }

        holder.layout.setOnClickListener{
            onClick.invoke(item,position)
        }

        // jika produk speasial tipe promo maka
        holder.hot_label.visibility = if (item.productType == 1) View.VISIBLE else View.GONE
    }

    class Holder : RecyclerView.ViewHolder {
        lateinit var image : ImageView
        lateinit var name : TextView
        lateinit var layout : LinearLayout
        lateinit var hot_label : ImageView
        constructor(itemView: View) : super(itemView) {
            this.image = itemView.findViewById(R.id.image)
            this.name = itemView.findViewById(R.id.name)
            this.layout = itemView.findViewById(R.id.adapter_layout)
            this.hot_label = itemView.findViewById(R.id.hot_label_imageview)
        }
    }
}