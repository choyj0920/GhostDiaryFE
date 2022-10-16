package com.ghostdiary.ghostdiary

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import java.text.SimpleDateFormat
import java.util.*


class AlarmReceiver: BroadcastReceiver() {

    companion object{
        private val CHANNEL_ID = "channel1"
        private val CHANEL_NAME = "Channel1"
    }
    override fun onReceive(p0: Context?, p1: Intent?) {
        if(p1?.extras?.get("code") == MainActivity.M_ALARM_REQUEST_CODE) {
            var count = p1.getIntExtra("count", 0)
            var time = Calendar.getInstance()
            var formatDate = SimpleDateFormat("HH:mm")

            var prefs = p0!!.getSharedPreferences("Prefs", Context.MODE_PRIVATE)
            var strdate = prefs.getString("alarmtime","21:30")
            var date = formatDate.parse(strdate)

            time.time=date
            Log.d("myLog", "$count")

            var notificationHelper = NotificationHelper(p0)

            val intent = Intent(p0, MainActivity::class.java)
            val openpendingIntent = PendingIntent.getActivity(
                p0,
                0,
                intent,
                PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )


            val nb: NotificationCompat.Builder = notificationHelper.getChannelNotification("GhostDiary", p0.resources.getString(R.string.alarm_text),openpendingIntent).setAutoCancel(true)

            notificationHelper.getManager().notify(1, nb.build())


            val alarmManager = p0?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val pendingIntent = Intent(p0, AlarmReceiver::class.java).let {
                it.putExtra("code", MainActivity.REQUEST_CODE)
                it.putExtra("count", 32)
                PendingIntent.getBroadcast(p0, MainActivity.REQUEST_CODE, it,
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) PendingIntent.FLAG_MUTABLE else 0)
            }

            // Case 3: 오전 8시 27분 Alarm 생성 (Interval: Day)
            val calendar = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
                set(Calendar.HOUR_OF_DAY, time.get(Calendar.HOUR_OF_DAY))
                set(Calendar.MINUTE, time.get(Calendar.MINUTE))
                set(Calendar.SECOND,0)
            }

            calendar.add(Calendar.DATE,1)

            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )

        }
        else{
            Log.d("TAG","알람을하긴하나..")
        }



    }



}