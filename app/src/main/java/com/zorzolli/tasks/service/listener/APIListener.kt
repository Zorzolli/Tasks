package com.zorzolli.tasks.service.listener

import com.zorzolli.tasks.service.model.HeaderModel

interface APIListener<T> {
    fun onSucess(model: T)
    fun onFailure(str: String)
}