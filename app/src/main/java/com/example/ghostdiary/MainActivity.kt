package com.example.ghostdiary

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.PopupWindow
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_LOCKED_CLOSED
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.adapter.FragmentViewHolder
import androidx.viewpager2.widget.ViewPager2
import com.beautycoder.pflockscreen.security.PFSecurityManager
import com.example.ghostdiary.databinding.ActivityMainBinding
import com.example.ghostdiary.databinding.MenuSideoptionBinding
import com.example.ghostdiary.databinding.MenuThemeBinding
import com.example.ghostdiary.fragment.calendar.CalendarFragment
import com.example.ghostdiary.fragment.calendar.RecordFragment
import com.example.ghostdiary.fragment.main.*
import com.example.ghostdiary.utilpackage.Util
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var calendarFragment: CalendarFragment
    private lateinit var recordFragment: RecordFragment
    private lateinit var memoFragment: MemoFragment
    private lateinit var prefs : SharedPreferences
    companion object{
        lateinit var mainactivity:MainActivity
        var isup=false
        var curTheme=1

        val M_ALARM_REQUEST_CODE = 1000
        val REQUEST_CODE= M_ALARM_REQUEST_CODE

        var fontname:Array<String> = arrayOf("roboto","나눔바른펜","나눔 손글씨펜","d2코딩","BM연성","고도","고도마음","이롭게바탕","미생","야체")
    }
    var lastTimeBackPressed : Long = 0
    lateinit var pageCallBack: ViewPager2.OnPageChangeCallback
    private lateinit var viewPager: ViewPager2
    private var alarmTime:Calendar=Calendar.getInstance()


    lateinit var viewModel: MainViewModel
    fun setfont(){
        Util.setGlobalFont(binding.root)

        binding.sidemenuTvCurfont.text= fontname[Util.curfont]

    }fun savefontsetting(fontindex:Int){
        val editor : SharedPreferences.Editor = prefs.edit() // 데이터 기록을 위한 editor
        editor.putInt("curfont",fontindex).apply()
        editor.commit()
        Util.curfont=fontindex

        setfont()

        try {
            calendarFragment.update_rv()

        }catch(e: Exception){

        }
        try {
            recordFragment.updatefont()

        }catch(e: Exception){

        }


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding=ActivityMainBinding.inflate(layoutInflater)
        viewModel= ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.getEmotionArray(this)

        viewPager = binding.pager

        init_view()

        setContentView(binding.root)
        setfont()

    }
    fun Update_pager(isCalendar: Boolean){
        val pagerAdapter = CustomPagerAdapter(this,isCalendar)
        viewPager.adapter = pagerAdapter
        viewPager.apply {  }
        update_topmenu(isCalendar)
        if(isCalendar){
            pageCallBack = object: ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    // 특정 페이지가 선택될 때 마다 여기가 호출됩니다.
                    // position 에는 현재 페이지 index - 첫페이지면 0
                    when(position){
                        0-> {
                            try {
                                if(calendarFragment.curCal.time.time!=viewModel.calendar.time.time)
                                    calendarFragment.init_rv()

                            }catch(e: Exception){
                            }
                            switchHidetopmenu(false)

                        }
                        1-> {
                            try {
                                if(calendarFragment.curCal.time.time!=recordFragment.curCal.time.time)
                                    recordFragment.update()


                            }catch(e: Exception){

                            }
                            switchHidetopmenu(true)
                        }
                    }
                }
            }
            viewPager.registerOnPageChangeCallback(pageCallBack)
        }else{
            viewPager.unregisterOnPageChangeCallback(pageCallBack)
        }


    }

    fun init_view(){
        binding.drawerlayout.setDrawerLockMode(LOCK_MODE_LOCKED_CLOSED)

        prefs = this.getSharedPreferences("Prefs", Context.MODE_PRIVATE)
        var array:ArrayList<Typeface?> = arrayListOf(
            ResourcesCompat.getFont(this,R.font.roboto),
            ResourcesCompat.getFont(this,R.font.nanumbarunpenr),
            ResourcesCompat.getFont(this,R.font.nanumpen),
            ResourcesCompat.getFont(this,R.font.d2coding),
            ResourcesCompat.getFont(this,R.font.bmhannaair),
            ResourcesCompat.getFont(this,R.font.godofont),
            ResourcesCompat.getFont(this,R.font.godomaum),
            ResourcesCompat.getFont(this,R.font.iropkebatangm),
            ResourcesCompat.getFont(this,R.font.sdmisaeng),
            ResourcesCompat.getFont(this,R.font.yafont),
        )

        var iscur=prefs.getInt("curfont",9)
        Util.init(array,prefs.getInt("isfontnum",iscur))
        curTheme=prefs.getInt("theme",1)

        val isfirst= prefs.getBoolean("firststart",true)
        if(isfirst){
            var intent = Intent(this, TutorialActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
            finish()
        }


        val islock= prefs.getBoolean("isLock",false)
        if(islock){
            lockapp()
            binding.sidemenuSwitchLock.isChecked=true
        }else{
            binding.sidemenuSwitchLock.isChecked=false
        }


        calendarFragment= CalendarFragment()
        recordFragment= RecordFragment()
        memoFragment=MemoFragment()
        mainactivity=this


        binding.swtichbutton.setOnCheckedChangeListener { button, ismemo ->
            if(ismemo){
                Update_pager(false)
            }else{
                Update_pager(true)
            }
        }


        init_sidemenu()

        init_topmenu()

    }


    @SuppressLint("ClickableViewAccessibility")
    fun init_topmenu(){
        binding.ivMemoplus.setOnClickListener {
            val dialog = MemoFolderAddDialog(memoFragment )
            dialog.isCancelable = true
            dialog.show(supportFragmentManager, "ConfirmDialog")
        }


        val popupInflater =
            applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupBind = MenuThemeBinding.inflate(popupInflater)


        val popupWindow = PopupWindow(
            popupBind.root,dpToPx(this,120f).toInt() ,dpToPx(this,140f).toInt()  , true
        ).apply { contentView.setOnClickListener { dismiss() }
            popupBind.btn1.setOnClickListener {
                curTheme=1
                val editor : SharedPreferences.Editor = prefs.edit() // 데이터 기록을 위한 editor
                editor.putInt("theme", curTheme).apply()
                editor.commit()
                dismiss()
                changeTheme(curTheme)
                try {
                    calendarFragment.init_rv()

                }catch(e: Exception){

                }
            }
            popupBind.btn2.setOnClickListener {
                curTheme=2

                val editor : SharedPreferences.Editor = prefs.edit() // 데이터 기록을 위한 editor
                editor.putInt("theme", curTheme).apply()
                editor.commit()
                dismiss()
                changeTheme(curTheme)
                try {
                    calendarFragment.init_rv()

                }catch(e: Exception){

                }

            }
            popupBind.btn3.setOnClickListener {
                curTheme=3

                val editor : SharedPreferences.Editor = prefs.edit() // 데이터 기록을 위한 editor
                editor.putInt("theme", curTheme).apply()
                editor.commit()
                dismiss()
                changeTheme(curTheme)
                try {
                    calendarFragment.init_rv()

                }catch(e: Exception){

                }
            }

        }
        Util.setGlobalFont(popupBind.root)
        // make sure you use number than wrap_content or match_parent,
        // because for me it is not showing anything if I set it to wrap_content from ConstraintLayout.LayoutParams.


        binding!!.btnTheme.setOnClickListener{
            var loc:IntArray= intArrayOf(0,0)
            binding!!.btnTheme.getLocationOnScreen(loc)
            popupWindow.showAtLocation(binding!!.btnTheme, Gravity.NO_GRAVITY, loc[0]+popupWindow.width/2, loc[1]);
        }


        var toplistener = OnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    v.setBackgroundResource(R.drawable.circle_backgroud_select)

                }
                MotionEvent.ACTION_UP->{
                    v.setBackgroundColor(Color.parseColor("#00000000"))
                    if(v.id ==binding.ivSetting.id){

                        binding.drawerlayout.openDrawer(GravityCompat.END)
                    }else if(v.id ==binding.ivCookies.id){

                        val intent = Intent(this, CookiesActivity::class.java)
                        intent.putExtra("data", "Test Popup")
                        if(isup){
                        }else {
                            isup=true
                            startActivity(intent)
                            overridePendingTransition(R.anim.vertical_enter, R.anim.none)
                        }

                    }else if(v.id==binding.ivShare.id){
                        val editor : SharedPreferences.Editor = prefs.edit() // 데이터 기록을 위한 editor

                        calendarFragment.ScreenShotActivity()

                    }

                }
            }
            false
        }

        binding.ivSetting.setOnTouchListener(toplistener)
        binding.ivShare.setOnTouchListener(toplistener)
        binding.ivCookies.setOnTouchListener(toplistener)
        update_topmenu(true)
        Update_pager(true)


    }

    fun changeTheme(index:Int=-1){
        var theme= curTheme
        if(index!=-1){
            theme=index
        }

        if(theme == 1){

            binding!!.root.setBackgroundColor(Color.parseColor("#FFFFFF"))
            binding!!.pager.setBackgroundColor(Color.parseColor("#FFFFFF"))
            binding!!.titleTv.setTextColor(Color.parseColor("#000000"))
            binding.btnTheme.setImageResource(R.drawable.ic_lamp_black)
            binding.ivShare.setImageResource(R.drawable.ic_share)

            binding.ivSetting.setImageResource(R.drawable.ic_setting)


        }else if(theme == 2){
            binding!!.root.setBackgroundColor(Color.parseColor("#000000"))
            binding!!.pager.setBackgroundColor(Color.parseColor("#000000"))
            binding!!.titleTv.setTextColor(Color.parseColor("#FFFFFF"))
            binding.btnTheme.setImageResource(R.drawable.ic_lamp_white)
            binding.ivShare.setImageResource(R.drawable.ic_share_white)
            binding.ivSetting.setImageResource(R.drawable.ic_setting_white)



        }
        else if(theme == 3){
            binding!!.root.setBackgroundColor(Color.parseColor("#000000"))
            binding!!.pager.setBackgroundColor(Color.parseColor("#000000"))
            binding!!.titleTv.setTextColor(Color.parseColor("#FFFFFF"))
            binding.btnTheme.setImageResource(R.drawable.ic_lamp_white)
            binding.ivShare.setImageResource(R.drawable.ic_share_white)
            binding.ivSetting.setImageResource(R.drawable.ic_setting_white)



        }


        

    }


    fun init_sidemenu(){
        //db 초기화
        initAlarm()
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
            binding.drawerlayout.closeDrawer(GravityCompat.END)
        }
        binding.sidemenuFont.setOnClickListener {
            val dialog = FontSelectDialog(this)
            dialog.isCancelable = true
            dialog.show(supportFragmentManager, "ConfirmDialog")
        }
        binding.sidemenuTvAlarm.setOnClickListener {
            val dialog = AlarmSetterDialog(this,alarmTime)
            dialog.isCancelable = true
            dialog.show(supportFragmentManager, "ConfirmDialog")
        }
        binding.sidemenuTvCurfont.setOnClickListener {
            val dialog = FontSelectDialog(this)
            dialog.isCancelable = true
            dialog.show(supportFragmentManager, "ConfirmDialog")
        }

        binding.sidemenuSwitchLock.setOnCheckedChangeListener { buttonview, ischecked ->
            if(ischecked){
                var intent = Intent(this, LockActivity::class.java)
                intent.putExtra("iscreate",true )
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                startActivity(intent)

                binding.drawerlayout.closeDrawer(GravityCompat.END)

            }else{
                val editor : SharedPreferences.Editor = prefs.edit() // 데이터 기록을 위한 editor
                editor.putBoolean("isLock",false).apply()
                editor.remove("pinencode").apply()
                editor.commit()
                PFSecurityManager.getInstance().getPinCodeHelper().delete {  }
            }
        }
    }

    fun update_topmenu(isCalendar:Boolean){
        if(isCalendar){
            binding!!.titleTv.text="Calendar"
            if(binding.swtichbutton.isChecked)
                binding.swtichbutton.isChecked=false
            binding.ivCookies.visibility=View.VISIBLE
            binding.ivSetting.visibility=View.VISIBLE
            binding.ivShare.visibility=View.VISIBLE
            binding.ivMemoplus.visibility=View.GONE
            binding.btnTheme.visibility=View.VISIBLE
            changeTheme()
        }else{
            binding!!.titleTv.text="Memo"
            if(!binding.swtichbutton.isChecked)
                binding.swtichbutton.isChecked=true
            binding.ivCookies.visibility=View.GONE
            binding.ivSetting.visibility=View.GONE
            binding.ivShare.visibility=View.GONE
            binding.ivMemoplus.visibility=View.VISIBLE
            binding.btnTheme.visibility=View.GONE
            changeTheme(1)


        }


    }
    fun switchHidetopmenu(ishide:Boolean){
        if(ishide)
            binding.layoutTopmenu.visibility=View.GONE
        else
            binding.layoutTopmenu.visibility=View.VISIBLE
    }

    override fun onStart() {
        super.onStart()

        if(!binding.swtichbutton.isChecked){

        }

        val islock= prefs.getBoolean("isLock",false)
        if(islock){
            binding.sidemenuSwitchLock.isChecked=true
        }else{
            binding.sidemenuSwitchLock.isChecked=false
        }



    }
    fun setAlarmtext(){

        var alarmtimetext=if(alarmTime.get(Calendar.AM_PM)==0) "AM "  else "PM "
        alarmtimetext+="${alarmTime.get(Calendar.HOUR)}:${String.format("%02d",alarmTime.get(Calendar.MINUTE))}"

        binding.sidemenuTvAlarm.text=alarmtimetext
    }

    fun reversechange(fragment: Fragment){
        getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        getSupportFragmentManager().popBackStack();
    }

    fun lockapp(){
        var intent = Intent(this, LockActivity::class.java)
        intent.putExtra("iscreate",false)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        startActivity(intent)
    }

    private fun initAlarm() {
        var formatDate = SimpleDateFormat("HH:mm");
        var strdate = prefs.getString("alarmtime","21:30")
        alarmTime.time = formatDate.parse(strdate)
        setAlarmtext()

        var isalarmOn=prefs.getBoolean("isalarmOn",false)




        if(isalarmOn){
            binding.sidemenuSwitchAlarm.isChecked=true
        }else{
            binding.sidemenuSwitchAlarm.isChecked=false
        }
        val editor : SharedPreferences.Editor = prefs.edit() // 데이터 기록을 위한 editor
        editor.putBoolean("isalarmOn",binding.sidemenuSwitchAlarm.isChecked).apply()
        editor.commit()
        binding.sidemenuSwitchAlarm.setOnCheckedChangeListener { buttonview, ischecked ->
                    // 온/오프 에 따라 작업을 처리한다
            updatealarm()
            val editor : SharedPreferences.Editor = prefs.edit() // 데이터 기록을 위한 editor
            editor.putBoolean("isalarmOn",ischecked).apply()
            editor.commit()


        }
    }
    fun updatealarm(hour:Int=-1,min:Int=-1){
        if(hour!=-1){
            alarmTime.set(Calendar.HOUR_OF_DAY,hour)
            alarmTime.set(Calendar.MINUTE,min)

            val editor : SharedPreferences.Editor = prefs.edit() // 데이터 기록을 위한 editor
            var formatDate = SimpleDateFormat("HH:mm");
            var temp=formatDate.format(alarmTime.time)
            editor.putString("alarmtime",temp).apply()

            editor.commit()

            setAlarmtext()

        }
        var ischecked=binding.sidemenuSwitchAlarm.isChecked

        val alarmManager = binding.root.context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pendingIntent = Intent(binding.root.context, AlarmReceiver::class.java).let {
            it.putExtra("code", REQUEST_CODE)
            it.putExtra("count", 32)

            PendingIntent.getBroadcast(binding.root.context, REQUEST_CODE, it,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) PendingIntent.FLAG_MUTABLE else 0)
        }

        if(ischecked) {
            alarmManager.cancel(pendingIntent)

            // Case 3: 오전 8시 27분 Alarm 생성 (Interval: Day)
            val calendar = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
                set(Calendar.HOUR_OF_DAY, alarmTime.get(Calendar.HOUR_OF_DAY))
                set(Calendar.MINUTE, alarmTime.get(Calendar.MINUTE))
                set(Calendar.SECOND,0)
            }
            if(calendar.before(Calendar.getInstance())){
                calendar.add(Calendar.DATE,1)
            }

            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
