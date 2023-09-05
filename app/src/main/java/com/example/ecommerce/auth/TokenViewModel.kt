//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.ecommerce.auth.AuthManager
//import com.example.ecommerce.preference.PrefHelper
//import dagger.hilt.android.AndroidEntryPoint
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//
//class TokenViewModel @Inject constructor(
//    private val authManager: AuthManager
//): ViewModel() {
//
//    private val _refreshTokenStatus = MutableLiveData<Result<Boolean>>()
//    val refreshTokenStatus: LiveData<Result<Boolean>> = _refreshTokenStatus
//
////    fun refreshAuthToken() {
////        viewModelScope.launch {
////            val result = authManager.refreshToken()
////            _refreshTokenStatus.value = Result.success(result)
////        }
////    }
//}