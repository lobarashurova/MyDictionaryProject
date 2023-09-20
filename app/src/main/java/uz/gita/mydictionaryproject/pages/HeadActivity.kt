package uz.gita.mydictionaryproject.pages

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import uz.gita.mydictionaryproject.R
import uz.gita.mydictionaryproject.databinding.ActivityHeadBinding

class HeadActivity : AppCompatActivity() {
    private lateinit var  binding:ActivityHeadBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        application.setTheme(R.style.Base_Theme_MyDictionaryProject)
        super.onCreate(savedInstanceState)
        binding= ActivityHeadBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}