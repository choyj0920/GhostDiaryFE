package com.example.ghostdiary

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import com.example.ghostdiary.databinding.ActivityLoginBinding
import com.example.ghostdiary.databinding.ActivityMainBinding
import com.example.ghostdiary.databinding.FragmentLoginEmailBinding
import com.example.ghostdiary.fragment.CalendarFragment
import com.example.ghostdiary.fragment.DefaultFragment
import com.example.ghostdiary.fragment.RecordFragment
import com.example.ghostdiary.fragment.login.LoginEmailFragment
import com.example.ghostdiary.fragment.login.LoginMainFragment


class LoginActivity : AppCompatActivity() {
    private lateinit var binding:ActivityLoginBinding
    lateinit var viewModel:LoginViewModel

    lateinit var loginMainFragment: LoginMainFragment
    lateinit var loginEmailFragment: LoginEmailFragment

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

        supportFragmentManager.beginTransaction().replace(binding.container.id,loginMainFragment).commit()


    }

    fun change_login_email(){
        Log.e(ContentValues.TAG,"전환 함수호출도 됩니다.")

        supportFragmentManager.beginTransaction().replace(binding.container.id,loginEmailFragment).commit()
    }
    fun change_login_main(){
        supportFragmentManager.beginTransaction().replace(binding.container.id,loginMainFragment).commit()
    }
    fun change_login_findpassword(){
        supportFragmentManager.beginTransaction().replace(binding.container.id,loginEmailFragment).commit()
    }
    fun change_login_register(){
        supportFragmentManager.beginTransaction().replace(binding.container.id,loginEmailFragment).commit()
    }


}