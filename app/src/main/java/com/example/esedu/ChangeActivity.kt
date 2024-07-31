package com.example.esedu

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class ChangeActivity : AppCompatActivity() {
    private lateinit var dbHelper: DBHelper
    private lateinit var searchEditText: EditText
    private lateinit var searchButton: Button
    private lateinit var nameEditText: EditText
    private lateinit var compositionEditText: EditText
    private lateinit var priceEditText: EditText
    private lateinit var chooseImageButton: Button
    private lateinit var selectedImage: ImageView
    private lateinit var saveButton: Button
    private lateinit var deleteButton: Button
    private lateinit var legendEditText: EditText

    private val PICK_IMAGE_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change)

        dbHelper = DBHelper(this, null)

        searchEditText = findViewById(R.id.searchEditText)
        searchButton = findViewById(R.id.searchButton)
        nameEditText = findViewById(R.id.loginText)
        compositionEditText = findViewById(R.id.passwordText)
        priceEditText = findViewById(R.id.priceEditText)
        legendEditText = findViewById(R.id.LegendEditText)
        chooseImageButton = findViewById(R.id.chooseImageButton)
        selectedImage = findViewById(R.id.selectedImage)
        saveButton = findViewById(R.id.saveButton)
        deleteButton = findViewById(R.id.deleteButton)
        saveButton.isEnabled=false
        deleteButton.isEnabled = false
        searchButton.setOnClickListener {

            searchItemAndDisplayData()
        }

        saveButton.setOnClickListener {

            updateDataInDatabase()
        }

        deleteButton.setOnClickListener {
            showDeleteConfirmationDialog()
        }
        chooseImageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }
    }

    private fun loadImage(imageIdentifier: String, imageView: ImageView) {
        // Проверяем, содержится ли в идентификаторе изображения префикс "file://"
        if (imageIdentifier.startsWith("file://")) {
            // Если префикс "file://" содержится в пути к файлу, удаляем его
            val imagePath = imageIdentifier.substring("file://".length)
            val imageFile = File(imagePath)

            if (imageFile.exists()) {
                // Если изображение найдено во внутреннем хранилище, загружаем его из этого места
                val drawable = Drawable.createFromPath(imageFile.absolutePath)
                imageView.setImageDrawable(drawable)
                return
            }
        }

        // Если изображение не найдено во внутреннем хранилище, пытаемся загрузить его из ресурсов папки drawable
        val imageId = resources.getIdentifier(imageIdentifier, "drawable", packageName)
        if (imageId != 0) {
            // Если изображение найдено в папке drawable, загружаем его из ресурсов
            imageView.setImageResource(imageId)
        } else {
            // Если изображение не найдено ни во внутреннем хранилище, ни в папке drawable,
            // то можно установить какое-то заглушечное изображение или оставить ImageView пустым
            // imageView.setImageResource(R.drawable.placeholder)
            imageView.setImageDrawable(null)
        }
    }

    private fun searchItemAndDisplayData() {

        try {
            val searchQuery = searchEditText.text.toString().trim()
            Log.e("e", "Searching for $searchQuery")
            val foundItem = dbHelper.searchItem(searchQuery)
            if(foundItem!=null)
            {
                deleteButton.isEnabled = true
                saveButton.isEnabled = true
                if (foundItem is Soup) {
                    nameEditText.setText(foundItem.name)
                    compositionEditText.setText(foundItem.composition)
                    legendEditText.setText(foundItem.Legend)
                    priceEditText.setText(foundItem.price.toString())
                    loadImage(foundItem.image, selectedImage)

                } else if (foundItem is FitBurger)
                {
                    nameEditText.setText(foundItem.name)
                    compositionEditText.setText(foundItem.composition)
                    priceEditText.setText(foundItem.price.toString())
                    loadImage(foundItem.image, selectedImage)

                } else if (foundItem is Dish) {
                    nameEditText.setText(foundItem.name)
                    compositionEditText.setText(foundItem.composition)
                    priceEditText.setText(foundItem.price.toString())
                    loadImage(foundItem.image, selectedImage)

                } else if (foundItem is Desert) {
                    nameEditText.setText(foundItem.name)
                    compositionEditText.setText(foundItem.composition)
                    priceEditText.setText(foundItem.price.toString())
                    loadImage(foundItem.image, selectedImage)

                } else if (foundItem is ColdSoup) {
                    nameEditText.setText(foundItem.name)
                    compositionEditText.setText(foundItem.composition)
                    priceEditText.setText(foundItem.price.toString())
                    loadImage(foundItem.image, selectedImage)

                } else if (foundItem is Drinks) {
                    nameEditText.setText(foundItem.name)
                    compositionEditText.setText(foundItem.composition)
                    priceEditText.setText(foundItem.price.toString())
                    loadImage(foundItem.image, selectedImage)

                }

                else {
                    Toast.makeText(this, "Элемент не найден", Toast.LENGTH_SHORT).show()
                }


            }

        }
        catch (e: Exception)
        {
            Log.e("E", "Error 3")
            Toast.makeText(this, "Введите существующий элемент", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showDeleteConfirmationDialog() {
        val itemName = searchEditText.text.toString()
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle("Удалить $itemName?")
        dialogBuilder.setMessage("Вы уверены, что хотите удалить этот элемент?")
        dialogBuilder.setPositiveButton("Удалить") { dialog, _ ->
            dbHelper.deleteItem(itemName)
            dialog.dismiss()
            finish() // Закрываем активность после удаления
        }
        dialogBuilder.setNegativeButton("Отмена") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = dialogBuilder.create()
        dialog.show()
    }

    private fun updateSoupDataInDatabase() {
        try {


            val itemName = nameEditText.text.toString().trim()
            val searchText = searchEditText.text.toString().trim()
            val composition = compositionEditText.text.toString().trim()
            val price = priceEditText.text.toString()
            val legend = legendEditText.text.toString().trim()
            val selectedImageUri = getImageUriFromImageView(selectedImage)
            val savedImageUri = saveImageToInternalStorage(selectedImageUri)

            val itemId = dbHelper.getItemIdByName(searchText)
            dbHelper.updateSoup(
                itemId,
                itemName,
                composition,
                price.toDouble(),
                legend,
                savedImageUri.toString()
            )
            Log.e("E", "Обновление супов")
            Toast.makeText(this, "Изменения сохранены", Toast.LENGTH_SHORT).show()
        }
        catch (e:Exception){Toast.makeText(this,"Введите корректные данные", Toast.LENGTH_SHORT).show()}
    }
    private fun updateDataInDatabase() {
        try {


            val itemName = nameEditText.text.toString()
            val composition = compositionEditText.text.toString()
            val price = priceEditText.text.toString()
            val selectedImageUri = getImageUriFromImageView(selectedImage)
            val itemSearchText = searchEditText.text.toString()
            val itemId = dbHelper.getItemIdByName(itemSearchText.trim())
            if (legendEditText.text.toString() != "") {
                Log.e("E", "Updating Soup")
                updateSoupDataInDatabase()
                return
            }
            Log.e("E", "Updating other tables")
            dbHelper.updateTable(
                itemId,
                itemName,
                composition,
                price.toDouble(),
                selectedImageUri.toString(),
                itemSearchText
            )

            Toast.makeText(this, "Изменения сохранены", Toast.LENGTH_SHORT).show()
        }        catch (e:Exception){Toast.makeText(this,"Введите корректные данные", Toast.LENGTH_SHORT).show()}

    }

    private fun getImageUriFromImageView(imageView: ImageView): Uri {
        val drawable = imageView.drawable ?: return Uri.EMPTY
        val bitmap = (drawable as BitmapDrawable).bitmap

        val tempFile = createTempFile("tempImage", ".png")
        tempFile.deleteOnExit()

        FileOutputStream(tempFile).use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        }

        return Uri.fromFile(tempFile)
    }
    @Throws(IOException::class)
    private fun saveImageToInternalStorage(imageUri: Uri): Uri {
        try {
            val inputStream = contentResolver.openInputStream(imageUri) ?: return Uri.EMPTY
            val tempFile = createTempFile("tempImage", ".png")
            tempFile.deleteOnExit()

            FileOutputStream(tempFile).use { outputStream ->
                inputStream.copyTo(outputStream)
            }

            return Uri.fromFile(tempFile)
        } catch (e: IOException) {
            Log.e("Error", "Failed to save image to internal storage", e)
        }
        return Uri.EMPTY
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            val selectedImageUri: Uri = data.data!!
            selectedImage.setImageURI(selectedImageUri)
        }
    }



}

