package com.laivinieks.cogimatch.utilities

enum class Stage(val columns: Int, val totalCard: Int, val sizeMultiplayer: Float, val totalTime:Long) {
    First(2, 4, Constants.MAX_SIZE_MULTIPLAYER,10000),
    Second(2, 8, Constants.MAX_SIZE_MULTIPLAYER,15000),
    Third(3, 12, Constants.MED_SIZE_MULTIPLAYER,30000),
    Fourth(4, 16, Constants.CURRENT_SIZE_MULTIPLAYER,45000),
    Fifth(4, 20, Constants.CURRENT_SIZE_MULTIPLAYER,60000),
    Sixth(4, 24, Constants.CURRENT_SIZE_MULTIPLAYER,75000),
    Seventh(5, 30, Constants.MIN_SIZE_MULTIPLAYER,90000),
    Eigth(5, 40, Constants.MIN_SIZE_MULTIPLAYER,120000),

}