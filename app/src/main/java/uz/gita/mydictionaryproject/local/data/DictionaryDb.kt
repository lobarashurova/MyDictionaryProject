package uz.gita.mydictionaryproject.local.data

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import uz.gita.mydictionaryproject.local.DBHelper
import uz.gita.mydictionaryproject.models.DictionaryModel

class DictionaryDb(context: Context) : DBHelper(context, "dictionary.db", 1) {
    private val tableName = "dictionary"
    fun getAllWords(): Cursor {
        return this.database().rawQuery("Select*from dictionary ", null)
    }
    fun getAllFavouriteWords(): Cursor {
        return this.database().rawQuery("Select*from dictionary where is_favourite==1", null)
    }

    fun updateWord(id: Int, value: Int) {
        val cv = ContentValues()
        cv.put("is_favourite", value)
        this.database().update(tableName, cv, "$tableName.id==$id", null)
    }

     fun getWordByEnglish(query: String): Cursor {
        return this.database()
            .rawQuery("Select*from $tableName where english like \'$query%\'", null)
    }
     fun getWordByUzbek(query: String): Cursor {
        return this.database()
            .rawQuery("Select*from $tableName where uzbek like \'$query%\'", null)
    }

    @SuppressLint("Range")
    fun getWordByPosition(position: Int, cursor: Cursor): DictionaryModel {
        val cursorDb = cursor ?: getAllWords()
        cursorDb.moveToPosition(position)

        return DictionaryModel(
            cursorDb.getInt(cursorDb.getColumnIndex("id")),
            cursorDb.getString(cursorDb.getColumnIndex("english")),
            cursorDb.getString(cursorDb.getColumnIndex("type")),
            cursorDb.getString(cursorDb.getColumnIndex("transcript")),
            cursorDb.getString(cursorDb.getColumnIndex("uzbek")),
            cursorDb.getString(cursorDb.getColumnIndex("countable")),
            cursorDb.getInt(cursorDb.getColumnIndex("is_favourite")),
        )
    }

}