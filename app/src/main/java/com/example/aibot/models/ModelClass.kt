package com.example.aibot.models

import android.graphics.Bitmap

data class ModelClass(val prompt: String, val image:Bitmap? = null, val isUser: Boolean = false)