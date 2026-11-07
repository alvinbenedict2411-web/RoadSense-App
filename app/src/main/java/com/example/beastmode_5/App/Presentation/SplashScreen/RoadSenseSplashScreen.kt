package com.example.beastmode_5.App.Presentation.SplashScreen

import android.provider.Settings
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.util.lerp
import com.example.beastmode_5.R
import kotlin.math.cos
import kotlin.math.exp
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

// -----------------------------------------------------------------------------
// Design tokens (these four colours are the knobs for matching the app theme)
// -----------------------------------------------------------------------------

private val SplashBackground = Color(0xFF030B1E) // keep in sync with colors.xml
private val SplashCyan = Color(0xFF1FD0FF)
private val SplashElectricBlue = Color(0xFF1E6BFF)
private val SplashIce = Color(0xFFD8F6FF)

private const val TWO_PI = 6.2831855f

/** Timeline in milliseconds. One Animatable drives every stage so they can never drift apart. */
private object Timeline {
    const val TOTAL = 3500f

    const val DOT_END = 700f          // 1. cinematic intro: breathing point, rays, aura
    const val PIN_START = 600f        // 2. point -> location pin (overlaps the intro on purpose)
    const val PIN_END = 1400f
    const val WAVE_START = 1300f      // 3. circular light wave reveals the logo from the pin
    const val WAVE_END = 2100f
    const val SETTLE_START = 2000f    // 4. logo settles into place, light sweep, floor light
    const val SETTLE_END = 2800f
    const val WORD_START = 2350f      // 5. wordmark, then tagline, rise in
    const val WORD_END = 3050f
    const val LOAD_START = 2700f      // 6. loading light segment
    const val LOAD_END = 3400f
}

/**
 * Geometry of the logo artwork as fractions of the square 1254x1254 logo image.
 * Pin values were measured from the image (head width 551-862 px, top y=165 px, tip y=585 px,
 * hole centre ~ (705, 335) px, hole radius ~ 77 px).
 */
private object LogoGeometry {
    const val PIN_CX = 0.562f
    const val PIN_TOP = 0.131f
    const val PIN_TIP = 0.466f
    const val PIN_HALF_WIDTH = 0.1235f
    const val ANCHOR_Y = (PIN_TOP + PIN_TIP) / 2f // vertical centre of the pin

    // Wordmark + tagline band (kept inside the glowing frame, so the frame never moves).
    const val BAND_LEFT = 0.05f
    const val BAND_RIGHT = 0.95f
    const val BAND_TOP = 0.735f
    const val BAND_BOTTOM = 0.93f
    const val BAND_FEATHER = 0.02f
    const val TAGLINE_TOP = 0.862f // gap between the wordmark and the tagline
}

private const val START_INTENSITY = 0.5f   // must match the native splash dot (roadsense_splash_dot.xml)
private const val DRIFT_START_SCALE = 0.86f // slow push-in: logo scale at t = 0 ...
private const val LOGO_START_SCALE = 0.9f  // ... and when the settle begins
private const val HANDOFF_FADE_MS = 450

// Radial reveal: solid core, soft edge. The light ring rides the middle of the soft edge.
private const val REVEAL_SOLID = 0.55f
private const val REVEAL_MAX = 1.75f // large enough that the solid core covers the far corner
private const val RING_FRACTION = (1f + REVEAL_SOLID) / 2f

// Easings: everything eases in AND out, so no stage starts or stops abruptly.
private val EaseInOut = CubicBezierEasing(0.65f, 0f, 0.35f, 1f)
private val SettleEasing = CubicBezierEasing(0.22f, 1f, 0.36f, 1f)
private val SweepEasing = CubicBezierEasing(0.4f, 0f, 0.2f, 1f)
private val WaveEasing = CubicBezierEasing(0.3f, 0f, 0.15f, 1f)
private val Smoother: Easing = object : Easing {
    // Perlin's smootherstep: zero velocity AND zero acceleration at both ends.
    override fun transform(fraction: Float): Float {
        val x = fraction.coerceIn(0f, 1f)
        return x * x * x * (x * (x * 6f - 15f) + 10f)
    }
}
private val PinSpring = springEasing(damping = 7f, cycles = 1.25f)  // ~6% overshoot, then settles
private val LogoSpring = springEasing(damping = 7f, cycles = 1.0f)  // ~3% overshoot, very subtle

