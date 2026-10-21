package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.DirectionsCar
import androidx.compose.material.icons.rounded.ElectricBolt
import androidx.compose.material.icons.rounded.Speed
import androidx.compose.material.icons.rounded.SportsMotorsports
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.ElectricBlueGlow
import com.example.ui.theme.GarageBackground
import com.example.ui.theme.GarageSurface
import com.example.ui.theme.GarageSurfaceBorder
import com.example.ui.theme.GarageSurfaceVariant
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.NeonOrange
import com.example.ui.theme.StatAccelerationColor
import com.example.ui.theme.StatHandlingColor
import com.example.ui.theme.StatSpeedColor
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

enum class VehicleType {
  SPORT_COUPE,
  HYPERCAR,
  EV_PROTOTYPE
}

data class VehicleModel(
  val id: String,
  val name: String,
  val classification: String,
  val description: String,
  val speed: Float,
  val speedLabel: String,
  val handling: Float,
  val handlingLabel: String,
  val acceleration: Float,
  val accelerationLabel: String,
  val accentColor: Color,
  val vehicleType: VehicleType,
)

val sampleVehicles =
  listOf(
    VehicleModel(
      id = "vento_gt",
      name = "Vento GT",
      classification = "SUPERCAR • TWIN TURBO V8",
      description =
        "Aerodynamically refined sport coupe engineered with active downforce flaps and surgical steering response for high-speed track endurance.",
      speed = 0.92f,
      speedLabel = "324 km/h",
      handling = 0.88f,
      handlingLabel = "8.8 / 10",
      acceleration = 0.95f,
      accelerationLabel = "2.8s 0-100",
      accentColor = ElectricBlue,
      vehicleType = VehicleType.SPORT_COUPE,
    ),
    VehicleModel(
      id = "aero_stryker",
      name = "Aero Stryker",
      classification = "HYPERCAR • LIGHTWEIGHT CARBON",
      description =
        "Track-dedicated aerodynamic hypercar utilizing full monocoque carbon fiber construction and dual ground-effect venturi channels.",
      speed = 0.97f,
      speedLabel = "350 km/h",
      handling = 0.94f,
      handlingLabel = "9.4 / 10",
      acceleration = 0.89f,
      accelerationLabel = "3.0s 0-100",
      accentColor = NeonOrange,
      vehicleType = VehicleType.HYPERCAR,
    ),
    VehicleModel(
      id = "apex_phantom",
      name = "Apex Phantom",
      classification = "EV PROTOTYPE • QUAD MOTOR",
      description =
        "All-electric powertrain delivering instantaneous all-wheel torque vectoring with active electromagnetic adaptive suspension.",
      speed = 0.90f,
      speedLabel = "315 km/h",
      handling = 0.92f,
      handlingLabel = "9.2 / 10",
      acceleration = 0.99f,
      accelerationLabel = "1.9s 0-100",
      accentColor = Color(0xFF00F5D4),
      vehicleType = VehicleType.EV_PROTOTYPE,
    ),
  )

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        BenchmarkAppRoot()
      }
    }
  }
}

@Composable
fun BenchmarkAppRoot(modifier: Modifier = Modifier) {
  var showShowroom by remember { mutableStateOf(false) }

  Box(modifier = modifier.fillMaxSize().background(GarageBackground)) {
    if (showShowroom) {
      GarageScreen(
        onLaunchBenchmark = { showShowroom = false }
      )
    } else {
      Benchmark3DWebViewScreen(
        onOpenGarage = { showShowroom = true }
      )
    }
  }
}

@android.annotation.SuppressLint("SetJavaScriptEnabled")
@Composable
fun Benchmark3DWebViewScreen(
  onOpenGarage: () -> Unit,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .fillMaxSize()
      .background(GarageBackground)
      .testTag("benchmark_3d_screen")
  ) {
    androidx.compose.ui.viewinterop.AndroidView(
      modifier = Modifier.fillMaxSize(),
      factory = { ctx ->
        android.webkit.WebView(ctx).apply {
          settings.javaScriptEnabled = true
          settings.domStorageEnabled = true
          settings.mediaPlaybackRequiresUserGesture = false
          settings.allowFileAccess = true
          setBackgroundColor(android.graphics.Color.parseColor("#080B11"))
          webChromeClient = android.webkit.WebChromeClient()
          loadUrl("file:///android_asset/benchmark_3d.html")
        }
      }
    )

    // Subtle Native Android Showroom Switcher Pill in Bottom-Center
    Box(
      modifier = Modifier
        .align(Alignment.BottomCenter)
        .padding(bottom = 18.dp)
        .clip(RoundedCornerShape(12.dp))
        .background(GarageSurface.copy(alpha = 0.88f))
        .border(1.dp, GarageSurfaceBorder, RoundedCornerShape(12.dp))
        .clickable { onOpenGarage() }
        .padding(horizontal = 14.dp, vertical = 8.dp)
        .testTag("switch_to_garage_button")
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        Icon(
          imageVector = Icons.Rounded.DirectionsCar,
          contentDescription = "Garage Showroom",
          tint = ElectricBlue,
          modifier = Modifier.size(16.dp)
        )
        Text(
          text = "GARAGE SPECS",
          style = MaterialTheme.typography.labelSmall.copy(
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
          ),
          color = TextPrimary
        )
      }
    }
  }
}

