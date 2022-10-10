package com.ghostdiary.ghostdiary

import android.os.Bundle
import android.os.Handler
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.ghostdiary.ghostdiary.databinding.ActivityMemoBinding
import com.ghostdiary.ghostdiary.fragment.analyze.SleepFragment
import com.ghostdiary.ghostdiary.fragment.memo.EditMemoFragment
import com.ghostdiary.ghostdiary.fragment.memo.MemoSelectFragment
import com.ghostdiary.ghostdiary.utilpackage.Util


class MemoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMemoBinding
    private lateinit var container:FrameLayout

    private lateinit var memoSelectFragment: MemoSelectFragment
    private lateinit var sleepFragment: SleepFragment

    companion object{

    }

    private lateinit var viewPager: ViewPager2

    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        binding=ActivityMemoBinding.inflate(layoutInflater)
        container=binding.container
        viewModel= MainActivity.mainactivity.viewModel

        var folder_index=intent.getIntExtra("memofoderindex",-1)
        if(folder_index==-1)
            finish()


        memoSelectFragment= MemoSelectFragment(this,viewModel.getMemo_FolderArray()[folder_index])
        supportFragmentManager.beginTransaction().replace(container.id,memoSelectFragment).commit()
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
        var curfragment=supportFragmentManager.fragments.get(0)
        if(curfragment is EditMemoFragment){
            if(curfragment.iseditmode){
                if(curfragment.memo ==null){
                    super.onBackPressed()

                }else{
                    curfragment.switcheditmode(false)
                }

            }else{
                super.onBackPressed()
            }
            return

        }

        super.onBackPressed()

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