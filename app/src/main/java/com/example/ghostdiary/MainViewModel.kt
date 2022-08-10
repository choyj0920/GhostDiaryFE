package com.example.ghostdiary

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.ghostdiary.database.SqliteHelper
import com.example.ghostdiary.dataclass.Day_Diary
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.*
import kotlin.collections.HashMap

class MainViewModel(): ViewModel() {
    var ishintinvisible: Boolean = false
    var calendar =Calendar.getInstance()
    var recordcalendar =Calendar.getInstance()

    var maindb:SqliteHelper?=null

    fun getdb(context: Context?):SqliteHelper{
        if(maindb==null){
            maindb=SqliteHelper(context,"db",null,1)
//            maindb!!.createdb()
        }
        return maindb!!

    }

    fun addDiary(newDiary:Day_Diary) {
        var day = newDiary.date
        var transFormat = SimpleDateFormat("yyyy-MM-dd")
        var to = transFormat.format(day)
        getEmotionArray().put(to, newDiary)
        getdb(null).insertDiary(newDiary)
        Log.d("TAG", "addDiary ${newDiary}")
    }
    fun deleteDiary(date:Date){
        var transFormat = SimpleDateFormat("yyyy-MM-dd")
        var to = transFormat.format(date)
        maindb!!.deleteDiary(date)
        calendar_emotionArray!!.remove(to)


    }

    var calendar_emotionArray: HashMap<String, Day_Diary>? = null

    fun getEmotionArray(context: Context?=null): HashMap<String, Day_Diary> {
        if (calendar_emotionArray == null) {
            if(maindb==null) {
                calendar_emotionArray = getdb(context!!)?.selectDiary()
            }
            else
                calendar_emotionArray =maindb!!.selectDiary()
        }
        return calendar_emotionArray!!

    }
}