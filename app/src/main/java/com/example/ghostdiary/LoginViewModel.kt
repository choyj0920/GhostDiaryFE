package com.example.ghostdiary

import androidx.lifecycle.ViewModel
import com.example.ghostdiary.dataclass.Day_Diary
import java.time.LocalDate
import java.time.ZoneId
import java.util.*
import kotlin.collections.HashMap

class LoginViewModel(): ViewModel() {

    class User(var name:String,var email:String,var password:String,var birthday:String){

    }

    var UserList: HashMap<String,User>?=null

    fun get_UserList() : HashMap<String,User>?{
        if (UserList==null){
            UserList= hashMapOf()

            UserList!!.put("admin1@ad.ad",User("admin1","admin1@ad.ad","123456789!","2022 / 07 / 10 "))
            UserList!!.put("admin2@ad.ad",User("admin2","admin2@ad.ad","123456789!","2022 / 07 / 20 "))
            UserList!!.put("admin3@ad.ad",User("admin3","admin3@ad.ad","123456789!","2022 / 07 / 30 "))


        }
        return UserList
    }

}