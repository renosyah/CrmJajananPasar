package com.dwi.crmjajananpasar.ui.dialog

import android.app.Activity
import android.content.Context
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.dwi.crmjajananpasar.R

class ErrorDialog {
    private lateinit var dialog: AlertDialog
    private lateinit var message: TextView
    private lateinit var ok :Button

    constructor(c: Context,  onclick : ()->Unit) {
        val v = (c as Activity).layoutInflater.inflate(R.layout.error_message_dialog,null)
        dialog = AlertDialog.Builder(c,R.style.DialogTheme).create()

        message = v.findViewById(R.id.error_message_text)
        ok = v.findViewById(R.id.button_try_again)
        ok.setOnClickListener {
            dialog.dismiss()
            onclick.invoke()
        }

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