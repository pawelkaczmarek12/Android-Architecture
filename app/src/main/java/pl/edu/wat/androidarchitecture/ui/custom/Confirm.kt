package pl.edu.wat.androidarchitecture.ui.custom

import android.app.Activity
import androidx.appcompat.app.AlertDialog

object Confirm {

    fun delete(activity: Activity, actionOnDelete: () -> Unit) {
        AlertDialog.Builder(activity)
            .apply {
                setMessage("Are you sure you want to delete?")
                setPositiveButton("Delete") { dialog, _ ->
                    actionOnDelete()
                    dialog.dismiss()
                }
                setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
            }
            .create()
            .show()
    }
}
