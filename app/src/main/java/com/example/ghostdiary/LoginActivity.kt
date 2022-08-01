package com.example.ghostdiary

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.example.ghostdiary.databinding.ActivityLoginBinding
import com.example.ghostdiary.fragment.login.LoginEmailFragment
import com.example.ghostdiary.fragment.login.LoginFindFragment
import com.example.ghostdiary.fragment.login.LoginMainFragment
import com.example.ghostdiary.fragment.login.LoginRegistFragment
import com.example.ghostdiary.fragment.main.CalendarFragment
import com.example.ghostdiary.fragment.main.DefaultFragment


class LoginActivity : AppCompatActivity() {
    private lateinit var binding:ActivityLoginBinding
    lateinit var viewModel:LoginViewModel

    lateinit var loginMainFragment: LoginMainFragment
    lateinit var loginEmailFragment: LoginEmailFragment
    lateinit var loginRegistFragment: LoginRegistFragment
    lateinit var loginFindFragment: LoginFindFragment

    companion object{
        lateinit var loginActivity:LoginActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loginActivity=this
        binding=ActivityLoginBinding.inflate(layoutInflater)

        setContentView(binding.root)

        viewModel= ViewModelProvider(this).get(LoginViewModel::class.java)
        loginMainFragment= LoginMainFragment()
        loginEmailFragment= LoginEmailFragment()
        loginRegistFragment=LoginRegistFragment()
        loginFindFragment= LoginFindFragment()

        supportFragmentManager.beginTransaction().replace(binding.container.id,loginMainFragment).commit()

        val prefs : SharedPreferences = this.getSharedPreferences("Prefs", Context.MODE_PRIVATE)
        val auto_email= prefs.getString("auto_email","")
        val auto_password=prefs.getString("auto_password","")
        if(viewModel.checkemail(auto_email!!,auto_password!!) ==1){
            var intent = Intent(this, MainActivity::class.java)
            intent.putExtra("uid",auto_email)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }

    }




    fun change_login_email(){
        supportFragmentManager.beginTransaction().replace(binding.container.id,loginEmailFragment).commit()
    }
    fun change_login_main(){
        supportFragmentManager.beginTransaction().replace(binding.container.id,loginMainFragment).commit()
    }
    fun change_login_find(){
        supportFragmentManager.beginTransaction().replace(binding.container.id,loginFindFragment).commit()
    }
    fun change_login_register(){
        supportFragmentManager.beginTransaction().replace(binding.container.id,loginRegistFragment).commit()
    }


    override fun onBackPressed() {

        if (supportFragmentManager.fragments.get(0) is LoginEmailFragment){
            change_login_main()
        }
        else if(supportFragmentManager.fragments.get(0) is LoginMainFragment) {
            finishAffinity() //해당 앱의 루트 액티비티를 종료시킨다.

            System.runFinalization() //현재 작업중인 쓰레드가 다 종료되면, 종료 시키라는 명령어이다.
            System.exit(0)

        }else if(supportFragmentManager.fragments.get(0) is LoginRegistFragment){
            change_login_email()
        }
        else if(supportFragmentManager.fragments.get(0) is LoginFindFragment){
            change_login_email()
        }
        else{
            super.onBackPressed()
        }

    }

}