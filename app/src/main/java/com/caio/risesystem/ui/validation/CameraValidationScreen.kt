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

    val poseLandmarkerHelper = remember {
        PoseLandmarkerHelper(
            context = context,
            onResults = { result ->
                val detectedPose =
                    result.landmarks().firstOrNull()

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
                        errorMessage = null
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
                        errorMessage = message
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