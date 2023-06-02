package space.mrandika.wisnu.ui.login

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import space.mrandika.wisnu.model.auth.LoginResponse
import space.mrandika.wisnu.repository.AuthRepository
import javax.inject.Inject
import kotlin.math.log

@HiltViewModel
class LoginViewModel @Inject constructor(private val repo:AuthRepository):ViewModel() {
    private val _state = MutableStateFlow(LoginUiState())
    val state : StateFlow<LoginUiState> = _state

    suspend fun userLogin(email: String, password : String){
        setError(false)
        setLoading(true)

        repo.login(email,password).collect{ result ->
            setLoading(false)
            Log.d("cek Result Login",result.toString())
            result.onSuccess {
                setResult(it)
            }
            result.onFailure {
                setError(true)
            }
        }
    }

    private fun setLoading(value:Boolean){
        _state.update {
            it.copy(isLoading = value)
        }
    }
    private fun setError(value:Boolean){
        _state.update {
            it.copy(isError = value)
        }
    }

    private fun setResult(value : LoginResponse){
        _state.update {
            it.copy(LoginResult = value)
        }
    }
}