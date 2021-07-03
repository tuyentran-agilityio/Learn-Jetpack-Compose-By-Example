package com.example.jetpackcompose.customview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.zoomable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.gesture.DragObserver
import androidx.compose.ui.gesture.rawDragGestureFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.activity.compose.setContent
import androidx.compose.ui.res.painterResource
import com.example.jetpackcompose.R

class ZoomableActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // This is an extension function of Activity that sets the @Composable function that's
        // passed to it as the root view of the activity. This is meant to replace the .xml file
        // that we would typically set using the setContent(R.id.xml_file) method. The setContent
        // block defines the activity's layout.
        setContent {
            ZoomableComposable()
        }
    }
}

// We represent a Composable function by annotating it with the @Composable annotation. Composable
// functions can only be called from within the scope of other composable functions. We should 
// think of composable functions to be similar to lego blocks - each composable function is in turn 
// built up of smaller composable functions.
@Composable
fun ZoomableComposable() {
    // Reacting to state changes is the core behavior of Compose. We use the state composable
    // that is used for holding a state value in this composable for representing the current
    // value scale(for zooming in the image) & translation(for panning across the image). Any 
    // composable that reads the value of counter will be recomposed any time the value changes. 
    // This ensures that only the composables that depend on this will be redraw while the 
    // rest remain unchanged. This ensures efficiency and is a performance optimization. It 
    // is inspired from existing frameworks like React.
    var scale by remember { mutableStateOf(1f) }
    var translate by remember { mutableStateOf(Offset(0f, 0f)) }

    // Column is a composable that places its children in a vertical sequence. You
    // can think of it similar to a LinearLayout with the vertical orientation. 
    // In addition we also pass a few modifiers to it.

    // You can think of Modifiers as implementations of the decorators pattern that are used to
    // modify the composable that its applied to. In the example below, we configure the
    // Box. In the example below, we make the Box composable zoomable by assigning the 
    // Modifier.zoomable modifier & also add a drag observer to it(for panning functionality)
    // by using the rawDragGestureFilter modifier. 
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.zoomable(onZoomDelta = { scale *= it }).rawDragGestureFilter(
            object : DragObserver {
                override fun onDrag(dragDistance: Offset): Offset {
                    translate = translate.plus(dragDistance)
                    return super.onDrag(dragDistance)
                }
            })
    ) {
        // There are multiple methods available to load an image resource in Compose. 
        // However, it would be advisable to use the painterResource method as it loads
        // an image resource asynchronously
        val imagepainter = painterResource(id = R.drawable.landscape)
        // Image is a pre-defined composable that lays out and draws a given [ImageBitmap].
        // We use the graphicsLayer modifier to modify the scale & translation of the image.
        // This is read from the state properties that we created above.
        Image(
            modifier = Modifier.fillMaxSize().graphicsLayer(
                scaleX = scale,
                scaleY = scale,
                translationX = translate.x,
                translationY = translate.y
            ),
            painter = imagepainter,
            contentDescription = "Landscape Image"
        )
    }
}

/**
 * Android Studio lets you preview your composable functions within the IDE itself, instead of
 * needing to download the app to an Android device or emulator. This is a fantastic feature as you
 * can preview all your custom components(read composable functions) from the comforts of the IDE.
 * The main restriction is, the composable function must not take any parameters. If your composable
 * function requires a parameter, you can simply wrap your component inside another composable
 * function that doesn't take any parameters and call your composable function with the appropriate
 * params. Also, don't forget to annotate it with @Preview & @Composable annotations.
 */
@Composable
fun ZoomableComposablePreview() {
    ZoomableComposable()
}
