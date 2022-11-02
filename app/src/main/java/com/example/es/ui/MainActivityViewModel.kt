package com.example.es.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.es.domain.FetchUserUseCase
import com.example.es.domain.model.ErrorType
import com.example.es.domain.model.ResultUser
import com.example.es.ui.model.DataUi
import com.example.es.ui.model.MapDomainToUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val fetchUserUseCase: FetchUserUseCase,
    private val mapper: MapDomainToUi
) : ViewModel() {

    private var mUser = MutableLiveData<DataUi>()
    private var mError = MutableLiveData<ErrorType>()

    val user: LiveData<DataUi>
        get() = mUser
    val error: LiveData<ErrorType>
        get() = mError

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        mError.value = ErrorType.GENERIC_ERROR
    }

    fun fetchData(id: String, phone: String) {
        viewModelScope.launch(exceptionHandler) {
            when (val result: ResultUser = fetchUserUseCase.execute(id = id, phone = phone)) {
                is ResultUser.Success -> {
                    val dataDomain = result.userDomain
                    mapper.mapDomainToUi(dataDomain)
                    mUser.value = mapper.mapDomainToUi(dataDomain)
                }
                is ResultUser.Fail -> {
                    mError.value = result.errorType
                }
            }
        }
    }
}