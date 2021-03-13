package com.dwi.crmjajananpasar.ui.dialog

import android.app.Activity
import android.content.Context
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.dwi.crmjajananpasar.R

class LoadingDialog {
    private lateinit var dialog: AlertDialog
    private lateinit var message: TextView

    constructor(c: Context)  {
        val v = (c as Activity).layoutInflater.inflate(R.layout.loading_data_dialog,null)
        dialog = AlertDialog.Builder(c,R.style.DialogTheme).create()
        message = v.findViewById(R.id.loading_message)

        dialog.setView(v)
        dialog.setCancelable(false)
    }

    fun setMessage(m: String) {
        message.text = m
    }

    fun setVisibility(v: Boolean) {
        if (v) dialog.show() else dialog.dismiss()
    }
}