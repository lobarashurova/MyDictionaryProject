package uz.gita.mydictionaryproject.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import uz.gita.mydictionaryproject.adaptoprs.FavouriteAdaptor
import uz.gita.mydictionaryproject.databinding.ActivityFavouritesBinding
import uz.gita.mydictionaryproject.local.data.DictionaryDb

class FavouritesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavouritesBinding
    private var adapter = FavouriteAdaptor()
    private lateinit var database: DictionaryDb
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavouritesBinding.inflate(layoutInflater)

        binding.backBtn.setOnClickListener {
            startActivity(Intent(this, MainFragment::class.java))
            finish()
        }
        database = DictionaryDb(this)
        setContentView(binding.root)
        initAdapter()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initAdapter() {
        binding.rvBookmark.adapter = adapter
        binding.rvBookmark.layoutManager = LinearLayoutManager(this)
        adapter.setCursor(database.getAllFavouriteWords())
        adapter.notifyDataSetChanged()
        adapter.setChangeListener { id, value, position ->
            database.updateWord(id, value)
            adapter.setCursor(database.getAllFavouriteWords())
            showPlaceHolder(database.getAllFavouriteWords().count == 0)
            adapter.notifyItemRemoved(position)
        }
    }

    private fun showPlaceHolder(isShow: Boolean) {
        binding.placeHolder.isVisible = isShow
    }


}