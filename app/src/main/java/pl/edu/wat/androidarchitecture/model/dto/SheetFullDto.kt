package pl.edu.wat.androidarchitecture.model.dto

data class SheetFullDto(
    val sheet: SheetDto,
    val expenses: List<ExpenseDto>,
    val incomes: List<IncomeDto>
)
