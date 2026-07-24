package com.vnteam.talktoai.presentation.screens.settings.settings_loaders

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.meet.compose.loaders.PixelLoader
import com.meet.compose.loaders.model.PixelAnimation
import com.meet.compose.loaders.model.PixelGridSize
import com.meet.compose.loaders.model.PixelShape
import com.meet.compose.loaders.presets.PixelPresets
import kotlinx.coroutines.delay

@Composable
fun LoadersShowcaseContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        SectionHeader("1. Basic — PixelPresets.Framer (defaults)")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            PixelLoader(
                preset = PixelPresets.Framer,
                modifier = Modifier
                    .size(56.dp)
                    .semantics { contentDescription = "Loading indicator" }
            )
        }

        SectionDivider()

        SectionHeader("2. Customized — Rocket · Grid7x7 · RoundedSquare · 1.5×")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            PixelLoader(
                preset = PixelPresets.Rocket,
                gridSize = PixelGridSize.Grid7x7,
                modifier = Modifier
                    .size(80.dp)
                    .semantics { contentDescription = "Loading indicator" },
                color = Color(0xFF6366F1),
                inactiveColor = Color(0xFFD1D5DB),
                shape = PixelShape.RoundedSquare,
                speedMultiplier = 1.5f
            )
        }

        SectionDivider()

        SectionHeader("3. Three Presets Side-by-Side")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            LabeledLoader("Framer") {
                PixelLoader(
                    preset = PixelPresets.Framer,
                    modifier = Modifier
                        .size(56.dp)
                        .semantics { contentDescription = "Framer loading indicator" }
                )
            }
            LabeledLoader("Newton") {
                PixelLoader(
                    preset = PixelPresets.Newton,
                    modifier = Modifier
                        .size(56.dp)
                        .semantics { contentDescription = "Newton loading indicator" }
                )
            }
            LabeledLoader("Galaxy") {
                PixelLoader(
                    preset = PixelPresets.Galaxy,
                    modifier = Modifier
                        .size(56.dp)
                        .semantics { contentDescription = "Galaxy loading indicator" }
                )
            }
        }

        SectionDivider()

        SectionHeader("4. Grid Sizes — Framer @ 5×5 vs 7×7")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            LabeledLoader("Grid5x5") {
                PixelLoader(
                    preset = PixelPresets.Framer,
                    gridSize = PixelGridSize.Grid5x5,
                    modifier = Modifier
                        .size(56.dp)
                        .semantics { contentDescription = "5x5 loading indicator" }
                )
            }
            LabeledLoader("Grid7x7") {
                PixelLoader(
                    preset = PixelPresets.Framer,
                    gridSize = PixelGridSize.Grid7x7,
                    modifier = Modifier
                        .size(56.dp)
                        .semantics { contentDescription = "7x7 loading indicator" }
                )
            }
        }

        SectionDivider()

        SectionHeader("5. Shapes — Circle · Square · RoundedSquare")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            LabeledLoader("Circle") {
                PixelLoader(
                    preset = PixelPresets.Bounce,
                    shape = PixelShape.Circle,
                    modifier = Modifier
                        .size(56.dp)
                        .semantics { contentDescription = "Circle loading indicator" }
                )
            }
            LabeledLoader("Square") {
                PixelLoader(
                    preset = PixelPresets.Bounce,
                    shape = PixelShape.Square,
                    modifier = Modifier
                        .size(56.dp)
                        .semantics { contentDescription = "Square loading indicator" }
                )
            }
            LabeledLoader("Rounded") {
                PixelLoader(
                    preset = PixelPresets.Bounce,
                    shape = PixelShape.RoundedSquare,
                    modifier = Modifier
                        .size(56.dp)
                        .semantics { contentDescription = "RoundedSquare loading indicator" }
                )
            }
        }

        SectionDivider()

        SectionHeader("6. Sizes — 32 / 56 / 80 dp")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            LabeledLoader("32 dp") {
                PixelLoader(
                    preset = PixelPresets.Ripple,
                    modifier = Modifier
                        .size(32.dp)
                        .semantics { contentDescription = "Small loading indicator" }
                )
            }
            LabeledLoader("56 dp") {
                PixelLoader(
                    preset = PixelPresets.Ripple,
                    modifier = Modifier
                        .size(56.dp)
                        .semantics { contentDescription = "Medium loading indicator" }
                )
            }
            LabeledLoader("80 dp") {
                PixelLoader(
                    preset = PixelPresets.Ripple,
                    modifier = Modifier
                        .size(80.dp)
                        .semantics { contentDescription = "Large loading indicator" }
                )
            }
        }

        SectionDivider()

        SectionHeader("7. Light vs Dark Background")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Color.White, RoundedCornerShape(8.dp))
                    .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                PixelLoader(
                    preset = PixelPresets.Comet,
                    color = Color(0xFF1E40AF),
                    inactiveColor = Color(0xFFE5E7EB),
                    modifier = Modifier
                        .size(56.dp)
                        .semantics { contentDescription = "Loading indicator on light background" }
                )
            }
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Color(0xFF1F2937), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                PixelLoader(
                    preset = PixelPresets.Comet,
                    color = Color(0xFF93C5FD),
                    inactiveColor = Color(0xFF374151),
                    modifier = Modifier
                        .size(56.dp)
                        .semantics { contentDescription = "Loading indicator on dark background" }
                )
            }
        }

        SectionDivider()

        SectionHeader("8. Speed Multiplier — 0f / 0.5f / 1f / 2f")
        NoteText("speedMultiplier=0f is clamped to 0.1f by the library (very slow, not frozen)")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            LabeledLoader("0f→0.1×") {
                PixelLoader(
                    preset = PixelPresets.ClassicLoading,
                    speedMultiplier = 0f,
                    modifier = Modifier
                        .size(48.dp)
                        .semantics { contentDescription = "Very slow loading indicator" }
                )
            }
            LabeledLoader("0.5×") {
                PixelLoader(
                    preset = PixelPresets.ClassicLoading,
                    speedMultiplier = 0.5f,
                    modifier = Modifier
                        .size(48.dp)
                        .semantics { contentDescription = "Half-speed loading indicator" }
                )
            }
            LabeledLoader("1× (default)") {
                PixelLoader(
                    preset = PixelPresets.ClassicLoading,
                    speedMultiplier = 1f,
                    modifier = Modifier
                        .size(48.dp)
                        .semantics { contentDescription = "Normal loading indicator" }
                )
            }
            LabeledLoader("2×") {
                PixelLoader(
                    preset = PixelPresets.ClassicLoading,
                    speedMultiplier = 2f,
                    modifier = Modifier
                        .size(48.dp)
                        .semantics { contentDescription = "Fast loading indicator" }
                )
            }
        }

        SectionDivider()

        SectionHeader("9. Custom PixelAnimation (fromBinaryStrings)")
        NoteText("3×3 custom animation — alternates X and O patterns")
        val customAnimation = remember {
            PixelAnimation.fromBinaryStrings(
                columns = 3,
                rows = 3,
                binaryStrings = listOf(
                    "101010101",
                    "010101010",
                    "101010101",
                    "000111000",
                    "111000111",
                    "000111000"
                ),
                frameDurationMillis = 200L
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            PixelLoader(
                animation = customAnimation,
                modifier = Modifier
                    .size(64.dp)
                    .semantics { contentDescription = "Custom animation loading indicator" },
                color = Color(0xFFEC4899),
                inactiveColor = Color(0xFFFCE7F3),
                shape = PixelShape.Square,
                speedMultiplier = 1f
            )
        }

        SectionDivider()

        SectionHeader("10. Edge Case — PixelGridSize.Custom with Preset")
        NoteText("Custom(6,6) falls back to the preset's grid5x5 animation (library behavior)")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            PixelLoader(
                preset = PixelPresets.Rocket,
                gridSize = PixelGridSize.Custom(6, 6),
                modifier = Modifier
                    .size(64.dp)
                    .semantics { contentDescription = "Custom grid loading indicator" },
                color = Color(0xFFF59E0B)
            )
        }

        SectionDivider()

        SectionHeader("11. Switch Preset While on Screen")
        SwitchingPresetDemo()

        SectionDivider()

        SectionHeader("12. Realistic Loading State")
        LoadingStateDemo()

        SectionDivider()

        SectionHeader("13. Multiple Loaders Simultaneously")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            PixelLoader(
                preset = PixelPresets.Framer,
                modifier = Modifier
                    .size(40.dp)
                    .semantics { contentDescription = "Loading indicator 1" }
            )
            PixelLoader(
                preset = PixelPresets.Heartbeat,
                color = Color(0xFFEF4444),
                modifier = Modifier
                    .size(40.dp)
                    .semantics { contentDescription = "Loading indicator 2" }
            )
            PixelLoader(
                preset = PixelPresets.Matrix,
                color = Color(0xFF10B981),
                modifier = Modifier
                    .size(40.dp)
                    .semantics { contentDescription = "Loading indicator 3" }
            )
            PixelLoader(
                preset = PixelPresets.Pacman,
                color = Color(0xFFF59E0B),
                modifier = Modifier
                    .size(40.dp)
                    .semantics { contentDescription = "Loading indicator 4" }
            )
            PixelLoader(
                preset = PixelPresets.Snake,
                color = Color(0xFF8B5CF6),
                modifier = Modifier
                    .size(40.dp)
                    .semantics { contentDescription = "Loading indicator 5" }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun SectionHeader(text: String) {
    Text(
        text = text,
        fontSize = 13.sp,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(bottom = 12.dp, top = 4.dp)
    )
}

@Composable
private fun NoteText(text: String) {
    Text(
        text = text,
        fontSize = 11.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Composable
private fun SectionDivider() {
    HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
}

@Composable
private fun LabeledLoader(label: String, content: @Composable () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        content()
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = label, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun SwitchingPresetDemo() {
    val presets = remember {
        listOf(
            "Framer" to PixelPresets.Framer,
            "Rocket" to PixelPresets.Rocket,
            "Ripple" to PixelPresets.Ripple,
        )
    }
    var selectedIndex by remember { mutableStateOf(0) }
    val (label, preset) = presets[selectedIndex]

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        PixelLoader(
            preset = preset,
            gridSize = PixelGridSize.Grid5x5,
            modifier = Modifier
                .size(64.dp)
                .semantics { contentDescription = "Switching preset loading indicator" },
            color = Color(0xFF6366F1),
            shape = PixelShape.RoundedSquare
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Current: $label", fontSize = 12.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            presets.forEachIndexed { index, (name, _) ->
                Button(
                    onClick = { selectedIndex = index },
                    modifier = Modifier.height(32.dp)
                ) {
                    Text(name, fontSize = 11.sp)
                }
            }
        }
    }
}

@Composable
private fun LoadingStateDemo() {
    var isLoading by remember { mutableStateOf(false) }
    var result by remember { mutableStateOf<String?>(null) }
    var triggerCount by remember { mutableStateOf(0) }

    LaunchedEffect(triggerCount) {
        if (triggerCount == 0) return@LaunchedEffect
        isLoading = true
        result = null
        delay(3000L)
        isLoading = false
        result = "Loaded successfully after 3 s"
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            when {
                isLoading -> Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    PixelLoader(
                        preset = PixelPresets.Importing,
                        gridSize = PixelGridSize.Grid7x7,
                        modifier = Modifier
                            .size(64.dp)
                            .semantics { contentDescription = "Loading, please wait" },
                        color = MaterialTheme.colorScheme.primary,
                        inactiveColor = MaterialTheme.colorScheme.surfaceVariant,
                        shape = PixelShape.RoundedSquare,
                        speedMultiplier = 1.2f
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("Loading…", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }

                result != null -> Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("✓", fontSize = 32.sp, color = Color(0xFF10B981))
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(result!!, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }

                else -> Text(
                    "Press the button to start loading",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Button(
            onClick = { triggerCount++ },
            enabled = !isLoading
        ) {
            Text(if (triggerCount == 0) "Start loading" else "Reload")
        }
    }
}
