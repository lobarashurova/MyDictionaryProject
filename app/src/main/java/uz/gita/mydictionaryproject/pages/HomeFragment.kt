package uz.gita.mydictionaryproject.pages

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import uz.gita.mydictionaryproject.R
import uz.gita.mydictionaryproject.adaptoprs.DictionaryAdapter
import uz.gita.mydictionaryproject.databinding.FragmentMainBinding
import uz.gita.mydictionaryproject.local.data.DictionaryDb
import java.util.Locale
import java.util.Objects

@SuppressLint("NotifyDataSetChanged")
class HomeFragment : Fragment(R.layout.fragment_main) {
    private lateinit var binding: FragmentMainBinding
    private var adapter = DictionaryAdapter()
    private lateinit var handler: Handler
    private lateinit var database: DictionaryDb
    private lateinit var speechRecognizer: SpeechRecognizer
    private val REQUEST_CODE_SPEECH_INPUT = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        database = DictionaryDb(requireContext())
        initAdapter()
        onCLick()
        attachSearch()
        onCLicked()
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(requireContext())
        speechRecognizer()
    }


    private fun initAdapter() {
        binding.list.adapter = adapter
        binding.list.layoutManager = LinearLayoutManager(context)
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
            bundle.putString("english", it.english)
            bundle.putString("transcript", it.transcript)
            bundle.putString("uzbek", it.uzbek)
            bundle.putString("countable", it.countable)
            bundle.putString("type", it.type)
            bundle.putInt("fav", it.isFavourite)
            bundle.putSerializable("data", it)
            findNavController().navigate(R.id.action_homeFragment_to_meaningWrodFragment, bundle)
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

    private fun showPlaceHolder(isShow: Boolean) {
        binding.placeHolder.isVisible = isShow


    }

    private fun onCLicked() {
        binding.ivBookmarks.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_favouritesFragment)
        }
        binding.backs.setOnClickListener {
            findNavController().popBackStack()
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    private fun speechRecognizer() {
        binding.voiceIcon.setOnClickListener {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)

            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )

            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE,
                Locale.getDefault()
            )

            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text")

            try {
                startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT)
            } catch (e: Exception) {
                Toast.makeText(
                    requireContext(), " " + e.message,
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_SPEECH_INPUT) {
            if (resultCode == AppCompatActivity.RESULT_OK && data != null) {

                val res: ArrayList<String> =
                    data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS) as ArrayList<String>

                binding.searchEditText.setQuery(Objects.requireNonNull(res)[0], true)
            }
        }
    }


}