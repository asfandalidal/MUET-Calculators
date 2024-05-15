package com.example.muetcalc

import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import com.example.muetcalc.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var editText: EditText
    private lateinit var theoryContainer: LinearLayout // Reference to theory subjects container
    private lateinit var practicalContainer: LinearLayout // Reference to practical subjects container
    private lateinit var addThButton: AppCompatButton
    private lateinit var etnoOfpracticals: EditText

    private var previousNumSubjects = 0
    private var practicalCount = 0 // Keep track of the number of practical views added

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        editText = binding.editText
        theoryContainer = binding.container
        practicalContainer = binding.Practcontainer
        addThButton = binding.btnAddTheorySubj
        etnoOfpracticals = binding.etPracticals

        addThButton.setOnClickListener {
            if(editText.text.isEmpty()) Toast.makeText(this,"Please enter subjects",Toast.LENGTH_SHORT).show()
            else
                addSubjectViews()
        }

        binding.btnPractical.setOnClickListener {
            if(etnoOfpracticals.text.isEmpty())  Toast.makeText(this,"Please enter practicals",Toast.LENGTH_SHORT).show()
            else
                addPracticalViews()
        }
        binding.gpaBtn.setOnClickListener()
        {
            showPopUp()
        }
    }

    // methods
    private fun addSubjectViews() {
        val numSubjects = editText.text.toString().toIntOrNull() ?: return // get no of th-subjects from edit text

        if (numSubjects < previousNumSubjects) {
            val viewsToRemove = previousNumSubjects - numSubjects
            for (i in 0 until viewsToRemove) {
                theoryContainer.removeViewAt(theoryContainer.childCount - 1)
            }
        }

        if (numSubjects > previousNumSubjects) {
            val viewsToAdd = numSubjects - previousNumSubjects
            repeat(viewsToAdd) {
                addSubjectView()
            }
        }

        previousNumSubjects = numSubjects
    }

    private fun addSubjectView() {
        val inflater = LayoutInflater.from(this)
        val subjectView = inflater.inflate(R.layout.item_subj, theoryContainer, false)

        val textViewSubject = subjectView.findViewById<TextView>(R.id.textViewSubject)
        val spinnerGrade = subjectView.findViewById<Spinner>(R.id.spinnerGrade)

        textViewSubject.text = "Subject ${theoryContainer.childCount + 1}"

        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.grade_options,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerGrade.adapter = adapter

       theoryContainer.addView(subjectView)
    }

    private fun addPracticalViews() {
        val numberOfPracticals = etnoOfpracticals.text.toString().toIntOrNull() ?: return

        if (practicalCount > numberOfPracticals) {
            val viewsToRemove = practicalCount - numberOfPracticals
            for (i in 0 until viewsToRemove) {
                practicalContainer.removeViewAt(practicalContainer.childCount - 1)
                practicalCount--
            }
            return
        }

        while (practicalCount < numberOfPracticals) {
            val practicalView = LayoutInflater.from(this).inflate(R.layout.practical, null)

            val textViewPracticalSubject = practicalView.findViewById<TextView>(R.id.textViewPracticalSubject)
            val spinnerPracticalGrade = practicalView.findViewById<Spinner>(R.id.spinnerPracticalGrade)
            textViewPracticalSubject.text = "Practical ${practicalCount + 1}"

            practicalContainer.addView(practicalView)
            practicalCount++
        }

        if (numberOfPracticals == 0 && editText.text.isEmpty()) {
            practicalContainer.visibility = View.GONE
        } else {
            practicalContainer.visibility = View.VISIBLE
        }
    }

    private fun showPopUp() {
        val inflater = LayoutInflater.from(this)
        val popUpinflate = inflater.inflate(R.layout.pop_up, null)

        val totalGradePoints = getTotalGradePoints(theoryContainer, practicalContainer)
        val totalCreditHours = getTotalCreditHours(theoryContainer, practicalContainer)

        if (totalCreditHours == 0.0) {
            Toast.makeText(this, "Please enter valid number", Toast.LENGTH_SHORT).show()
            return
        }

        // Calculate GPA using the formula: GPA = Total Grade Points / Total Credit Hours
        val gpa = totalGradePoints / totalCreditHours

        // Ensure GPA is not greater than 4.0
        val cappedGPA = if (gpa > 4.0) 4.0 else gpa

        // Display GPA and Total Credit Hours
        val tvGPA = popUpinflate.findViewById<TextView>(R.id.tvGPA)
        tvGPA.text = "Your GPA is : ${String.format("%.2f", cappedGPA)}"

        val totalHoursTextView = popUpinflate.findViewById<TextView>(R.id.totalHours)
        totalHoursTextView.text = "Total Credit Hours for this Semester : ${String.format("%.2f", totalCreditHours)}"
        // Calculate and display total quality points
        val totalQualityPointsTextView = popUpinflate.findViewById<TextView>(R.id.qtPoints)
        totalQualityPointsTextView.text = "Total Quality Points : ${(gpa * totalCreditHours)}"
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setView(popUpinflate)
        dialogBuilder.setPositiveButton("Close") { dialog, which ->
            dialog.dismiss()
        }

        val alertDialog = dialogBuilder.create()
        alertDialog.show()
    }

    private fun getTotalGradePoints(theoryContainer: LinearLayout, practicalContainer: LinearLayout): Double {
        var totalGradePoints = 0.0

        // Loop through theory subjects and add their grade points
        for (i in 0 until theoryContainer.childCount) {
            val view = theoryContainer.getChildAt(i)
            val gradeSpinner = view.findViewById<Spinner>(R.id.spinnerGrade)
            val grade = gradeSpinner.selectedItem.toString()

            // Get the credit hours from the EditText
            val creditHoursEditText = view.findViewById<EditText>(R.id.editTextSubject)
            val creditHours = creditHoursEditText.text.toString().toDoubleOrNull() ?: 0.0

            // Calculate grade points for the grade
            val gradePoint = when (grade) {
                "A+" -> 4.0
                "A" -> 3.5
                "B+" -> 3.0
                "B" -> 2.5
                "C+" -> 2.0
                "C" -> 1.5
                "C-" -> 1.0
                "F" -> 0.0
                else -> 0.0
            }

            // Add the grade points multiplied by credit hours to the total grade points
            totalGradePoints += gradePoint * creditHours
        }

        // Loop through practical subjects and add their grade points
        for (i in 0 until practicalContainer.childCount) {
            val view = practicalContainer.getChildAt(i)
            val gradeSpinner = view.findViewById<Spinner>(R.id.spinnerPracticalGrade)
            val grade = gradeSpinner.selectedItem.toString()
            val creditHours = 1.0 // Assuming each practical subject has 1 credit hour

            // Calculate grade points for the grade
            val gradePoint = when (grade) {
                "A+" -> 4.0
                "A" -> 3.5
                "B+" -> 3.0
                "B" -> 2.5
                "C+" -> 2.0
                "C" -> 1.5
                "C-" -> 1.0
                "F" -> 0.0
                else -> 0.0
            }

            // Add the grade points multiplied by credit hours to the total grade points
            totalGradePoints += gradePoint * creditHours
        }

        return (totalGradePoints)
    }



    private fun getTotalCreditHours(theoryContainer: LinearLayout, practicalContainer: LinearLayout): Double {
        var totalCreditHours = 0.0

        // Loop through theory subjects and add their credit hours
        for (i in 0 until theoryContainer.childCount) {
            val view = theoryContainer.getChildAt(i)
            val creditHoursEditText = view.findViewById<EditText>(R.id.editTextSubject)
            val creditHours = creditHoursEditText.text.toString().toDoubleOrNull() ?: 0.0
            totalCreditHours += creditHours
        }

        // Add credit hours for practical subjects
        totalCreditHours += practicalContainer.childCount // Assuming each practical subject has 1 credit hour

        return totalCreditHours
    }


}
