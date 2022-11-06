package com.example.es.ui.screens

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.es.domain.model.DataDomain
import com.example.es.domain.model.ErrorType
import com.example.es.domain.model.ResultUser
import com.example.es.domain.usecases.FetchUseCase
import com.example.es.domain.usecases.PostUseCase
import com.example.es.ui.model.DataUi
import com.example.es.ui.model.MapDomainToUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainFragmentViewModel @Inject constructor(
    private val fetchUseCase: FetchUseCase,
    private val postUseCase: PostUseCase,
    private val mapper: MapDomainToUi,
) : ViewModel() {

    private var mUser = MutableLiveData<DataUi>()
    private var mError = MutableLiveData<ErrorType>()

    val user: LiveData<DataUi>
        get() = mUser
    val error: LiveData<ErrorType>
        get() = mError

    private val exceptionHandler = CoroutineExceptionHandler { _, _ -> }

    fun fetchExistedUser(id: String) {
        viewModelScope.launch(exceptionHandler) {
            when (val result = fetchUseCase.fetchExisted(id = id)) {
                is ResultUser.Success<*> -> mUser.value =
                    mapper.mapDomainToUi(result.user as DataDomain)
                is ResultUser.Fail -> mError.value = result.error
                else -> {}
            }
        }
    }

    fun postLocation(id: String, latitude: String, longitude: String) {
        viewModelScope.launch(exceptionHandler) {
            when (val result = postUseCase.postLocation(id, latitude, longitude)) {
                is ResultUser.Fail -> mError.value = result.error
                else -> {}
            }
        }
    }
}