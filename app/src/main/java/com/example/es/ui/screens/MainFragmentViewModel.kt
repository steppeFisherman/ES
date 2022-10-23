package com.example.es.ui.screens

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.example.es.domain.FetchDataUseCase
import com.example.es.domain.model.ErrorType
import com.example.es.ui.model.DataUi
import com.example.es.ui.model.MapDomainToUi
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainFragmentViewModel @Inject constructor(
    fetchDataUseCase: FetchDataUseCase,
    private val mapper: MapDomainToUi,
) : ViewModel() {

    private var mAllItems = MutableLiveData<List<DataUi>>()
    private var mError = MutableLiveData<ErrorType>()
    val allItems: LiveData<List<DataUi>>
        get() = mAllItems
    val error: LiveData<ErrorType>
        get() = mError

    init {
        when (val result = fetchDataUseCase.execute()) {
            is com.example.es.domain.model.Result.Success -> {
                mAllItems = result.itemsDomain.map { list ->
                    list.map { mapper.mapDomainToUi(it) }
                } as MutableLiveData<List<DataUi>>
            }
            is com.example.es.domain.model.Result.Fail -> mError.value = result.errorType
        }
    }
}