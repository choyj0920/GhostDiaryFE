package com.ghostdiary.ghostdiary.utilpackage

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Typeface
import android.os.Environment
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException


class Util {

    companion object{
        fun init(fontarr:ArrayList<Typeface?>,cur:Int){
            curfont =cur

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
        fun setChildColor(view:View?,color:String){
            if(view !=null){
                if(view is ViewGroup){
                    var len =view.childCount
                    for(i in 0..len-1){
                        var v=view.getChildAt(i)
                        if(v is TextView ){
                            v.setTextColor(Color.parseColor(color))
                        }else if( v is EditText){
                            v.setTextColor(Color.parseColor(color))
                        }
                        setChildColor(v,color)
                    }
                }
            }
        }

        fun getExternalFilePath(context: Context?): String? {
            return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                .toString() + "/"
        }

        fun saveImageIntoFileFromUri(
            context: Context?,
            bitmap: Bitmap,
            fileName: String?,
            path: String?
        ): File? {
            val file = File(path, fileName)
            try {
                val fileOutputStream = FileOutputStream(file)
                when (file.getAbsolutePath()
                    .substring(file.getAbsolutePath().lastIndexOf(".") + 1)) {
                    "jpeg", "jpg" -> bitmap.compress(
                        Bitmap.CompressFormat.JPEG,
                        100,
                        fileOutputStream
                    )
                    "png" -> bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
                }
                fileOutputStream.close()
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
                Log.e("Utils", "saveImageIntoFileFromUri FileNotFoundException : " + e.toString())
            } catch (e: IOException) {
                e.printStackTrace()
                Log.e("Utils", "saveImageIntoFileFromUri IOException : " + e.toString())
            }
            return file
        }

    }
}