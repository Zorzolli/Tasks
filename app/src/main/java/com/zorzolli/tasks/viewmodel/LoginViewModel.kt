package com.zorzolli.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.zorzolli.tasks.service.model.HeaderModel
import com.zorzolli.tasks.service.constants.TaskConstants
import com.zorzolli.tasks.service.helper.FingerprintHelper
import com.zorzolli.tasks.service.listener.APIListener
import com.zorzolli.tasks.service.listener.ValidationListener
import com.zorzolli.tasks.service.model.PriorityModel
import com.zorzolli.tasks.service.repository.PersonRepository
import com.zorzolli.tasks.service.repository.PriorityRepository
import com.zorzolli.tasks.service.repository.local.SecurityPreferences
import com.zorzolli.tasks.service.repository.remote.RetrofitClient

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val mPersonRepository = PersonRepository(application)
    private val mPriorityRepository = PriorityRepository(application)
    private val mSharedPreferences = SecurityPreferences(application)

    private val mLogin = MutableLiveData<ValidationListener>()
    val login: LiveData<ValidationListener> = mLogin

    private val mFingerPrint = MutableLiveData<Boolean>()
    val fingerprint: LiveData<Boolean> = mFingerPrint

    /**
     * Faz login usando API
     */
     fun doLogin(email: String, password: String) {
        mPersonRepository.login(email, password, object : APIListener<HeaderModel> {
            override fun onSucess(model: HeaderModel) {

                mSharedPreferences.store(TaskConstants.SHARED.TOKEN_KEY, model.token)
                mSharedPreferences.store(TaskConstants.SHARED.PERSON_KEY, model.personKey)
                mSharedPreferences.store(TaskConstants.SHARED.PERSON_NAME, model.name)

                RetrofitClient.addHeader(model.token, model.personKey)

                mLogin.value = ValidationListener()
            }

            override fun onFailure(str: String) {
                mLogin.value = ValidationListener(str)
            }
        })
    }

    fun isAuthenticationAvaible() {

        val token = mSharedPreferences.get(TaskConstants.SHARED.TOKEN_KEY)
        val person = mSharedPreferences.get(TaskConstants.SHARED.PERSON_KEY)

        val everLogged = (token != "" && person != "")

        RetrofitClient.addHeader(token, person)

        if(!everLogged) {
            mPriorityRepository.all(object : APIListener<List<PriorityModel>>{
                override fun onSucess(model: List<PriorityModel>) {
                    mPriorityRepository.save(model)
                }

                override fun onFailure(str: String) {
                }

            })
        }

        if (FingerprintHelper.isAuthenticationAvaible(getApplication())) {
            mFingerPrint.value = everLogged
        }
    }
}

