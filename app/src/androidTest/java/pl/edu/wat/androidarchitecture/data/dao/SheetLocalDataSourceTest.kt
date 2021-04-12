package pl.edu.wat.androidarchitecture.data.dao

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import pl.edu.wat.androidarchitecture.blockingObserve
import pl.edu.wat.androidarchitecture.data.config.ApplicationDatabase
import pl.edu.wat.androidarchitecture.model.entity.Expense
import pl.edu.wat.androidarchitecture.model.entity.Income
import pl.edu.wat.androidarchitecture.model.entity.Sheet
import java.util.concurrent.Executors

@RunWith(AndroidJUnit4::class)
class SheetLocalDataSourceTest {

    private lateinit var db: ApplicationDatabase

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() {
        db = Room
            .inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), ApplicationDatabase::class.java)
            .setTransactionExecutor(Executors.newSingleThreadExecutor())
            .build()
    }

    @Test
    fun insertSheetTest() = runBlocking {
        val sheet = Sheet(title = "Example 1", description = "Description 1")

        val sheetId = db.sheetDao().insertSheet(sheet)

        assertNotNull(sheetId)
        assertTrue(sheetId > 0)
    }

    @Test
    fun insertExpenseTest() = runBlocking {
        val sheet = Sheet(title = "Example 1", description = "Description 1")
        val sheetId = db.sheetDao().insertSheet(sheet)
        val expense = Expense(sheetId = sheetId, title = "Title 1", description = "Description 1", costs = 12.0)

        val expenseId = db.sheetDao().insertExpense(expense)

        assertNotNull(expenseId)
    }

    @Test
    fun insertIncomeTest() = runBlocking {
        val sheet = Sheet(title = "Example 1", description = "Description 1")
        val sheetId = db.sheetDao().insertSheet(sheet)
        val income = Income(sheetId = sheetId, title = "Title 1", description = "Description 1", costs = 12.0)

        val incomeId = db.sheetDao().insertIncome(income)

        assertNotNull(incomeId)
    }

    @Test
    fun findAllSheetsTest() = runBlocking {
        val sheet = Sheet(title = "Example 1", description = "Description 1")
        db.sheetDao().insertSheet(sheet)

        val allSheets = db.sheetDao().getAllSheets().blockingObserve()

        assertNotNull(allSheets)
        assertTrue(allSheets!!.isNotEmpty())
    }

    @Test
    fun findAllSheetsFullTest() = runBlocking {
        val sheet = Sheet(title = "Example 1", description = "Description 1")
        val sheetId = db.sheetDao().insertSheet(sheet)
        db.sheetDao().insertExpense(Expense(sheetId = sheetId, title = "Title 1", description = "Description 1", costs = 12.0))
        db.sheetDao().insertIncome(Income(sheetId = sheetId, title = "Title 2", description = "Description 2", costs = 23.0))

        val sheetFullList = db.sheetDao().getAllSheetsFull().blockingObserve()

        assertNotNull(sheetFullList)
        assertTrue(sheetFullList!!.isNotEmpty())
    }

    @After
    fun closeDb() {
        db.close()
    }

}