private fun segment(t: Float, start: Float, end: Float, easing: Easing = LinearEasing): Float =
    easing.transform(((t - start) / (end - start)).coerceIn(0f, 1f))

/** Smooth 0..1..0 pulse centred on [center]; used for bloom / streak moments. */
private fun bump(t: Float, center: Float, width: Float): Float {
    val x = (t - center) / width
    return exp(-x * x)
}

/** Damped-spring curve that starts at 0 and lands exactly on 1. */
private fun springEasing(damping: Float, cycles: Float): Easing {
    val endValue = 1f - exp(-damping) * cos(TWO_PI * cycles)
    return object : Easing {
        override fun transform(fraction: Float): Float {
            if (fraction <= 0f) return 0f
            if (fraction >= 1f) return 1f
            val raw = 1f - exp(-damping * fraction) * cos(TWO_PI * cycles * fraction)
            return raw + (1f - endValue) * fraction
        }
    }
}

/** Progress of the final settle (0 -> 1, with a tiny spring overshoot). */
private fun cameraProgress(t: Float): Float =
    segment(t, Timeline.SETTLE_START, Timeline.SETTLE_END, LogoSpring)

/**
 * Logo scale over time: a continuous slow push-in, then the spring settle to 1.0.
 * Shared by the logo group and the light overlay so the pin always stays aligned.
 */
private fun logoScale(t: Float): Float {
    val drift = lerp(
        DRIFT_START_SCALE,
        LOGO_START_SCALE,
        (t / Timeline.SETTLE_START).coerceIn(0f, 1f),
    )
    return lerp(drift, 1f, cameraProgress(t))
}

// -----------------------------------------------------------------------------
// Host: put this around your existing root content in MainActivity
// -----------------------------------------------------------------------------

/**
 * Plays the splash animation first, then composes [content] (your existing app)
 * underneath and dissolves the splash over it (fade + a very slight push-through).
 *
 * - [content] is NOT composed until the animation ends, so your NavHost, auth
 *   checks, permission prompts etc. behave exactly as before.
 * - The splash is not a navigation destination, so Back can never return to it.
 * - `finished` is saved, so rotation / process recreation after the splash
 *   does not replay it.
 */
@Composable
fun RoadSenseSplashHost(content: @Composable () -> Unit) {

    var finished by rememberSaveable { mutableStateOf(false) }

    // Swallow Back while the splash is playing so it cannot close the app mid-animation.
    BackHandler(enabled = !finished) { }

    Box(Modifier.fillMaxSize()) {
        if (finished) content()

        AnimatedVisibility(
            visible = !finished,
            exit = fadeOut(tween(HANDOFF_FADE_MS, easing = EaseInOut)) +
                    scaleOut(
                        animationSpec = tween(HANDOFF_FADE_MS, easing = EaseInOut),
                        targetScale = 1.04f,
                    ),
        ) {
            RoadSenseSplashScreen(onAnimationFinished = { finished = true })
        }
    }
}

// -----------------------------------------------------------------------------
// Splash screen
// -----------------------------------------------------------------------------

