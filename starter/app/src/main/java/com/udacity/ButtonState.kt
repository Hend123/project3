package com.udacity


sealed class ButtonState(var buttonText: Int) {
    object Clicked : ButtonState(R.string.download)
    object Loading : ButtonState(R.string.we_are_loading)
    object Completed : ButtonState(R.string.complete)
}