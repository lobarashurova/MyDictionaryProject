package uz.gita.mydictionaryproject.models
import java.io.Serializable

data class DictionaryModel(
    val id:Int,
    val english:String,
    val type:String,
    val transcript:String,
    val uzbek:String,
    val countable:String,
    val isFavourite:Int
):Serializable