@Composable
fun RoadSenseSplashScreen(
    modifier: Modifier = Modifier,
    onAnimationFinished: () -> Unit,
) {
    val time = remember { Animatable(0f) }
    val timeState = remember {
        derivedStateOf { time.value }
    }
    val context = LocalContext.current
    val resources = context.resources

    // "Remove animations" in system settings sets the animator duration scale to 0.
    val reducedMotion = remember {
        Settings.Global.getFloat(
            context.contentResolver,
            Settings.Global.ANIMATOR_DURATION_SCALE,
            1f,
        ) == 0f
    }

    // Decoded once, off the main thread; first needed at 1.3 s.
    val logo = produceState<ImageBitmap?>(initialValue = null) {
        value = withContext(Dispatchers.Default) {
            ImageBitmap.imageResource(resources, R.drawable.roadsense_logo)
        }
    }

    val currentOnFinished by rememberUpdatedState(onAnimationFinished)

    LaunchedEffect(Unit) {
        if (reducedMotion) {
            time.snapTo(Timeline.TOTAL)
            snapshotFlow { logo.value }.first { it != null }
            delay(700)
        } else {
            time.animateTo(
                targetValue = Timeline.TOTAL,
                animationSpec = tween(Timeline.TOTAL.toInt(), easing = LinearEasing),
            )
        }
        currentOnFinished()
    }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(SplashBackground),
    ) {
        val logoSize = min(maxWidth * 0.88f, maxHeight * 0.6f)
        val logoSizePx = with(LocalDensity.current) { logoSize.toPx() }

        // Ambient aura, floor light and vignette (depth).
        AmbientLayer(time = timeState, logoSizePx = logoSizePx, modifier = Modifier.fillMaxSize())

        // Logo + wordmark share one transform so they always stay aligned.
        // The pin is held at the exact screen centre (where the native splash dot is) while the
        // camera slowly pushes in, then the group springs gently into its resting position.
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .size(logoSize)
                .graphicsLayer {
                    val t = timeState.value
                    val cam = cameraProgress(t)
                    val s = logoScale(t)
                    val pinDx = (LogoGeometry.PIN_CX - 0.5f) * size.width
                    val pinDy = (LogoGeometry.ANCHOR_Y - 0.5f) * size.height
                    scaleX = s
                    scaleY = s
                    translationX = -pinDx * s * (1f - cam)
                    translationY = -pinDy * s * (1f - cam)
                },
        ) {
            LogoLayer(logo = logo, time = timeState, modifier = Modifier.fillMaxSize())
            WordmarkLayer(logo = logo, time = timeState, modifier = Modifier.fillMaxSize())
        }

        // Full-screen light effects: breathing point, rays, pin, bloom, anamorphic streak, light wave.
        IntroOverlay(time = timeState, logoSizePx = logoSizePx, modifier = Modifier.fillMaxSize())

        LoadingLine(
            time = timeState,
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = logoSize / 2 + 32.dp)
                .width(logoSize * 0.5f)
                .height(3.dp),
        )
    }
}

// -----------------------------------------------------------------------------
// Ambient aura + floor light + vignette
// -----------------------------------------------------------------------------

@Composable
private fun AmbientLayer(
    time: State<Float>,
    logoSizePx: Float,
    modifier: Modifier = Modifier,
) {
    Spacer(
        modifier = modifier.drawWithCache {
            val center = Offset(size.width / 2f, size.height / 2f)

            val auraRadius = size.minDimension
            val auraBrush = Brush.radialGradient(
                colors = listOf(SplashElectricBlue, Color.Transparent),
                center = center,
                radius = auraRadius,
            )

            val vignetteBrush = Brush.radialGradient(
                colorStops = arrayOf(
                    0f to Color.Transparent,
                    0.6f to Color.Transparent,
                    1f to Color.Black,
                ),
                center = center,
                radius = maxOf(size.width, size.height) * 0.8f,
            )

            // A soft pool of light on the "floor" under the logo, right where the loading line sits.
            val floorCenter = Offset(center.x, center.y + logoSizePx * 0.5f + 33.5.dp.toPx())
            val floorRadius = logoSizePx * 0.62f
            val floorBrush = Brush.radialGradient(
                colors = listOf(SplashCyan, Color.Transparent),
                center = floorCenter,
                radius = floorRadius,
            )

            onDrawBehind {
                val t = time.value
                val intro = segment(t, 0f, Timeline.PIN_END, Smoother)
                if (intro <= 0f) return@onDrawBehind // keeps frame 0 identical to the native splash

                val full = segment(t, Timeline.WAVE_START, Timeline.SETTLE_END, Smoother)
                val floor = segment(t, Timeline.SETTLE_START, Timeline.SETTLE_END + 200f, Smoother)
                val breath = 0.5f + 0.5f * sin(TWO_PI * t / 1800f)

                val strength = ((0.10f * intro + 0.20f * full) * (0.9f + 0.1f * breath)).coerceIn(0f, 1f)
                val auraScale = lerp(0.6f, 1.0f, intro)
                withTransform({ scale(auraScale, auraScale, center) }) {
                    drawCircle(
                        brush = auraBrush,
                        radius = auraRadius,
                        center = center,
                        alpha = strength,
                    )
                }

                if (floor > 0f) {
                    withTransform({ scale(1f, 0.16f, floorCenter) }) {
                        drawCircle(
                            brush = floorBrush,
                            radius = floorRadius,
                            center = floorCenter,
                            alpha = 0.22f * floor * (0.85f + 0.15f * breath),
                        )
                    }
                }

                if (full > 0f) {
                    drawRect(brush = vignetteBrush, alpha = 0.45f * full)
                }
            }
        },
    )
}

