package pl.edu.wat.androidarchitecture.ui.custom

import com.google.android.material.textfield.TextInputLayout
import com.wajahatkarim3.easyvalidation.core.view_ktx.nonEmpty

fun nonEmptyList(vararg inputLayouts: TextInputLayout, callback: (view: TextInputLayout, message: String) -> Unit): Boolean {
    var result = true

    for (inputLayout in inputLayouts) {
        inputLayout.error = null

        result = result and inputLayout.editText!!.nonEmpty {
            callback.invoke(inputLayout, it)
        }
    }

    return result
}
