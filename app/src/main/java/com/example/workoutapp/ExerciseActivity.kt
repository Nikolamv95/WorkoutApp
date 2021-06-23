package com.example.workoutapp

import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.workoutapp.Constants.Companion.defaultExerciseList
import com.example.workoutapp.databinding.ActivityExcerciseBinding
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private var initialRestTimer: CountDownTimer? = null
    private var initialRestProgress = 0
    private var initialDuration = 1

    private var exerciseTimer: CountDownTimer? = null
    private var exerciseProgress = 0
    private var exerciseDuration = 1

    private var exerciseList: ArrayList<ExerciseModel>? = null
    private var currentExercisePosition = -1

    private var tts: TextToSpeech? = null
    private var player: MediaPlayer? = null

    private var exerciseAdapter: ExerciseStatusAdapter? = null

    private lateinit var binding: ActivityExcerciseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExcerciseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarExerciseActivity)
        val actionbar = supportActionBar
        actionbar?.setDisplayHomeAsUpEnabled(true)

        binding.toolbarExerciseActivity.setOnClickListener {
            // if someone click on back button accidentally
            customDialogForBackButton()
        }

        tts = TextToSpeech(this, this)
        exerciseList = defaultExerciseList()
        setupExerciseStatusRecyclerView()
        setProgressView()
    }

    // Built-in method which destroys everything before the activity is shut down
    override fun onDestroy() {
        if (initialRestTimer != null) {
            initialRestTimer!!.cancel()
            initialRestProgress = 0
        }

        if (exerciseTimer != null) {
            exerciseTimer!!.cancel()
            exerciseProgress = 0
        }

        if (tts != null){
            tts!!.stop()
            tts!!.shutdown()
        }

        if (player!= null){
            player!!.stop()
        }

        super.onDestroy()
    }

    // Text to speech functionality - Start
    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS){
            val result = tts!!.setLanguage(Locale.ENGLISH)

            if (result == TextToSpeech.LANG_MISSING_DATA ||
                result == TextToSpeech.LANG_NOT_SUPPORTED){
                Log.e("TTS", "The Language specified is not supported!")
            }else{
                Log.e("TTS", "Initialization Failed!")
            }
        }
    }

    private fun speakOut(text: String){
        tts!!.speak(text, TextToSpeech.QUEUE_FLUSH, null)
    }
    // Text to speech functionality - End

    // Timer functionality - Start
    private fun setExerciseView() {

        binding.llRestView.visibility = View.GONE
        binding.llExerciseView.visibility = View.VISIBLE

        if (exerciseTimer != null) {
            exerciseTimer!!.cancel()
            exerciseProgress = 0
        }

        binding.ivImage.setImageResource(exerciseList!![currentExercisePosition].getImage())
        binding.tvExerciseName.text = exerciseList!![currentExercisePosition].getName()
        speakOut(exerciseList!![currentExercisePosition].getName())
        setExerciseProgressBar()
    }

    private fun setExerciseProgressBar() {
        binding.progressBarExercise.progress = exerciseProgress
        exerciseTimer = object : CountDownTimer(1000, 1000) {

            // On ever 1 sec change will be executed
            override fun onTick(millisUntilFinished: Long) {
                exerciseProgress++
                binding.progressBarExercise.progress = exerciseDuration - exerciseProgress
                binding.tvExerciseTimer.text = ((exerciseDuration - exerciseProgress).toString())
            }

            // After the end of the onThick will be executed
            override fun onFinish() {
                if (currentExercisePosition < exerciseList?.size!! - 1) {
                    exerciseList!![currentExercisePosition].setIsSelected(false)
                    exerciseList!![currentExercisePosition].setIsCompleted(true)
                    exerciseAdapter!!.notifyDataSetChanged()
                    setProgressView()
                } else {
                    // Finish the activate and pass the next one
                    finish()
                    val intent = Intent(this@ExerciseActivity, FinishActivity::class.java)
                    startActivity(intent)
                }
            }
            // start the object restTimer.start()
        }.start()
    }

    private fun setProgressView() {

        produceSound()

        binding.llRestView.visibility = View.VISIBLE
        binding.llExerciseView.visibility = View.GONE

        if (initialRestTimer != null) {
            initialRestTimer!!.cancel()
            initialRestProgress = 0
        }

        binding.tvUpcomingExerciseName.text = exerciseList!![currentExercisePosition + 1].getName()
        setProgressBar()
    }

    private fun setProgressBar() {
        // sets the current progress to the specified value
        binding.progressBar.progress = initialRestProgress

        // assign object which inherit CountDownTimer and pass 10000 -> 10 sec, 1000 -> 1 sec
        initialRestTimer = object : CountDownTimer(1000, 1000) {

            // On ever 1 sec change will be executed
            override fun onTick(millisUntilFinished: Long) {
                initialRestProgress++
                binding.progressBar.progress = initialDuration - initialRestProgress
                binding.tvTimer.text = ((initialDuration - initialRestProgress).toString())
            }

            // After the end of the onThick will be executed
            override fun onFinish() {
                currentExercisePosition++

                exerciseList!![currentExercisePosition].setIsSelected(true)
                exerciseAdapter!!.notifyDataSetChanged()

                setExerciseView()
            }
            // start the object restTimer.start()
        }.start()
    }
    // Timer functionality - End

    // Media player sound - Start
    private fun produceSound() {
        try {
            player = MediaPlayer.create(applicationContext, R.raw.press_start)
            player!!.isLooping = false
            player!!.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    // Media player sound - End

    // Recycler view - Start
    private fun setupExerciseStatusRecyclerView(){
        binding.rvExerciseStatus.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false)

        exerciseAdapter = ExerciseStatusAdapter(exerciseList!!, this)
        binding.rvExerciseStatus.adapter = exerciseAdapter

    }
    // Recycler view - End

    private fun customDialogForBackButton(){

        val customDialog = Dialog(this)
        customDialog.setContentView(R.layout.dialog_custom_back_information)

        customDialog.findViewById<Button>(R.id.tvYes).setOnClickListener {
            finish()
            customDialog.dismiss()
        }

        customDialog.findViewById<Button>(R.id.tvNo).setOnClickListener {
            customDialog.dismiss()
        }

        customDialog.show()
    }
}