import androidx.compose.ui.window.ComposeUIViewController
import com.vnteam.talktoai.presentation.App
import di_ios.doInitKoin
import org.koin.compose.koinInject
import org.koin.core.context.GlobalContext
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController {
    if (GlobalContext.getOrNull() == null) doInitKoin()
    return ComposeUIViewController {
        App(koinInject())
    }
}
