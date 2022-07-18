package com.example.ghostdiary.dataclass

import java.util.*

class Day_Diary(
    var date: Date,
    var today_emotion:Int,
    var whom:Int=-1,
    var doing:Int=-1,
    var where:Int=-1,
    var mood:Int=-1,
    var weather:Int=-1,
    var sleepstart: Date?=null,
    var sleepend : Date? = null,
    var text:String="",
    var image:String?=null



) {
    companion object {
        var emotionname= arrayListOf<String>("하루의 감정","누구와","무엇을","어디에서","분위기","날씨")
    }
    fun getEmotionarr():MutableList<Int>{
        var arr:MutableList<Int> = mutableListOf()
        arr.add(today_emotion)
        if(whom !=-1)
            arr.add(whom)
        if(doing !=-1)
            arr.add(doing)
        if(where !=-1)
            arr.add(where)
        if(mood !=-1)
            arr.add(mood)
        if(weather !=-1)
            arr.add(weather)
        return arr
    }
    fun getEmotionarr_name():MutableList<String>{
        var arr:MutableList<String> = mutableListOf()
        arr.add(emotionname[0])
        if(whom !=-1)
            arr.add(emotionname[1])
        if(doing !=-1)
            arr.add(emotionname[2])
        if(where !=-1)
            arr.add(emotionname[3])
        if(mood !=-1)
            arr.add(emotionname[4])
        if(weather !=-1)
            arr.add(emotionname[5])
        return arr
    }
}