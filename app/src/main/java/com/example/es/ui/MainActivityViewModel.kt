package com.example.es.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.es.domain.usecases.FetchUseCase
import com.example.es.domain.model.ResultUser
import com.example.es.ui.model.MapDomainToUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val fetchUseCase: FetchUseCase,
    private val mapper: MapDomainToUi
) : ViewModel() {

    private val mUser = MutableLiveData<ResultUser>()
//    private var mError = MutableLiveData<ResultUser>()

    val user: LiveData<ResultUser>
        get() = mUser
//    val error: LiveData<ResultUser>
//        get() = mError

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
//        mError.value = ValueWrapper(ErrorType.GENERIC_ERROR)
    }

//    fun fetchData(id: String, phone: String) {
//        viewModelScope.launch(exceptionHandler) {
//            mUser.value = ResultUser.Loading(true)
//            delay(3000)
//
//            when (val result: ResultUser = fetchUserUseCase.execute(id = id, phone = phone)) {
//                is ResultUser.Success -> {
//                    mUser.value = result
//                }
//                is ResultUser.Fail -> {
//                    mUser.value = result
//                }
//                else -> {}
//            }
//        }
//    }
}