@Composable
fun GarageScreen(
  modifier: Modifier = Modifier,
  onLaunchBenchmark: () -> Unit = {}
) {
  val context = LocalContext.current
  var selectedIndex by remember { mutableIntStateOf(0) }
  val currentVehicle = sampleVehicles[selectedIndex]
  var selectionConfirmedName by remember { mutableStateOf<String?>(null) }

  Scaffold(
    modifier = modifier
      .fillMaxSize()
      .background(GarageBackground)
      .testTag("garage_screen"),
    containerColor = GarageBackground,
    topBar = {
      GarageTopBar()
    },
    bottomBar = {
      GarageBottomBar(
        vehicleName = currentVehicle.name,
        accentColor = currentVehicle.accentColor,
        isConfirmed = selectionConfirmedName == currentVehicle.name,
        onSelectClick = {
          selectionConfirmedName = currentVehicle.name
          Toast.makeText(
            context,
            "${currentVehicle.name} selected as active vehicle!",
            Toast.LENGTH_SHORT
          ).show()
          onLaunchBenchmark()
        }
      )
    }
  ) { innerPadding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .verticalScroll(rememberScrollState())
        .padding(horizontal = 20.dp, vertical = 12.dp),
      verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
      // 1. Large Hero Card showing Car Image Placeholder, Car Name, and Description
      CarHeroCard(vehicle = currentVehicle)

      // 2. Three Horizontal Stat Bars: Speed, Handling, Acceleration
      CarStatsSection(vehicle = currentVehicle)

      // 3. Horizontally Scrollable Row of 3 Selectable Car Cards
      VehicleSelectorRow(
        vehicles = sampleVehicles,
        selectedIndex = selectedIndex,
        onSelectVehicle = { index ->
          selectedIndex = index
        }
      )

      Spacer(modifier = Modifier.height(12.dp))
    }
  }
}

@Composable
fun GarageTopBar() {
  Surface(
    modifier = Modifier.fillMaxWidth(),
    color = GarageBackground,
    tonalElevation = 0.dp
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp, vertical = 16.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        Box(
          modifier = Modifier
            .size(40.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(GarageSurfaceVariant)
            .border(1.dp, GarageSurfaceBorder, RoundedCornerShape(12.dp)),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Rounded.DirectionsCar,
            contentDescription = "Garage Icon",
            tint = ElectricBlue,
            modifier = Modifier.size(22.dp)
          )
        }

        Column {
          Text(
            text = stringResource(R.string.garage_title),
            style = MaterialTheme.typography.titleLarge.copy(
              fontWeight = FontWeight.ExtraBold,
              letterSpacing = 1.2.sp
            ),
            color = TextPrimary
          )
          Text(
            text = "VEHICLE SHOWROOM",
            style = MaterialTheme.typography.labelSmall.copy(
              letterSpacing = 1.5.sp,
              fontWeight = FontWeight.SemiBold
            ),
            color = ElectricBlue
          )
        }
      }

      // Fleet Count Indicator Badge
      Box(
        modifier = Modifier
          .clip(RoundedCornerShape(20.dp))
          .background(GarageSurfaceVariant)
          .border(1.dp, GarageSurfaceBorder, RoundedCornerShape(20.dp))
          .padding(horizontal = 12.dp, vertical = 6.dp)
      ) {
        Text(
          text = "3 MODELS",
          style = MaterialTheme.typography.labelSmall.copy(
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.8.sp
          ),
          color = TextSecondary
        )
      }
    }
  }
}

