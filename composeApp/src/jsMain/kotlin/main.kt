import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.vnteam.talktoai.di.doInitKoin
import com.vnteam.talktoai.presentation.App
import kotlinx.browser.document
import org.koin.compose.koinInject

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    doInitKoin()
    val body = document.body ?: return
    ComposeViewport(body) {
        App(koinInject())
    }
}
