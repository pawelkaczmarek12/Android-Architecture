package pl.edu.wat.androidarchitecture.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import pl.edu.wat.androidarchitecture.data.repository.SyncRepository


class MainViewModel @ViewModelInject constructor(
    private val repository: SyncRepository
) : ViewModel() {

    fun sync() = repository.syncAllData()
}