@Composable
fun CarHeroCard(
  vehicle: VehicleModel,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(24.dp))
      .background(
        Brush.verticalGradient(
          colors = listOf(
            GarageSurfaceVariant,
            GarageSurface
          )
        )
      )
      .border(
        width = 1.dp,
        brush = Brush.linearGradient(
          colors = listOf(
            vehicle.accentColor.copy(alpha = 0.6f),
            GarageSurfaceBorder,
            GarageSurfaceBorder
          )
        ),
        shape = RoundedCornerShape(24.dp)
      )
      .padding(20.dp)
  ) {
    Column(
      modifier = Modifier.fillMaxWidth(),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // Top row: Class Badge & Accent Indicator
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(vehicle.accentColor.copy(alpha = 0.15f))
            .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
          Text(
            text = vehicle.classification,
            style = MaterialTheme.typography.labelSmall.copy(
              fontWeight = FontWeight.Bold,
              letterSpacing = 1.sp
            ),
            color = vehicle.accentColor
          )
        }

        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          Box(
            modifier = Modifier
              .size(8.dp)
              .clip(CircleShape)
              .background(vehicle.accentColor)
          )
          Text(
            text = "READY",
            style = MaterialTheme.typography.labelSmall.copy(
              fontWeight = FontWeight.Bold,
              letterSpacing = 0.8.sp
            ),
            color = TextSecondary
          )
        }
      }

      // Car Image Placeholder (Stylized Aerodynamic Canvas Silhouette Graphic)
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(180.dp)
          .clip(RoundedCornerShape(16.dp))
          .background(
            Brush.verticalGradient(
              colors = listOf(
                Color(0xFF0F1420),
                GarageBackground,
                Color(0xFF07090E)
              )
            )
          )
          .border(
            1.dp,
            Brush.verticalGradient(
              colors = listOf(
                vehicle.accentColor.copy(alpha = 0.35f),
                GarageSurfaceBorder.copy(alpha = 0.6f)
              )
            ),
            RoundedCornerShape(16.dp)
          ),
        contentAlignment = Alignment.Center
      ) {
        // Subtle animated showroom spotlight
        val infiniteTransition = rememberInfiniteTransition(label = "studio_light")
        val pulseAlpha by infiniteTransition.animateFloat(
          initialValue = 0.12f,
          targetValue = 0.28f,
          animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
          ),
          label = "spotlight_pulse"
        )

        Box(
          modifier = Modifier
            .fillMaxSize()
            .background(
              Brush.radialGradient(
                colors = listOf(
                  vehicle.accentColor.copy(alpha = pulseAlpha),
                  Color.Transparent
                ),
                center = Offset(200f, 0f),
                radius = 600f
              )
            )
        )

        CarSilhouetteGraphic(
          modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
          accentColor = vehicle.accentColor,
          vehicleType = vehicle.vehicleType
        )

        // Watermark Label
        Text(
          text = "CAR IMAGE PLACEHOLDER",
          style = MaterialTheme.typography.labelSmall.copy(
            letterSpacing = 1.8.sp,
            fontWeight = FontWeight.SemiBold
          ),
          color = TextMuted.copy(alpha = 0.45f),
          modifier = Modifier
            .align(Alignment.BottomCenter)
            .padding(bottom = 8.dp)
        )
      }

      // Car Name & Description with smooth crossfade
      AnimatedContent(
        targetState = vehicle,
        transitionSpec = {
          fadeIn(animationSpec = tween(220)) togetherWith fadeOut(animationSpec = tween(150))
        },
        label = "hero_text_transition"
      ) { currentCar ->
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
          Text(
            text = currentCar.name,
            style = MaterialTheme.typography.headlineMedium.copy(
              fontWeight = FontWeight.ExtraBold,
              letterSpacing = 0.5.sp
            ),
            color = TextPrimary
          )

          Text(
            text = currentCar.description,
            style = MaterialTheme.typography.bodyMedium.copy(
              lineHeight = 20.sp
            ),
            color = TextSecondary
          )
        }
      }
    }
  }
}

/**
 * High-fidelity vector graphics engine for vehicles with distinctive body profiles,
 * realistic headlights/taillights, metallic body gradients, brake calipers, and alloy spokes.
 */
