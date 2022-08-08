package com.example.ghostdiary

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.ghostdiary.database.SqliteHelper
import com.example.ghostdiary.dataclass.Day_Diary
import java.time.LocalDate
import java.time.ZoneId
import java.util.*
import kotlin.collections.HashMap

class MainViewModel(): ViewModel() {
    var ishintinvisible: Boolean = false
    var calendar =Calendar.getInstance()

    var maindb:SqliteHelper?=null

    fun getdb(context: Context?):SqliteHelper{
        if(maindb==null){
            maindb=SqliteHelper(context,"db",null,1)
//            maindb!!.createdb()
        }
        return maindb!!

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