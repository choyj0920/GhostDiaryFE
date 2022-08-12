package com.example.ghostdiary

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
import com.example.ghostdiary.databinding.ActivityLockBinding
import com.example.ghostdiary.databinding.ActivityMainBinding


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
    }

    fun createPin() {
        val fragment = PFLockScreenFragment()
        val builder = PFFLockScreenConfiguration.Builder(this)
            .setMode(PFFLockScreenConfiguration.MODE_CREATE).setCodeLength(4)
            .setTitle("새 비밀번호를 입력해 주세요").setUseFingerprint(false)
            .setNextButton("다 음")
            .setNewCodeValidation(true)

            .setNewCodeValidationTitle("한번 더 입력해 주세요")
        fragment.setConfiguration(builder.build())
        fragment.setCodeCreateListener(
            object : OnPFLockScreenCodeCreateListener {
                override fun onCodeCreated(encodedCode: String) {

                    Log.e("ERR", "$encodedCode 가 만든 비번이에요")
                    showmessage("입력하신 비밀번호로 잠금되었습니다.")

                    editor.putBoolean("isLock", true).apply()
                    editor.putString("pinencode", encodedCode).apply()
                    editor.commit()
                    finish()

                }
                override fun onNewCodeValidationFailed() {

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
            .setTitle("비밀번호를 입력해 주세요").setClearCodeOnError(true)
        fragment.setConfiguration(builder.build())
        fragment.setLoginListener(
            object : PFLockScreenFragment.OnPFLockScreenLoginListener {
                override fun onCodeInputSuccessful() {
                    Log.d("TAG", "코드 확인")
                    showmessage("pin인증 완료")

                    finish()
                }

                override fun onFingerprintSuccessful() {
                    showmessage("지문인증 완료")
                    Log.d("TAG", "지문 확인")

                    finish()

                }

                override fun onPinLoginFailed() {
                    showmessage("비밀번호가 틀립니다.")
                    Log.d("TAG", "코드 확인 실패")

                }

                override fun onFingerprintLoginFailed() {
                    showmessage("올바르지 않은 지문입니다.")
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
                showmessage("'뒤로' 버튼을 한번 더 누르시면 종료됩니다.")

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

