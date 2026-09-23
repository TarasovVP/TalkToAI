import androidx.compose.ui.window.ComposeUIViewController
import com.vnteam.talktoai.presentation.App
import di_ios.doInitKoin
import org.koin.compose.koinInject
import org.koin.mp.KoinPlatform
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController {
    if (KoinPlatform.getKoinOrNull() == null) doInitKoin()
    return ComposeUIViewController {
        App(koinInject())
    }
}
