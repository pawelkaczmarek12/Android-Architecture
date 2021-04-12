package pl.edu.wat.androidarchitecture.model.mapper

import pl.edu.wat.androidarchitecture.model.dto.ExpenseDto
import pl.edu.wat.androidarchitecture.model.dto.IncomeDto
import pl.edu.wat.androidarchitecture.model.dto.SheetDto
import pl.edu.wat.androidarchitecture.model.dto.SheetFullDto
import pl.edu.wat.androidarchitecture.model.entity.Expense
import pl.edu.wat.androidarchitecture.model.entity.Income
import pl.edu.wat.androidarchitecture.model.entity.Sheet
import pl.edu.wat.androidarchitecture.model.entity.SheetFull

fun Sheet.toDto() = SheetDto(
    id = remoteId,
    title = title,
    description = description,
)

fun SheetDto.toEntity(entityId: Long? = null) = Sheet(
    id = entityId,
    remoteId = this.id,
    title = title,
    description = description
)

fun Income.toDto(sheetRemoteId: Long) = IncomeDto(
    id = this.id,
    title = this.title,
    description = this.description,
    costs = this.costs,
    sheetId = sheetRemoteId
)

fun IncomeDto.toEntity(entityId: Long? = null, sheetId: Long) = Income(
    id = entityId,
    remoteId = this.id,
    sheetRemoteId =  this.sheetId,
    sheetId = sheetId,
    title = this.title,
    description = this.description,
    costs = this.costs
)

fun Expense.toDto(sheetRemoteId: Long) = ExpenseDto(
    title = this.title,
    description = this.description,
    costs = this.costs,
    sheetId = sheetRemoteId
)

fun ExpenseDto.toEntity(entityId: Long? = null, sheetId: Long) = Expense(
    id = entityId,
    remoteId = this.id,
    sheetRemoteId =  this.sheetId,
    sheetId = sheetId,
    title = this.title,
    description = this.description,
    costs = this.costs
)
