package com.example.ghostdiary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.example.ghostdiary.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {
    private lateinit var activityLoginBinding:ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityLoginBinding=ActivityLoginBinding.inflate(layoutInflater)

        setContentView(activityLoginBinding.root)

        activityLoginBinding.btnLoginEmail.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }
}