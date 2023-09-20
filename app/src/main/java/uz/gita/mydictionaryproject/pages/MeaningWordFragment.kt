package uz.gita.mydictionaryproject.pages

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import uz.gita.mydictionaryproject.R
import uz.gita.mydictionaryproject.databinding.DialogWordBinding
import uz.gita.mydictionaryproject.local.data.DictionaryDb
import uz.gita.mydictionaryproject.models.DictionaryModel
import uz.gita.mydictionaryproject.utils.underLine
import java.util.Locale

class MeaningWordFragment : Fragment(R.layout.dialog_word), TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = null
    private var isFav = 0
    private lateinit var database: DictionaryDb
    private lateinit var binding: DialogWordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = DictionaryDb(requireContext())
    }

    //    private var isFav = false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DialogWordBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val args = this.arguments
        val data = args?.getSerializable("data") as DictionaryModel
        binding.engWord.text = data.english
        binding.uzbWord.text = data.uzbek
        binding.transcription.text = data.transcript
        binding.countableText.text = data.countable
        binding.type.text = data.type
        isFav = data.isFavourite
        binding.type.underLine()

        binding.ivBookmark.setImageResource(if (isFav == 1) R.drawable.baseline_save_242 else R.drawable.baseline_save_24)

        binding.ivBookmark.setOnClickListener {
            isFav = if (isFav==1){
                binding.ivBookmark.setImageResource(R.drawable.baseline_save_24)
                0
            } else{
                binding.ivBookmark.setImageResource(R.drawable.baseline_save_242)
                1
            }
            database.updateWord(data.id,isFav)
        }
        binding.backs.setOnClickListener {
            findNavController().popBackStack()
        }

        tts = TextToSpeech(requireContext(), this)

        binding.voiceBtn.setOnClickListener {
            speakOut()
        }
        binding.ivShare.setOnClickListener {
            shareProject(requireContext())
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts!!.setLanguage(Locale.US)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.d("TTS", "The Language not supported!")
            } else {
                binding.voiceBtn.isEnabled = true
            }
        }
    }

    private fun speakOut() {
        val text = binding.engWord.text.toString()
        tts!!.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }


    override fun onDestroy() {
        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }
        super.onDestroy()
    }

    private fun shareProject(context: Context) {
        val packageName = context.packageName
        val sendIntent = Intent()
        val args = this.arguments
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(
            Intent.EXTRA_TEXT,
            "${args?.getString("english").toString()}  ---> ${args?.getString("uzbek").toString()}" +
                    "" +
                    "\n \nDownloand now:https://t.me/laibaBlog/107"
        )
        sendIntent.type = "text/plain"
        context.startActivity(sendIntent)
    }
}