package com.khumam.dicodingsubmissiontwo.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.khumam.dicodingsubmissiontwo.receiver.AlarmReceiver
import com.khumam.dicodingsubmissiontwo.R
import com.khumam.dicodingsubmissiontwo.fragment.TimeFragment
import com.khumam.dicodingsubmissiontwo.databinding.ActivitySettingBinding
import java.text.SimpleDateFormat
import java.util.*

class SettingActivity : AppCompatActivity(), View.OnClickListener, TimeFragment.DialogTimeListener {

    private lateinit var alarmReceiver: AlarmReceiver
    private var binding: ActivitySettingBinding? = null

    companion object {
        private const val TIME_TAG = "TimeTag"
        private val TAG = SettingActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.btnSetAlarm?.setOnClickListener(this)
        binding?.btnGetTime?.setOnClickListener(this)

        alarmReceiver = AlarmReceiver()
    }

    private fun setAlarm(time: String, message: String?) {
       alarmReceiver.setRepeatingAlarm(this, AlarmReceiver.REPEATING_ALARM, time, message)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btn_get_time -> {
                val timeFragment = TimeFragment()
                timeFragment.show(supportFragmentManager, TIME_TAG)

            }
            R.id.btn_set_alarm -> {
                val timeValue = binding?.showTime?.text.toString()
                alarmReceiver.setRepeatingAlarm(this, AlarmReceiver.ALARM_TYPE,
                        timeValue, "You gonna back!")
            }
        }
    }

    override fun onDialogTimeSet(tag: String?, hourOfDay: Int, minute: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)

        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        when (tag) {
            TIME_TAG -> binding?.showTime?.text = dateFormat.format(calendar.time)
            else -> {
            }
        }
    }

}