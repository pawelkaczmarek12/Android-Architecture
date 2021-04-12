package pl.edu.wat.androidarchitecture.ui.financial

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import pl.edu.wat.androidarchitecture.data.repository.SheetRepository
import pl.edu.wat.androidarchitecture.model.entity.Expense
import pl.edu.wat.androidarchitecture.model.entity.Income

class CreateFinancialElementViewModel @ViewModelInject constructor(
    val repository: SheetRepository
) : ViewModel() {

    fun add(expense: Expense, sheetRemoteId: Long) = repository.addExpense(expense,sheetRemoteId)

    fun add(income: Income, sheetRemoteId: Long) = repository.addIncome(income, sheetRemoteId)

}
