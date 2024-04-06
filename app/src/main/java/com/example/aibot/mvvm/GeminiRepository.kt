package com.example.aibot.mvvm

import android.graphics.Bitmap
import com.example.aibot.Services

class GeminiRepository(private val services : Services) {

    suspend fun generateText(prompt: String)  = services.generateText(prompt)

    suspend fun generateImageResponse(image: Bitmap, prompt: String) = services.generateImageResponse(image, prompt)

}