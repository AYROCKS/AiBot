package com.example.aibot

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aibot.models.GeminiAdapter
import com.example.aibot.mvvm.GeminiRepository
import com.example.aibot.mvvm.GeminiViewModelFactory
import com.example.aibot.mvvm.GeminiViewModel
import kotlinx.coroutines.coroutineScope

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: GeminiViewModel
    private lateinit var recyclerView: RecyclerView
    private var selectedImage: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textView: EditText = findViewById(R.id.editTextText)
        val button: ImageButton = findViewById(R.id.enterButton)
        val addImage: ImageButton = findViewById(R.id.addImage)
        val image: ImageView = findViewById(R.id.image)

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    selectedImage = uri
                    image.setImageURI(uri)
                    image.visibility = View.VISIBLE
                }

            }


        recyclerView = findViewById(R.id.recyclerView)

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        val adapter = GeminiAdapter()
        recyclerView.adapter = adapter

        val repository = GeminiRepository(Services)
        val factory = GeminiViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(GeminiViewModel::class.java)

        viewModel.generatedResponse.observe(this) {

            adapter.differ.submitList(it)
        }

        addImage.setOnClickListener {

            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))

            image.setOnClickListener {
                image.setImageResource(0)
                image.visibility = View.GONE
                selectedImage = null
            }

        }
        button.setOnClickListener {

            val prompt = textView.text.toString()
            if (prompt.isNotEmpty()) {
                viewModel.generatePrompt(prompt)

            } else if (selectedImage != null) {

                val uri = uriToBitmap(selectedImage!!)
                viewModel.generatePrompt(prompt = prompt, image = uri!!)
            }

            textView.text = null
            image.setImageResource(0)
            image.visibility = View.GONE
            selectedImage = null
            recyclerView.smoothScrollToPosition(adapter.itemCount - 1)

            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(textView.windowToken, 0)
        }


    }

    private fun uriToBitmap(uri: Uri): Bitmap? {
        return try {
            // Decode the Uri into a Bitmap
            val inputStream = contentResolver.openInputStream(uri)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}