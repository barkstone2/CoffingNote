package com.note.coffee.data.entity.beans

enum class RoastDegree(
    private val engName: String,
    private val korName: String
) {
    LIGHT("Light" , "약배전"),
    CINNAMON("Cinnamon", "약배전"),
    MEDIUM("Medium", "중배전"),
    HIGH("High", "중배전"),
    CITY("City", "중강배전"),
    FULL_CITY("Full City", "중강배전"),
    FRENCH("French", "강배전"),
    ITALIAN("Italian", "강배전");

    fun getName(): String {
        return "$engName($korName)"
    }
}