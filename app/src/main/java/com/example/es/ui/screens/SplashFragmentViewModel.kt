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

@HiltViewModel
class SplashFragmentViewModel @Inject constructor(
    private val fetchUseCase: FetchUseCase,
    private val mapper: MapDomainToUi,
) : ViewModel() {
    private var mUserAuth = MutableLiveData<DataUi>()
    private var mError = MutableLiveData<ErrorType>()

    val userAuth: LiveData<DataUi>
        get() = mUserAuth
    val error: LiveData<ErrorType>
        get() = mError

    private val exceptionHandler = CoroutineExceptionHandler { _, _ -> }

    fun fetchData(id: String, phone: String) {
        viewModelScope.launch(exceptionHandler) {
            when (val result: ResultUser = fetchUseCase.executeAuth(id, phone)) {
                is ResultUser.Success -> mUserAuth.value =
                    mapper.mapDomainToUi(result.user)
                is ResultUser.Fail -> mError.value = result.error
                else -> {}
            }
        }
    }
}
