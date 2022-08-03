package com.example.ghostdiary.dataclass

import com.example.ghostdiary.R
import java.util.*
import kotlin.collections.HashMap

class Day_Diary(
    var date: Date,
    var today_emotion:emotionclass,
    var whom:emotionclass?=null,
    var doing:emotionclass?=null,
    var where:emotionclass?=null,
//    var mood:Int=-1,
    var weather:emotionclass?=null,
    var sleepstart: Date?=null,
    var sleepend : Date? = null,
    var text:String="",
    var image:String?=null



) {
    constructor(date:Date,today: Int,text: String=""):this(date, emotionclass(emotionarr["오늘의 감정"]?.get(today)!!.text,today,true),text=text){

    }

    companion object {

        fun int_to_Image(index:Int):Int{
            return when(index){
                0 -> R.drawable.ghost_verygood
                1 -> R.drawable.ghost_good
                2 -> R.drawable.ghost_normal
                3 -> R.drawable.ghost_bad
                4 -> R.drawable.ghost_verybad
                else -> {R.drawable.rectangle}
            }

        }

        var emotionname= arrayOf("오늘의 감정","누구와","무엇을","어디에서","날씨","수면시간")
        var emotionarr: HashMap<String,Array<emotionclass>> = hashMapOf(
            "오늘의 감정" to arrayOf(emotionclass("매우좋음",0,false),emotionclass("좋음",1,true),
                emotionclass("보통",2,false),emotionclass("나쁨",3,false),
                emotionclass("매우나쁨",4,false)),
            "누구와" to arrayOf(emotionclass("혼자",0,false),emotionclass("가족",1,false),
                emotionclass("친구",2,false),emotionclass("연인",3,false),
                emotionclass("반려동물",4,false)),

            "무엇을" to arrayOf(emotionclass("공부",0,false),emotionclass("일",1,false),
                emotionclass("게임",2,false),emotionclass("운동",3,false),
                emotionclass("독서",4,false)),
            "어디에서" to arrayOf(emotionclass("집",0,false),emotionclass("학교",1,false),
                emotionclass("직장",2,false),emotionclass("카페",3,false),
                emotionclass("식당",4,false)),
            "날씨" to arrayOf(emotionclass("맑음",0,false),emotionclass("비",1,false),
                emotionclass("구름",2,false))
        )

    }
    fun getEmotionarr():MutableList<emotionclass>{
        var arr:MutableList<emotionclass> = mutableListOf()
        arr.add(today_emotion)
        if(whom !=null)
            arr.add(whom!!)
        if(doing !=null)
            arr.add(doing!!)
        if(where !=null)
            arr.add(where!!)
//        if(mood !=-1)
//            arr.add(mood)
        if(weather !=null)
            arr.add(weather!!)
        return arr
    }
    fun getEmotionarr_name():MutableList<String>{
        var arr:MutableList<String> = mutableListOf()
        arr.add(emotionname[0])
        if(whom !=null)
            arr.add(emotionname[1])
        if(doing !=null)
            arr.add(emotionname[2])
        if(where !=null)
            arr.add(emotionname[3])
//        if(mood !=-1)
//            arr.add(emotionname[4])
        if(weather !=null)
            arr.add(emotionname[5])
        return arr
    }
}
