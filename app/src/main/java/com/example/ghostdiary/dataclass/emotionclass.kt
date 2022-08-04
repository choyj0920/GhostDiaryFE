package com.example.ghostdiary.dataclass

class emotionclass(var text:String,var ghostimage:Int,var isactive:Boolean):Cloneable{
    public override fun clone(): emotionclass{
        return super.clone() as emotionclass
    }
}
