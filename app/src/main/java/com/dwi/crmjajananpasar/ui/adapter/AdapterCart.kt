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
import com.dwi.crmjajananpasar.model.cart.Cart
import com.dwi.crmjajananpasar.model.product.Product
import com.dwi.crmjajananpasar.util.Formatter.Companion.decimalFormat
import com.squareup.picasso.Picasso

class AdapterCart : RecyclerView.Adapter<AdapterCart.Holder> {

    companion object {
        val QTY_ADD = 1
        val QTY_REMOVE = -1
    }

    lateinit var context: Context
    lateinit var list : ArrayList<Cart>
    lateinit var onQtyClick : (Cart, Int, Int) -> Unit

    constructor(context: Context, list: ArrayList<Cart>, onQtyClick : (Cart,Int, Int) -> Unit ) : super() {
        this.context = context
        this.list = list
        this.onQtyClick = onQtyClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
       return Holder((context as Activity).layoutInflater.inflate(R.layout.adapter_cart,parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = list.get(position)

        holder.name.text = "${item.product.name}"
        holder.price.text = " ${decimalFormat(item.price)}"
        holder.qty.text = "${item.quantity}X"

        holder.add.visibility = if (item.product.productType == 1) View.INVISIBLE  else View.VISIBLE
        holder.add.isEnabled = item.product.productType == 0
        holder.add.setOnClickListener {
            item.quantity++
            item.subTotal = item.quantity*item.price
            onQtyClick.invoke(item, QTY_ADD, position)
        }

        holder.min.visibility = if (item.product.productType == 1) View.INVISIBLE  else View.VISIBLE
        holder.min.isEnabled = item.product.productType == 0
        holder.min.setOnClickListener {
            if (item.quantity == 1) return@setOnClickListener
            item.quantity--
            item.subTotal = item.quantity*item.price
            onQtyClick.invoke(item, QTY_REMOVE, position)
        }
    }

    class Holder : RecyclerView.ViewHolder {
        lateinit var name : TextView
        lateinit var add : ImageView
        lateinit var qty : TextView
        lateinit var min : ImageView
        lateinit var price : TextView
        constructor(itemView: View) : super(itemView) {
            this.name = itemView.findViewById(R.id.name)
            this.price = itemView.findViewById(R.id.price)
            this.qty = itemView.findViewById(R.id.qty)
            this.add = itemView.findViewById(R.id.add_qty)
            this.min = itemView.findViewById(R.id.min_qty)
        }
    }
}