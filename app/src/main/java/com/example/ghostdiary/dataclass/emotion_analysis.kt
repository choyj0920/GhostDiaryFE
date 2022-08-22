package com.example.ghostdiary.dataclass

import java.lang.Math.round

data class emotion_analysis(var text:String,var ghostnum:Int,var emotioncount: Array<Int> = arrayOf(0,0,0,0,0)){

    var myscore:Int=-1

    var mypercentage:ArrayList<Int>?=null

    fun init(){
        myscore=getmyscore()
        mypercentage=getmypercentage()
    }


    companion object{
        var allemotion:Array<Int> = arrayOf(0,0,0,0,0)

        fun getallpercentage(): ArrayList<Int> {
            return getpercentage(allemotion)
        }fun getallscore():Int{
            return getScore(allemotion)
        }

        fun getpercentage(arr :Array<Int>): ArrayList<Int> {
            var allsum=arr.sum()
            var result:ArrayList<Int> = arrayListOf()
            for(i in arr){
                result.add(i*100/allsum)
            }
            return result
        }
        fun getScore(arr:Array<Int>): Int {
            var allsum=arr.sum()
            var temp:Int=0
            for(i in 0..arr.size-1){
                temp +=(100- i*20)*arr[i]

            }

            var score= temp / allsum.toFloat()

            return round(score)
        }
    }
    fun getmypercentage(): ArrayList<Int> {
        return getpercentage(this.emotioncount)
    }fun getmyscore(): Int {
        return getScore(this.emotioncount)
    }

}
