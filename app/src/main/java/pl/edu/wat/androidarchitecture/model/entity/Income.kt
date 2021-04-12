package pl.edu.wat.androidarchitecture.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "income")
data class Income(
    @PrimaryKey(autoGenerate = true) override val id: Long? = null,
    override val remoteId: Long? = null,
    override val sheetId: Long,
    override val sheetRemoteId: Long? = null,
    override val title: String,
    override val description: String,
    override val costs: Double
) : FinancialElement(
    remoteId = remoteId,
    sheetId = sheetId,
    sheetRemoteId = sheetRemoteId,
    title = title,
    description = description,
    costs = costs
)
