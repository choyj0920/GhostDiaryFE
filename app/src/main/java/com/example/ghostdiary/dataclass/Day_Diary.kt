package com.example.ghostdiary.dataclass

import java.util.*
import kotlin.collections.HashMap

class Day_Diary(
    var date: Date,
    var today_emotion:Int,
    var whom:Int=-1,
    var doing:Int=-1,
    var where:Int=-1,
//    var mood:Int=-1,
    var weather:Int=-1,
    var sleepstart: Date?=null,
    var sleepend : Date? = null,
    var text:String="",
    var image:String?=null



) {
    companion object {
        var emotionname= arrayOf("하루의 감정","누구와","무엇을","어디에서","날씨","수면시간")
        var selecttexts: HashMap<String,Array<String>> = hashMapOf(
            "하루의 감정" to arrayOf("매우좋음","좋음","보통","나쁨","매우나쁨"),
            "누구와" to arrayOf("혼자","가족","친구","연인","반려동물"),
            "무엇을" to arrayOf("공부","일","게임","운동","독서"),
            "어디에서" to arrayOf("집","학교","직장","카페","식당"),
            "날씨" to  arrayOf("밝음","구름","비")
        )

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
//        if(mood !=-1)
//            arr.add(mood)
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
//        if(mood !=-1)
//            arr.add(emotionname[4])
        if(weather !=-1)
            arr.add(emotionname[5])
        return arr
    }
}