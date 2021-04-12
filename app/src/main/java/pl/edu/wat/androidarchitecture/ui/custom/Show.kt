package pl.edu.wat.androidarchitecture.ui.custom

import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import pl.edu.wat.androidarchitecture.R

object Show {

    fun error(view: View, text: String?) {
        val snackbar = Snackbar.make(view, text ?: "Undefined error", Snackbar.LENGTH_INDEFINITE)
        snackbar.view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.red_dark2))
        snackbar.setAction("Ok") { snackbar.dismiss() }
        snackbar.show()
    }
    fun info(view: View, text: String) {
        val snackbar = Snackbar.make(view, text, Snackbar.LENGTH_INDEFINITE)
        snackbar.view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.colorPrimaryDark))
        snackbar.setAction("Ok") { snackbar.dismiss() }
        snackbar.show()
    }

}