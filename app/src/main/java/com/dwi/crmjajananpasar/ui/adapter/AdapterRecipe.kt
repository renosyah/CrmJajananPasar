package com.dwi.crmjajananpasar.ui.adapter

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dwi.crmjajananpasar.R
import com.dwi.crmjajananpasar.model.BaseModel
import com.dwi.crmjajananpasar.model.recipe.Recipe

class AdapterRecipe : RecyclerView.Adapter<AdapterRecipe.Holder> {

    lateinit var context: Context
    var list : ArrayList<TwoProduct>? = null
    lateinit var onClick : (Recipe) -> Unit

    constructor(context: Context, onClick: (Recipe) -> Unit) : super() {
        this.context = context
        this.onClick = onClick
    }

    fun setItems(list : ArrayList<TwoProduct>){
        this.list = list
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
       return Holder((context as Activity).layoutInflater.inflate(R.layout.adapter_recipe,parent,false))
    }

    override fun getItemCount(): Int {
        if (list == null){
            return 0
        }
        return list!!.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

        if (list == null){
            return
        }

        val item = list!!.get(position)

        if (item.l != null){
            holder.left.text = item.l!!.product.name
            holder.left.setOnClickListener {
                onClick.invoke(item.l!!)
            }
        }
        if (item.r != null){
            holder.right.text = item.r!!.product.name
            holder.right.setOnClickListener {
                onClick.invoke(item.r!!)
            }
        }

        holder.left_layout.visibility = if (item.l != null) View.VISIBLE else View.GONE
        holder.right_layout.visibility = if (item.r != null) View.VISIBLE else View.GONE
    }

    class Holder : RecyclerView.ViewHolder {
        lateinit var left : TextView
        lateinit var right : TextView
        lateinit var left_layout : FrameLayout
        lateinit var right_layout : FrameLayout

        constructor(itemView: View) : super(itemView) {
            this.left = itemView.findViewById(R.id.name_left_product_textview)
            this.right = itemView.findViewById(R.id.name_right_product_textview)
            this.left_layout = itemView.findViewById(R.id.left_layout)
            this.right_layout = itemView.findViewById(R.id.right_layout)
        }
    }

    class TwoProduct(var l : Recipe? = null, var r : Recipe? = null) : BaseModel

    companion object {

        fun toTwoProducts(p : ArrayList<Recipe>) : ArrayList<TwoProduct> {
            val l = ArrayList<TwoProduct>()
            for (i in 0 until p.size step 2){
                l.add(TwoProduct(p[i],if (i + 1 > p.size-1) null else p[i + 1] ))
            }
            return l
        }

    }
}