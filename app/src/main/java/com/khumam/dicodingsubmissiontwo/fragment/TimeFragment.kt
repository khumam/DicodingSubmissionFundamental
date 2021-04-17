package com.khumam.dicodingsubmissiontwo.fragment

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import java.util.*

class TimeFragment: DialogFragment(), TimePickerDialog.OnTimeSetListener {
    private var mListener: DialogTimeListener? = null

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        mListener?.onDialogTimeSet(tag, hourOfDay, minute)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = context as DialogTimeListener?
    }

    override fun onDetach() {
        super.onDetach()
        if (mListener != null) {
            mListener = null
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendarAlarm = Calendar.getInstance()
        val hour = calendarAlarm.get(Calendar.HOUR_OF_DAY)
        val minute = calendarAlarm.get(Calendar.MINUTE)
        val is24Hour = true

        return TimePickerDialog(activity, this, hour, minute, is24Hour)
    }

    interface DialogTimeListener {
        fun onDialogTimeSet(tag: String?, hourOfDay: Int, minute: Int)
    }

}