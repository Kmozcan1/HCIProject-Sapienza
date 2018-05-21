package com.example.onlinemarket.onlinemarket

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.example.onlinemarket.onlinemarket.R.id.*
import com.example.onlinemarket.onlinemarket.Utilities.Companion.handleProgressBarAction
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_insert_company.*


private const val INVALID_TIME_MESSAGE = "Closing time must be larger than the opening time"
private const val COMPANY_NAME_NOT_ENTERED_MESSAGE = "Please enter a company name"
private const val COMPANY_SUBMITTED_MESSAGE = "You have submitted a company successfully!"
private const val COMPANY_NAME_EXISTS_MESSAGE = "A company with this name already exists"
private const val BROWSE_GALLERY_FOR_COMPANY_IMAGE = 0
private const val PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 0
var companyList: ArrayList<Company>? = null

@RequiresApi(api = Build.VERSION_CODES.O)
class InsertCompanyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insert_company)

        setTimePickers()

        //Make the progress bar visible
        handleProgressBarAction(insertCompany_progressBar, window, true)

        //Fetch companies for validation (don't allow company with same name)
        fetchCompanies()

        val hasPermission = Utilities.hasPermission(this@InsertCompanyActivity,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE)
        insertCompany_imageBrowse_button.setOnClickListener {
            if (hasPermission) {
                Utilities.openGallery(this@InsertCompanyActivity, packageManager, BROWSE_GALLERY_FOR_COMPANY_IMAGE, savedInstanceState)
            }
        }

        submitCompany_button.setOnClickListener {
            submitCompany()
        }

        handleProgressBarAction(insertCompany_progressBar, window, false)
    }

    private fun setTimePickers() {
        closeTime_timePicker.setIs24HourView(true)
        openTime_timePicker.setIs24HourView(true)
    }

    private fun fetchCompanies() {
        Utilities.getCompanies(object: FireBaseListener {
            override fun onCallBack(value: Any, listener: ValueEventListener, query: DatabaseReference) {
                companyList = value as ArrayList<Company>
            }
        })
    }

    private fun submitCompany() {
        when(submissionValid()) {
            true -> {
                Utilities.insertCompany(createCompany())
                Utilities.handleProgressBarAction(insertCompany_progressBar, window, false)
                Toast.makeText(applicationContext,
                        COMPANY_SUBMITTED_MESSAGE, Toast.LENGTH_LONG).show()
            }
            false -> {
                Utilities.handleProgressBarAction(insertCompany_progressBar, window, false)
            }
        }
    }

    private fun submissionValid(): Boolean {
        when {
            companyName_autoCompleteTextView.text == null ||
                    companyName_autoCompleteTextView.text.toString().compareTo("") == 0 -> {
                Toast.makeText(applicationContext,
                        COMPANY_NAME_NOT_ENTERED_MESSAGE, Toast.LENGTH_LONG).show()
                return false
            }
            //compare closing time to opening time
            (closeTime_timePicker.hour.toString() + closeTime_timePicker.minute.toString()).toInt().
                    compareTo((openTime_timePicker.hour.toString() + openTime_timePicker.minute.toString()).toInt()) != 1 -> {
                Toast.makeText(applicationContext,
                        INVALID_TIME_MESSAGE, Toast.LENGTH_LONG).show()
                return false
            }
        }
        for (company in companyList!!) {
            if (company.companyName.toLowerCase()
                            .contains(companyName_autoCompleteTextView.text.toString().toLowerCase())) {
                Toast.makeText(applicationContext,
                        COMPANY_NAME_EXISTS_MESSAGE, Toast.LENGTH_LONG).show()
                return false
            }
        }
        return true
    }

    //Reads the fields and creates the Company object
    private fun createCompany(): Company {
        val companyName = companyName_autoCompleteTextView.text.toString()
        val companyImage = imageTransform.DrawabletoString(companyImage_imageView.drawable)
        val openTime = openTime_timePicker.hour.toString().padStart(2, '0') +
                ":" + openTime_timePicker.minute.toString().padEnd(2, '0')
        val closeTime = closeTime_timePicker.hour.toString().padStart(2, '0')+
                ":" + closeTime_timePicker.minute.toString().padEnd(2, '0')
        return if (intent.getStringExtra("companyKey") != null) {
            val companyKey = intent.getStringExtra("companyKey")
            Company(companyKey, companyName, companyImage, openTime, closeTime)
        }
        else {
            Company(companyName, companyImage, openTime, closeTime)
        }
    }

    //Intent result
    override fun onActivityResult(requestCode: Int, resultCode: Int, result: Intent?) {
        super.onActivityResult(requestCode, resultCode, result)
        when (requestCode) {
            BROWSE_GALLERY_FOR_COMPANY_IMAGE -> if (resultCode === Activity.RESULT_OK) {
                val selectedImage = result!!.data
                companyImage_imageView.setImageURI(selectedImage)
                //fixImageOrientation(selectedImage)
            }
        }
    }
}