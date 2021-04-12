package pl.edu.wat.androidarchitecture.model.entity

import androidx.room.Ignore

open class FinancialElement(
    @Ignore open val id: Long? = null,
    @Ignore open val remoteId: Long? = null,
    @Ignore open val sheetId: Long,
    @Ignore open val sheetRemoteId: Long? = null,
    @Ignore open val title: String,
    @Ignore open val description: String,
    @Ignore open val costs: Double
) {

    fun isSynced() = remoteId != null

}
