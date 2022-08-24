package com.example.ghostdiary.dataclass

import java.util.*

class Sleep_data(var date: Date, var sleepstart :Int, var sleepend:Int, var sleeptime:Int) {
    companion object{
        var avgsleepstart :Float?= null
        var avgsleepend :Float?= null
        var avgsleeptime :Float?= null
    }

}