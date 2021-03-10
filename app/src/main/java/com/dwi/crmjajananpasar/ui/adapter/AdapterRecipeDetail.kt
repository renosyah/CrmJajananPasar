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
import com.dwi.crmjajananpasar.model.recipe_detail.RecipeDetail
import com.dwi.crmjajananpasar.util.Formatter.Companion.decimalFormat
import com.squareup.picasso.Picasso

class AdapterRecipeDetail : RecyclerView.Adapter<AdapterRecipeDetail.Holder> {

    lateinit var context: Context
    lateinit var list : ArrayList<RecipeDetail>
    lateinit var onClick : (RecipeDetail, Int) -> Unit

    constructor(context: Context, list: ArrayList<RecipeDetail>, onClick: (RecipeDetail, Int) -> Unit) : super() {
        this.context = context
        this.list = list
        this.onClick = onClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
       return Holder((context as Activity).layoutInflater.inflate(R.layout.adapter_recipe_detail,parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = list.get(position)
        holder.text.text = "- ${item.text}"
    }

    class Holder : RecyclerView.ViewHolder {
        lateinit var text  : TextView
        constructor(itemView: View) : super(itemView) {
            this.text = itemView.findViewById(R.id.text)
        }
    }
}