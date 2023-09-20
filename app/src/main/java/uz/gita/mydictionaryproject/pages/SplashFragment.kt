package uz.gita.mydictionaryproject.pages

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import uz.gita.mydictionaryproject.R
import uz.gita.mydictionaryproject.databinding.ActivitySplashBinding
import kotlin.system.exitProcess


class SplashFragment : Fragment(R.layout.activity_splash) {
    private lateinit var binding: ActivitySplashBinding
    private lateinit var builder:AlertDialog.Builder
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= ActivitySplashBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.myDrawerLayout.setOnClickListener {  }
        binding.mainLayout.btnEngUzb.setOnClickListener {
          findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
        }
        onClicks()
    }

    private fun onClicks() {
        binding.mainLayout.setting.setOnClickListener {
            toggleLeftDrawer()
        }
        binding.leftDrawer.flShare.setOnClickListener { v: View? ->
            shareProject(
                requireContext()
            )
        }
        binding.leftDrawer.flContact.setOnClickListener { v: View? ->
            gotoLink("https://t.me/astrogirll06")
        }
        builder = AlertDialog.Builder(requireContext())
        binding.leftDrawer.flExit.setOnClickListener {
            builder.setTitle("Do you want to close application?")
                .setCancelable(true)
                .setPositiveButton("Yes"
                ) { _: DialogInterface?, _: Int -> exitProcess(0) }
                .setNegativeButton("No"
                ) { dialog: DialogInterface, _: Int -> dialog.cancel() }
                .show()
        }
    }

    private fun shareProject(context: Context) {
        val packageName = context.packageName
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(
            Intent.EXTRA_TEXT,
            "Downloand now:https://t.me/laibaBlog/107$packageName"
        )
        sendIntent.type = "text/plain"
        context.startActivity(sendIntent)
    }

    private fun gotoLink(s: String) {
        val uri = Uri.parse(s)
        startActivity(Intent(Intent.ACTION_VIEW, uri))
    }
    private fun toggleLeftDrawer() {
        val drawerLayout=binding.myDrawerLayout
        val leftLayout=binding.leftDrawer.root
        if (drawerLayout.isDrawerOpen(leftLayout)) {
            drawerLayout.closeDrawer(leftLayout)
        } else {
            drawerLayout.openDrawer(leftLayout)
        }
    }
}

