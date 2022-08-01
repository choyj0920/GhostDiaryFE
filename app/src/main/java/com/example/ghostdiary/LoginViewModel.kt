package com.example.ghostdiary

import androidx.lifecycle.ViewModel
import com.example.ghostdiary.dataclass.Day_Diary
import com.example.ghostdiary.fragment.login.FindEmailFragment
import com.example.ghostdiary.fragment.login.FindPasswordFragment
import java.time.LocalDate
import java.time.ZoneId
import java.util.*
import kotlin.collections.HashMap

class LoginViewModel(): ViewModel() {


    fun checkemail(email:String,password: String):Int{ // 회원 체크 0리턴 이메일도없음. 1리턴 로그인성공, 2리턴 비밀번호 오류
        if(get_UserList().contains(email)){
            if(get_UserList().get(email)!!.password==password) {
                return 1
            }
            else{
                return 2
            }
        }
        return 0

    }

    class User(var name:String,var email:String,var password:String,var birthday:String,var registerday:String="2022 / 07 / 29"){
        override fun toString(): String {
            return "name:${name},email:${email},password:${password},birthday:${birthday}"

        }

    }
    var UserList: HashMap<String,User>?=null

    fun get_UserList() : HashMap<String,User>{
        if (UserList==null){
            UserList= hashMapOf()

            UserList!!.put("ad",User("admin1","ad","ad","2022 / 07 / 10 "))
            UserList!!.put("admin1@ad.ad",User("admin1","admin1@ad.ad","123456789!","2022 / 07 / 10"))
            UserList!!.put("admin2@ad.ad",User("admin2","admin2@ad.ad","123456789!","2022 / 07 / 20"))
            UserList!!.put("admin3@ad.ad",User("admin3","admin3@ad.ad","123456789!","2022 / 07 / 30"))
        }
        else{

        }
        return UserList!!
    }

}