// -----------------------------------------------------------------------------
// Logo artwork (stages 3 and 4)
// -----------------------------------------------------------------------------

/** Alpha mask that is 1 inside the wordmark band and feathers to 0 at its top and bottom edges. */
private fun bandFeatherBrush(top: Float, bottom: Float, layerHeight: Float): Brush {
    val f = (LogoGeometry.BAND_FEATHER * layerHeight / (bottom - top)).coerceIn(0.01f, 0.45f)
    return Brush.verticalGradient(
        colorStops = arrayOf(
            0f to Color.Transparent,
            f to Color.White,
            (1f - f) to Color.White,
            1f to Color.Transparent,
        ),
        startY = top,
        endY = bottom,
    )
}

/** Soft diagonal highlight band that travels left to right as [progress] goes 0 -> 1. */
private fun sweepBrush(width: Float, height: Float, progress: Float, peakAlpha: Float): Brush {
    val bandW = width * 0.30f
    val x0 = lerp(-bandW, width, progress)
    return Brush.linearGradient(
        colorStops = arrayOf(
            0f to Color.Transparent,
            0.5f to SplashIce.copy(alpha = peakAlpha),
            1f to Color.Transparent,
        ),
        start = Offset(x0, 0f),
        end = Offset(x0 + bandW, height * 0.22f),
    )
}

/**
 * The logo image minus its wordmark band. Revealed by a radial wave growing out of the pin,
 * followed by a luminous sweep. The wordmark band is cut out here and drawn by [WordmarkLayer].
 */
@Composable
private fun LogoLayer(
    logo: State<ImageBitmap?>,
    time: State<Float>,
    modifier: Modifier = Modifier,
) {
    Spacer(
        modifier = modifier
            // Offscreen so the DstIn / DstOut / SrcAtop passes only affect the logo pixels.
            .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
            .drawWithCache {
                val w = size.width
                val h = size.height
                val dst = IntSize(w.roundToInt(), h.roundToInt())
                val revealCenter = Offset(LogoGeometry.PIN_CX * w, LogoGeometry.ANCHOR_Y * h)
                val bandTopLeft = Offset(LogoGeometry.BAND_LEFT * w, LogoGeometry.BAND_TOP * h)
                val bandSize = Size(
                    (LogoGeometry.BAND_RIGHT - LogoGeometry.BAND_LEFT) * w,
                    (LogoGeometry.BAND_BOTTOM - LogoGeometry.BAND_TOP) * h,
                )
                val holeBrush = bandFeatherBrush(bandTopLeft.y, bandTopLeft.y + bandSize.height, h)

                onDrawBehind {
                    val image = logo.value ?: return@onDrawBehind
                    val t = time.value
                    if (t < Timeline.WAVE_START) return@onDrawBehind

                    drawImage(image = image, dstSize = dst, filterQuality = FilterQuality.High)

                    // Stage 3: circular reveal growing out of the pin (solid core, wide soft edge).
                    val reveal = segment(t, Timeline.WAVE_START, Timeline.WAVE_END, WaveEasing)
                    if (reveal < 1f) {
                        drawRect(
                            brush = Brush.radialGradient(
                                colorStops = arrayOf(
                                    0f to Color.White,
                                    REVEAL_SOLID to Color.White,
                                    1f to Color.Transparent,
                                ),
                                center = revealCenter,
                                radius = lerp(0.02f, REVEAL_MAX, reveal) * w,
                            ),
                            blendMode = BlendMode.DstIn,
                        )
                    }

                    // The wordmark band is rendered separately (so it can rise and sweep on its own).
                    drawRect(
                        brush = holeBrush,
                        topLeft = bandTopLeft,
                        size = bandSize,
                        blendMode = BlendMode.DstOut,
                    )

                    // Stage 4: restrained luminous sweep across the artwork.
                    val sweep = segment(t, Timeline.SETTLE_START + 50f, Timeline.SETTLE_END + 150f, SweepEasing)
                    if (sweep > 0f && sweep < 1f) {
                        drawRect(
                            brush = sweepBrush(w, h, sweep, peakAlpha = 0.20f),
                            blendMode = BlendMode.SrcAtop,
                        )
                    }
                }
            },
    )
}

