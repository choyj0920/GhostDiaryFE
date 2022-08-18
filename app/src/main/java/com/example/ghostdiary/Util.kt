package com.example.ghostdiary

import android.content.Context
import android.content.res.Resources
import android.graphics.Typeface
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView

class Util {

    companion object{
        fun init(fontarr:ArrayList<Typeface?>,cur:Int){
            curfont=cur

            fontarray = fontarr

        }
        lateinit var fontarray:ArrayList<Typeface?>
        var curfont:Int=0


        fun setGlobalFont( view:View?){
            if(view !=null){
                if(view is ViewGroup){
                    var len =view.childCount
                    for(i in 0..len-1){
                        var v=view.getChildAt(i)
                        if(v is TextView ){
                            v.setTypeface(fontarray[curfont])
                        }else if( v is EditText){
                            v.setTypeface(fontarray[curfont])
                        }
                        setGlobalFont(v)
                    }
                }
            }
        }

    }
}