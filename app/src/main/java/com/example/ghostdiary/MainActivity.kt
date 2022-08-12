package com.example.ghostdiary

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import com.beautycoder.pflockscreen.security.PFSecurityManager
import com.example.ghostdiary.databinding.ActivityMainBinding
import com.example.ghostdiary.dataclass.Day_Diary
import com.example.ghostdiary.fragment.main.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var keyboardVisibilityUtils: KeyboardVisibilityUtils
    private lateinit var binding: ActivityMainBinding
    private lateinit var defaultFragment: DefaultFragment
    private lateinit var calendarFragment: CalendarFragment
    private lateinit var recordFragment: RecordFragment
    private lateinit var prefs : SharedPreferences
    companion object{
        lateinit var mainactivity:MainActivity
    }
    var lastTimeBackPressed : Long = 0



    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding=ActivityMainBinding.inflate(layoutInflater)
        viewModel= ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.getEmotionArray(this)

        prefs = this.getSharedPreferences("Prefs", Context.MODE_PRIVATE)

        val islock= prefs.getBoolean("isLock",false)
        if(islock){
            lockapp()
            binding.sidemenuSwitchLock.isChecked=true
        }else{
            binding.sidemenuSwitchLock.isChecked=false

        }

        defaultFragment= DefaultFragment()
        calendarFragment= CalendarFragment()
        recordFragment= RecordFragment()
        mainactivity=this

        var today= LocalDateTime.now()
        var to = today.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))

        binding!!.tvDate.text=to

        binding!!.btnSideomenu.setOnClickListener {
            binding!!.drawerlayout.openDrawer(GravityCompat.START)
        }

        supportFragmentManager.beginTransaction().replace(binding.container.id,defaultFragment).commit()

        keyboardVisibilityUtils = KeyboardVisibilityUtils(window,
            onShowKeyboard = {
                binding.drawerlayout.run {
                    //smoothScrollTo(scrollX, scrollY + keyboardHeight)
                    //키보드 올라왔을때 원하는 동작
                    binding.navigationbar.visibility = View.GONE
                }
            },
            onHideKeyboard = {
                binding.drawerlayout.run {
                    //키보드 내려갔을때 원하는 동작
                    //smoothScrollTo(scrollX, scrollY + keyboardHeight)
                    binding.navigationbar.visibility = View.VISIBLE
                }
            }
        )


        binding.navigationbar.setOnItemSelectedListener { item ->
            when(item.itemId) {

                R.id.invisible -> {
                    var today= LocalDateTime.now()
                    var to = today.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))
                    binding!!.tvDate.text=to
                    binding!!.tvDate.gravity=Gravity.LEFT

                    supportFragmentManager.beginTransaction().replace(binding.container.id, defaultFragment).commit()
                }
                R.id.calendar ->  {
                    binding!!.tvDate.text="캘린더"
                    binding!!.tvDate.gravity=Gravity.CENTER
                    supportFragmentManager.beginTransaction().replace(binding.container.id,calendarFragment).commit()
                }

                R.id.record-> {
                    binding!!.tvDate.text="기록"
                    binding!!.tvDate.gravity=Gravity.CENTER
                    supportFragmentManager.beginTransaction().replace(binding.container.id,recordFragment).commit()
                }
                R.id.memo->{
                    binding!!.tvDate.text="메모"
                    binding!!.tvDate.gravity=Gravity.CENTER
                }
                R.id.clinic->{
                    binding!!.tvDate.text="클리닉"
                    binding!!.tvDate.gravity=Gravity.CENTER
                }

            }
            hide_emotionmenu()
            true
        }

        // 로그아웃 부분 주석
        /*
        binding.sidemenuLogout.setOnClickListener{
            val prefs : SharedPreferences = this.getSharedPreferences("Prefs", Context.MODE_PRIVATE)
            val editor : SharedPreferences.Editor = prefs.edit() // 데이터 기록을 위한 editor
            editor.remove("auto_email")
            editor.remove("auto_password")
            editor.commit()
            var intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
            finish()
        } */

        init_sidemenu()

        setContentView(binding.root)

    }

    fun init_sidemenu(){
        //db 초기화
        binding.sidemenuDbinit.setOnClickListener{
            viewModel.getdb(this).createdb()
            finishAffinity() //해당 앱의 루트 액티비티를 종료시킨다.

            System.runFinalization() //현재 작업중인 쓰레드가 다 종료되면, 종료 시키라는 명령어이다.
            System.exit(0)

        }

        binding.sidemenuLock.setOnClickListener {
            val islock= prefs.getBoolean("isLock",false)
            if(islock) // 글자 눌러서 잠금되어있을때만 락
                lockapp()
            binding.drawerlayout.closeDrawer(GravityCompat.START)
        }


        binding.sidemenuSwitchLock.setOnCheckedChangeListener { buttonview, ischecked ->
            if(ischecked){
                var intent = Intent(this, LockActivity::class.java)
                intent.putExtra("iscreate",true )
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                startActivity(intent)
                binding.drawerlayout.closeDrawer(GravityCompat.START)
            }else{
                val editor : SharedPreferences.Editor = prefs.edit() // 데이터 기록을 위한 editor
                editor.putBoolean("isLock",false).apply()
                editor.remove("pinencode").apply()
                editor.commit()
                PFSecurityManager.getInstance().getPinCodeHelper().delete {  }

            }
        }
    }
    fun lockapp(){
        var intent = Intent(this, LockActivity::class.java)
        intent.putExtra("iscreate",false)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        startActivity(intent)
    }

    fun change_to_selectemotion(date :Date){
        var parent=when(binding.navigationbar.selectedItemId){
            R.id.calendar->calendarFragment
            R.id.record->recordFragment
            else -> {return}

        }
        var newselectemotion=SelectEmotionFragment(parent,date)
        supportFragmentManager.beginTransaction().replace(binding.container.id, newselectemotion).commit()
        binding!!.tvDate.text="하루의 감정"
        binding.btnPostcheck.visibility=View.VISIBLE
        binding.btnPosttext.visibility=View.VISIBLE

        binding.btnPostcheck.setOnClickListener {
            if(parent is CalendarFragment){
                newselectemotion.postemotion(parent)
                binding.navigationbar.selectedItemId=R.id.calendar
            }else if (parent is RecordFragment){
                newselectemotion.postemotion(parent)
                binding.navigationbar.selectedItemId=R.id.record
            }else{

            }
        }
        binding.btnPosttext.setOnClickListener {
            var diary =newselectemotion.getcurDiary()

            supportFragmentManager.beginTransaction().replace(binding.container.id, EditDiaryFragment(supportFragmentManager.fragments.get(0),date, diary = diary),).commit()
            binding.tvDate.text=""
            hide_emotionmenu()
        }
        binding.tvEdittext.visibility=View.VISIBLE
        binding.tvEdittext.setOnClickListener {
            newselectemotion.switcheditmode(!newselectemotion.editmode)
        }
        binding.btnSideomenu.visibility=View.INVISIBLE

    }


    fun change_to_editDiary(date :Date){

        supportFragmentManager.beginTransaction().replace(binding.container.id, EditDiaryFragment(supportFragmentManager.fragments.get(0),date),).commit()
        binding.tvDate.text=""
        hide_emotionmenu()

    }


    fun hide_emotionmenu(){
        binding.btnPostcheck.visibility=View.GONE
        binding.btnPosttext.visibility=View.GONE
        binding.tvEdittext.visibility=View.GONE
        binding.btnSideomenu.visibility=View.VISIBLE
    }

    override fun onDestroy() {
        keyboardVisibilityUtils.detachKeyboardListeners()
        super.onDestroy()
    }


    override fun onBackPressed() {

        if (supportFragmentManager.fragments.get(0) is CalendarFragment && calendarFragment.isshow){
            calendarFragment.down_post()
        }
        else if(supportFragmentManager.fragments.get(0) is DefaultFragment) {
            if(System.currentTimeMillis() - lastTimeBackPressed >= 1500){
                lastTimeBackPressed = System.currentTimeMillis()
                Toast.makeText(this,"'뒤로' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_LONG).show() }
            else {
                finishAffinity()
                System.runFinalization()
                System.exit(0)
            }


        }else if(supportFragmentManager.fragments.get(0) is SelectEmotionFragment){
            var frag=supportFragmentManager.fragments.get(0) as SelectEmotionFragment
            if(frag.parent is CalendarFragment){
                binding.navigationbar.selectedItemId=R.id.calendar
            }else{

            }

        }else if(supportFragmentManager.fragments.get(0) is EditDiaryFragment){
            var frag=supportFragmentManager.fragments.get(0) as EditDiaryFragment
            if(frag.parent is RecordFragment){
                binding.navigationbar.selectedItemId=R.id.record
            }else if(frag.parent is SelectEmotionFragment){
                supportFragmentManager.beginTransaction().replace(binding.container.id, frag.parent).commit()
                binding!!.tvDate.text="하루의 감정"
                binding.btnPostcheck.visibility=View.VISIBLE
                binding.btnPosttext.visibility=View.VISIBLE
                binding.tvEdittext.visibility=View.VISIBLE
                binding.tvEdittext.setOnClickListener {
                    (frag.parent as SelectEmotionFragment).switcheditmode(!(frag.parent as SelectEmotionFragment).editmode)
                }
                binding.btnSideomenu.visibility=View.INVISIBLE

            }
        }


        else{
            binding.navigationbar.selectedItemId=R.id.invisible
            supportFragmentManager.beginTransaction().replace(binding.container.id,defaultFragment).commit()

        }

    }

    fun change_calendar(){
        supportFragmentManager.beginTransaction().replace(binding.container.id,calendarFragment).commit()
    }
    fun change_record(){
        supportFragmentManager.beginTransaction().replace(binding.container.id,recordFragment).commit()
    }
    fun change_memo(){
//        supportFragmentManager.beginTransaction().replace(binding.container.id,).commit()
    }
    fun change_clinic(){
//        supportFragmentManager.beginTransaction().replace(binding.container.id,).commit()
    }

}