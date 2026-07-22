package com.caio.risesystem.ui.validation

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.SystemClock
import android.view.Surface
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.caio.risesystem.domain.biomechanics.MediaPipeSquatProcessor
import com.caio.risesystem.domain.biomechanics.SquatAnalysisResult
import com.caio.risesystem.domain.biomechanics.SquatState
import com.caio.risesystem.domain.model.Mission
import com.caio.risesystem.ui.validation.pose.PoseDetectionState
import com.caio.risesystem.ui.validation.pose.PoseLandmarkerHelper
import com.caio.risesystem.ui.validation.pose.PoseOverlay
import com.google.mediapipe.framework.image.BitmapImageBuilder
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Composable
fun CameraValidationScreen(
    mission: Mission,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val mainExecutor = remember(context) {
        ContextCompat.getMainExecutor(context)
    }

    var permissionGranted by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    var poseState by remember {
        mutableStateOf(PoseDetectionState())
    }

    var targetReps by remember { mutableStateOf(10) }
    var isSeriesCompleted by remember { mutableStateOf(false) }

    val mediaPipeSquatProcessor = remember {
        MediaPipeSquatProcessor()
    }

    var showRepFeedback by remember { mutableStateOf(false) }

    LaunchedEffect(poseState.squatAnalysisResult?.repCount) {
        val count = poseState.squatAnalysisResult?.repCount ?: 0
        if (count > 0 && count <= targetReps) {
            showRepFeedback = true
            kotlinx.coroutines.delay(1000)
            showRepFeedback = false
        }
    }

    val poseLandmarkerHelper = remember {
        PoseLandmarkerHelper(
            context = context,
            onResults = { result ->
                val detectedPose =
                    result.landmarks().firstOrNull()

                val squatResult = if (!isSeriesCompleted) {
                    val res = mediaPipeSquatProcessor.process(result)
                    if (res.repCount >= targetReps) {
                        isSeriesCompleted = true
                    }
                    res
                } else {
                    poseState.squatAnalysisResult
                }

                mainExecutor.execute {
                    poseState = poseState.copy(
                        detectorReady = true,
                        personDetected =
                            detectedPose != null &&
                                    detectedPose.isNotEmpty(),
                        detectedLandmarks =
                            detectedPose?.size ?: 0,
                        landmarks =
                            detectedPose ?: emptyList(),
                        errorMessage = null,
                        squatAnalysisResult = squatResult
                    )
                }
            },
            onError = { message ->
                mainExecutor.execute {
                    poseState = poseState.copy(
                        detectorReady = false,
                        personDetected = false,
                        detectedLandmarks = 0,
                        landmarks = emptyList(),
                        errorMessage = message,
                        squatAnalysisResult = null
                    )
                }
            }
        )
    }

    LaunchedEffect(poseLandmarkerHelper) {
        poseState = poseState.copy(
            detectorReady =
                poseLandmarkerHelper.isReady()
        )
    }

    DisposableEffect(poseLandmarkerHelper) {
        onDispose {
            poseLandmarkerHelper.close()
            mediaPipeSquatProcessor.reset()
        }
    }

    val permissionLauncher =
        rememberLauncherForActivityResult(
            contract =
                ActivityResultContracts.RequestPermission()
        ) { granted ->
            permissionGranted = granted
        }

    LaunchedEffect(Unit) {
        if (!permissionGranted) {
            permissionLauncher.launch(
                Manifest.permission.CAMERA
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF020711))
            .padding(18.dp),
        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {
        Text(
            text = "VALIDAÇÃO POR CÂMERA",
            color = Color(0xFF2AF5F5),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp,
            textAlign = TextAlign.Center
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = mission.title,
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text =
                "Meta: ${mission.targetValue} " +
                        mission.targetUnit,
            color = Color(0xFF8D96A8),
            fontSize = 14.sp
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        DetectionStatus(
            poseState = poseState
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        TargetSelector(
            currentTarget = targetReps,
            onTargetSelected = { newTarget ->
                if (newTarget != targetReps) {
                    targetReps = newTarget
                    mediaPipeSquatProcessor.reset()
                    isSeriesCompleted = false
                    poseState = poseState.copy(squatAnalysisResult = null)
                    showRepFeedback = false
                }
            },
            enabled = !isSeriesCompleted
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(Color(0xFF071823)),
            contentAlignment = Alignment.Center
        ) {
            if (permissionGranted) {
                CameraPreviewWithAnalysis(
                    modifier = Modifier.fillMaxSize(),
                    lifecycleOwner = lifecycleOwner,
                    poseLandmarkerHelper =
                        poseLandmarkerHelper
                )

                PoseOverlay(
                    landmarks = poseState.landmarks,
                    modifier = Modifier.fillMaxSize(),
                    mirrorHorizontally = true
                )

                SquatResultOverlay(
                    result = poseState.squatAnalysisResult,
                    targetReps = targetReps,
                    showFeedback = showRepFeedback,
                    modifier = Modifier.fillMaxSize()
                )

                if (isSeriesCompleted) {
                    SeriesCompletionOverlay(
                        repCount = targetReps,
                        onNewSeries = {
                            mediaPipeSquatProcessor.reset()
                            isSeriesCompleted = false
                            poseState = poseState.copy(squatAnalysisResult = null)
                            showRepFeedback = false
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            } else {
                PermissionDeniedContent(
                    onRequestPermission = {
                        permissionLauncher.launch(
                            Manifest.permission.CAMERA
                        )
                    }
                )
            }
        }

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Text(
            text =
                if (poseState.personDetected) {
                    "Corpo detectado. Mantenha-se " +
                            "inteiro dentro do quadro."
                } else {
                    "Posicione o celular e mantenha " +
                            "seu corpo inteiro dentro do quadro."
                },
            color =
                if (poseState.personDetected) {
                    Color(0xFF2ADFA6)
                } else {
                    Color(0xFF8D96A8)
                },
            fontSize = 13.sp,
            textAlign = TextAlign.Center
        )

        Spacer(
            modifier = Modifier.height(14.dp)
        )

        OutlinedButton(
            onClick = {
                mediaPipeSquatProcessor.reset()
                isSeriesCompleted = false
                poseState = poseState.copy(
                    squatAnalysisResult = null,
                    personDetected = false,
                    detectedLandmarks = 0,
                    landmarks = emptyList()
                )
                showRepFeedback = false
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Color(0xFFFFC857)
            )
        ) {
            Text(
                text = "REINICIAR SÉRIE",
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        OutlinedButton(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "VOLTAR",
                color = Color.White
            )
        }
    }
}

@Composable
private fun DetectionStatus(
    poseState: PoseDetectionState
) {
    val statusText: String
    val statusColor: Color

    when {
        poseState.errorMessage != null -> {
            statusText =
                "ERRO: ${poseState.errorMessage}"

            statusColor = Color(0xFFFF5252)
        }

        !poseState.detectorReady -> {
            statusText = "INICIANDO DETECTOR..."
            statusColor = Color(0xFFFFC857)
        }

        poseState.personDetected -> {
            statusText =
                "CORPO DETECTADO • " +
                        "${poseState.detectedLandmarks} PONTOS"

            statusColor = Color(0xFF2ADFA6)
        }

        else -> {
            statusText = "PROCURANDO CORPO..."
            statusColor = Color(0xFFFFC857)
        }
    }

    Text(
        text = statusText,
        color = statusColor,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
    )
}

@Composable
private fun CameraPreviewWithAnalysis(
    modifier: Modifier,
    lifecycleOwner: LifecycleOwner,
    poseLandmarkerHelper: PoseLandmarkerHelper
) {
    val context = LocalContext.current

    val cameraExecutor: ExecutorService = remember {
        Executors.newSingleThreadExecutor()
    }

    val previewView = remember {
        PreviewView(context).apply {
            scaleType =
                PreviewView.ScaleType.FILL_CENTER

            implementationMode =
                PreviewView.ImplementationMode.COMPATIBLE
        }
    }

    DisposableEffect(
        lifecycleOwner,
        previewView,
        poseLandmarkerHelper
    ) {
        val cameraProviderFuture =
            ProcessCameraProvider.getInstance(context)

        val listener = Runnable {
            try {
                val cameraProvider =
                    cameraProviderFuture.get()

                val targetRotation =
                    if (android.os.Build.VERSION.SDK_INT >=
                        android.os.Build.VERSION_CODES.R
                    ) {
                        context.display?.rotation
                            ?: Surface.ROTATION_0
                    } else {
                        @Suppress("DEPRECATION")
                        (context.getSystemService(
                            android.content.Context.WINDOW_SERVICE
                        ) as android.view.WindowManager)
                            .defaultDisplay
                            .rotation
                    }

                val preview =
                    Preview.Builder()
                        .setTargetRotation(targetRotation)
                        .build()
                        .also { cameraPreview ->
                            cameraPreview.setSurfaceProvider(
                                previewView.surfaceProvider
                            )
                        }

                val imageAnalysis =
                    ImageAnalysis.Builder()
                        .setTargetRotation(targetRotation)
                        .setBackpressureStrategy(
                            ImageAnalysis
                                .STRATEGY_KEEP_ONLY_LATEST
                        )
                        .setOutputImageFormat(
                            ImageAnalysis
                                .OUTPUT_IMAGE_FORMAT_RGBA_8888
                        )
                        .build()

                imageAnalysis.setAnalyzer(
                    cameraExecutor
                ) { imageProxy ->
                    analyzeImage(
                        imageProxy =
                            imageProxy,
                        poseLandmarkerHelper =
                            poseLandmarkerHelper
                    )
                }

                cameraProvider.unbindAll()

                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    CameraSelector.DEFAULT_FRONT_CAMERA,
                    preview,
                    imageAnalysis
                )
            } catch (exception: Exception) {
                exception.printStackTrace()
            }
        }

        cameraProviderFuture.addListener(
            listener,
            ContextCompat.getMainExecutor(context)
        )

        onDispose {
            try {
                cameraProviderFuture
                    .get()
                    .unbindAll()
            } catch (_: Exception) {
                // A câmera já pode estar encerrada.
            }

            cameraExecutor.shutdown()
        }
    }

    AndroidView(
        factory = {
            previewView
        },
        modifier = modifier
    )
}

private fun analyzeImage(
    imageProxy: ImageProxy,
    poseLandmarkerHelper: PoseLandmarkerHelper
) {
    try {
        if (!poseLandmarkerHelper.isReady()) {
            android.util.Log.e(
                "RisePose",
                "Detector ainda não está pronto."
            )
            return
        }

        val bitmap =
            imageProxy.toRotatedBitmap()

        if (bitmap == null) {
            android.util.Log.e(
                "RisePose",
                "Falha ao converter o quadro."
            )
            return
        }

        android.util.Log.d(
            "RisePose",
            "Quadro enviado: " +
                    "${bitmap.width}x${bitmap.height}"
        )

        val mpImage =
            BitmapImageBuilder(bitmap)
                .build()

        poseLandmarkerHelper.detectAsync(
            image = mpImage,
            timestampMilliseconds =
                SystemClock.uptimeMillis()
        )
    } catch (exception: Exception) {
        android.util.Log.e(
            "RisePose",
            "Erro durante análise.",
            exception
        )
    } finally {
        imageProxy.close()
    }
}

private fun ImageProxy.toRotatedBitmap(): Bitmap? {
    val bitmap = toBitmap()

    val rotationDegrees =
        imageInfo.rotationDegrees

    if (rotationDegrees == 0) {
        return bitmap
    }

    val matrix =
        android.graphics.Matrix().apply {
            postRotate(
                rotationDegrees.toFloat()
            )
        }

    val rotatedBitmap =
        Bitmap.createBitmap(
            bitmap,
            0,
            0,
            bitmap.width,
            bitmap.height,
            matrix,
            true
        )

    if (rotatedBitmap !== bitmap) {
        bitmap.recycle()
    }

    return rotatedBitmap
}

@Composable
private fun TargetSelector(
    currentTarget: Int,
    onTargetSelected: (Int) -> Unit,
    enabled: Boolean
) {
    androidx.compose.foundation.layout.Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "META:",
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
        
        listOf(5, 10, 15).forEach { target ->
            val isSelected = target == currentTarget
            androidx.compose.material3.FilterChip(
                selected = isSelected,
                onClick = { if (enabled) onTargetSelected(target) },
                label = { Text(target.toString()) },
                enabled = enabled,
                colors = androidx.compose.material3.FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFF2AF5F5),
                    selectedLabelColor = Color.Black,
                    labelColor = Color.White,
                    containerColor = Color(0xFF071823)
                ),
                border = androidx.compose.material3.FilterChipDefaults.filterChipBorder(
                    enabled = enabled,
                    selected = isSelected,
                    borderColor = Color(0xFF1E2A37),
                    selectedBorderColor = Color(0xFF2AF5F5)
                )
            )
        }
    }
}

@Composable
private fun SeriesCompletionOverlay(
    repCount: Int,
    onNewSeries: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(Color.Black.copy(alpha = 0.8f))
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .background(Color(0xFF071823), shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp))
                .padding(32.dp)
        ) {
            Text(
                text = "SÉRIE CONCLUÍDA",
                color = Color(0xFF2ADFA6),
                fontSize = 24.sp,
                fontWeight = FontWeight.Black
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Você realizou $repCount agachamentos!",
                color = Color.White,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Button(
                onClick = onNewSeries,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2AF5F5),
                    contentColor = Color.Black
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "NOVA SÉRIE",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun SquatResultOverlay(
    result: SquatAnalysisResult?,
    targetReps: Int,
    showFeedback: Boolean,
    modifier: Modifier = Modifier
) {
    val rawRepCount = result?.repCount ?: 0
    val repCount = if (rawRepCount > targetReps) targetReps else rawRepCount
    val state = result?.state ?: SquatState.UNKNOWN
    val hasValidData = result?.hasValidData ?: false

    val statusText = when (state) {
        SquatState.UNKNOWN -> "AGUARDANDO MOVIMENTO"
        SquatState.STANDING -> "POSIÇÃO INICIAL"
        SquatState.DESCENDING -> "DESCENDO..."
        SquatState.BOTTOM -> "NO FUNDO"
        SquatState.ASCENDING -> "SUBINDO..."
    }

    val guidanceText = when {
        showFeedback -> "Repetição válida"
        result == null -> "Preparando análise..."
        !hasValidData -> "Posicione o corpo inteiro na câmera"
        state == SquatState.STANDING -> "Pronto — comece a descer"
        state == SquatState.DESCENDING -> "Continue descendo"
        state == SquatState.BOTTOM -> "Profundidade atingida — agora suba"
        state == SquatState.ASCENDING -> "Continue subindo"
        state == SquatState.UNKNOWN -> "Posicione o corpo inteiro na câmera"
        else -> ""
    }

    Box(
        modifier = modifier.padding(16.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .background(
                    Color.Black.copy(alpha = 0.6f),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
                )
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Text(
                text = "$repCount / $targetReps",
                color = Color(0xFF2AF5F5),
                fontSize = 42.sp,
                fontWeight = FontWeight.Black
            )
            Text(
                text = "REPETIÇÕES",
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = statusText,
                color = if (hasValidData) Color(0xFF2ADFA6) else Color(0xFFFFC857),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = guidanceText,
                color = if (showFeedback) Color(0xFF2AF5F5) else Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun PermissionDeniedContent(
    onRequestPermission: () -> Unit
) {
    Column(
        modifier = Modifier.padding(24.dp),
        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {
        Text(
            text = "PERMISSÃO NECESSÁRIA",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Text(
            text =
                "A câmera é necessária para detectar " +
                        "seu corpo e validar as repetições.",
            color = Color(0xFF8D96A8),
            textAlign = TextAlign.Center
        )

        Spacer(
            modifier = Modifier.height(18.dp)
        )

        Button(
            onClick = onRequestPermission,
            colors = ButtonDefaults.buttonColors(
                containerColor =
                    Color(0xFF2AF5F5),
                contentColor = Color.Black
            )
        ) {
            Text(
                text = "PERMITIR CÂMERA",
                fontWeight = FontWeight.Bold
            )
        }
    }
}