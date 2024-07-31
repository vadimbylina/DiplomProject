package com.example.esedu

import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception

class AddingMenuActivity : AppCompatActivity() {
    private lateinit var dbHelper: DBHelper
    private lateinit var categorySpinner: Spinner
    private lateinit var nameEditText: EditText
    private lateinit var input1EditText: EditText
    private lateinit var input2EditText: EditText
    private lateinit var chooseImageButton: Button
    private lateinit var selectedImage: ImageView
    private lateinit var saveButton: Button
    private lateinit var legendLabel: TextView
    private lateinit var legendEditText: EditText

    private val PICK_IMAGE_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adding_menu)
        dbHelper = DBHelper(this, null)

        categorySpinner = findViewById(R.id.categorySpinner)
        nameEditText = findViewById(R.id.loginText)
        input1EditText = findViewById(R.id.passwordText)
        input2EditText = findViewById(R.id.priceEditText)
        chooseImageButton = findViewById(R.id.chooseImageButton)
        selectedImage = findViewById(R.id.selectedImage)
        saveButton = findViewById(R.id.saveButton)
        legendLabel = findViewById(R.id.legendLabel)
        legendEditText = findViewById(R.id.LegendEditText)

        populateCategorySpinner()

        chooseImageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        saveButton.setOnClickListener {
            saveDataToDatabase()
        }

        categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View, position: Int, id: Long) {
                val selectedCategory = parentView.getItemAtPosition(position).toString()
                updateLegendVisibility(selectedCategory)
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
                // Ничего не делаем
            }
        }
    }

    private fun populateCategorySpinner() {
        val categories: ArrayList<String> = ArrayList()
        val cursor: Cursor = dbHelper.readableDatabase.rawQuery("SELECT name FROM category", null)

        while (cursor.moveToNext()) {
            val categoryNameIndex = cursor.getColumnIndex("name")
            if (categoryNameIndex != -1) {
                val categoryName = cursor.getString(categoryNameIndex)
                categories.add(categoryName)
            }
        }

        cursor.close()

        val adapter = ArrayAdapter(this, R.layout.custom_spinner_item, categories)
        adapter.setDropDownViewResource(R.layout.custom_spinner_item)
        categorySpinner.adapter = adapter
    }

    private fun updateLegendVisibility(selectedCategory: String) {
        if (selectedCategory == "Суп") {
            legendLabel.visibility = View.VISIBLE
            legendEditText.visibility = View.VISIBLE
        } else {
            legendLabel.visibility = View.GONE
            legendEditText.visibility = View.GONE
        }
    }

    private fun saveDataToDatabase() {
        try {


            val selectedCategory = categorySpinner.selectedItem.toString()
            val name = nameEditText.text.toString()
            val input1 = input1EditText.text.toString()
            val input2 = input2EditText.text.toString()
            val selectedImageUri = getImageUriFromImageView(selectedImage)
            val savedImageUri = saveImageToInternalStorage(selectedImageUri)

            when (selectedCategory) {
                "Суп" -> {
                    val soup = Soup(
                        name,
                        1,
                        input1,
                        input2.toDouble(),
                        legendEditText.text.toString(),
                        savedImageUri.toString()
                    )
                    dbHelper.addSoup(soup)
                }

                "FitBurger" -> {
                    val fit =
                        FitBurger(name, 2, input1, input2.toDouble(), savedImageUri.toString())
                    dbHelper.addFit(fit)
                }

                "Десерт" -> {
                    val desert =
                        Desert(name, 5, input1, input2.toDouble(), savedImageUri.toString())
                    dbHelper.addDesert(desert)
                }

                "Горячее блюдо" -> {
                    val dish = Dish(name, 3, input1, input2.toDouble(), savedImageUri.toString())
                    dbHelper.addDish(dish)
                }

                "Салат" -> {
                    val dish = Dish(name, 4, input1, input2.toDouble(), savedImageUri.toString())
                    dbHelper.addDish(dish)
                }

                "Чай" -> {
                    val drinks =
                        Drinks(name, 6, input1, input2.toDouble(), savedImageUri.toString())
                    dbHelper.addDrinks(drinks)
                }

                "Холодные напитки" -> {
                    val drinks =
                        Drinks(name, 7, input1, input2.toDouble(), savedImageUri.toString())
                    dbHelper.addDrinks(drinks)
                }

                "Комбуча" -> {
                    val drinks =
                        Drinks(name, 8, input1, input2.toDouble(), savedImageUri.toString())
                    dbHelper.addDrinks(drinks)
                }

                "Лимонады" -> {
                    val drinks =
                        Drinks(name, 9, input1, input2.toDouble(), savedImageUri.toString())
                    dbHelper.addDrinks(drinks)
                }

                "Охлаждённые cупы" -> {
                    val coldSoup =
                        ColdSoup(name, 10, input1, input2.toDouble(), savedImageUri.toString())
                    dbHelper.addColdScoldS(coldSoup)
                }

                "Холодные сендвичи" -> {
                    val coldSoup =
                        ColdSoup(name, 11, input1, input2.toDouble(), savedImageUri.toString())
                    dbHelper.addColdScoldS(coldSoup)
                }

                "Питьевые супы" -> {
                    val coldSoup =
                        ColdSoup(name, 12, input1, input2.toDouble(), savedImageUri.toString())
                    dbHelper.addColdScoldS(coldSoup)
                }
            }
            categorySpinner.clearFocus()
            nameEditText.text.clear()
            input1EditText.text.clear()
            input2EditText.text.clear()
            legendEditText.text.clear()

            Toast.makeText(this, "Данные успешно сохранены", Toast.LENGTH_SHORT).show()
        }
        catch (e: Exception)
        {
            Toast.makeText(this,"Введите корректные данные",Toast.LENGTH_SHORT).show()
        }

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

            contentResolver.openInputStream(imageUri)?.use { inputStream ->
                val tempFile = createTempFile("tempImage", ".png")
                tempFile.deleteOnExit()

                FileOutputStream(tempFile).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }

                return Uri.fromFile(tempFile)
            }
            throw IOException("Failed to save image to internal storage")


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            val selectedImageUri: Uri = data.data!!
            selectedImage.setImageURI(selectedImageUri)
        }
    }
}
