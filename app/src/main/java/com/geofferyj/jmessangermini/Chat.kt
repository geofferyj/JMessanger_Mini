package com.geofferyj.jmessangermini

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Chat(val chatId: String="", val receiver: String=""): Parcelable