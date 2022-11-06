package com.example.es.ui.screens

import androidx.lifecycle.*
import com.example.es.domain.model.DataDomain
import com.example.es.domain.model.ErrorType
import com.example.es.domain.model.ResultUser
import com.example.es.domain.usecases.FetchUseCase
import com.example.es.ui.model.DataUi
import com.example.es.ui.model.MapDomainToUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
@HiltViewModel
class HistoryFragmentViewModel @Inject constructor(
    private val fetchUseCase: FetchUseCase,
    private val mapper: MapDomainToUi
) : ViewModel() {

    private var mUsers = MutableLiveData<List<DataUi>>()
    private var mError = MutableLiveData<ErrorType>()

    val users: LiveData<List<DataUi>>
        get() = mUsers
    val error: LiveData<ErrorType>
        get() = mError

    init {
        val exceptionHandler = CoroutineExceptionHandler { _, _ -> }
        viewModelScope.launch(exceptionHandler) {
            when (val result = fetchUseCase.fetchCached()) {
                is ResultUser.Success<*> -> {
                    val data: MutableLiveData<List<DataDomain>> =
                        result.user as MutableLiveData<List<DataDomain>>
                    mUsers = data.map { list ->
                        list.map { dataDomain ->
                            mapper.mapDomainToUi(dataDomain)
                        }
                    } as MutableLiveData<List<DataUi>>
                }
                is ResultUser.Fail -> mError.value = result.error
                else -> {}
            }
        }
    }
}