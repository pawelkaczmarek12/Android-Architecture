package pl.edu.wat.androidarchitecture.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import pl.edu.wat.androidarchitecture.data.repository.local.SheetDao
import pl.edu.wat.androidarchitecture.data.repository.remote.SheetApi
import pl.edu.wat.androidarchitecture.model.entity.Expense
import pl.edu.wat.androidarchitecture.model.entity.Income
import pl.edu.wat.androidarchitecture.model.entity.SheetFull
import pl.edu.wat.androidarchitecture.model.internal.Resource
import pl.edu.wat.androidarchitecture.model.internal.Status
import pl.edu.wat.androidarchitecture.model.mapper.toDto
import pl.edu.wat.androidarchitecture.model.mapper.toEntity
import javax.inject.Inject

class SyncRepository @Inject constructor(
    private val sheetDao: SheetDao,
    private val sheetApi: SheetApi
) : Repository {

    fun syncAllData(): LiveData<Resource<Boolean>> = liveData(Dispatchers.IO) {
        emit(Resource.loading())

        val allSheetsFull = sheetDao.getAllSheetsFullAsList()

        allSheetsFull.forEach { sheetFull ->
            if (sheetFull.sheet.isSynced()) {
                sheetFull.sheet.remoteId
            } else {
                syncSheet(sheetFull)
            }?.let { remoteSheetId ->
                sheetFull.expenses.forEach { expense -> syncExpense(expense, remoteSheetId) }
                sheetFull.incomes.forEach { income -> syncIncome(income, remoteSheetId) }
            }
        }

        emit(Resource.success(true))
    }

    private suspend fun syncSheet(sheetFull: SheetFull): Long? {
        val postedSheetResource = resource { sheetApi.postSheet(sheetFull.sheet.toDto()) }
        if (postedSheetResource.status == Status.SUCCESS && postedSheetResource.data != null) {
            sheetDao.updateSheet(postedSheetResource.data.sheet.toEntity(sheetFull.sheet.id))
        }
        return postedSheetResource.data?.sheet?.id

    }


    private suspend fun syncIncome(income: Income, remoteSheetId: Long) {
        val postedIncome = resource { sheetApi.postIncome(income.toDto(remoteSheetId)) }
        if (postedIncome.status == Status.SUCCESS && postedIncome.data != null) {
            sheetDao.updateIncome(postedIncome.data.toEntity(entityId = income.id, sheetId = income.sheetId))
        }
    }

    private suspend fun syncExpense(expense: Expense, remoteSheetId: Long) {
        val postedExpense = resource { sheetApi.postExpense(expense.toDto(remoteSheetId)) }
        if (postedExpense.status == Status.SUCCESS && postedExpense.data != null) {
            sheetDao.updateExpense(postedExpense.data.toEntity(entityId = expense.id, sheetId = expense.sheetId))

        }
    }

}