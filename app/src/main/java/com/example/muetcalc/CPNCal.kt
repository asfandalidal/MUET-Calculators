package com.example.muetcalc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.muetcalc.databinding.ActivityCpncalBinding

class CPNCal : AppCompatActivity() {
    private lateinit var binding: ActivityCpncalBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityCpncalBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.infoBtn.setOnClickListener()
        {
            popUpCPNInfo()
        }
        binding.cpnBtn.setOnClickListener()
        {
            calculateCPN()
        }



    }

    private fun calculateCPN() {
        // Parse EditText values to Float or null
        val etMatricObt = binding.etMatricObt.text.toString().toFloatOrNull()
        val etMatricTot = binding.etMatricTot.text.toString().toFloatOrNull()
        val etHSCObt = binding.etHSCObt.text.toString().toFloatOrNull()
        val etHSCTot = binding.etHSCTot.text.toString().toFloatOrNull()
        val etEntryMarks = binding.etEntryMarks.text.toString().toFloatOrNull()

        // Check for valid input values
        if (etMatricObt == null || etMatricTot == null || etHSCObt == null || etHSCTot == null || etEntryMarks == null) {
            Toast.makeText(this, "Please input valid marks", Toast.LENGTH_SHORT).show()
            return
        }

        // If any input is zero, show error toast
        if (etMatricObt == 0f || etMatricTot == 0f || etHSCObt == 0f || etHSCTot == 0f || etEntryMarks == 0f) {
            Toast.makeText(this, "Please input valid marks", Toast.LENGTH_SHORT).show()
            return
        }

        // Calculate CPN using the provided formula
        val sscPercentage = (etMatricObt / etMatricTot) * 100 // Convert to percentage

        // Adjust HSC marks based on radio button selection
        val adjustedHSCObt = if (binding.radioButton1.isChecked) etHSCObt + 10f else etHSCObt
        val hscPercentage = (adjustedHSCObt / etHSCTot) * 100 // Convert to percentage

        val preAdmissionTestScore = etEntryMarks

        val sscWeightage = 0.10f
        val hscWeightage = 0.30f
        val preAdmissionTestWeightage = 0.60f

        val cpn = (sscPercentage * sscWeightage) + (hscPercentage * hscWeightage) + (preAdmissionTestScore * preAdmissionTestWeightage)

        // Display CPN in a dialog
        val inflater = LayoutInflater.from(this)
        val popUpinflate = inflater.inflate(R.layout.cpnresult, null)
        popUpinflate.findViewById<TextView>(R.id.tvCPN).text = "Your CPN is : $cpn"
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setView(popUpinflate)
        dialogBuilder.setPositiveButton("Close") { dialog, which ->
            dialog.dismiss()
        }
        val alertDialog = dialogBuilder.create()
        alertDialog.show()
    }




    private fun popUpCPNInfo()
    {
        val inflater = LayoutInflater.from(this)
        val popUpinflate = inflater.inflate(R.layout.cpnweightage, null)
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setView(popUpinflate)
        dialogBuilder.setPositiveButton("Close") { dialog, which ->
            dialog.dismiss()
        }
        val alertDialog = dialogBuilder.create()
        alertDialog.show()
    }
}