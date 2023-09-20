package uz.gita.mydictionaryproject.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import uz.gita.mydictionaryproject.adaptoprs.DictionaryAdapter
import uz.gita.mydictionaryproject.databinding.FragmentMainBinding
import uz.gita.mydictionaryproject.local.data.DictionaryDb
import uz.gita.mydictionaryproject.models.DictionaryModel
import uz.gita.mydictionaryproject.pages.MeaningWordFragment

@SuppressLint("NotifyDataSetChanged")
class MainFragment :AppCompatActivity() {
    private lateinit var binding: FragmentMainBinding
    private var adapter = DictionaryAdapter()
    private lateinit var handler: Handler
    private val list = ArrayList<DictionaryModel>()
    private lateinit var database: DictionaryDb
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        database = DictionaryDb(requireContext())
        initAdapter()
        onCLick()
        attachSearch()
        onCLicked()
    }



    @SuppressLint("NotifyDataSetChanged")
    private fun initAdapter() {
        binding.list.adapter = adapter
        binding.list.layoutManager = LinearLayoutManager(this)
        adapter.setCursor(database.getAllWords())
        adapter.notifyDataSetChanged()


        adapter.setChangeListener { id, value, position ->
            database.updateWord(id, value)
            adapter.setCursor(database.getAllWords())
            adapter.notifyItemChanged(position)
        }
    }

    private fun onCLick() {
        adapter.setLongClickListener {
            val bundle = Bundle()
            val fragment = MeaningWordFragment()
            fragment.arguments = bundle
            bundle.putString("english", it.english)
            bundle.putString("transcript", it.transcript)
            bundle.putString("uzbek", it.uzbek)
            bundle.putString("countable", it.countable)
            bundle.putString("type", it.type)
            bundle.putSerializable("data", it)
//            startFragment(fragment)

        }
    }

    private fun attachSearch() {
        handler = Handler(Looper.getMainLooper())
        binding.searchEditText.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                handler.removeCallbacksAndMessages(null)
                query?.let {
                    adapter.setCursor(database.getWordByEnglish(it.trim()))
                    showPlaceHolder(database.getWordByEnglish(it.trim()).count == 0)
                    adapter.setQuery(it.trim())
                    adapter.notifyDataSetChanged()
                }
                return true
            }

            private fun showPlaceHolder(isShow: Boolean) {
                binding.placeHolder.isVisible = isShow


            }


            override fun onQueryTextChange(newText: String?): Boolean {
                handler.removeCallbacksAndMessages(null)
                handler.postDelayed({
                    newText?.let {
                        adapter.setCursor(database.getWordByEnglish(it.trim()))
                        showPlaceHolder(database.getWordByEnglish(it.trim()).count == 0)
                        adapter.setQuery(it.trim())
                        adapter.notifyDataSetChanged()
                    }
                }, 500)
                return true
            }
        })
    }

    private fun onCLicked() {
        binding.ivBookmarks.setOnClickListener {
//            startFragment(FavouritesFragment())
        }
    }
}