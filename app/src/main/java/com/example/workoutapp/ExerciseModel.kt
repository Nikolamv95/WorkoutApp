package com.example.workoutapp

data class ExerciseModel(
    var id: Int,
    var name: String,
    var image: Int,
    var isCompleted: Boolean,
    var isSelected: Boolean
)