package pl.edu.wat.androidarchitecture.ui.sheets

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import pl.edu.wat.androidarchitecture.data.repository.SheetRepository
import pl.edu.wat.androidarchitecture.data.repository.SyncRepository
import pl.edu.wat.androidarchitecture.model.entity.SheetFull
import pl.edu.wat.androidarchitecture.model.internal.Resource

class SheetsViewModel @ViewModelInject constructor(
    private val repository: SheetRepository,
    private val syncRepository: SyncRepository
) : ViewModel() {

    val sheets = getDataAndSync()


    fun delete(sheetFull: SheetFull) = repository.deleteSheet(sheetFull)

    private fun getDataAndSync(): LiveData<Resource<List<SheetFull>>> {
        syncRepository.syncAllData()
        return repository.getAllSheetsFull()
    }
}