@Composable
fun CarSilhouetteGraphic(
  modifier: Modifier = Modifier,
  accentColor: Color,
  vehicleType: VehicleType = VehicleType.SPORT_COUPE
) {
  Canvas(modifier = modifier) {
    val w = size.width
    val h = size.height

    // 1. Studio Underglow / Neon Ground Reflection Pool
    drawOval(
      brush = Brush.radialGradient(
        colors = listOf(
          accentColor.copy(alpha = 0.45f),
          accentColor.copy(alpha = 0.15f),
          Color.Transparent
        ),
        center = Offset(w * 0.5f, h * 0.74f),
        radius = w * 0.46f
      ),
      topLeft = Offset(w * 0.06f, h * 0.65f),
      size = Size(w * 0.88f, h * 0.18f)
    )

    // 2. Wet asphalt horizon / ground contact line
    drawLine(
      brush = Brush.horizontalGradient(
        listOf(
          Color.Transparent,
          accentColor.copy(alpha = 0.35f),
          accentColor.copy(alpha = 0.8f),
          accentColor.copy(alpha = 0.35f),
          Color.Transparent
        )
      ),
      start = Offset(w * 0.05f, h * 0.75f),
      end = Offset(w * 0.95f, h * 0.75f),
      strokeWidth = 2.dp.toPx()
    )

    // 3. Vehicle Body Geometries according to VehicleType
    val bodyPath = Path()
    val windowPath = Path()
    val intakePath = Path()
    val splitterPath = Path()

    when (vehicleType) {
      VehicleType.SPORT_COUPE -> {
        // Vento GT: Classic muscular grand-tourer sports coupe profile
        bodyPath.apply {
          moveTo(w * 0.12f, h * 0.68f) // rear bottom diffuser
          lineTo(w * 0.10f, h * 0.58f) // rear bumper curve
          lineTo(w * 0.11f, h * 0.48f) // rear deck
          lineTo(w * 0.07f, h * 0.45f) // aggressive rear wing
          lineTo(w * 0.14f, h * 0.45f)
          lineTo(w * 0.20f, h * 0.49f) // trunk line
          cubicTo(w * 0.26f, h * 0.41f, w * 0.34f, h * 0.28f, w * 0.46f, h * 0.26f) // rear pillar & roof
          lineTo(w * 0.57f, h * 0.26f) // roof flat
          cubicTo(w * 0.65f, h * 0.28f, w * 0.74f, h * 0.43f, w * 0.84f, h * 0.49f) // windshield & hood
          lineTo(w * 0.91f, h * 0.56f) // front nose curve
          lineTo(w * 0.93f, h * 0.64f) // front air damn
          lineTo(w * 0.89f, h * 0.68f) // front splitter
          lineTo(w * 0.81f, h * 0.68f)
          // Front wheel arch
          arcTo(
            rect = androidx.compose.ui.geometry.Rect(w * 0.69f, h * 0.54f, w * 0.81f, h * 0.78f),
            startAngleDegrees = 0f,
            sweepAngleDegrees = -180f,
            forceMoveTo = false
          )
          lineTo(w * 0.40f, h * 0.68f) // side skirt
          // Rear wheel arch
          arcTo(
            rect = androidx.compose.ui.geometry.Rect(w * 0.24f, h * 0.54f, w * 0.36f, h * 0.78f),
            startAngleDegrees = 0f,
            sweepAngleDegrees = -180f,
            forceMoveTo = false
          )
          lineTo(w * 0.12f, h * 0.68f)
          close()
        }

        windowPath.apply {
          moveTo(w * 0.36f, h * 0.47f)
          lineTo(w * 0.44f, h * 0.31f)
          lineTo(w * 0.57f, h * 0.31f)
          lineTo(w * 0.69f, h * 0.47f)
          close()
        }

        intakePath.apply {
          moveTo(w * 0.85f, h * 0.59f)
          lineTo(w * 0.89f, h * 0.59f)
          lineTo(w * 0.88f, h * 0.65f)
          lineTo(w * 0.83f, h * 0.65f)
          close()
        }
      }

      VehicleType.HYPERCAR -> {
        // Aero Stryker: Ultra-low, track-focused mid-engine hypercar with massive dorsal fin
        bodyPath.apply {
          moveTo(w * 0.10f, h * 0.68f)
          lineTo(w * 0.08f, h * 0.56f)
          lineTo(w * 0.06f, h * 0.42f) // huge GT3 rear wing
          lineTo(w * 0.16f, h * 0.42f)
          lineTo(w * 0.22f, h * 0.50f)
          cubicTo(w * 0.28f, h * 0.44f, w * 0.38f, h * 0.27f, w * 0.50f, h * 0.25f) // canopy dome
          lineTo(w * 0.59f, h * 0.25f)
          cubicTo(w * 0.68f, h * 0.27f, w * 0.78f, h * 0.43f, w * 0.88f, h * 0.52f) // long low wedge hood
          lineTo(w * 0.94f, h * 0.60f) // hypercar needle nose
          lineTo(w * 0.92f, h * 0.66f)
          lineTo(w * 0.88f, h * 0.68f)
          lineTo(w * 0.81f, h * 0.68f)
          // Front wheel arch
          arcTo(
            rect = androidx.compose.ui.geometry.Rect(w * 0.69f, h * 0.54f, w * 0.81f, h * 0.78f),
            startAngleDegrees = 0f,
            sweepAngleDegrees = -180f,
            forceMoveTo = false
          )
          lineTo(w * 0.40f, h * 0.68f)
          // Rear wheel arch
          arcTo(
            rect = androidx.compose.ui.geometry.Rect(w * 0.24f, h * 0.54f, w * 0.36f, h * 0.78f),
            startAngleDegrees = 0f,
            sweepAngleDegrees = -180f,
            forceMoveTo = false
          )
          lineTo(w * 0.10f, h * 0.68f)
          close()
        }

        windowPath.apply {
          moveTo(w * 0.41f, h * 0.46f)
          lineTo(w * 0.48f, h * 0.30f)
          lineTo(w * 0.60f, h * 0.30f)
          lineTo(w * 0.73f, h * 0.47f)
          close()
        }

        intakePath.apply {
          // Mid-engine side NACA air scoop
          moveTo(w * 0.43f, h * 0.52f)
          lineTo(w * 0.49f, h * 0.52f)
          lineTo(w * 0.45f, h * 0.62f)
          lineTo(w * 0.39f, h * 0.62f)
          close()
        }
      }

      VehicleType.EV_PROTOTYPE -> {
        // Apex Phantom: Cyberpunk electric prototype, seamless tear-drop aerodynamic streamliner
        bodyPath.apply {
          moveTo(w * 0.13f, h * 0.68f)
          lineTo(w * 0.10f, h * 0.60f)
          lineTo(w * 0.12f, h * 0.48f) // active aero spoiler ducktail
          lineTo(w * 0.16f, h * 0.44f)
          cubicTo(w * 0.26f, h * 0.37f, w * 0.38f, h * 0.24f, w * 0.52f, h * 0.24f) // fastback glass dome
          lineTo(w * 0.60f, h * 0.24f)
          cubicTo(w * 0.70f, h * 0.26f, w * 0.81f, h * 0.41f, w * 0.89f, h * 0.51f)
          lineTo(w * 0.94f, h * 0.59f)
          lineTo(w * 0.91f, h * 0.66f)
          lineTo(w * 0.87f, h * 0.68f)
          lineTo(w * 0.81f, h * 0.68f)
          // Front wheel arch
          arcTo(
            rect = androidx.compose.ui.geometry.Rect(w * 0.69f, h * 0.54f, w * 0.81f, h * 0.78f),
            startAngleDegrees = 0f,
            sweepAngleDegrees = -180f,
            forceMoveTo = false
          )
          lineTo(w * 0.40f, h * 0.68f)
          // Rear wheel arch
          arcTo(
            rect = androidx.compose.ui.geometry.Rect(w * 0.24f, h * 0.54f, w * 0.36f, h * 0.78f),
            startAngleDegrees = 0f,
            sweepAngleDegrees = -180f,
            forceMoveTo = false
          )
          lineTo(w * 0.13f, h * 0.68f)
          close()
        }

        windowPath.apply {
          moveTo(w * 0.38f, h * 0.45f)
          lineTo(w * 0.47f, h * 0.28f)
          lineTo(w * 0.62f, h * 0.28f)
          lineTo(w * 0.75f, h * 0.46f)
          close()
        }

        intakePath.apply {
          // Battery cooling bottom intake channel
          moveTo(w * 0.84f, h * 0.61f)
          lineTo(w * 0.89f, h * 0.61f)
          lineTo(w * 0.87f, h * 0.66f)
          lineTo(w * 0.83f, h * 0.66f)
          close()
        }
      }
    }

    // 4. Draw Rich Car Paint Body Gradient (Metallic Sheen with Sky Reflection)
    drawPath(
      path = bodyPath,
      brush = Brush.verticalGradient(
        colors = listOf(
          Color(0xFF2E384D),
          Color(0xFF1E2536),
          Color(0xFF131825),
          Color(0xFF0C101A)
        ),
        startY = h * 0.22f,
        endY = h * 0.70f
      )
    )

    // 5. Body Shoulder Highlight Line (Metallic Car Paint Specular Edge)
    drawPath(
      path = bodyPath,
      brush = Brush.horizontalGradient(
        colors = listOf(
          accentColor.copy(alpha = 0.4f),
          Color.White.copy(alpha = 0.75f),
          accentColor.copy(alpha = 0.9f),
          Color.White.copy(alpha = 0.6f),
          accentColor.copy(alpha = 0.5f)
        )
      ),
      style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
    )

    // 6. Cockpit Windshield & Tinted Privacy Glass with Specular Reflection
    drawPath(
      path = windowPath,
      brush = Brush.verticalGradient(
        colors = listOf(
          accentColor.copy(alpha = 0.5f),
          Color(0xFF0F172A).copy(alpha = 0.85f),
          Color(0xFF020617).copy(alpha = 0.95f)
        )
      )
    )
    drawPath(
      path = windowPath,
      brush = Brush.horizontalGradient(
        colors = listOf(
          Color.White.copy(alpha = 0.65f),
          accentColor.copy(alpha = 0.35f),
          Color.Transparent
        )
      ),
      style = Stroke(width = 1.5.dp.toPx())
    )

    // Glass Reflection Diagonal Streak
    drawLine(
      brush = Brush.linearGradient(
        colors = listOf(Color.Transparent, Color.White.copy(alpha = 0.35f), Color.Transparent),
        start = Offset(w * 0.44f, h * 0.32f),
        end = Offset(w * 0.58f, h * 0.46f)
      ),
      start = Offset(w * 0.44f, h * 0.32f),
      end = Offset(w * 0.58f, h * 0.46f),
      strokeWidth = 2.dp.toPx()
    )

    // 7. Aerodynamic Vent / Carbon Fiber Side Scoops
    drawPath(
      path = intakePath,
      brush = Brush.verticalGradient(
        colors = listOf(Color(0xFF05070A), Color(0xFF161B26))
      )
    )
    drawPath(
      path = intakePath,
      color = accentColor.copy(alpha = 0.6f),
      style = Stroke(width = 1.dp.toPx())
    )

    // 8. Realistic High-Performance Wheels with Alloy Rims, Discs & Calipers
    val rearWheelCenter = Offset(w * 0.30f, h * 0.66f)
    val frontWheelCenter = Offset(w * 0.75f, h * 0.66f)
    val wheelRadius = w * 0.062f
    val rimRadius = wheelRadius * 0.78f

    listOf(rearWheelCenter, frontWheelCenter).forEach { center ->
      // Tire Tread Outer Ring (Rubber Matte Dark)
      drawCircle(
        brush = Brush.radialGradient(
          colors = listOf(Color(0xFF1E2430), Color(0xFF080B10)),
          center = center,
          radius = wheelRadius
        ),
        radius = wheelRadius,
        center = center
      )
      drawCircle(
        color = Color(0xFF2E384D),
        radius = wheelRadius,
        center = center,
        style = Stroke(width = 2.dp.toPx())
      )

      // Drilled Carbon-Ceramic Brake Rotor
      drawCircle(
        brush = Brush.radialGradient(
          colors = listOf(Color(0xFF555F73), Color(0xFF333A48)),
          center = center,
          radius = rimRadius * 0.85f
        ),
        radius = rimRadius * 0.85f,
        center = center
      )

      // High-Performance Brembo-style Brake Caliper (painted accentColor)
      drawArc(
        color = accentColor,
        startAngle = 200f,
        sweepAngle = 70f,
        useCenter = false,
        topLeft = Offset(center.x - rimRadius * 0.82f, center.y - rimRadius * 0.82f),
        size = Size(rimRadius * 1.64f, rimRadius * 1.64f),
        style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
      )

      // Polished Alloy Rim Outer Ring
      drawCircle(
        brush = Brush.sweepGradient(
          listOf(Color.White.copy(alpha = 0.8f), Color(0xFF4A5568), Color.White.copy(alpha = 0.8f)),
          center = center
        ),
        radius = rimRadius,
        center = center,
        style = Stroke(width = 2.dp.toPx())
      )

      // Multi-Spoke Alloy Wheels (GTA-style sports rims)
      val spokeCount = 5
      for (i in 0 until spokeCount) {
        val angle = (i * (360f / spokeCount) * (Math.PI / 180f)).toFloat()
        val spokeEnd = Offset(
          center.x + (rimRadius * 0.9f) * kotlin.math.cos(angle),
          center.y + (rimRadius * 0.9f) * kotlin.math.sin(angle)
        )
        drawLine(
          color = Color(0xFFDDE4F0),
          start = center,
          end = spokeEnd,
          strokeWidth = 2.dp.toPx()
        )
      }

      // Center Hub Cap (Logo Emblem)
      drawCircle(
        color = accentColor,
        radius = rimRadius * 0.22f,
        center = center
      )
      drawCircle(
        color = Color.White,
        radius = rimRadius * 0.10f,
        center = center
      )
    }

    // 9. Laser Headlights with Realistic Forward Projector Beam Flare
    val headlightOrigin = Offset(w * 0.89f, h * 0.54f)
    val headlightTip = Offset(w * 0.94f, h * 0.59f)

    // Projector Light Beam Flare (Forward Cone)
    val beamPath = Path().apply {
      moveTo(headlightTip.x, headlightTip.y - 4.dp.toPx())
      lineTo(w * 0.99f, headlightTip.y - 12.dp.toPx())
      lineTo(w * 0.99f, headlightTip.y + 18.dp.toPx())
      lineTo(headlightTip.x, headlightTip.y + 4.dp.toPx())
      close()
    }
    drawPath(
      path = beamPath,
      brush = Brush.horizontalGradient(
        colors = listOf(
          Color.White.copy(alpha = 0.7f),
          accentColor.copy(alpha = 0.45f),
          Color.Transparent
        ),
        startX = headlightTip.x,
        endX = w * 0.99f
      )
    )

    // Headlight Lens Housing
    drawLine(
      color = Color.White,
      start = headlightOrigin,
      end = headlightTip,
      strokeWidth = 3.5.dp.toPx(),
      cap = StrokeCap.Round
    )
    drawLine(
      color = accentColor,
      start = headlightOrigin,
      end = headlightTip,
      strokeWidth = 6.dp.toPx(),
      cap = StrokeCap.Round
    )

    // 10. OLED Taillight Lightbar (Neon Red Laser Bar with Bloom)
    val taillightStart = Offset(w * 0.08f, h * 0.52f)
    val taillightEnd = Offset(w * 0.15f, h * 0.52f)

    // Taillight Ambient Bloom Glow
    drawLine(
      color = Color(0xFFFF1744).copy(alpha = 0.55f),
      start = taillightStart,
      end = taillightEnd,
      strokeWidth = 7.dp.toPx(),
      cap = StrokeCap.Round
    )
    // Sharp Core LED Laser Bar
    drawLine(
      color = Color(0xFFFF5252),
      start = taillightStart,
      end = taillightEnd,
      strokeWidth = 3.5.dp.toPx(),
      cap = StrokeCap.Round
    )
    drawLine(
      color = Color.White,
      start = Offset(w * 0.09f, h * 0.52f),
      end = Offset(w * 0.13f, h * 0.52f),
      strokeWidth = 1.5.dp.toPx(),
      cap = StrokeCap.Round
    )
  }
}

