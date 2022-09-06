package com.example.ghostdiary

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.ghostdiary.databinding.ActivityCookiesBinding
import com.example.ghostdiary.fragment.memo.EditMemoFragment
import com.example.ghostdiary.utilpackage.Util


class CookiesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCookiesBinding
    private lateinit var container:FrameLayout

    companion object{

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature( Window.FEATURE_NO_TITLE );


        val layoutParams = WindowManager.LayoutParams()
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
        layoutParams.dimAmount = 0.7f
        window.attributes = layoutParams

        val dm = applicationContext.resources.displayMetrics

        val height = (dm.heightPixels * 0.9).toInt() // Display 사이즈의 90%

        window.attributes.gravity=Gravity.BOTTOM


        window.attributes.height = height

        binding=ActivityCookiesBinding.inflate(layoutInflater)
        container=binding.container


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





    override fun onStart() {
        super.onStart()

    }

    override fun onDestroy() {
        super.onDestroy()
    }


    override fun onBackPressed() {
        super.onBackPressed()
        return
        var curfragment=supportFragmentManager.fragments.get(0)
        if(curfragment is EditMemoFragment){

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