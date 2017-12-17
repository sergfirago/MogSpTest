package com.firago.serg.mogsptest.ui.view

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import com.firago.serg.mogsptest.R

class NewsDialog: DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity.layoutInflater
        builder.setView(inflater.inflate(R.layout.news_dialog, null))
                .setPositiveButton("Ok",{_,_->})

        return builder.create()
    }
}