/**
 * The wordmark + tagline, taken straight from the logo image (nothing is re-typed).
 * The wordmark fades in and rises a few percent into place, then the tagline follows a beat later.
 * Once finished it is pixel-for-pixel the region that [LogoLayer] cut out, so the logo is intact.
 */
@Composable
private fun WordmarkLayer(
    logo: State<ImageBitmap?>,
    time: State<Float>,
    modifier: Modifier = Modifier,
) {
    Spacer(
        modifier = modifier
            .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
            .drawWithCache {
                val w = size.width
                val h = size.height
                val left = LogoGeometry.BAND_LEFT * w
                val right = LogoGeometry.BAND_RIGHT * w
                val top = LogoGeometry.BAND_TOP * h
                val bottom = LogoGeometry.BAND_BOTTOM * h
                val dstOffset = IntOffset(left.roundToInt(), top.roundToInt())
                val dstSize = IntSize((right - left).roundToInt(), (bottom - top).roundToInt())
                val featherBrush = bandFeatherBrush(top, bottom, h)

                // Where the tagline starts inside the band, as a fraction, with a soft transition
                // that stays inside the empty gap between wordmark and tagline.
                val bandH = bottom - top
                val split = (LogoGeometry.TAGLINE_TOP * h - top) / bandH
                val splitSoft = (0.008f * h / bandH).coerceAtLeast(0.01f)

                onDrawBehind {
                    val image = logo.value ?: return@onDrawBehind
                    val t = time.value
                    val move = segment(t, Timeline.WORD_START, Timeline.WORD_END, SettleEasing)
                    if (move <= 0f) return@onDrawBehind

                    val wordAlpha = segment(t, Timeline.WORD_START, Timeline.WORD_START + 450f, Smoother)
                    val tagAlpha = segment(t, Timeline.WORD_START + 280f, Timeline.WORD_START + 730f, Smoother)
                    val rise = (1f - move) * 0.03f * h

                    val srcOffset = IntOffset(
                        (LogoGeometry.BAND_LEFT * image.width).roundToInt(),
                        (LogoGeometry.BAND_TOP * image.height).roundToInt(),
                    )
                    val srcSize = IntSize(
                        ((LogoGeometry.BAND_RIGHT - LogoGeometry.BAND_LEFT) * image.width).roundToInt(),
                        ((LogoGeometry.BAND_BOTTOM - LogoGeometry.BAND_TOP) * image.height).roundToInt(),
                    )

                    withTransform({ translate(0f, rise) }) {
                        drawImage(
                            image = image,
                            srcOffset = srcOffset,
                            srcSize = srcSize,
                            dstOffset = dstOffset,
                            dstSize = dstSize,
                            filterQuality = FilterQuality.High,
                        )
                        // Different fade levels for wordmark (top) and tagline (bottom).
                        drawRect(
                            brush = Brush.verticalGradient(
                                colorStops = arrayOf(
                                    0f to Color.White.copy(alpha = wordAlpha),
                                    (split - splitSoft) to Color.White.copy(alpha = wordAlpha),
                                    (split + splitSoft) to Color.White.copy(alpha = tagAlpha),
                                    1f to Color.White.copy(alpha = tagAlpha),
                                ),
                                startY = top,
                                endY = bottom,
                            ),
                            topLeft = Offset(left, top),
                            size = Size(right - left, bandH),
                            blendMode = BlendMode.DstIn,
                        )
                        drawRect(
                            brush = featherBrush,
                            topLeft = Offset(left, top),
                            size = Size(right - left, bandH),
                            blendMode = BlendMode.DstIn,
                        )
                    }

                    // Refined light sweep, only visible where the band has pixels.
                    val sweep = segment(t, Timeline.WORD_START + 250f, Timeline.WORD_END + 350f, SweepEasing)
                    if (sweep > 0f && sweep < 1f) {
                        drawRect(
                            brush = sweepBrush(w, h, sweep, peakAlpha = 0.28f),
                            blendMode = BlendMode.SrcAtop,
                        )
                    }
                }
            },
    )
}