/**
 * Three horizontal stat bars: Speed, Handling, Acceleration
 * Each as a labeled progress bar with smooth transition animations.
 */
@Composable
fun CarStatsSection(
  vehicle: VehicleModel,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(20.dp))
      .background(GarageSurface)
      .border(1.dp, GarageSurfaceBorder, RoundedCornerShape(20.dp))
      .padding(18.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = "PERFORMANCE SPECS",
        style = MaterialTheme.typography.labelSmall.copy(
          fontWeight = FontWeight.Bold,
          letterSpacing = 1.2.sp
        ),
        color = TextMuted
      )

      Icon(
        imageVector = Icons.Rounded.Speed,
        contentDescription = "Specs Icon",
        tint = TextMuted,
        modifier = Modifier.size(18.dp)
      )
    }

    // 1. Speed Stat Bar
    StatBarItem(
      label = stringResource(R.string.stat_speed),
      value = vehicle.speed,
      valueText = vehicle.speedLabel,
      accentColor = StatSpeedColor,
      testTag = "stat_bar_speed"
    )

    // 2. Handling Stat Bar
    StatBarItem(
      label = stringResource(R.string.stat_handling),
      value = vehicle.handling,
      valueText = vehicle.handlingLabel,
      accentColor = StatHandlingColor,
      testTag = "stat_bar_handling"
    )

    // 3. Acceleration Stat Bar
    StatBarItem(
      label = stringResource(R.string.stat_acceleration),
      value = vehicle.acceleration,
      valueText = vehicle.accelerationLabel,
      accentColor = StatAccelerationColor,
      testTag = "stat_bar_acceleration"
    )
  }
}

