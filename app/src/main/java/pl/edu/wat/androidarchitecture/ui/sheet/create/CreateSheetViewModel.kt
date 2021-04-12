package pl.edu.wat.androidarchitecture.ui.sheet.create

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import pl.edu.wat.androidarchitecture.data.repository.SheetRepository
import pl.edu.wat.androidarchitecture.model.entity.Sheet

class           CreateSheetViewModel @ViewModelInject constructor(
    val repository: SheetRepository
) : ViewModel() {

    fun create(sheet: Sheet) = repository.saveSheet(sheet)

}
