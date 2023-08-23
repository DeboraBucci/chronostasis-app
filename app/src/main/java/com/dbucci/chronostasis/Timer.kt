package com.dbucci.chronostasis

class Timer(
    private val hs: Int = 0,
    private val min: Int = 25,
    private val secs: Int = 0,
    private var type: String,
    private var icon: Int,
    private var tag: String = "No tag selected",
    private var color: Int = com.google.android.material.R.color.design_default_color_primary
) {
    fun getTime(): String {
        val hoursStr = String.format("%02d", hs)
        val minutesStr = String.format("%02d", min)
        val secondsStr = String.format("%02d", secs)

        return "${if (hs != 0) "$hoursStr:" else ""}${minutesStr}:${secondsStr}"
    }

    fun getType(): String {
        return this.type
    }

    fun setType(type: String) {
        this.type = type
    }

    fun getIcon(): Int {
        return this.icon
    }

    fun setIcon(icon: Int) {
        this.icon = icon
    }

    fun getTag(): String {
        return this.tag
    }

    fun setTag(tag: String) {
        this.tag = tag
    }

    fun getColor(): Int {
        return this.color
    }

    fun setColor(color: Int) {
        this.color = color
    }
}