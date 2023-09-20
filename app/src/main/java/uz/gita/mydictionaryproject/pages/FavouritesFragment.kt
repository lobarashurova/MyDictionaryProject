package uz.gita.mydictionaryproject.pages

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import uz.gita.mydictionaryproject.R
import uz.gita.mydictionaryproject.adaptoprs.FavouriteAdaptor
import uz.gita.mydictionaryproject.databinding.ActivityFavouritesBinding
import uz.gita.mydictionaryproject.local.data.DictionaryDb


class FavouritesFragment:Fragment(R.layout.activity_favourites) {
    private lateinit var binding: ActivityFavouritesBinding
    private var adapter = FavouriteAdaptor()
    private lateinit var database: DictionaryDb
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= ActivityFavouritesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        database = DictionaryDb(view.context)
        initAdapter()
        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initAdapter() {
        binding.rvBookmark.adapter = adapter
        binding.rvBookmark.layoutManager = LinearLayoutManager(context)
        adapter.setCursor(database.getAllFavouriteWords())
        adapter.notifyDataSetChanged()
        adapter.setChangeListener { id, value, position ->
            database.updateWord(id, value)
            adapter.setCursor(database.getAllFavouriteWords())
            showPlaceHolder(database.getAllFavouriteWords().count == 0)
            adapter.notifyItemRemoved(position)
        }
        adapter.setLongClickListener {
            val bundle=Bundle()
            bundle.putSerializable("data",it)
            Log.d("TTT", "initAdapter:$it ")
            findNavController().navigate(R.id.action_favouritesFragment_to_meaningWrodFragment,bundle)

        }
    }

    private fun showPlaceHolder(isShow: Boolean) {
        binding.placeHolder.isVisible = isShow
    }
}