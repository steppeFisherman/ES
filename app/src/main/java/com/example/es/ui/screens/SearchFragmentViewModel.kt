package com.example.es.ui.screens

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class SearchFragmentViewModel @Inject constructor(
    private val fetchUseCase: FetchUseCase,
    private val mapper: MapDomainToUi
) : ViewModel() {

    private var mUsers = MutableLiveData<List<DataUi>>()
    private var mError = MutableLiveData<ErrorType>()

    val users: LiveData<List<DataUi>>
        get() = mUsers
    val error: LiveData<ErrorType>
        get() = mError

    private val exceptionHandler = CoroutineExceptionHandler { _, _ -> }

    fun fetchUsersByDate(timeStart: Long, timeEnd: Long) {
        viewModelScope.launch(exceptionHandler) {
            when (val result = fetchUseCase.fetchCachedByDate(timeStart, timeEnd)) {
                is ResultUser.SuccessList -> {
                    mUsers.value = result.users.map { mapper.mapDomainToUi(it) }
                }
                is ResultUser.Fail -> mError.value = result.error
                else -> {}
            }
        }
    }
}