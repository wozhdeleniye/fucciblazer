package com.example.compicprogtamming

import android.app.Application
import com.example.compicprogtamming.model.BlocksService

class App : Application() {
    val blocksService = BlocksService()

}