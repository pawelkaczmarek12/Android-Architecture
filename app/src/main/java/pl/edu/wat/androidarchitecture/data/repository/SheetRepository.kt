package pl.edu.wat.androidarchitecture.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import kotlinx.coroutines.Dispatchers
import pl.edu.wat.androidarchitecture.data.repository.local.SheetDao
import pl.edu.wat.androidarchitecture.data.repository.remote.SheetApi
import pl.edu.wat.androidarchitecture.model.entity.Expense
import pl.edu.wat.androidarchitecture.model.entity.Income
import pl.edu.wat.androidarchitecture.model.entity.Sheet
import pl.edu.wat.androidarchitecture.model.entity.SheetFull
import pl.edu.wat.androidarchitecture.model.internal.Resource
import pl.edu.wat.androidarchitecture.model.internal.Status.SUCCESS
import pl.edu.wat.androidarchitecture.model.mapper.toDto
import pl.edu.wat.androidarchitecture.model.mapper.toEntity
import timber.log.Timber
import javax.inject.Inject

class SheetRepository @Inject constructor(
    private val sheetDao: SheetDao,
    private val sheetApi: SheetApi
) : Repository {

    fun getAllSheetsFull(): LiveData<Resource<List<SheetFull>>> = liveData(Dispatchers.IO) {
        // 1. Loading
        emit(Resource.loading())
        // 2. Get data from database
        val sheetsFromDb = sheetDao.getAllSheetsFull().map { Resource.success(it) }
        emitSource(sheetsFromDb)
        // 4. Get data from API
        val resourceFromApi = resource { sheetApi.getAllSheetFull() }
        if (resourceFromApi.status == SUCCESS) {
            // 5. Sync with database
            resourceFromApi.data?.forEach { sheetFullDto ->
                val sheetByRemoteId = sheetDao.findSheetByRemoteId(sheetFullDto.sheet.id!!)
                if (sheetByRemoteId == null) {
                    sheetDao.insertSheet(sheetFullDto.sheet.toEntity())
                }
                sheetFullDto.expenses.forEach { expenseDto ->
                    val expenseByRemoteId = sheetDao.findExpenseByRemoteId(expenseDto.id!!)
                    if (expenseByRemoteId == null) {
                        sheetDao.insertExpense(expenseDto.toEntity(sheetId = sheetFullDto.sheet.id))
                    }
                    sheetFullDto.incomes.forEach { incomeDto ->
                        val incomeByRemoteId = sheetDao.findIncomeByRemoteId(incomeDto.id!!)
                        if (incomeByRemoteId == null) {
                            sheetDao.insertIncome(incomeDto.toEntity(sheetId = sheetFullDto.sheet.id))
                        }
                    }
                }
                // 6. Show synced data
                val sheetsFromDbAfterSync = sheetDao.getAllSheetsFull().map { Resource.success(it) }
                emitSource(sheetsFromDbAfterSync)
            }
        }
    }

    fun getSheetFullById(id: Long): LiveData<Resource<SheetFull>> = liveData(Dispatchers.IO) {
        emit(Resource.loading())
        val sheetFullById = sheetDao.getSheetFullById(id).map { Resource.success(it) }
        emitSource(sheetFullById)
    }

    fun saveSheet(sheet: Sheet): LiveData<Resource<Sheet>> = liveData(Dispatchers.IO) {
        emit(Resource.loading())

        val id = sheetDao.insertSheet(sheet)

        emitSource(sheetDao.getSheetFullById(id).map { Resource.success(it.sheet) })

        val resource = resource { sheetApi.postSheet(sheet.toDto()) }
        if (resource.status == SUCCESS) {
            resource.data?.let {
                val syncedSheet = it.sheet.toEntity(id)
                sheetDao.updateSheet(syncedSheet)
                emit(Resource.success(syncedSheet))
            }
        }
    }

    fun addIncome(income: Income, sheetRemoteId: Long): LiveData<Resource<Income>> = liveData(Dispatchers.IO) {
        emit(Resource.loading())

        val id = sheetDao.insertIncome(income)
        emit(Resource.success(income))

        val resource = resource { sheetApi.postIncome(income.toDto(sheetRemoteId)) }
        if (resource.status == SUCCESS) {
            resource.data?.let {
                val syncedIncome = it.toEntity(id, sheetId = income.sheetId)
                sheetDao.updateIncome(syncedIncome)
                emit(Resource.success(syncedIncome))
            }
        }
    }

    fun addExpense(expense: Expense, sheetRemoteId: Long): LiveData<Resource<Expense>> = liveData(Dispatchers.IO) {
        emit(Resource.loading())

        val id = sheetDao.insertExpense(expense)
        emit(Resource.success(expense))

        val resource = resource { sheetApi.postExpense(expense.toDto(sheetRemoteId)) }
        if (resource.status == SUCCESS) {
            resource.data?.let {
                val syncedExpense = it.toEntity(id, sheetId = expense.sheetId)
                sheetDao.updateExpense(syncedExpense)
                emit(Resource.success(syncedExpense))
            }
        }
    }

    fun deleteSheet(sheetFull: SheetFull): LiveData<Resource<Boolean>> = liveData(Dispatchers.IO) {
        emit(Resource.loading())

        sheetDao.delete(sheetFull)

        sheetFull.expenses.forEach {
            sheetDao.delete(it)
            it.remoteId?.let { remoteId ->
                val status = resource { sheetApi.deleteExpense(remoteId) }
                emit(status)
            }
        }
        sheetFull.incomes.forEach {
            sheetDao.delete(it)
            it.remoteId?.let { remoteId ->
                val status = resource { sheetApi.deleteIncome(remoteId) }
                emit(status)
            }
        }

        sheetFull.sheet.remoteId?.let { remoteId ->
            val status = resource { sheetApi.deleteSheet(remoteId) }
            emit(status)
        }
    }

    fun deleteExpense(expense: Expense): LiveData<Resource<Boolean>> = liveData(Dispatchers.IO) {
        emit(Resource.loading())

        sheetDao.delete(expense)

        expense.remoteId?.let { remoteId ->
            val status = resource { sheetApi.deleteExpense(remoteId) }
            emit(status)
        }
    }

    fun deleteIncome(income: Income): LiveData<Resource<Boolean>> = liveData(Dispatchers.IO) {
        emit(Resource.loading())

        sheetDao.delete(income)

        income.remoteId?.let { remoteId ->
            val status = resource { sheetApi.deleteIncome(remoteId) }
            emit(status)
        }
    }

}
