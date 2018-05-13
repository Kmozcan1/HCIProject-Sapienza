package com.example.onlinemarket.onlinemarket

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_insert_product.*
import android.view.View
import android.view.WindowManager
import android.widget.*
import android.media.ExifInterface
import java.io.File
import android.provider.MediaStore
import android.net.Uri
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable

private const val PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 0
private const val BROWSE_GALLERY_FOR_PRODUCT_IMAGE = 0
private const val PRODUCT_SUBMITTED_MESSAGE = "You have submitted a product successfully!"
private const val CATEGORY_NOT_SELECTED_MESSAGE = "Please select a category before submitting the product."
private const val COMPANY_NOT_SELECTED_MESSAGE = "Please select a company before submitting the product."
private const val PRODUCT_NAME_NOT_ENTERED_MESSAGE = "Please enter a product name before submitting the product."
private const val PRODUCT_PRICE_NOT_ENTERED_MESSAGE = "Product price should be higher than zero!"

@RequiresApi(api = Build.VERSION_CODES.O)
class InsertProductActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insert_product)
        fillCompanies()
        fillCategories()
        imageBrowse_button.setOnClickListener {
            if (hasPermission()) {
                openGallery()
            }
        }
        submitProduct_button.setOnClickListener {
            submitProduct()
        }
    }

    private fun submitProduct() {
        //Make the screen non-interactive & progressBar visible
        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        progressBar.visibility = View.VISIBLE
        when(submissionValid()) {
            true -> {
                //Insert Product into Firebase Database
                val ref = FirebaseDatabase.getInstance().getReference("products")
                val productId = ref.push().key
                ref.child(productId).setValue(createProduct()).addOnCompleteListener{
                    progressBar.visibility = View.INVISIBLE
                    window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(applicationContext,
                            PRODUCT_SUBMITTED_MESSAGE, Toast.LENGTH_LONG).show()
                }
            }
            false -> {
                progressBar.visibility = View.INVISIBLE
                window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        }


    }

    //Reads the fields and creates the Product object
    private fun createProduct(): Product {
        val productName = productName_autoCompleteTextView.text.toString()
        val price = productPrice_editText.text.toString().toDoubleOrNull()
        val company = company_spinner.selectedItem.toString()
        val productImage = imageTransform.DrawabletoString(productImage_imageView.drawable)
        val category = category_spinner.selectedItem.toString()
        return Product(productName, price, company, productImage, category)
    }

    //Intent result
    override fun onActivityResult(requestCode: Int, resultCode: Int, result: Intent?) {
        super.onActivityResult(requestCode, resultCode, result)
        when (requestCode) {
            BROWSE_GALLERY_FOR_PRODUCT_IMAGE -> if (resultCode === Activity.RESULT_OK) {
                val selectedImage = result!!.data
                productImage_imageView.setImageURI(selectedImage)
                fixImageOrientation(selectedImage)
            }
        }
    }

    //Flips the image if the orientation is one of 3, 6 or 8.
    private fun fixImageOrientation(selectedImage: Uri) {
        val bitmap = (productImage_imageView.drawable as BitmapDrawable).bitmap
        val finalFile = File(getRealPathFromURI(selectedImage))
        val exif = ExifInterface(finalFile.path)
        val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1)
        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> {
                val matrix = Matrix()
                matrix.postRotate(90.toFloat())
                val rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
                productImage_imageView.setImageBitmap(rotatedBitmap)
            }
            ExifInterface.ORIENTATION_ROTATE_180 -> {
                val matrix = Matrix()
                matrix.postRotate(180.toFloat())
                val rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
                productImage_imageView.setImageBitmap(rotatedBitmap)
            }
            ExifInterface.ORIENTATION_ROTATE_270 -> {
                val matrix = Matrix()
                matrix.postRotate(270.toFloat())
                val rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
                productImage_imageView.setImageBitmap(rotatedBitmap)
            }
        }
    }

    private fun fillCompanies() {
        //Fetch the list of items from DB
        val companyList = ArrayList<String?>()
        var fBdatabase : DatabaseReference? = null
        fBdatabase = FirebaseDatabase.getInstance().getReference("companies")
        fBdatabase!!.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
            override fun onDataChange(p0: DataSnapshot?) {
                companyList.clear()
                companyList.add("Please Select a Company")
                if(p0!!.exists()) {
                    for (cmpObject in p0.children) {
                        val name = cmpObject.child("companyName").getValue(String::class.java)
                        companyList.add(name)
                    }
                    val companyAdapter = ArrayAdapter(this@InsertProductActivity,
                            R.layout.spinner_list_item_without_padding, companyList)
                    company_spinner.adapter = companyAdapter
                }
            }
        })
    }

    private fun fillCategories() {
        val categoryList = arrayOf("Please Select a Category", "Cleaning", "Drink", "Food", "Personal Care")
        val categoryAdapter = ArrayAdapter(this@InsertProductActivity,
                R.layout.spinner_list_item_without_padding, categoryList)
        category_spinner.adapter = categoryAdapter
    }

    private fun getRealPathFromURI(uri: Uri): String {
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor!!.moveToFirst()
        val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
        return cursor.getString(idx)
    }

    private fun hasPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(this@InsertProductActivity,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this@InsertProductActivity,
                        arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE)
                return false
        } else {
            return true
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    openGallery()
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return
            }

        // Add other 'when' lines to check for other
        // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        if (intent.resolveActivity(packageManager) != null) {
            startActivityForResult(intent, BROWSE_GALLERY_FOR_PRODUCT_IMAGE)
        }
    }

    private fun submissionValid(): Boolean {
        when {
            company_spinner.selectedItemPosition == 0 -> {
                Toast.makeText(applicationContext,
                        COMPANY_NOT_SELECTED_MESSAGE, Toast.LENGTH_LONG).show()
                return false
            }
            productName_autoCompleteTextView.text == null ||
                    productName_autoCompleteTextView.text.toString().compareTo("") == 0 -> {
                Toast.makeText(applicationContext,
                        PRODUCT_NAME_NOT_ENTERED_MESSAGE, Toast.LENGTH_LONG).show()
                return false
            }
            category_spinner.selectedItemPosition == 0 -> {
                Toast.makeText(applicationContext,
                        CATEGORY_NOT_SELECTED_MESSAGE, Toast.LENGTH_LONG).show()
                return false
            }
            productPrice_editText.text.toString().toDoubleOrNull() == null ||
                    productPrice_editText.text.toString().toDoubleOrNull()!!.compareTo(0.0) != 1  -> {
                Toast.makeText(applicationContext,
                        PRODUCT_PRICE_NOT_ENTERED_MESSAGE, Toast.LENGTH_LONG).show()
                return false
            }
        }
        return true
    }
}