// -----------------------------------------------------------------------------
// Stages 1-3 light effects: breathing point, rays, pin, bloom, streak, light wave
// -----------------------------------------------------------------------------

@Composable
private fun IntroOverlay(
    time: State<Float>,
    logoSizePx: Float,
    modifier: Modifier = Modifier,
) {
    Spacer(
        modifier = modifier.drawWithCache {
            // Everything lives in screen space; the pin anchor is the exact screen centre,
            // which is where the native splash dot is and where the logo's own pin is held.
            val anchor = Offset(size.width / 2f, size.height / 2f)
            val k = logoSizePx // pin geometry at logo scale 1.0; scaled per frame by logoScale(t)
            fun px(fx: Float) = anchor.x + (fx - LogoGeometry.PIN_CX) * k
            fun py(fy: Float) = anchor.y + (fy - LogoGeometry.ANCHOR_Y) * k

            // Glowing point (matches roadsense_splash_dot.xml at t = 0).
            val dotGlowBase = 44.dp.toPx()
            val dotGlowEnd = 78.dp.toPx()
            val dotGlowMerge = 40.dp.toPx()
            val coreRadius = 5.dp.toPx()
            val dotGlowBrush = Brush.radialGradient(
                colorStops = arrayOf(
                    0f to SplashCyan.copy(alpha = 0.55f),
                    0.35f to SplashElectricBlue.copy(alpha = 0.28f),
                    1f to Color.Transparent,
                ),
                center = anchor,
                radius = dotGlowBase,
            )

            // Subtle light rays: one thin gradient streak, rotated and scaled per ray.
            val rayBase = 100.dp.toPx()
            val rayThickness = 1.5.dp.toPx()
            val rayBrush = Brush.horizontalGradient(
                colorStops = arrayOf(
                    0f to Color.Transparent,
                    0.5f to SplashCyan.copy(alpha = 0.55f),
                    1f to Color.Transparent,
                ),
                startX = anchor.x - rayBase,
                endX = anchor.x + rayBase,
            )

            // Bloom: a short, soft flash at the moment the pin springs into shape.
            val bloomRadius = 0.55f * k
            val bloomBrush = Brush.radialGradient(
                colors = listOf(SplashIce.copy(alpha = 0.55f), Color.Transparent),
                center = anchor,
                radius = bloomRadius,
            )

            // Anamorphic streak: a wide radial gradient squashed vertically into a thin lens flare.
            val streakRadius = size.width * 0.6f
            val streakBrush = Brush.radialGradient(
                colorStops = arrayOf(
                    0f to SplashIce.copy(alpha = 0.60f),
                    0.3f to SplashCyan.copy(alpha = 0.30f),
                    1f to Color.Transparent,
                ),
                center = anchor,
                radius = streakRadius,
            )

            // Location pin: one outline (teardrop) with the hole as an even-odd sub-path.
            val pinCx = px(LogoGeometry.PIN_CX)
            val pinR = LogoGeometry.PIN_HALF_WIDTH * k
            val pinTop = py(LogoGeometry.PIN_TOP)
            val pinCy = py(LogoGeometry.PIN_TOP + LogoGeometry.PIN_HALF_WIDTH)
            val pinTip = py(LogoGeometry.PIN_TIP)
            val pinPath = Path().apply {
                fillType = PathFillType.EvenOdd
                moveTo(pinCx - pinR * 0.866f, pinCy + pinR * 0.5f)
                arcTo(Rect(Offset(pinCx, pinCy), pinR), 150f, 240f, false)
                cubicTo(
                    pinCx + pinR * 0.716f, pinCy + pinR * 0.95f,
                    pinCx + pinR * 0.25f, pinCy + pinR * 1.4f,
                    pinCx, pinTip,
                )
                cubicTo(
                    pinCx - pinR * 0.25f, pinCy + pinR * 1.4f,
                    pinCx - pinR * 0.716f, pinCy + pinR * 0.95f,
                    pinCx - pinR * 0.866f, pinCy + pinR * 0.5f,
                )
                close()
                addOval(Rect(Offset(pinCx, pinCy + pinR * 0.1f), pinR * 0.49f))
            }
            val pinBrush = Brush.linearGradient(
                colors = listOf(Color.White, Color(0xFFC6F1FF), Color(0xFF63D3FF)),
                start = Offset(pinCx - pinR, pinTop),
                end = Offset(pinCx + pinR, pinTip),
            )
            val pinGlowRadius = 0.4f * k
            val pinGlowBrush = Brush.radialGradient(
                colorStops = arrayOf(
                    0f to SplashCyan.copy(alpha = 0.45f),
                    0.5f to SplashElectricBlue.copy(alpha = 0.18f),
                    1f to Color.Transparent,
                ),
                center = anchor,
                radius = pinGlowRadius,
            )

            onDrawBehind {
                val t = time.value
                val ls = logoScale(t)
                // Breathing fades out as the pin takes shape. sin(0) = 0 keeps frame 0 identical to the native dot.
                val breath = sin(TWO_PI * t / 1000f) * (1f - segment(t, Timeline.PIN_START, Timeline.PIN_END, Smoother))

                // ---- Stage 1: breathing glow point ----
                val dotIn = segment(t, 0f, Timeline.DOT_END, EaseInOut)
                val dotOut = 1f - segment(t, Timeline.PIN_START, Timeline.PIN_START + 450f, Smoother)
                val intensity = (lerp(START_INTENSITY, 1f, dotIn) * dotOut * (1f + 0.12f * breath))
                    .coerceIn(0f, 1f)
                if (intensity > 0f) {
                    val glow = (
                            lerp(dotGlowBase, dotGlowEnd, dotIn) +
                                    lerp(0f, dotGlowMerge, segment(t, Timeline.PIN_START, Timeline.PIN_END, Smoother))
                            ) * (1f + 0.07f * breath)
                    val glowScale = glow / dotGlowBase
                    withTransform({ scale(glowScale, glowScale, anchor) }) {
                        drawCircle(
                            brush = dotGlowBrush,
                            radius = dotGlowBase,
                            center = anchor,
                            alpha = intensity,
                        )
                    }
                    drawCircle(
                        color = Color.White,
                        radius = coreRadius,
                        center = anchor,
                        alpha = intensity,
                    )
                }

                // ---- Stage 1: subtle light rays ----
                val rayAlpha = segment(t, 150f, 600f, Smoother) *
                        (1f - segment(t, Timeline.PIN_START + 100f, Timeline.PIN_START + 650f, Smoother))
                if (rayAlpha > 0f) {
                    val rayLength = lerp(0.6f, 1.5f, segment(t, 0f, Timeline.PIN_START, EaseInOut)) *
                            (1f + 0.08f * breath)
                    for (i in 0 until 4) {
                        val angle = i * 45f + t * 0.006f
                        val lengthScale = rayLength * (if (i % 2 == 0) 1f else 0.6f)
                        withTransform({
                            rotate(angle, anchor)
                            scale(lengthScale, 1f, anchor)
                        }) {
                            drawRect(
                                brush = rayBrush,
                                topLeft = Offset(anchor.x - rayBase, anchor.y - rayThickness / 2f),
                                size = Size(rayBase * 2f, rayThickness),
                                alpha = rayAlpha * (if (i % 2 == 0) 1f else 0.6f),
                            )
                        }
                    }
                }

                // ---- Stage 2: bloom + anamorphic streak at the spring's peak ----
                val flash = bump(t, 950f, 220f)
                if (flash > 0.01f) {
                    withTransform({ scale(ls, ls, anchor) }) {
                        drawCircle(
                            brush = bloomBrush,
                            radius = bloomRadius,
                            center = anchor,
                            alpha = 0.8f * flash,
                        )
                    }
                    withTransform({ scale(1f, 0.035f, anchor) }) {
                        drawCircle(
                            brush = streakBrush,
                            radius = streakRadius,
                            center = anchor,
                            alpha = flash,
                        )
                    }
                }

                // ---- Stage 2: pin forms from the point, spring-settles, then hands over to the real logo ----
                val pinProgress = segment(t, Timeline.PIN_START, Timeline.PIN_END, PinSpring)
                val pinAlpha =
                    segment(t, Timeline.PIN_START, Timeline.PIN_START + 400f, Smoother) *
                            (1f - segment(t, Timeline.WAVE_START + 50f, Timeline.WAVE_START + 500f, Smoother))
                if (pinAlpha > 0f) {
                    val pinScale = lerp(0.12f, 1f, pinProgress) * ls
                    val glowScale = pinScale * (1f + 0.03f * sin(TWO_PI * t / 900f))

                    withTransform({ scale(glowScale, glowScale, anchor) }) {
                        drawCircle(
                            brush = pinGlowBrush,
                            radius = pinGlowRadius,
                            center = anchor,
                            alpha = pinAlpha,
                        )
                    }
                    withTransform({ scale(pinScale, pinScale, anchor) }) {
                        drawPath(path = pinPath, brush = pinBrush, alpha = pinAlpha)
                    }
                }

                // ---- Stage 3: expanding light wave (plus a fainter echo), locked to the logo's radial reveal ----
                val reveal = segment(t, Timeline.WAVE_START, Timeline.WAVE_END, WaveEasing)
                if (t >= Timeline.WAVE_START && reveal < 1f) {
                    val revealRadius = lerp(0.02f, REVEAL_MAX, reveal) * logoSizePx * ls
                    val waveAlpha = (1f - reveal) * segment(t, Timeline.WAVE_START, Timeline.WAVE_START + 200f, Smoother)
                    val thickness = revealRadius * 0.10f + 6.dp.toPx()
                    drawLightRing(anchor, revealRadius * RING_FRACTION, thickness, 0.55f * waveAlpha)
                    drawLightRing(anchor, revealRadius * RING_FRACTION * 0.7f, thickness * 0.8f, 0.20f * waveAlpha)
                }
            }
        },
    )
}

