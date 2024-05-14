package com.mshdabiola.desktop

import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.mshdabiola.designsystem.string.appName
import com.mshdabiola.skeletonapp.di.appModule
import com.mshdabiola.skeletonapp.ui.SkeletonApp
import org.koin.core.context.GlobalContext.startKoin
import java.io.File
import java.io.IOException
import java.io.PrintWriter
import java.nio.file.Paths
import java.util.Properties
import kotlin.io.path.exists
import kotlin.io.path.inputStream

fun mainApp() {

    application {
        val windowState = rememberWindowState(
            size = DpSize(width = 1100.dp, height = 600.dp),
            placement = WindowPlacement.Maximized,
            position = WindowPosition.Aligned(Alignment.Center),
        )

        val appIcon = remember {
            System.getProperty("app.dir")
                ?.let { Paths.get(it, "icon-square-512.png") }
                ?.takeIf { it.exists() }
                ?.inputStream()
                ?.buffered()
                ?.use { BitmapPainter(loadImageBitmap(it)) }
        }

        val version ="1.0.7"
        Window(
            onCloseRequest = ::exitApplication,
            title = "$appName v$version",
            icon = appIcon,
            state = windowState,
        ) {
            SkeletonApp()
        }
    }
}

fun main() {
    val path = File("${System.getProperty("user.home")}/AppData/Local/hydraulic")
    if (path.exists().not()) {
        path.mkdirs()
    }
    val file = File(path, "main error.txt")

    try {
        startKoin {
            modules(appModule)
        }


        mainApp()
    } catch (e: Exception) {
        e.printStackTrace(PrintWriter(file.bufferedWriter()))
        throw e
    }
}