package com.example.workoutapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.workoutapp.databinding.ActivityBmiactivityBinding
import java.math.BigDecimal
import java.math.RoundingMode

private const val METRIC_UNITS_VIEW = "METRIC_UNIT_VIEW"
private const val US_UNITS_VIEW = "US_UNIT_VIEW"

class BMIActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBmiactivityBinding
    private var currentVisibleView: String = METRIC_UNITS_VIEW

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBmiactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarBmiActivity)
        val actionbar = supportActionBar
        actionbar?.setDisplayHomeAsUpEnabled(true)
        actionbar?.title = "BMI Calculator"
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        binding.toolbarBmiActivity.setOnClickListener {
            // if someone click on back button accidentally
            onBackPressed()
        }

        binding.btnCalculateUnits.setOnClickListener {

            if (currentVisibleView == METRIC_UNITS_VIEW) {
                setupMetricUnitsView()
            } else {
                setupUSUnitsView()
            }
        }

        makeVisibleMetricUnitsView()
        binding.rgUnits.setOnCheckedChangeListener { group, checkId ->
            if (checkId == R.id.rbMetricUnits) {
                makeVisibleMetricUnitsView()
            } else {
                makeVisibleUsUnitsView()
            }
        }
    }

    // BMI Functionality START
    private fun isValidMetricUnits() = binding.etMetricUnitWeight.text.toString().isNotEmpty() &&
            binding.etMetricUnitHeight.text.toString().isNotEmpty()

    private fun displayBMIResult(bmi: Float) {
        val bmiLabel: String
        val bmiDescription: String

        if (bmi.compareTo(15f) <= 0) {
            bmiLabel = "Very severely underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(15f) > 0 && bmi.compareTo(16f) <= 0) {
            bmiLabel = "Severely underweight"
            bmiDescription = "Oops!You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(16f) > 0 && bmi.compareTo(18.5f) <= 0) {
            bmiLabel = "Underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(18.5f) > 0 && bmi.compareTo(25f) <= 0) {
            bmiLabel = "Normal"
            bmiDescription = "Congratulations! You are in a good shape!"
        } else if (bmi.compareTo(25f) > 0 && bmi.compareTo(30f) <= 0) {
            bmiLabel = "Overweight"
            bmiDescription = "Oops! You really need to take care of your yourself! Workout maybe!"
        } else if (bmi.compareTo(30f) > 0 && bmi.compareTo(35f) <= 0) {
            bmiLabel = "Obese Class | (Moderately obese)"
            bmiDescription = "Oops! You really need to take care of your yourself! Workout maybe!"
        } else if (bmi.compareTo(35f) > 0 && bmi.compareTo(40f) <= 0) {
            bmiLabel = "Obese Class || (Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        } else {
            bmiLabel = "Obese Class ||| (Very Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        }

        binding.llDisplayBMIResult.visibility = View.VISIBLE

        val bmiValue =
            BigDecimal(bmi.toDouble()).setScale(2, RoundingMode.HALF_EVEN).toString()

        binding.tvBMIValue.text = bmiValue
        binding.tvBMIType.text = bmiLabel
        binding.tvBMIDescription.text = bmiDescription
    }

    private fun validateUSUnits() = binding.etUsUnitWeight.text.toString().isNotEmpty()
            && binding.etUsUnitHeightFeet.text.toString().isNotEmpty()

    private fun setupMetricUnitsView() {
        if (isValidMetricUnits()) {
            val heightValue: Float =
                binding.etMetricUnitHeight.text.toString().toFloat() / 100
            val weightValue: Float = binding.etMetricUnitWeight.text.toString().toFloat()
            val bmi = weightValue / (heightValue * heightValue)
            displayBMIResult(bmi)
        } else {
            Toast.makeText(
                this@BMIActivity,
                "Please enter valid values.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun setupUSUnitsView() {
        if (validateUSUnits()) {
            val usUnitHeightValueFeet: String =
                binding.etUsUnitHeightFeet.text.toString()

            val usUnitHeightValueInch: String =
                binding.etUsUnitHeightInch.text.toString()

            val usUnitWeightValue: Float = binding.etUsUnitWeight.text.toString()
                .toFloat()

            val heightValue =
                usUnitHeightValueInch.toFloat() + usUnitHeightValueFeet.toFloat() * 12

            val bmi = 703 * (usUnitWeightValue / (heightValue * heightValue))

            displayBMIResult(bmi) // Displaying the result into UI
        } else {
            Toast.makeText(
                this@BMIActivity,
                "Please enter valid values.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    // BMI Functionality END

    // INPUT START
    private fun makeVisibleMetricUnitsView() {
        currentVisibleView = METRIC_UNITS_VIEW

        // Make Metric Units Visible
        binding.tilMetricUnitWeight.visibility = View.VISIBLE
        binding.tilMetricUnitHeight.visibility = View.VISIBLE

        // Clear input
        binding.etMetricUnitHeight.text?.clear()
        binding.etMetricUnitWeight.text?.clear()

        // Make UsUnits visible
        binding.tilUsUnitWeight.visibility = View.GONE
        binding.llUsUnitsHeight.visibility = View.GONE

        // Hide BMI Result
        binding.llDisplayBMIResult.visibility = View.INVISIBLE
    }

    private fun makeVisibleUsUnitsView() {
        currentVisibleView = US_UNITS_VIEW

        // Make Metric Units invisible
        binding.tilMetricUnitWeight.visibility = View.GONE
        binding.tilMetricUnitHeight.visibility = View.GONE

        // Clear input
        binding.etUsUnitWeight.text?.clear()
        binding.etUsUnitHeightFeet.text?.clear()
        binding.etUsUnitHeightInch.text?.clear()

        // Make UsUnits visible
        binding.tilUsUnitWeight.visibility = View.VISIBLE
        binding.llUsUnitsHeight.visibility = View.VISIBLE

        // Hide BMI Result
        binding.llDisplayBMIResult.visibility = View.INVISIBLE
    }
    // INPUT END
}