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
import com.dwi.crmjajananpasar.model.payment.Payment
import com.dwi.crmjajananpasar.model.product.Product
import com.dwi.crmjajananpasar.util.Formatter.Companion.decimalFormat
import com.squareup.picasso.Picasso

class AdapterPayment : RecyclerView.Adapter<AdapterPayment.Holder> {

    lateinit var context: Context
    lateinit var list : ArrayList<Payment>
    lateinit var onClick : (Payment, Int) -> Unit

    constructor(context: Context, list: ArrayList<Payment>, onClick: (Payment, Int) -> Unit) : super() {
        this.context = context
        this.list = list
        this.onClick = onClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
       return Holder((context as Activity).layoutInflater.inflate(R.layout.adapter_payment,parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = list.get(position)

        holder.name.text = item.name
        holder.number.text = item.detail

        holder.copy.setOnClickListener{
            onClick.invoke(item,position)
        }
    }

    class Holder : RecyclerView.ViewHolder {
        lateinit var copy : ImageView
        lateinit var name : TextView
        lateinit var number : TextView

        constructor(itemView: View) : super(itemView) {
            this.copy = itemView.findViewById(R.id.copy)
            this.name = itemView.findViewById(R.id.name)
            this.number = itemView.findViewById(R.id.number)
        }
    }
}