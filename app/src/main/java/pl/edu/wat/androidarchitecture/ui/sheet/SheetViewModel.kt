package pl.edu.wat.androidarchitecture.ui.sheet

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import pl.edu.wat.androidarchitecture.data.repository.SheetRepository
import pl.edu.wat.androidarchitecture.model.entity.*
import pl.edu.wat.androidarchitecture.model.internal.Resource

class SheetViewModel @ViewModelInject constructor(
    val repository: SheetRepository
) : ViewModel() {

    private val _sheetId = MutableLiveData<Long>()
    private val _sheetFull = _sheetId.switchMap { id -> repository.getSheetFullById(id) }

    val sheetFull: LiveData<Resource<SheetFull>> = _sheetFull

    fun initWithId(id: Long) {
        _sheetId.value = id
    }


    fun delete(financialElement: FinancialElement): LiveData<Resource<Boolean>> {
        return if (financialElement is Income) {
            repository.deleteIncome(financialElement)
        } else {
            repository.deleteExpense(financialElement as Expense)
        }
    }

}