@Composable
fun StatBarItem(
  label: String,
  value: Float,
  valueText: String,
  accentColor: Color,
  testTag: String,
  modifier: Modifier = Modifier
) {
  // Custom Animatable animation so stats slide and grow smoothly to their new values
  val animatableProgress = remember { Animatable(0f) }

  LaunchedEffect(value) {
    animatableProgress.animateTo(
      targetValue = value.coerceIn(0f, 1f),
      animationSpec = tween(durationMillis = 650, easing = FastOutSlowInEasing)
    )
  }

  Column(
    modifier = modifier
      .fillMaxWidth()
      .testTag(testTag),
    verticalArrangement = Arrangement.spacedBy(6.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = label,
        style = MaterialTheme.typography.bodyMedium.copy(
          fontWeight = FontWeight.SemiBold
        ),
        color = TextPrimary
      )

      Text(
        text = valueText,
        style = MaterialTheme.typography.bodySmall.copy(
          fontWeight = FontWeight.Bold,
          fontFamily = FontFamily.Monospace
        ),
        color = accentColor
      )
    }

    // Progress Bar Track with Rounded Corners & Glowing Fill
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .height(10.dp)
        .clip(RoundedCornerShape(5.dp))
        .background(GarageBackground)
        .border(1.dp, GarageSurfaceBorder, RoundedCornerShape(5.dp))
    ) {
      Box(
        modifier = Modifier
          .fillMaxWidth(animatableProgress.value)
          .height(10.dp)
          .clip(RoundedCornerShape(5.dp))
          .background(
            Brush.horizontalGradient(
              colors = listOf(
                accentColor.copy(alpha = 0.7f),
                accentColor
              )
            )
          )
      )
    }
  }
}

