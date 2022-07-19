package com.example.ghostdiary.fragment.main

import androidx.lifecycle.ViewModel
import com.example.ghostdiary.databinding.FragmentCalendarBinding
import com.example.ghostdiary.dataclass.Day_Diary
import java.time.LocalDate
import java.time.ZoneId
import java.util.*
import kotlin.collections.HashMap

class CalendarViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    var ishintinvisible:Boolean=false

    var calendar_emotionArray: HashMap<String, Day_Diary>?=null

    fun getEmotionArray(): HashMap<String,Day_Diary>{
        if(calendar_emotionArray==null){
            calendar_emotionArray= hashMapOf()
            var instant = LocalDate.of(2022,7,10).atStartOfDay(ZoneId.systemDefault()).toInstant()
            var date = Date.from(instant);

            calendar_emotionArray!!.put("20220710" ,Day_Diary(date,2))
            date= Date.from(LocalDate.of(2022,7,14).atStartOfDay(ZoneId.systemDefault()).toInstant())
            calendar_emotionArray!!.put("20220714" , Day_Diary(date,1))
            date= Date.from(LocalDate.of(2022,7,15).atStartOfDay(ZoneId.systemDefault()).toInstant())
            calendar_emotionArray!!.put("20220715" ,Day_Diary(date,0, text = "배고프다."))
            date= Date.from(LocalDate.of(2022,7,9).atStartOfDay(ZoneId.systemDefault()).toInstant())
            calendar_emotionArray!!.put("20220709" ,Day_Diary(date,3))

        }
        return calendar_emotionArray!!

    }










}