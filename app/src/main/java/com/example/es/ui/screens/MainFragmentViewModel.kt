package com.example.es.ui.screens

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.es.domain.model.ErrorType
import com.example.es.domain.model.ResultUser
import com.example.es.domain.usecases.FetchUseCase
import com.example.es.domain.usecases.PostUseCase
import com.example.es.ui.model.DataUi
import com.example.es.ui.model.MapDomainToUi
import com.example.es.ui.model.MapUiToDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainFragmentViewModel @Inject constructor(
    private val fetchUseCase: FetchUseCase,
    private val postUseCase: PostUseCase,
    private val mapperToUi: MapDomainToUi,
    private val mapperToDomain: MapUiToDomain,
) : ViewModel() {

    private var mUser = MutableLiveData<DataUi>()
    private var mError = MutableLiveData<ErrorType>()
    private var mLoading = MutableLiveData<Boolean>(false)

    val user: LiveData<DataUi>
        get() = mUser
    val error: LiveData<ErrorType>
        get() = mError
    val loading: LiveData<Boolean>
        get() = mLoading

    private val exceptionHandler = CoroutineExceptionHandler { _, _ -> }

    fun fetchExistedUser(id: String) {
        viewModelScope.launch(exceptionHandler) {
            when (val result = fetchUseCase.fetchExisted(id = id)) {
                is ResultUser.Success -> mUser.value =
                    mapperToUi.mapDomainToUi(result.user)
                is ResultUser.Fail -> mError.value = result.error
                else -> {}
            }
        }
    }

    fun postUpdates(id: String, map: MutableMap<String, Any>) {
        viewModelScope.launch(exceptionHandler) {
            mLoading.value = true
            val result = postUseCase.postUpdates(id, map)
            when (result) {
                is ResultUser.Success -> {
                    mUser.value = mapperToUi.mapDomainToUi(result.user)
                    mLoading.value = false
                }
                is ResultUser.Fail -> {
                    mError.value = result.error
                    mLoading.value = false
                }
                else -> {}
            }
        }
    }
}