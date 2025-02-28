import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.vnteam.talktoai.Constants.APP_NAME
import com.vnteam.talktoai.di_desktop.doInitKoin
import org.koin.compose.koinInject
import presentation.App

fun main() {
    doInitKoin()
    application {
        Window(onCloseRequest = ::exitApplication, title = APP_NAME) {
            App(koinInject())
        }
    }
}