package com.example.ghostdiary

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.example.ghostdiary.databinding.ActivityLoginBinding
import com.example.ghostdiary.fragment.login.LoginEmailFragment
import com.example.ghostdiary.fragment.login.LoginMainFragment
import com.example.ghostdiary.fragment.login.LoginRegistFragment


class LoginActivity : AppCompatActivity() {
    private lateinit var binding:ActivityLoginBinding
    lateinit var viewModel:LoginViewModel

    lateinit var loginMainFragment: LoginMainFragment
    lateinit var loginEmailFragment: LoginEmailFragment
    lateinit var loginRegistFragment: LoginRegistFragment

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
        supportFragmentManager.beginTransaction().replace(binding.container.id,loginRegistFragment).commit()
    }


}