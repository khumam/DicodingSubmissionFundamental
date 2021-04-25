package com.khumam.dicodingsubmissiontwo.receiver

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.khumam.dicodingsubmissiontwo.R
import com.khumam.dicodingsubmissiontwo.activity.SettingActivity
import java.util.*
import java.text.ParseException
import java.text.SimpleDateFormat

class AlarmReceiver : BroadcastReceiver() {

    companion object {
        const val REMINDER_TIME = "reminderTime"
        const val NOTIFICATION_ID = 101
        const val ALARM_MESSAGE = "message"
        const val ALARM_TYPE = "repeat"
        const val REPEATING_ALARM = "RepeatingAlarm"
        private const val TIME_FORMAT = "HH:mm"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val hoursAlarm =  intent.getStringExtra(REMINDER_TIME)
        val notificationId = NOTIFICATION_ID

        showNotification(context, context.getResources().getString(R.string.alarm_on), "", notificationId)
    }

    private fun showToast(context: Context, title: String, message: String?) {
        Toast.makeText(context, "$title, $message", Toast.LENGTH_LONG).show()
    }

    private fun showNotification(context: Context, title: String, message: String?, notificationId: Int) {
        val channelId = "Channel_1"
        val channelName = "AlarmManager channel"

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val soundAlarm = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_alarm_check)
            .setContentTitle(title)
            .setContentText(message)
            .setColor(ContextCompat.getColor(context, android.R.color.transparent))
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setSound(soundAlarm)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelNotification = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            channelNotification.enableVibration(true)
            channelNotification.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)

            builder.setChannelId(channelId)

            notificationManager.createNotificationChannel(channelNotification)
        }

        val notification = builder.build()
        notificationManager.notify(notificationId, notification)
    }

    fun setRepeatingAlarm(context: Context, type: String, time: String, message: String?) {

        if (isDateInvalid(time, TIME_FORMAT)) return

        val alarmManager =  context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intentAlarm = Intent(context, AlarmReceiver::class.java)

        intentAlarm.putExtra(ALARM_MESSAGE, message)

        val putExtra = intentAlarm.putExtra(ALARM_TYPE, type)

        val timeArrays = time.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        val calendarAlarm = Calendar.getInstance()
        calendarAlarm.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArrays[0]))
        calendarAlarm.set(Calendar.MINUTE, Integer.parseInt(timeArrays[1]))
        calendarAlarm.set(Calendar.SECOND, 0)

        val pendingIntent = PendingIntent.getBroadcast(context, NOTIFICATION_ID, intentAlarm, 0)
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendarAlarm.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)

        showToast(context, context.getResources().getString(R.string.alarm_setup), "")
    }

    private fun isDateInvalid(date: String, format: String): Boolean {
        return try {
            val df = SimpleDateFormat(format, Locale.getDefault())
            df.isLenient = false
            df.parse(date)
            false
        } catch (e: ParseException) {
            true
        }
    }

    fun isAlarmSet(context: Context, type: String): Boolean {
        val intent = Intent(context, AlarmReceiver::class.java)
        val requestCode = NOTIFICATION_ID

        return PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_NO_CREATE) != null
    }


}