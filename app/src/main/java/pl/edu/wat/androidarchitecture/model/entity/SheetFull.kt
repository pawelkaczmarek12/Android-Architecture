package pl.edu.wat.androidarchitecture.model.entity

import androidx.room.Embedded
import androidx.room.Relation

data class SheetFull(

    @Embedded val sheet: Sheet,

    @Relation(
        parentColumn = "id",
        entityColumn = "sheetId",
        entity = Expense::class
    )
    val expenses: List<Expense>,

    @Relation(
        parentColumn = "id",
        entityColumn = "sheetId",
        entity = Income::class
    )
    val incomes: List<Income>

)
