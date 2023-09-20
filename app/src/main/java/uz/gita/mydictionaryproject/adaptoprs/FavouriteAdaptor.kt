package uz.gita.mydictionaryproject.adaptoprs

import android.annotation.SuppressLint
import android.database.Cursor
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.gita.mydictionaryproject.R
import uz.gita.mydictionaryproject.databinding.ItemWordBinding
import uz.gita.mydictionaryproject.models.DictionaryModel

class FavouriteAdaptor : RecyclerView.Adapter<FavouriteAdaptor.ViewHolder>() {
    private lateinit var cursor: Cursor
    private var setOnItemChangeListener: ((Int, Int, Int) -> Unit)? = null
    private var longClickListener: ((DictionaryModel) -> Unit)? = null


    fun setCursor(cursor: Cursor) {
        this.cursor = cursor
    }


    fun setChangeListener(listener: (Int, Int, Int) -> Unit) {
        setOnItemChangeListener = listener
    }

    fun setLongClickListener(listener: (DictionaryModel) -> Unit) {
        longClickListener = listener
    }


    inner class ViewHolder(val binding: ItemWordBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.bookmarkImg.setOnClickListener {
                val data = getWordByPosition(adapterPosition)
                if (data.isFavourite == 0) {
                    setOnItemChangeListener?.invoke(data.id, 1, adapterPosition)
                } else {
                    setOnItemChangeListener?.invoke(data.id, 0, adapterPosition)
                }
            }
            binding.root.setOnClickListener {
                val data = getWordByPosition(adapterPosition)
                longClickListener?.invoke(data)
            }

        }

        fun bind(data: DictionaryModel) {
            binding.apply {
                engWord.text = data.english
                engWord.text = data.english
                typeTxt.text = data.type
                if (data.isFavourite == 0) {
                    bookmarkImg.setImageResource(R.drawable.baseline_save_24)
                } else {
                    bookmarkImg.setImageResource(R.drawable.baseline_save_242)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemWordBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = cursor.count

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getWordByPosition(position))
    }

    private fun getWordByPosition(position: Int): DictionaryModel {
        cursor.moveToPosition(position)
        @SuppressLint("Range")
        val dictionaryModel = DictionaryModel(
            cursor.getInt(cursor.getColumnIndex("id")),
            cursor.getString(cursor.getColumnIndex("english")),
            cursor.getString(cursor.getColumnIndex("type")),
            cursor.getString(cursor.getColumnIndex("transcript")),
            cursor.getString(cursor.getColumnIndex("uzbek")),
            cursor.getString(cursor.getColumnIndex("countable")),
            cursor.getInt(cursor.getColumnIndex("is_favourite")),
        )
        Log.d("TTT", "getWordByPosition:$dictionaryModel ")
        return dictionaryModel
    }
}