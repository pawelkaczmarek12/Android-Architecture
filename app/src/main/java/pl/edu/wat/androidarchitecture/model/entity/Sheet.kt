package pl.edu.wat.androidarchitecture.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sheet")
data class Sheet(
    @PrimaryKey(autoGenerate = true) val id: Long? = null,
    val remoteId: Long? = null,
    val title: String,
    val description: String
) {

    fun isSynced() = remoteId != null

}