//            Toast.makeText(applicationContext, "Start", Toast.LENGTH_SHORT).show()
            Log.d("myLog", "Start")
        } else {
            alarmManager.cancel(pendingIntent)
//            Toast.makeText(applicationContext, "Cancel", Toast.LENGTH_SHORT).show()
            Log.d("myLog", "Cancel")
        }
    }




    fun dpToPx(context: Context, dp: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics)
    }


    override fun onDestroy() {
        super.onDestroy()
    }


    override fun onBackPressed() {
        if(binding.drawerlayout.isDrawerOpen(GravityCompat.END)){
            binding.drawerlayout.closeDrawer(GravityCompat.END)
            return
        }
        else {

            if (viewPager.currentItem == 0) {
                if(System.currentTimeMillis() - lastTimeBackPressed >= 1500){
                    lastTimeBackPressed = System.currentTimeMillis()
                    showmessage("\"'뒤로' 버튼을 한번 더 누르시면 종료됩니다.\"")
                }
                else {
                    finishAffinity()
                    System.runFinalization()
                    System.exit(0)
                }
                return

            } else {
                // Otherwise, select the previous step.
                viewPager.currentItem = viewPager.currentItem - 1
                return

            }
        }




    }
    fun showmessage(str: String) {
        val toast:Toast  = Toast.makeText(this, str, Toast.LENGTH_SHORT);
        toast.show();

        var handler = Handler();
        handler.postDelayed( Runnable() {
            run {
                toast.cancel()
            }
        }, 1000);
    }




    private inner class CustomPagerAdapter(var fa: FragmentActivity,var isCalendar: Boolean) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int {
            if (isCalendar)
                return 2
            else
                return 1
        }

        override fun onBindViewHolder(
            holder: FragmentViewHolder,
            position: Int,
            payloads: MutableList<Any>
        ) {
            val fragment  =  fa.supportFragmentManager.findFragmentByTag("f$position")
            fragment?.let{
                if( it is CalendarFragment){
                    it.initView()
                }else if(it is RecordFragment){
                    Log.d("TAG","확인 vie호출")
                    it.update()
                }
            }

            super.onBindViewHolder(holder, position, payloads)
        }

        override fun createFragment(position: Int): Fragment {
            if (isCalendar) {
                when (position) {
                    0 -> {
                        return calendarFragment
                    }
                    else ->{
                        return recordFragment
                    }

                }
            } else {
                return memoFragment
            }
        }

    }

}