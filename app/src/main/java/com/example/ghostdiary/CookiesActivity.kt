package com.example.ghostdiary

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.*
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.ghostdiary.databinding.ActivityCookiesBinding
import com.example.ghostdiary.fragment.cookie.AdviceFragment
import com.example.ghostdiary.fragment.cookie.SelectCookieFragment
import com.example.ghostdiary.utilpackage.Util
import java.text.SimpleDateFormat
import java.util.*


class CookiesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCookiesBinding
    private lateinit var container:FrameLayout
    private lateinit var prefs : SharedPreferences
    lateinit var selectCookieFragment: SelectCookieFragment
    lateinit var adviceFragment: AdviceFragment
    var istodaycookie=true

    companion object{


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature( Window.FEATURE_NO_TITLE );



        val layoutParams = WindowManager.LayoutParams()
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
//        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
        layoutParams.dimAmount = .5f




        window.attributes = layoutParams



        window.attributes.gravity=Gravity.BOTTOM

        window.decorView.setOnClickListener{
            Log.d("TAG","클릭")
        }





        binding=ActivityCookiesBinding.inflate(layoutInflater)
        container=binding.container

        prefs = this.getSharedPreferences("Prefs", Context.MODE_PRIVATE)
        var datefomat=SimpleDateFormat("yyyy-MM-dd")
        var curday= datefomat.format(Calendar.getInstance().time)
        var iscurrentday=prefs.getString("curday","0")
        if(iscurrentday!=curday){
            istodaycookie=true
            show_Todaycookie()
        }else{
            istodaycookie=false
            show_advice()

        }
        binding.outsidelayout.setOnClickListener{ finish() }

//        supportFragmentManager.beginTransaction().replace(container.id,memoSelectFragment).commit()
        setContentView(binding.root)
        Util.setGlobalFont(binding.root)


    }

    fun containerChange(new_fragment:Fragment){
        getSupportFragmentManager().beginTransaction().replace(binding.container.id, new_fragment).addToBackStack(null).commit();
    }fun reversechange(fragment: Fragment){
        getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        getSupportFragmentManager().popBackStack();
    }

    override fun finish() {
        super.finish()
        MainActivity.isup=false
        overridePendingTransition(R.anim.none,R.anim.vertical_exit);

    }

    fun show_Todaycookie(){
        binding.ivBanner.setImageResource(R.drawable.banner_cookies)

        var arraytoday=resources.getStringArray(R.array.TODAYCOOKIES)

        selectCookieFragment= SelectCookieFragment(true, arraytoday)

        getSupportFragmentManager().beginTransaction().replace(binding.container.id, selectCookieFragment).addToBackStack(null).commit();
    }
    fun show_advice(){
        binding.ivBanner.setImageResource(R.drawable.banner_advice)

        adviceFragment= AdviceFragment()

        getSupportFragmentManager().beginTransaction().replace(binding.container.id, adviceFragment).addToBackStack(null).commit();
    }




    override fun onStart() {
        super.onStart()

    }

    override fun onDestroy() {
        super.onDestroy()
    }


    override fun onBackPressed() {

        var curfragment=supportFragmentManager.fragments.get(0)
        if(curfragment is SelectCookieFragment && istodaycookie){

            finish()

            return
        }else if(!istodaycookie && curfragment is SelectCookieFragment){
            reversechange(curfragment)
            return
        }
        else{
            finish()
            return
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






}