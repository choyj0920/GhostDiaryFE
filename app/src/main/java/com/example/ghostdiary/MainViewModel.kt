package com.example.ghostdiary

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.ghostdiary.database.SqliteHelper
import com.example.ghostdiary.dataclass.Day_Diary
import com.example.ghostdiary.dataclass.Memo_Folder
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.*
import kotlin.collections.ArrayList
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
    var memofolder_array: ArrayList<Memo_Folder>? = null

    fun getMemo_FolderArray(context: Context?=null): ArrayList<Memo_Folder> {
        if (memofolder_array == null) {
            if(maindb==null) {
                memofolder_array = getdb(context!!)?.select_Memo()
            }
            else
                memofolder_array =maindb!!.select_Memo()
        }
        return memofolder_array!!
    }fun update_memo(context: Context?=null):ArrayList<Memo_Folder> {
        if (maindb == null) {
            memofolder_array = getdb(context!!)?.select_Memo()
        } else
            memofolder_array = maindb!!.select_Memo()
        return memofolder_array!!
    }

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