/** Soft ring of light: transparent inside, brightest at [radius], transparent outside. */
private fun DrawScope.drawLightRing(center: Offset, radius: Float, thickness: Float, alpha: Float) {
    if (alpha <= 0.002f || radius <= 0f) return
    val outer = radius + thickness
    val inner = ((radius - thickness) / outer).coerceIn(0f, 0.98f)
    val mid = radius / outer
    drawCircle(
        brush = Brush.radialGradient(
            colorStops = arrayOf(
                0f to Color.Transparent,
                inner to Color.Transparent,
                mid to SplashIce.copy(alpha = alpha),
                1f to Color.Transparent,
            ),
            center = center,
            radius = outer,
        ),
        radius = outer,
        center = center,
    )
}

// -----------------------------------------------------------------------------
// Loading indicator (stage 6)
// -----------------------------------------------------------------------------

/** A glowing light segment gliding along a faint rounded track. Indeterminate: not real progress. */
@Composable
private fun LoadingLine(
    time: State<Float>,
    modifier: Modifier = Modifier,
) {
    Spacer(
        modifier = modifier.drawBehind {
            val t = time.value
            val appear = segment(t, Timeline.LOAD_START - 150f, Timeline.LOAD_START + 150f, Smoother)
            if (appear <= 0f) return@drawBehind

            val w = size.width
            val h = size.height
            val corner = CornerRadius(h / 2f)

            // The line grows out from its centre as it appears.
            withTransform({ scale(lerp(0.4f, 1f, appear), 1f, Offset(w / 2f, h / 2f)) }) {
                drawRoundRect(
                    color = SplashCyan.copy(alpha = 0.14f * appear),
                    size = size,
                    cornerRadius = corner,
                )

                val p = segment(t, Timeline.LOAD_START, Timeline.LOAD_END, EaseInOut)
                val segW = w * 0.42f
                val left = lerp(-segW, w, p)

                // Single smooth pass: the segment enters, crosses and exits, then the splash hands off.
                clipRect(0f, -h * 8f, w, h * 9f) {
                    // Soft halo travelling with the segment.
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(SplashCyan.copy(alpha = 0.40f * appear), Color.Transparent),
                            center = Offset(left + segW * 0.5f, h / 2f),
                            radius = 16.dp.toPx(),
                        ),
                        radius = 16.dp.toPx(),
                        center = Offset(left + segW * 0.5f, h / 2f),
                    )
                    // The bright segment with rounded ends.
                    drawRoundRect(
                        brush = Brush.horizontalGradient(
                            colorStops = arrayOf(
                                0f to Color.Transparent,
                                0.5f to SplashCyan,
                                1f to Color.Transparent,
                            ),
                            startX = left,
                            endX = left + segW,
                        ),
                        topLeft = Offset(left, 0f),
                        size = Size(segW, h),
                        cornerRadius = corner,
                        alpha = appear,
                    )
                }
            }
        },
    )
}
