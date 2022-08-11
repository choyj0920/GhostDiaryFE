package com.example.ghostdiary

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.beautycoder.pflockscreen.PFFLockScreenConfiguration
import com.beautycoder.pflockscreen.fragments.PFLockScreenFragment
import com.beautycoder.pflockscreen.fragments.PFLockScreenFragment.OnPFLockScreenCodeCreateListener
import com.example.ghostdiary.databinding.ActivityLockBinding
import com.example.ghostdiary.databinding.ActivityMainBinding


class LockActivity : AppCompatActivity() {
    private lateinit var binding :ActivityLockBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityLockBinding.inflate(layoutInflater)
        setContentView(binding.root)

        createPin()
    }

    fun createPin() {
        val fragment = PFLockScreenFragment()
        val builder = PFFLockScreenConfiguration.Builder(this)
            .setMode(PFFLockScreenConfiguration.MODE_CREATE).setCodeLength(6)
        fragment.setConfiguration(builder.build())
        fragment.setCodeCreateListener(
            object : OnPFLockScreenCodeCreateListener {
                override fun onCodeCreated(encodedCode: String) {
                    
                    Log.e("ERR","$encodedCode 가 만든 비번이에요")


                }

                override fun onNewCodeValidationFailed() {

                }
            }
        )

        supportFragmentManager.beginTransaction().replace(binding.container.id, fragment).commit()



    }

}