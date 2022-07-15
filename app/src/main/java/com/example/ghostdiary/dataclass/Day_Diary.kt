package com.example.ghostdiary.dataclass

import java.util.*

class Day_Diary(
    var date: Date,
    var today_emotion:Int,
    var whom:Int=-1,
    var doing:Int=-1,
    var where:Int=-1,
    var mood:Int=-1,
    var sleepstart: Date?=null,
    var sleepend : Date? = null,
    var text:String="",
    var image:String?=null

) {

}