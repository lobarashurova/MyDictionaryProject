package uz.gita.mydictionaryproject.utils

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.provider.Settings
import android.text.Spannable
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.webkit.PermissionRequest
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import uz.gita.mydictionaryproject.R

fun Fragment.finish(){
    parentFragmentManager.popBackStack()
}
fun Fragment.startFragment(fragment: Fragment){
    parentFragmentManager.beginTransaction()

//        .addToBackStack(fragment::class.java.name)
//        .replace(R.id.container_view, fragment)
//        .commit()
}



fun Context.checkPermissions(array: List<String>, blockSuccess: () -> Unit) {
    Dexter.withContext(this).withPermissions(array).withListener(object :
        MultiplePermissionsListener {
        override fun onPermissionsChecked(report: MultiplePermissionsReport) {
            if (report.areAllPermissionsGranted()) {
                blockSuccess.invoke()
            } else {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.parse("package:${this@checkPermissions.packageName}")
                startActivity(intent)
            }
        }


        override fun onPermissionRationaleShouldBeShown(
            p0: MutableList<com.karumi.dexter.listener.PermissionRequest>?,
            p1: PermissionToken?
        ) {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            intent.data = Uri.parse("package:${this@checkPermissions.packageName}")
            startActivity(intent)
        }
    }).check()




}


/**
 * Extension method to set different color for substring TextView.
 */
fun TextView.setColorOfSubstring(substring: String, color: Int) {
    try {
        val spannable = android.text.SpannableString(text)
        val start = text.indexOf(substring)
        spannable.setSpan(ForegroundColorSpan(ContextCompat.getColor(context, color)), start, start + substring.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        text = spannable
    } catch (e: Exception) {
        Log.d("ViewExtensions",  "exception in setColorOfSubstring, text=$text, substring=$substring", e)
    }
}


fun TextView.underLine() {
    paint.flags = paint.flags or Paint.UNDERLINE_TEXT_FLAG
    paint.isAntiAlias = true
}
