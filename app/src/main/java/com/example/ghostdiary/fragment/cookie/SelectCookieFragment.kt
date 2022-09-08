package com.example.ghostdiary.fragment.cookie

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.ghostdiary.R
import com.example.ghostdiary.databinding.FragmentSelectCookieBinding
import com.example.ghostdiary.utilpackage.Util
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class SelectCookieFragment(var isTodaycookie:Boolean,var arraytext:ArrayList<String>) : Fragment() {

    private var binding: FragmentSelectCookieBinding?=null
    private lateinit var cookiearray:Array<ImageView>
    private var curselect=-1
    private lateinit var prefs : SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        prefs = requireActivity().getSharedPreferences("Prefs", Context.MODE_PRIVATE)

        binding= FragmentSelectCookieBinding.inflate(inflater,container,false)
        cookiearray= arrayOf(binding!!.ivCookies0,binding!!.ivCookies1,binding!!.ivCookies2,binding!!.ivCookies3,binding!!.ivCookies4)

        init()
        Util.setGlobalFont(binding!!.root)



        return binding!!.root
    }


    @SuppressLint("ClickableViewAccessibility")
    private fun init() {
        binding!!.layoutText.visibility=View.GONE
        binding!!.tvTitle.text= if(isTodaycookie) "오늘의 유령쿠키를 골라보세요." else "도움될만한 말을 쿠키에 적어놨어요.\n하나 골라볼래요?"

        var touchlistener = View.OnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    var temp= cookiearray.indexOf(v)
                    if(curselect==temp){


                    }else{
                        v.startAnimation(AnimationUtils.loadAnimation(context, R.anim.scaleup))
                        if(curselect != -1){
                            cookiearray[curselect].clearAnimation()
                        }
                        curselect=temp

                    }

                }
                MotionEvent.ACTION_UP -> {
                    var temp= cookiearray.indexOf(v)

                    var index:Int = (Math.random()*arraytext.size ).toInt()
//                    if(activity is CookiesActivity){
//                        (activity as CookiesActivity).containerChange(CookieViewFragment(arraytext[index]))
//
//                    }

                    moveToCenterAnimation(requireContext(),v,500)
                    for(i in cookiearray){
                        if(i != v){
                            i.visibility=View.INVISIBLE
                            i.setOnClickListener {  }
                        }
                    }



                    v.setOnTouchListener(null)

                    uptextlayout(arraytext[index] )

                    if(isTodaycookie){
                        val editor : SharedPreferences.Editor = prefs.edit() // 데이터 기록을 위한 editor
                        var datefomat= SimpleDateFormat("yyyy-MM-dd")
                        var curday= datefomat.format(Calendar.getInstance().time)
                        editor.putString("curday",curday).apply()

                        editor.commit()

                    }





                }
            }
            false
        }
        binding!!.ivCookies0.setOnTouchListener(touchlistener)
        binding!!.ivCookies1.setOnTouchListener(touchlistener)
        binding!!.ivCookies2.setOnTouchListener(touchlistener)
        binding!!.ivCookies3.setOnTouchListener(touchlistener)
        binding!!.ivCookies4.setOnTouchListener(touchlistener)

    }



    fun uptextlayout(text:String){
        binding!!.tvTitle.visibility=View.GONE
        binding!!.layoutText.visibility=View.VISIBLE
        binding!!.layoutText.startAnimation(AnimationUtils.loadAnimation(context, R.anim.vertical_enter))
        binding!!.tvText.text=text
        binding!!.tvCancel.setOnClickListener {
            Log.d("TAG","${cookiearray[curselect].width}    , ${cookiearray[curselect].height}")

            activity?.onBackPressed() }
    }

    override fun onDestroyView() {
        binding=null
        super.onDestroyView()
    }
    private fun moveViewToScreenCenter(view: View) {

        var layout= binding!!.cookiesParentlayout

        val pos = IntArray(2)
        val poslayout = IntArray(2)
        view.getLocationOnScreen(pos)

        layout.getLocationOnScreen(poslayout)





        view.animate()
//            .translationX((-layoutWidth).toFloat())
            .translationY(0f)
            .scaleX(1.5f).scaleY(1.5f)
            .setDuration(500)
            .setInterpolator(LinearInterpolator())

            .start()


    }
    fun moveToCenterAnimation(context: Context, view: View, duration: Long) {

        //Get position on screen
        val pos = IntArray(2)
        val poslayout = IntArray(2)
        view.getLocationOnScreen(pos)
        binding!!.cookiesParentlayout.getLocationOnScreen(poslayout)

        Log.d("TAG","${view.width}    , ${view.height}")
        Log.d("TAG","parent ${binding!!.cookiesParentlayout.width}    , ${binding!!.cookiesParentlayout.height}")
        Log.d("TAG","pose $pos   $poslayout ")

        var curlocX  = pos[0]+view.width/2
        var curlocY  = pos[1]+view.height/2

        view.pivotX=50f
        view.pivotY=50f


        var destlocX=poslayout[0] +binding!!.cookiesParentlayout.width/2f
        var destlocY=poslayout[1] +binding!!.cookiesParentlayout.height/2f

        //Get screen size
        val screenWidth: Int
        val screenHeight: Int

        screenWidth = context.getResources().getDisplayMetrics().widthPixels
        screenHeight = binding!!.cookiesParentlayout.height


        //x and y translate animators
        val xAnimator = ObjectAnimator.ofFloat(
            view,
            "translationX",
            destlocX-curlocX

        )
        val yAnimator = ObjectAnimator.ofFloat(
            view,
            "translationY",
            destlocY-curlocY
        )
        val scalex=ObjectAnimator.ofFloat(
            view,
            "scaleX",1.5f
        )
        val scaley=ObjectAnimator.ofFloat(
            view,
            "scaleY",1.5f
        )

        xAnimator.duration = duration
        yAnimator.duration = duration
        scalex.duration=duration
        scaley.duration=duration

        //Play animator
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(xAnimator, yAnimator,scalex,scaley)
        animatorSet.start()
    }


}