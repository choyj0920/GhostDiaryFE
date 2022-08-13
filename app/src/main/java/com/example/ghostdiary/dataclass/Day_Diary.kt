package com.example.ghostdiary.dataclass

import com.example.ghostdiary.R
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class Day_Diary(
    var date: Date,
    var today_emotion:emotionclass,
    var whom:ArrayList<emotionclass> = arrayListOf(),
    var doing:ArrayList<emotionclass> =arrayListOf(),
    var where:ArrayList<emotionclass> = arrayListOf(),
    var weather:ArrayList<emotionclass> = arrayListOf(),
    var sleepstart: Date?=null,
    var sleepend : Date? = null,
    var text:String="",
    var image:String?=null



) {
    init {

    }

    constructor(date:Date,today: Int=2,text: String=""):this(date, emotionclass(emotionarr["오늘의 감정"]?.get(today)!!.text,today,true),text=text){
        whom=arrayListOf<emotionclass>()
        whom.addAll(emotionarr["누구와"]!!)
        doing=arrayListOf<emotionclass>()
        doing.addAll(emotionarr["무엇을"]!!)
        where=arrayListOf<emotionclass>()
        where.addAll(emotionarr["어디에서"]!!)
        weather=arrayListOf<emotionclass>()
        weather.addAll(emotionarr["날씨"]!!)

    }


    companion object {

        var int_to_image:ArrayList<Int> = arrayListOf(
            R.drawable.ghost_verygood,
            R.drawable.ghost_good,
            R.drawable.ghost_normal,
            R.drawable.ghost_bad,
            R.drawable.ghost_verybad
        )

        val emotionname= arrayOf("오늘의 감정","누구와","무엇을","어디에서","날씨","수면시간")
        val emotionarr: HashMap<String,Array<emotionclass>> = hashMapOf(
            "오늘의 감정" to arrayOf(emotionclass("매우좋음",0,false),emotionclass("좋음",1,false),
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
                emotionclass("식당",4,false),emotionclass("헬스장",4,false),emotionclass("여행지",4,false)),
            "날씨" to arrayOf(emotionclass("맑음",0,false),emotionclass("비",1,false),
                emotionclass("구름",2,false))
        )

    }


    fun getEmotionarr():ArrayList<ArrayList<emotionclass>>{
        var arr:ArrayList<ArrayList<emotionclass>> = arrayListOf()
        var today=arrayListOf<emotionclass>()
        for (i in emotionarr["오늘의 감정"]!!){
            today.add(i.clone())
        }
        today!![today_emotion.ghostimage].isactive=true
        arr.add(today)
        arr.add(whom)
        arr.add(doing)
        arr.add(where)
        arr.add(weather)
        return arr
    }
    fun getEmotionarrElement():ArrayList<emotionclass?>{
        var arr:ArrayList<emotionclass?> = arrayListOf()
        arr.add(today_emotion)
        arr.add(null)
        for( i in whom){
            if(i.isactive)
                arr.add(i)
        }
        if(whom.size!=0)
            arr.add(null)
        for( i in doing){
            if(i.isactive)
                arr.add(i)
        }
        if(doing.size!=0)
            arr.add(null)
        for( i in where){
            if(i.isactive)
                arr.add(i)
        }
        if(where.size !=0)
            arr.add(null)
        for( i in weather){
            if(i.isactive)
                arr.add(i)
        }
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
