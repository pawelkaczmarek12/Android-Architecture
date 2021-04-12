package pl.edu.wat.androidarchitecture.model.dto

data class ExpenseDto(
        val id: Long? = null,
        val title: String,
        val description: String,
        val costs: Double,
        val sheetId: Long
)
