package com.ghostdiary.ghostdiary

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.beautycoder.pflockscreen.PFFLockScreenConfiguration
import com.beautycoder.pflockscreen.fragments.PFLockScreenFragment
import com.beautycoder.pflockscreen.fragments.PFLockScreenFragment.OnPFLockScreenCodeCreateListener
import com.ghostdiary.ghostdiary.databinding.ActivityLockBinding

import com.ghostdiary.ghostdiary.utilpackage.Util

class LockActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLockBinding
    var iscreatemode = false
    var lastTimeBackPressed: Long = 0
    lateinit var prefs: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    lateinit var context: Context
    lateinit var curPinencode: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this
        binding = ActivityLockBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefs = this.getSharedPreferences("Prefs", Context.MODE_PRIVATE)
        editor = prefs.edit() // 데이터 기록을 위한 editor
        iscreatemode = intent.getBooleanExtra("iscreate", false)
        curPinencode = prefs.getString("pinencode", "")!!
        var islock=prefs.getBoolean("isLock",false)

        if(!iscreatemode && !islock){
            var intent = Intent(context, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
            finish()
        }

        if (iscreatemode) {
            createPin()
        } else {
            authPin()

        }
        Util.setGlobalFont(binding!!.root)

    }


    fun createPin() {


        val fragment = PFLockScreenFragment()
        val builder = PFFLockScreenConfiguration.Builder(this)
            .setMode(PFFLockScreenConfiguration.MODE_CREATE).setCodeLength(4)
            .setTitle(resources.getString(R.string.please_input_password)).setUseFingerprint(false)
            .setNextButton(resources.getString(R.string.next))
            .setNewCodeValidation(true)
            .setNewCodeValidationTitle(resources.getString(R.string.please_enter_again))
        fragment.setConfiguration(builder.build())
        fragment.setCodeCreateListener(
            object : OnPFLockScreenCodeCreateListener {
                override fun onCodeCreated(encodedCode: String) {

                    Log.e("ERR", "$encodedCode 가 만든 비번이에요")
                    showmessage(resources.getString(R.string.lock_sucess))

                    editor.putBoolean("isLock", true).apply()
                    editor.putString("pinencode", encodedCode).apply()
                    editor.commit()
                    finish()

                }
                override fun onNewCodeValidationFailed() {
                    title=resources.getString(R.string.please_enter_again)

                    showmessage(resources.getString(R.string.wrong_password))
                    

                }
            }
        )
        supportFragmentManager.beginTransaction().replace(binding.container.id, fragment).commit()
    }

    fun authPin() {
        if (curPinencode == "")
            finish()
        val fragment = PFLockScreenFragment()
        fragment.setEncodedPinCode(curPinencode)
        val builder = PFFLockScreenConfiguration.Builder(this)
            .setMode(PFFLockScreenConfiguration.MODE_AUTH).setCodeLength(4).setUseFingerprint(false)
            .setTitle(resources.getString(R.string.please_input_password)).setClearCodeOnError(true)
        fragment.setConfiguration(builder.build())
        fragment.setLoginListener(
            object : PFLockScreenFragment.OnPFLockScreenLoginListener {
                override fun onCodeInputSuccessful() {
                    Log.d("TAG", "코드 확인")
                    finish()
                }

                override fun onFingerprintSuccessful() {
                    Log.d("TAG", "지문 확인")

                    finish()

                }

                override fun onPinLoginFailed() {
                    showmessage(resources.getString(R.string.wrong_password))
                    Log.d("TAG", "코드 확인 실패")

                }

                override fun onFingerprintLoginFailed() {
                    Log.d("TAG", "지문 확인 실패")

                }
            }
        )
        supportFragmentManager.beginTransaction().replace(binding.container.id, fragment).commit()



    }


    override fun onBackPressed() {
        if (iscreatemode) {
            super.onBackPressed()
        } else {

            if (System.currentTimeMillis() - lastTimeBackPressed >= 1500) {
                lastTimeBackPressed = System.currentTimeMillis()
                showmessage(resources.getString(R.string.app_close_message))

            } else {
                finishAffinity()
                System.runFinalization()
                System.exit(0)
            }

        }
    }
    fun showmessage(str: String) {
        val toast:Toast  = Toast.makeText(context, str, Toast.LENGTH_SHORT);
        toast.show();

        var handler = Handler();
            handler.postDelayed( Runnable() {
               run {
                   toast.cancel()
               }
        }, 1000);
    }
}