/**
 * Horizontally scrollable row of 3 selectable car cards
 * showing car name, thumbnail placeholder, and highlighted selection with subtle scale animation.
 */
@Composable
fun VehicleSelectorRow(
  vehicles: List<VehicleModel>,
  selectedIndex: Int,
  onSelectVehicle: (Int) -> Unit,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier.fillMaxWidth(),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    Text(
      text = "AVAILABLE VEHICLES",
      style = MaterialTheme.typography.labelSmall.copy(
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.2.sp
      ),
      color = TextMuted
    )

    Row(
      modifier = Modifier
        .fillMaxWidth()
        .horizontalScroll(rememberScrollState()),
      horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      vehicles.forEachIndexed { index, vehicle ->
        val isSelected = index == selectedIndex
        SelectableCarCard(
          vehicle = vehicle,
          isSelected = isSelected,
          onClick = { onSelectVehicle(index) }
        )
      }
    }
  }
}

@Composable
fun SelectableCarCard(
  vehicle: VehicleModel,
  isSelected: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val interactionSource = remember { MutableInteractionSource() }
  val isPressed by interactionSource.collectIsPressedAsState()

  // Subtle press & selection scale animation
  val scale by animateFloatAsState(
    targetValue = when {
      isPressed -> 0.94f
      isSelected -> 1.04f
      else -> 1.0f
    },
    animationSpec = spring(
      dampingRatio = Spring.DampingRatioMediumBouncy,
      stiffness = Spring.StiffnessLow
    ),
    label = "car_card_scale_${vehicle.id}"
  )

  Box(
    modifier = modifier
      .width(160.dp)
      .graphicsLayer {
        scaleX = scale
        scaleY = scale
      }
      .clip(RoundedCornerShape(18.dp))
      .background(
        if (isSelected) GarageSurfaceVariant else GarageSurface
      )
      .border(
        width = if (isSelected) 2.dp else 1.dp,
        color = if (isSelected) vehicle.accentColor else GarageSurfaceBorder,
        shape = RoundedCornerShape(18.dp)
      )
      .clickable(
        interactionSource = interactionSource,
        indication = null,
        onClick = onClick
      )
      .testTag("car_card_${vehicle.id}")
      .padding(14.dp)
  ) {
    Column(
      modifier = Modifier.fillMaxWidth(),
      verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      // Thumbnail Graphic Placeholder
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(76.dp)
          .clip(RoundedCornerShape(12.dp))
          .background(GarageBackground)
          .border(
            1.dp,
            if (isSelected) vehicle.accentColor.copy(alpha = 0.4f) else GarageSurfaceBorder,
            RoundedCornerShape(12.dp)
          ),
        contentAlignment = Alignment.Center
      ) {
        CarSilhouetteGraphic(
          modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
          accentColor = vehicle.accentColor,
          vehicleType = vehicle.vehicleType
        )

        if (isSelected) {
          Box(
            modifier = Modifier
              .align(Alignment.TopEnd)
              .padding(6.dp)
              .size(16.dp)
              .clip(CircleShape)
              .background(vehicle.accentColor),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Rounded.CheckCircle,
              contentDescription = "Selected",
              tint = GarageBackground,
              modifier = Modifier.size(12.dp)
            )
          }
        }
      }

      // Car Name & Spec preview
      Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(
          text = vehicle.name,
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold
          ),
          color = if (isSelected) TextPrimary else TextSecondary,
          maxLines = 1
        )

        Text(
          text = vehicle.speedLabel,
          style = MaterialTheme.typography.labelSmall.copy(
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.SemiBold
          ),
          color = if (isSelected) vehicle.accentColor else TextMuted
        )
      }
    }
  }
}

