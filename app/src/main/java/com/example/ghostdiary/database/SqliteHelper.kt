package com.example.ghostdiary.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast
import com.example.ghostdiary.MainActivity
import com.example.ghostdiary.MainViewModel
import com.example.ghostdiary.dataclass.Day_Diary
import com.example.ghostdiary.dataclass.emotionclass
import java.text.SimpleDateFormat
import java.util.*

// SQLiteOpenHelper 상속받아 SQLite 를 사용하도록 하겠습니다.
class SqliteHelper(context: Context?, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int) : SQLiteOpenHelper(context, name, factory, version) {

    //onCreate(), onUpgrade() 두가지 메소드를 오버라이드 받아 줍시다.
    override fun onConfigure(db: SQLiteDatabase?) {
        super.onConfigure(db)
        db?.setForeignKeyConstraintsEnabled(true)
    }


    override fun onOpen(db: SQLiteDatabase) {
        super.onOpen(db)
        db.setForeignKeyConstraintsEnabled(true)
        db.execSQL("PRAGMA foreign_keys=ON")
    }

    //데이터베이스가 만들어 지지않은 상태에서만 작동합니다. 이미 만들어져 있는 상태라면 실행되지 않습니다.
    override fun onCreate(db: SQLiteDatabase?) {
        //테이블을 생성할 쿼리를 작성하여 줍시다.
        Log.d("DEBUG","db 재생성")
        db?.setForeignKeyConstraintsEnabled(true)

        db?.execSQL("PRAGMA foreign_keys=ON");

        val create1= "CREATE TABLE Diary(                " +
                "diary_id    integer  NOT NULL    PRIMARY KEY autoincrement ," +
                "date     TEXT  NOT NULL," +
                "image TEXT  ," +
                "text TEXT  ," +
                "sleepstart TEXT ," +
                "sleepend    TEXT );"
        db?.execSQL(create1)

        val create2="CREATE TABLE emotions(" +
                " emotion_id  integer NOT NULL PRIMARY KEY autoincrement," +
                "diary_id    integer ," +
                "category    integer NOT NULL," +
                "ghost_num   integer NOT NULL," +
                "isactive integer NOT NULL," +
                "text   TEXT ," +
                "FOREIGN KEY(diary_id) REFERENCES Diary(diary_id) ON DELETE CASCADE ON UPDATE CASCADE);"
        db?.execSQL(create2)

    }
    fun createdb(){
        var db =writableDatabase
        db?.setForeignKeyConstraintsEnabled(true)


        db?.execSQL("DROP TABLE IF EXISTS emotions;")
        db?.execSQL("DROP TABLE IF EXISTS Diary;")

        val create1= "CREATE TABLE Diary(                " +
                "diary_id    integer  NOT NULL    PRIMARY KEY autoincrement ," +
                "date     TEXT  NOT NULL," +
                "image TEXT  ," +
                "text TEXT  ," +
                "sleepstart TEXT ," +
                "sleepend    TEXT );"
        db?.execSQL(create1)

        val create2="CREATE TABLE emotions(" +
                " emotion_id  integer NOT NULL PRIMARY KEY autoincrement," +
                "diary_id    integer ," +
                "category    integer NOT NULL," +
                "ghost_num   integer NOT NULL," +
                "isactive integer NOT NULL," +
                "text   TEXT ," +
                "FOREIGN KEY(diary_id) REFERENCES Diary(diary_id) ON DELETE CASCADE ON UPDATE CASCADE);"
        db?.execSQL(create2)

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    //insert 메소드
    fun insertDiary(diary:Day_Diary){
        //있엇으면 삭제
        deleteDiary(diary.date)

        val values = ContentValues()
        //넘겨줄 컬럼의 매개변수 지정
        var formatDate = SimpleDateFormat("yyyy-MM-dd");
        var formatDatetime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


        values.put("date",formatDate.format(diary.date))
        values.put("image",diary.image)
        values.put("text",diary.text)
        if(diary.sleepstart==null || diary.sleepend==null){
            values.put("sleepstart", "")
            values.put("sleepend", "")
        }else {
            values.put("sleepstart", formatDatetime.format(diary.sleepstart))
            values.put("sleepend", formatDatetime.format(diary.sleepend))
        }

        //쓰기나 수정이 가능한 데이터베이스 변수
        val wd = writableDatabase
        var result =wd.insert("Diary",null,values).toInt()
        //사용이 끝나면 반드시 close()를 사용하여 메모리누수 가 되지않도록 합시다.

        if(result != -1){
            var temp=ContentValues()
            temp.put("diary_id",result)
            temp.put("category",0)
            temp.put("ghost_num",diary.today_emotion.ghostimage)
            temp.put("text",diary.today_emotion.text ?: "")
            diary.today_emotion
            Log.d("TAG"," today emotion ${diary.today_emotion}" )
            wd.insert("emotions",null,temp)

            diary.whom?.let {
                for (i in it){
                    var temp=ContentValues()
                    temp.put("diary_id",result)
                    temp.put("category",1)
                    temp.put("ghost_num",i.ghostimage)
                    temp.put("isactive", if(i.isactive) 1 else 0)
                    temp.put("text",i.text)
                    Log.d("TAG","emotion1 ${i}" )

                    wd.insert("emotions",null,temp)
                }
            }
            diary.doing?.let {
                for (i in it){
                    var temp=ContentValues()
                    temp.put("diary_id",result)
                    temp.put("category",2)
                    temp.put("ghost_num",i.ghostimage)
                    temp.put("isactive", if(i.isactive) 1 else 0)
                    temp.put("text",i.text)
                    wd.insert("emotions",null,temp)
                    Log.d("TAG","emotion2 ${i}" )

                }
            }
            diary.where?.let {
                for (i in it){
                    var temp=ContentValues()
                    temp.put("diary_id",result)
                    temp.put("category",3)
                    temp.put("ghost_num",i.ghostimage)
                    temp.put("isactive", if(i.isactive) 1 else 0)
                    temp.put("text",i.text)
                    wd.insert("emotions",null,temp)
                    Log.d("TAG","emotion3 ${i}" )

                }
            }
            diary.weather?.let {
                for (i in it){
                    var temp=ContentValues()
                    temp.put("diary_id",result)
                    temp.put("category",4)
                    temp.put("ghost_num",i.ghostimage)
                    temp.put("isactive", if(i.isactive) 1 else 0)
                    temp.put("text",i.text)
                    wd.insert("emotions",null,temp)
                }
            }


        }


        wd.close()
    }

    //select 메소드
    @SuppressLint("Range")
    fun selectDiary():HashMap<String, Day_Diary> {
        val list = hashMapOf<String,Day_Diary>()
        //전체조회
        val selectAll_diary = "select * from Diary"
        val selectAll_emotions = "select * from emotions where diary_id = "
        //읽기전용 데이터베이스 변수
        val rd = readableDatabase
        //데이터를 받아 줍니다.
        val cursor = rd.rawQuery(selectAll_diary,null)

        var formatDate = SimpleDateFormat("yyyy-MM-dd");
        var formatDatetime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


        //반복문을 사용하여 list 에 데이터를 넘겨 줍시다.
        while(cursor.moveToNext()){

            val id = cursor.getLong(cursor.getColumnIndex("diary_id"))
            var strdate = cursor.getString(cursor.getColumnIndex("date"))
            var date:Date = formatDate.parse(strdate)
            var diary:Day_Diary= Day_Diary(date = date)
            diary.whom= arrayListOf()
            diary.where= arrayListOf()
            diary.doing= arrayListOf()
            diary.weather= arrayListOf()

            diary.image = cursor.getString(cursor.getColumnIndex("image"))

            diary.text = cursor.getString(cursor.getColumnIndex("text"))?: ""
            var strStart=cursor.getString(cursor.getColumnIndex("sleepstart"))
            var strEnd=cursor.getString(cursor.getColumnIndex("sleepend"))
            if(strStart=="" ||strEnd ==""){

            }else{
                diary.sleepstart = formatDatetime.parse(strStart)
                diary.sleepend = formatDatetime.parse(strEnd)
            }



            val emotioncursor=rd.rawQuery(selectAll_emotions+id.toString()+";",null)
            Log.e("TAG","현재 다이어리 id ${id} ,${date} , ${diary.image}, ${diary.text} ,${diary.sleepstart},${diary.sleepend}")
            while(emotioncursor.moveToNext()){
                cursor_emotion(emotioncursor,diary)
            }

            list.put(strdate,diary)

        }
        cursor.close()
        rd.close()

        return list
    }

    @SuppressLint("Range")
    fun cursor_emotion(cursor: Cursor,Diary:Day_Diary){


        try {
            val diary_id = cursor.getLong(cursor.getColumnIndex("diary_id")).toInt()
            var category = cursor.getLong(cursor.getColumnIndex("category")).toInt()
            val ghost_num = cursor.getString(cursor.getColumnIndex("ghost_num")).toInt()
            val isactive = cursor.getString(cursor.getColumnIndex("isactive")).toInt()==1
            val text = cursor.getString(cursor.getColumnIndex("text"))

            val emotion =emotionclass(text,ghost_num,isactive)
            when(category){
                0->Diary.today_emotion=emotion
                1->Diary.whom!!.add(emotion)
                2->Diary.doing!!.add(emotion)
                3->Diary.where!!.add(emotion)
                4->Diary.weather!!.add(emotion)
                else->{

                }
            }

        }catch (e:Exception){
            Log.e("ERROR","오류로 db가 초기화 되었습니다.")
            Toast.makeText(MainActivity.mainactivity,"기존 db테이블과 충돌으로 db가 초기화됩니다.",Toast.LENGTH_SHORT)
            createdb()

        }


    }

    //update 메소드
    fun updateMemo(){
        val values = ContentValues()

        values.put("content",1)
        values.put("datetime",1)

        val wd = writableDatabase
        wd.update("memo",values,"id=${1}}",null)
        wd.close()

    }

    //delete 메소드
    @SuppressLint("Range")
    fun deleteDiary(date:Date){
        var formatDate = SimpleDateFormat("yyyy-MM-dd");
        var strdate=formatDate.format(date)
        val selectAll_diary = "select * from Diary where date =${strdate}"
        val selectAll_emotions = "select * from emotions where diary_id = "
        //읽기전용 데이터베이스 변수
        val db = writableDatabase
        //데이터를 받아 줍니다.
        val cursor = db.rawQuery(selectAll_diary,null)


        //반복문을 사용하여 list 에 데이터를 넘겨 줍시다.
        while(cursor.moveToNext()){
            val id = cursor.getLong(cursor.getColumnIndex("diary_id")).toInt()

            db.delete("emotions","diary_id = ${id}",null)

        }
        db.delete("Diary","date = \"${strdate}\"",null)
        db.close()
    }

}