import androidx.compose.ui.window.ComposeUIViewController
import com.vnteam.talktoai.presentation.App
import org.koin.compose.koinInject
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController {
    return ComposeUIViewController {
        App(koinInject())
    }
}