/**
 * Bottom Bar with full-width rounded "Select Vehicle" button
 * Featuring subtle spring press animation and feedback.
 */
@Composable
fun GarageBottomBar(
  vehicleName: String,
  accentColor: Color,
  isConfirmed: Boolean,
  onSelectClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val interactionSource = remember { MutableInteractionSource() }
  val isPressed by interactionSource.collectIsPressedAsState()

  val buttonScale by animateFloatAsState(
    targetValue = if (isPressed) 0.95f else 1.0f,
    animationSpec = spring(
      dampingRatio = Spring.DampingRatioMediumBouncy,
      stiffness = Spring.StiffnessMedium
    ),
    label = "select_button_scale"
  )

  Surface(
    modifier = modifier.fillMaxWidth(),
    color = GarageBackground,
    tonalElevation = 0.dp
  ) {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp, vertical = 14.dp)
    ) {
      Button(
        onClick = onSelectClick,
        interactionSource = interactionSource,
        modifier = Modifier
          .fillMaxWidth()
          .height(56.dp)
          .graphicsLayer {
            scaleX = buttonScale
            scaleY = buttonScale
          }
          .shadow(
            elevation = if (isPressed) 2.dp else 8.dp,
            shape = RoundedCornerShape(16.dp),
            ambientColor = accentColor,
            spotColor = accentColor
          )
          .testTag("select_vehicle_button"),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
          containerColor = accentColor,
          contentColor = GarageBackground
        )
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Icon(
            imageVector = if (isConfirmed) Icons.Rounded.CheckCircle else Icons.Rounded.ElectricBolt,
            contentDescription = null,
            tint = GarageBackground,
            modifier = Modifier.size(20.dp)
          )

          Text(
            text = if (isConfirmed) "$vehicleName Selected" else stringResource(R.string.select_vehicle),
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.ExtraBold,
              letterSpacing = 0.8.sp
            ),
            color = GarageBackground
          )
        }
      }
    }
  }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
  Text(text = "Hello $name!", modifier = modifier)
}

@Preview(showBackground = true)
@Composable
fun GaragePreview() {
  MyApplicationTheme {
    GarageScreen()
  }
}

