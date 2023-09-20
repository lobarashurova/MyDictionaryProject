package uz.gita.mydictionaryproject.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import uz.gita.mydictionaryproject.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}