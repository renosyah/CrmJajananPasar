package com.dwi.crmjajananpasar.ui.util

import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.dwi.crmjajananpasar.R

class EmptyLayout {
    private lateinit var c: Context
    private lateinit  var includeParent: View
    private lateinit var image : ImageView
    private lateinit var title: TextView
    private lateinit var message: TextView

   constructor(c: Context, includeParent: View) {
        this.c = c
        this.includeParent = includeParent
        this.message = this.includeParent.findViewById(R.id.empty_message_content_text)
        this.title = this.includeParent.findViewById(R.id.empty_message_title_text)
        this.image = this.includeParent.findViewById(R.id.empty_image)
        show()
    }

    fun setMessageAndIcon(t: String,m: String,icn : Int) {
        this.message.text = m
        this.title.text = t
        this.image.setImageDrawable(ContextCompat.getDrawable(c,icn))
    }

    fun setVisibility(v: Boolean) {
        includeParent.visibility = (if (v) View.VISIBLE else View.GONE)
    }

    fun show() {
        includeParent.visibility = (View.VISIBLE)
    }

    fun hide() {
        includeParent.visibility = (View.GONE)
    }
}