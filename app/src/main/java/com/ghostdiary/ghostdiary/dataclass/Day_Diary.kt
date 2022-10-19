package com.ghostdiary.ghostdiary.dataclass

import android.content.Context
import com.ghostdiary.ghostdiary.R
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
    var sleepstart: Int?=-1,
    var sleepend : Int? = -1,
    var text:String="",
    var image:String?=null



):Cloneable {


    init {

    }



    constructor(date:Date,today: Int=2,text: String=""):this(date, emotionclass(emotionarr[emotionname[0]]?.get(today)!!.text,today,true),text=text){
        whom=arrayListOf<emotionclass>()
        whom.addAll(emotionarr[emotionname[1]]!!)
        doing=arrayListOf<emotionclass>()
        doing.addAll(emotionarr[emotionname[3]]!!)
        where=arrayListOf<emotionclass>()
        where.addAll(emotionarr[emotionname[2]]!!)
        weather=arrayListOf<emotionclass>()
        weather.addAll(emotionarr[emotionname[4]]!!)

    }
    public override fun clone(): Day_Diary{
        return Day_Diary(date,today_emotion.clone(), copyEmotionarr(whom), copyEmotionarr(doing),
            copyEmotionarr(where), copyEmotionarr(weather),sleepstart,sleepend,text,image)
    }

    fun ishavetext(find:String):Boolean{
        if(date.toString().contains(find)){
            return true
        }
        if(today_emotion.text.contains(find)){
            return true
        }
        for(i in getEmotionarrElement()){
            if(i?.text?.contains(find) == true)
                return true

        }
        if(text.contains(find)){
            return true
        }



        return false
    }

    companion object {
        fun copyEmotionarr(arr:ArrayList<emotionclass>):ArrayList<emotionclass>{
            var newarr:ArrayList<emotionclass> = arrayListOf()

            for (i in arr){
                newarr.add(i.clone())

            }
            return newarr

        }


        var int_to_image:ArrayList<Int> = arrayListOf(
            R.drawable.ghost_00_verygood,
            R.drawable.ghost_01_good,
            R.drawable.ghost_02_normal,
            R.drawable.ghost_03_bad,
            R.drawable.ghost_04_verybad,
            R.drawable.ghost_05_alone,
            R.drawable.ghost_06_family,
            R.drawable.ghost_07_friend,
            R.drawable.ghost_08_lover,
            R.drawable.ghost_09_pat,
            R.drawable.ghost_10_home,
            R.drawable.ghost_11_school,
            R.drawable.ghost_12_office,
            R.drawable.ghost_13_cafe,
            R.drawable.ghost_14_restaurant,
            R.drawable.ghost_15_travel,
            R.drawable.ghost_16_health,
            R.drawable.ghost_17_shopping,
            R.drawable.ghost_18_movie,
            R.drawable.ghost_19_library,
            R.drawable.ghost_20_walking,
            R.drawable.ghost_21_study,
            R.drawable.ghost_22_sport,
            R.drawable.ghost_23_work,
            R.drawable.ghost_24_shopping,
            R.drawable.ghost_25_drawing,
            R.drawable.ghost_26_reading,
            R.drawable.ghost_27_drinking,
            R.drawable.ghost_28_game,
            R.drawable.ghost_29_travel,
            R.drawable.ghost_30_sunny,
            R.drawable.ghost_31_cloudy,
            R.drawable.ghost_32_rain,
            R.drawable.ghost_33_snow,
        )


        lateinit var emotionname:Array<String>
        lateinit var emotionarr: HashMap<String,Array<emotionclass>>

        fun init_data(context: Context){
            emotionname=context.resources.getStringArray(R.array.emotionname)

            emotionarr= hashMapOf()

            emotionarr.put(emotionname[0], arrayOf())
            val category0=context.resources.getStringArray(R.array.emotionCategory0)
            val category1=context.resources.getStringArray(R.array.emotionCategory1)
            val category2=context.resources.getStringArray(R.array.emotionCategory2)
            val category3=context.resources.getStringArray(R.array.emotionCategory3)
            val category4=context.resources.getStringArray(R.array.emotionCategory4)


            emotionarr= hashMapOf(
            emotionname[0] to arrayOf(emotionclass(category0[0],0,false),emotionclass(category0[1],1,false),
                emotionclass(category0[2],2,false),emotionclass(category0[3],3,false),
                emotionclass(category0[4],4,false)),

                emotionname[1] to arrayOf(emotionclass(category1[0],5,false),emotionclass(category1[1],6,false),
                    emotionclass(category1[2],7,false),emotionclass(category1[3],8,false),
                    emotionclass(category1[4],9,false)),

                emotionname[2] to arrayOf(emotionclass(category2[0],10,false),emotionclass(category2[1],11,false),
                    emotionclass(category2[2],12,false),emotionclass(category2[3],13,false),
                    emotionclass(category2[4],14,false),emotionclass(category2[5],15,false),emotionclass(category2[6],16,false),
                    emotionclass(category2[7],17,false),emotionclass(category2[8],18,false),emotionclass(category2[9],19,false)),


                emotionname[3] to arrayOf(emotionclass(category3[0],20,false),emotionclass(category3[1],21,false),
                    emotionclass(category3[2],22,false),emotionclass(category3[3],23,false),emotionclass(category3[4],24,false),
                    emotionclass(category3[5],25,false),emotionclass(category3[6],26,false),emotionclass(category3[7],27,false),
                    emotionclass(category3[8],28,false),emotionclass(category3[9],29,false)),

                emotionname[4] to arrayOf(emotionclass(category4[0],30,false),emotionclass(category4[1],31,false),
                    emotionclass(category4[2],32,false),emotionclass(category4[3],33,false))
            )


        }


        fun addghost_arr():ArrayList<Int>{
            var arr=arrayListOf(
                0,1,2,3,4,-1,-1,-1,-1,-1,
                5,6,7,8,9,-1,-1,-1,-1,-1,
                20,21,22,23,24,25,26,27,28,29,
                10,11,12,13,14,15,16,17,18,19
            )
            var temp=(arr.size/4).toInt()
            var result= arrayListOf<Int>()
            for(i in 0..temp-1){
                result.add(arr[10*0+i])
                result.add(arr[10+i])
                result.add(arr[20+i])
                result.add(arr[30+i])
            }
            return result

        }

    }


    fun getEmotionarr():ArrayList<ArrayList<emotionclass>>{
        var arr:ArrayList<ArrayList<emotionclass>> = arrayListOf()
        var today=arrayListOf<emotionclass>()
        for (i in emotionarr[emotionname[0]]!!){
            today.add(i.clone())
        }
        today!![today_emotion.ghostimage].isactive=true
        arr.add(today)
        var _whom=arrayListOf<emotionclass>()
        for(i in whom){
            _whom.add(i.clone())
        }
        arr.add(_whom)
        var _where=arrayListOf<emotionclass>()
        for(i in where){
            _where.add(i.clone())
        }
        arr.add(_where)

        var _doing=arrayListOf<emotionclass>()
        for(i in doing){
            _doing.add(i.clone())
        }
        arr.add(_doing)

        var _weather=arrayListOf<emotionclass>()
        for(i in weather){
            _weather.add(i.clone())
        }
        arr.add(_weather)
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
        if(where.size !=0)
            arr.add(null)
        for( i in weather){
            if(i.isactive)
                arr.add(i)
        }
        if(doing.size!=0)
            arr.add(null)
        for( i in where){
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
