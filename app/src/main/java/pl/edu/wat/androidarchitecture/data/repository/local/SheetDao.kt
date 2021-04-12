package pl.edu.wat.androidarchitecture.data.repository.local

import androidx.lifecycle.LiveData
import androidx.room.*
import pl.edu.wat.androidarchitecture.model.entity.Expense
import pl.edu.wat.androidarchitecture.model.entity.Income
import pl.edu.wat.androidarchitecture.model.entity.Sheet
import pl.edu.wat.androidarchitecture.model.entity.SheetFull

@Dao
interface SheetDao {

    @Transaction
    @Query("SELECT * FROM Sheet")
    fun getAllSheetsFull(): LiveData<List<SheetFull>>

    @Transaction
    @Query("SELECT * FROM Sheet")
    suspend fun getAllSheetsFullAsList(): List<SheetFull>

    @Query("SELECT * FROM Sheet")
    fun getAllSheets(): LiveData<List<Sheet>>

    @Query("SELECT * FROM Sheet WHERE remoteId = :remoteId")
    suspend fun findSheetByRemoteId(remoteId: Long): Sheet?

    @Query("SELECT * FROM Expense WHERE remoteId = :remoteId")
    suspend fun findExpenseByRemoteId(remoteId: Long): Expense?

    @Query("SELECT * FROM Income WHERE remoteId = :remoteId")
    suspend fun findIncomeByRemoteId(remoteId: Long): Income?

    @Transaction
    @Query("SELECT * FROM Sheet WHERE id = :id")
    fun getSheetFullById(id: Long): LiveData<SheetFull>

    @Query("SELECT * FROM Sheet WHERE id = :id")
    suspend fun getSheetById(id: Long): Sheet

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateSheet(sheet: Sheet)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSheet(sheet: Sheet): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateIncome(income: Income)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIncome(income: Income): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateExpense(expense: Expense)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: Expense): Long

    @Transaction
    suspend fun delete(sheetFull: SheetFull) {
        sheetFull.expenses.forEach { delete(it) }
        sheetFull.incomes.forEach { delete(it) }
        delete(sheetFull.sheet)
    }

    @Delete
    suspend fun delete(sheet: Sheet)

    @Delete
    suspend fun delete(expense: Expense)

    @Delete
    suspend fun delete(income: Income)

}
