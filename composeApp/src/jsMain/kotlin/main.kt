import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import com.vnteam.talktoai.Constants.APP_NAME
import com.vnteam.talktoai.di.doInitKoin
import org.jetbrains.skiko.wasm.onWasmReady
import org.koin.compose.koinInject
import presentation.App

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    doInitKoin()
    onWasmReady {
        CanvasBasedWindow(APP_NAME) {
            App(koinInject())
        }
    }
}
