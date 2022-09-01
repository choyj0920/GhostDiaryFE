package com.example.ghostdiary

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.ghostdiary.database.SqliteHelper
import com.example.ghostdiary.dataclass.*
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

    fun addMemo(folder_id:Int ,title: String,text:String){
        maindb!!.insert_Memo(folder_id,title,text, date = Calendar.getInstance().time)

    }
    fun editmemo(memo: Memo){
        var memo_id=maindb!!.insert_Memo(memo.folder_id,memo.title,memo.text, date = Calendar.getInstance().time,memo.memoid)
        if(memo.memoid ==-1){
            memo.memoid=memo_id
            for (folder in memofolder_array!!){
                if(folder.folder_id==memo.folder_id)
                    folder.arrMemo.add(memo)
            }
        }
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
    var diaryAnalysisMap: HashMap<String, emotion_analysis>? = null
    var memofolder_array: ArrayList<Memo_Folder>? = null
    var sleepdata_array: ArrayList<Sleep_data>? = null

    fun getsleepdataArray(context: Context?=null): ArrayList<Sleep_data> {
        if(maindb==null) {
            sleepdata_array = getdb(MainActivity.mainactivity)?.select_sleepdata()
        }
        else
            sleepdata_array =maindb!!.select_sleepdata()

        return sleepdata_array!!
    }
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
    fun getdiaryAnalysisMap(context: Context?=null): HashMap<String, emotion_analysis> {
        if(maindb==null) {
            diaryAnalysisMap = getdb(context!!)?.select_diaryanalysis()
        }
        else
            diaryAnalysisMap =maindb!!.select_diaryanalysis()
        for((key,value) in diaryAnalysisMap!!){
            value.init()
        }

        return diaryAnalysisMap!!
    }
}