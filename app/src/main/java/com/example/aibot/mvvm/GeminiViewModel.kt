package com.example.aibot.mvvm

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aibot.models.ModelClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GeminiViewModel(private val repository: GeminiRepository) : ViewModel() {

    private val _generatedResponse = MutableLiveData<List<ModelClass>>()
    val generatedResponse: LiveData<List<ModelClass>> get() = _generatedResponse

    fun generatePrompt(prompt: String) {

        val userPrompt = ModelClass(prompt = prompt, isUser = true)
        _generatedResponse.value =
            _generatedResponse.value?.plus(listOf(userPrompt)) ?: listOf(userPrompt)

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.generateText(prompt)
                val aiPrompt = ModelClass(response, isUser = false)

                val newList = _generatedResponse.value!!.toMutableList()
                newList.add(aiPrompt)
                _generatedResponse.postValue(newList)

            } catch (e: Exception) {
                Log.e("AY", e.localizedMessage ?: "Error BC")
            }
        }
    }


    fun generatePrompt(image: Bitmap, prompt: String) {
        val userPrompt = ModelClass(prompt = prompt, image = image, isUser = true)
        _generatedResponse.value =
            _generatedResponse.value?.plus(listOf(userPrompt)) ?: listOf(userPrompt)

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.generateImageResponse(image, prompt)
                val aiPrompt = ModelClass(prompt = response, isUser = false)

                val newList = _generatedResponse.value!!.toMutableList()
                newList.add(aiPrompt)
                _generatedResponse.postValue(newList)

            } catch (e: Exception) {
                Log.e("AY", e.localizedMessage ?: "Error BC")
            }
        }
    }
}
