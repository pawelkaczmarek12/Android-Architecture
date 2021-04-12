package pl.edu.wat.androidarchitecture.data.repository.remote

import pl.edu.wat.androidarchitecture.model.dto.ExpenseDto
import pl.edu.wat.androidarchitecture.model.dto.IncomeDto
import pl.edu.wat.androidarchitecture.model.dto.SheetDto
import pl.edu.wat.androidarchitecture.model.dto.SheetFullDto
import retrofit2.Response
import retrofit2.http.*

interface SheetApi {

    @GET("sheet")
    suspend fun getAllSheetFull(): Response<List<SheetFullDto>>

    @POST("sheet")
    suspend fun postSheet(@Body sheetDto: SheetDto): Response<SheetFullDto>

    @POST("expense")
    suspend fun postExpense(@Body expenseDto: ExpenseDto): Response<ExpenseDto>

    @POST("income")
    suspend fun postIncome(@Body incomeDto: IncomeDto): Response<IncomeDto>

    @DELETE("sheet")
    suspend fun deleteSheet(@Query("sheet-id") sheetId: Long): Response<Boolean>

    @DELETE("expense")
    suspend fun deleteExpense(@Query("expense-id") expenseId: Long): Response<Boolean>

    @DELETE("income")
    suspend fun deleteIncome(@Query("income-id") incomeId: Long): Response<Boolean>

}
