package uz.gita.mydictionaryproject.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import uz.gita.mydictionaryproject.databinding.DialogWordBinding
import uz.gita.mydictionaryproject.models.DictionaryModel

class WordItem:AppCompatActivity() {
    private var binding:DialogWordBinding?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogWordBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        binding!!.engWord.text=intent.getStringExtra("english")
        binding!!.uzbWord.text=intent.getStringExtra("uzbek")
        binding!!.transcription.text=intent.getStringExtra("transcript")
        binding!!.countableText.text=intent.getStringExtra("countable")
        binding!!.type.text=intent.getStringExtra("type")
        val data=intent.getSerializableExtra("data") as DictionaryModel
        binding!!.engWord.text=data.english


    }
}