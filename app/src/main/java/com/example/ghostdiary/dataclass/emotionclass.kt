package com.example.ghostdiary.dataclass

class emotionclass(var text:String,var ghostimage:Int,var isactive:Boolean=true):Cloneable{
    public override fun clone(): emotionclass{
        return emotionclass(text,ghostimage,isactive)
    }

    override fun toString(): String {
        return "\t text: ${text}, ghostimage${ghostimage},isactive${isactive}"
    }

}
