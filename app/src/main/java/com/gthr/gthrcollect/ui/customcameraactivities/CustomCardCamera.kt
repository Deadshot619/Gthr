package com.gthr.gthrcollect.ui.customcameraactivities

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.hardware.camera2.*
import android.hardware.camera2.CameraCaptureSession.CaptureCallback
import android.hardware.camera2.params.StreamConfigurationMap
import android.media.Image
import android.media.ImageReader
import android.media.ImageReader.OnImageAvailableListener
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.util.Size
import android.util.SparseIntArray
import android.view.*
import android.view.TextureView.SurfaceTextureListener
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.utils.extensions.cropImage
import com.gthr.gthrcollect.utils.extensions.getPreviewOutputSize
import com.gthr.gthrcollect.utils.extensions.visible
import com.gthr.gthrcollect.utils.logger.GthrLogger
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

class CustomCardCamera : AppCompatActivity() {

    private lateinit var mCardLayout: View
    lateinit var mBtn_camera: ImageView
    private lateinit var mChangeCamera: ImageView
    private lateinit var mOuterOverlay: View
    private var mTextureView: TextureView? = null
    private var mCameraDevice: CameraDevice? = null
    private var cameraId: String? = CAMERA_BACK
    private var mImageDimensions: Size? = null
    private var mCameraCaptureSession: CameraCaptureSession? = null
    private var mCaptureRequestBuilder: CaptureRequest.Builder? = null
    private var mImageReader: ImageReader? = null
    private val flashSupported = false
    private var mBackgroundThread: HandlerThread? = null
    private var mBackgroundHandler: Handler? = null
    var mStateCallback: CameraDevice.StateCallback? = null

    //   var mFile: File? = null
    private var mPreviewTextView: TextView? = null

    var mIsFrontView: Boolean = false
    var mLabelMsg: String? = null
    var mUriFront: String? = null
    var mUriBack: String? = null
    var mUri: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.custom_card_camera)

        mIsFrontView = intent.getBooleanExtra(IS_FRONT, false)
        mLabelMsg = intent.getStringExtra(LABEL_MSG)
        mUriFront = intent.getStringExtra(URI_FRONT)
        mUriBack = intent.getStringExtra(URI_BACK)

        if (mIsFrontView) {
            mUri = mUriFront
        } else {
            mUri = mUriBack
        }

        mTextureView = findViewById(R.id.textureView)
        mTextureView?.setSurfaceTextureListener(textureListener)
        mCardLayout = findViewById(R.id.card_layout)
        mPreviewTextView = findViewById(R.id.tv_preview_text)
        mOuterOverlay = findViewById(R.id.outer_overlay)
        mBtn_camera = findViewById(R.id.btn_camera)
        mChangeCamera = findViewById(R.id.changeCamera)

        mPreviewTextView?.text = mLabelMsg
        mCardLayout.visible()

        // checking if image URI is available, if avaailable send for preview else open camera
        if (mUri.isNullOrEmpty()) {
            mStateCallback = object : CameraDevice.StateCallback() {
                override fun onOpened(camera: CameraDevice) {
                    mCameraDevice = camera
                    createCameraPreview()
                }

                override fun onDisconnected(mCameraDevice: CameraDevice) {
                    this@CustomCardCamera.mCameraDevice!!.close()
                }

                override fun onError(mCameraDevice: CameraDevice, i: Int) {
                    this@CustomCardCamera.mCameraDevice!!.close()
                    this@CustomCardCamera.mCameraDevice = null // Change to global
                }
            }

        } else {

            // send user to preview screen
            startActivityForResult(
                CardImagePreview.getInstance(
                    this,
                    mUriFront!!,
                    mUriBack!!,
                    mUri!!,
                    mIsFrontView,
                    mLabelMsg
                ), REQUEST_CODE_PREVIEW
            )
        }
        mBtn_camera.setOnClickListener { takePicture() }

        mChangeCamera.setOnClickListener {

            if (cameraId.equals(CAMERA_BACK)) {
                openFrontCamera()
            } else {
                openBackCamera()
            }
        }
    }

    private fun openBackCamera() {

        cameraId = CAMERA_BACK
        this@CustomCardCamera.mCameraDevice!!.close()
        this@CustomCardCamera.mCameraDevice = null

        stopBackgroundThread()

        mStateCallback = object : CameraDevice.StateCallback() {
            override fun onOpened(camera: CameraDevice) {
                mCameraDevice = camera
                createCameraPreview()
            }

            override fun onDisconnected(mCameraDevice: CameraDevice) {
                this@CustomCardCamera.mCameraDevice!!.close()
            }

            override fun onError(mCameraDevice: CameraDevice, i: Int) {
                this@CustomCardCamera.mCameraDevice!!.close()
                this@CustomCardCamera.mCameraDevice = null // Change to global
            }
        }

        startBackgroundThread()
        if (mTextureView!!.isAvailable) openCamera() else mTextureView!!.surfaceTextureListener =
            textureListener
    }

    private fun openFrontCamera() {

        cameraId = CAMERA_FRONT
        this@CustomCardCamera.mCameraDevice!!.close()
        this@CustomCardCamera.mCameraDevice = null
        stopBackgroundThread()
        mStateCallback = object : CameraDevice.StateCallback() {
            override fun onOpened(camera: CameraDevice) {
                mCameraDevice = camera
                createCameraPreview()
            }

            override fun onDisconnected(mCameraDevice: CameraDevice) {
                this@CustomCardCamera.mCameraDevice!!.close()
            }

            override fun onError(mCameraDevice: CameraDevice, i: Int) {
                this@CustomCardCamera.mCameraDevice!!.close()
                this@CustomCardCamera.mCameraDevice = null // Change to global
            }
        }

        startBackgroundThread()
        if (mTextureView!!.isAvailable) openCamera() else mTextureView!!.surfaceTextureListener =
            textureListener
    }

    fun takePicture() {

        if (mCameraDevice == null) return
        val cameraManager = getSystemService(CAMERA_SERVICE) as CameraManager
        try {
            val cameraCharacteristics = cameraManager.getCameraCharacteristics(
                mCameraDevice!!.id
            )

            val size = cameraCharacteristics.get(
                CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP
            )!!.getOutputSizes(ImageFormat.JPEG).maxByOrNull { it.height * it.width }!!

            // Capture image with custom size
            mImageReader = ImageReader.newInstance(size.width, size.height, ImageFormat.JPEG, 1)
//            mImageReader = ImageReader.newInstance(mTextureView!!.width, mTextureView!!.height, ImageFormat.JPEG, 1)

            GthrLogger.e("Resolution", "mImageReader  ${size.width}  ${size.height}")

            val outputSurface: MutableList<Surface> = ArrayList(2)
            outputSurface.add(mImageReader!!.surface)
            outputSurface.add(Surface(mTextureView!!.surfaceTexture))
            val captureBuilder =
                mCameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)
            captureBuilder.addTarget(mImageReader!!.surface)
            captureBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO)

            // Check orientation base on device
            val rotationD = windowManager.defaultDisplay.rotation
            /*  captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS[rotation])*/

            val rotation = cameraCharacteristics[CameraCharacteristics.SENSOR_ORIENTATION]
            captureBuilder[CaptureRequest.JPEG_ORIENTATION] = rotation
            //    captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, rotation)

            val readerListener = OnImageAvailableListener { mImageReader ->
                var image: Image? = null
                try {
                    image = mImageReader.acquireLatestImage()
                    val buffer = image.planes[0].buffer
                    buffer.rewind()
                    val bytes = ByteArray(buffer.capacity())
                    buffer[bytes]
                    //    processImage(image);
                    val bitmapPicture = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                    val display = (getSystemService(WINDOW_SERVICE) as WindowManager).defaultDisplay

                    Log.e("Resolution", "orignal  ${bitmapPicture.width}  ${bitmapPicture.height}")

                    if (display.rotation == Surface.ROTATION_0) {
                        //rotate bitmap, because camera sensor usually in landscape mode
                        val matrix = Matrix()
                        val rotatedBitmap: Bitmap
                        when (rotationD) {

                            Surface.ROTATION_0 -> {

                                if (android.os.Build.VERSION.SDK_INT == android.os.Build.VERSION_CODES.R) {
                                    matrix.postRotate(90f)
                                    rotatedBitmap = Bitmap.createBitmap(
                                        bitmapPicture, 0, 0,
                                        bitmapPicture.width, bitmapPicture.height, matrix, true
                                    )
                                } else {
                                    matrix.postRotate(0f)
                                    rotatedBitmap = Bitmap.createBitmap(
                                        bitmapPicture, 0, 0,
                                        bitmapPicture.width, bitmapPicture.height, matrix, true
                                    )
                                }
                            }

                            Surface.ROTATION_90 -> {

                                matrix.postRotate(90f)
                                rotatedBitmap = Bitmap.createBitmap(
                                    bitmapPicture, 0, 0,
                                    bitmapPicture.width, bitmapPicture.height, matrix, true
                                )

                            }

                            Surface.ROTATION_180 -> {
                                matrix.postRotate(180f)
                                rotatedBitmap = Bitmap.createBitmap(
                                    bitmapPicture, 0, 0,
                                    bitmapPicture.width, bitmapPicture.height, matrix, true
                                )
                            }

                            Surface.ROTATION_270 -> {
                                matrix.postRotate(270f)
                                rotatedBitmap = Bitmap.createBitmap(
                                    bitmapPicture, 0, 0,
                                    bitmapPicture.width, bitmapPicture.height, matrix, true
                                )
                            }

                            else -> rotatedBitmap = bitmapPicture
                        }

                        GthrLogger.e(
                            "Resolution",
                            "Roted  ${rotatedBitmap.width}  ${rotatedBitmap.height}"
                        )
                        cropBitmapImage(rotatedBitmap)

                    } else if (display.rotation == Surface.ROTATION_270) {
                        // for Landscape mode

                    }
                } finally {
                    image?.close()
                }
            }

            mImageReader!!.setOnImageAvailableListener(readerListener, mBackgroundHandler)
            val captureCallback: CaptureCallback = object : CaptureCallback() {
                override fun onCaptureCompleted(
                    session: CameraCaptureSession,
                    request: CaptureRequest,
                    result: TotalCaptureResult
                ) {
                    super.onCaptureCompleted(session, request, result)
                    createCameraPreview()
                }
            }
            mCameraDevice!!.createCaptureSession(
                outputSurface,
                object : CameraCaptureSession.StateCallback() {
                    override fun onConfigured(mCameraCaptureSession: CameraCaptureSession) {
                        try {
                            mCameraCaptureSession.capture(
                                captureBuilder.build(),
                                captureCallback,
                                mBackgroundHandler
                            )
                        } catch (e: CameraAccessException) {
                            e.printStackTrace()
                        }
                    }

                    override fun onConfigureFailed(mCameraCaptureSession: CameraCaptureSession) {}
                },
                mBackgroundHandler
            )
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    private fun createCameraPreview() {
        val texture = mTextureView!!.surfaceTexture!!
        texture.setDefaultBufferSize(mImageDimensions!!.width, mImageDimensions!!.height)

        val surface = Surface(texture)
        try {
            mCaptureRequestBuilder =
                mCameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
            mCaptureRequestBuilder!!.addTarget(surface)
            mCameraDevice!!.createCaptureSession(
                Arrays.asList(surface),
                object : CameraCaptureSession.StateCallback() {

                    override fun onConfigured(session: CameraCaptureSession) {
                        if (mCameraDevice == null) return
                        mCameraCaptureSession = session
                        updatePreview()
                    }

                    override fun onConfigureFailed(session: CameraCaptureSession) {}
                },
                null
            )
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    private fun updatePreview() {
        if (mCameraDevice == null) Toast.makeText(
            this@CustomCardCamera,
            getString(R.string.error),
            Toast.LENGTH_SHORT
        )
            .show()
        mCaptureRequestBuilder!!.set(CaptureRequest.CONTROL_MODE, CaptureRequest.CONTROL_MODE_AUTO)
        try {
            mCameraCaptureSession!!.setRepeatingRequest(
                mCaptureRequestBuilder!!.build(),
                null,
                mBackgroundHandler
            )
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    private fun openCamera() {
        val cameraManager = getSystemService(CAMERA_SERVICE) as CameraManager
        try {
            //    cameraId = cameraManager.cameraIdList[0]
            val cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraId!!)
            val map: StreamConfigurationMap =
                cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)!!

            mImageDimensions = getPreviewOutputSize(
                mTextureView!!.display,
                cameraCharacteristics,
                SurfaceHolder::class.java
            )
            //   mImageDimensions = map.getOutputSizes(SurfaceTexture::class.java)[0]

            //  val []permissions={Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE}
            if (ContextCompat.checkSelfPermission(
                    this@CustomCardCamera,
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    this@CustomCardCamera,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                // Permission to Camera is not granted, request for permission

                ActivityCompat.requestPermissions(
                    this@CustomCardCamera, arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ), MY_PERMISSIONS_REQUEST_CAMERA
                )
                return
            }
            cameraManager.openCamera(cameraId!!, mStateCallback!!, null)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    var textureListener: SurfaceTextureListener = object : SurfaceTextureListener {
        override fun onSurfaceTextureAvailable(surfaceTexture: SurfaceTexture, i: Int, i1: Int) {
            GthrLogger.e("aaa", "i= ${i} i1 ${i1} ")
            openCamera()
        }

        override fun onSurfaceTextureSizeChanged(surfaceTexture: SurfaceTexture, i: Int, i1: Int) {
            GthrLogger.e("aaa", "i= ${i} i1 ${i1} ")

        }

        override fun onSurfaceTextureDestroyed(surfaceTexture: SurfaceTexture): Boolean {
            GthrLogger.e("Surfacetexturedestroyed", "called")
            if (mCameraDevice != null) {
                GthrLogger.e("Camera not null", "make null")
                mCameraDevice!!.close()
                mCameraDevice = null
            }
            return false
        }

        override fun onSurfaceTextureUpdated(surfaceTexture: SurfaceTexture) {}
    }

    override fun onResume() {
        super.onResume()

        startBackgroundThread()
        if (ContextCompat.checkSelfPermission(
                this@CustomCardCamera,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
            &&
            ContextCompat.checkSelfPermission(
                this@CustomCardCamera,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            if (mTextureView!!.isAvailable) openCamera() else mTextureView!!.surfaceTextureListener =
                textureListener
        }
    }

    override fun onPause() {
        stopBackgroundThread()
        super.onPause()
    }

    private fun stopBackgroundThread() {
        mBackgroundThread!!.quitSafely()
        try {
            mBackgroundThread!!.join()
            mBackgroundThread = null
            mBackgroundHandler = null
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    private fun startBackgroundThread() {

        mBackgroundThread = HandlerThread(HANDLER_NAME)
        mBackgroundThread!!.start()
        mBackgroundHandler = Handler(mBackgroundThread!!.looper)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED
        ) {
            openCamera(); }
    }

    private fun createFile(context: Context, extension: String): File {
        val sdf = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss_SSS", Locale.US)
        return File(context.filesDir, "IMG_${sdf.format(Date())}.$extension")
    }

    fun cropBitmapImage(rotatedBitmap: Bitmap) {

        try {
            val viewFinderRect: Rect = Rect(
                mOuterOverlay.left,
                mOuterOverlay.top,
                mOuterOverlay.right,
                mOuterOverlay.bottom
            )

            val cropped = cropImage(
                rotatedBitmap,
                Size(mTextureView!!.width, mTextureView!!.height),
                viewFinderRect
            )
            createImageFile(cropped)
        } catch (exp: Exception) {
            GthrLogger.e("Exception", "${exp.message}")
        }
    }

    fun createImageFile(cropped: Bitmap) {
        try {
            val ext = ".jpg"
            val mFile = createFile(this, ext)

            val os: OutputStream = FileOutputStream(mFile)
            cropped.compress(Bitmap.CompressFormat.JPEG, 100, os)
            os.flush()
            os.close()

            // capture image and Sending image to preview screen
            startActivityForResult(
                CardImagePreview.getInstance(
                    this,
                    mUriFront!!,
                    mUriBack!!,
                    mFile.path.toString(),
                    mIsFrontView,
                    mLabelMsg
                ), REQUEST_CODE_PREVIEW

            )
        } catch (e: IOException) {
            e.printStackTrace()

        } catch (exp: FileNotFoundException) {
            exp.printStackTrace()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result when image is clicked and sending it to fragment to view
        if (data != null && requestCode == REQUEST_CODE_PREVIEW && resultCode == Activity.RESULT_OK) {
            val returnIntent = Intent()
            returnIntent.putExtra(INTENT_KEY_URL, data.getStringExtra(INTENT_KEY_URL))
            returnIntent.putExtra(IS_FRONT, mIsFrontView)
            setResult(RESULT_OK, returnIntent)
            finish()
        } else {
            // Open camera when user press Retake btn
            mStateCallback = object : CameraDevice.StateCallback() {
                override fun onOpened(camera: CameraDevice) {
                    mCameraDevice = camera
                    createCameraPreview()
                }

                override fun onDisconnected(mCameraDevice: CameraDevice) {
                    this@CustomCardCamera.mCameraDevice!!.close()
                }

                override fun onError(mCameraDevice: CameraDevice, i: Int) {
                    this@CustomCardCamera.mCameraDevice!!.close()
                    this@CustomCardCamera.mCameraDevice = null // Change to global
                }
            }

        }
    }

    companion object {
        private val ORIENTATIONS = SparseIntArray()
        const val INTENT_KEY_URL = "url"
        const val MY_PERMISSIONS_REQUEST_CAMERA = 102
        const val HANDLER_NAME = "Camera Background"

        const val URI_FRONT = "uri_front"
        const val URI_BACK = "uri_back"
        const val IS_FRONT = "is_front"

        const val LABEL_MSG = "label_msg"

        const val CAMERA_FRONT = "1"
        const val CAMERA_BACK = "0"
        const val REQUEST_CODE_PREVIEW = 1

        init {
            ORIENTATIONS.append(Surface.ROTATION_0, 90)
            ORIENTATIONS.append(Surface.ROTATION_90, 0)
            ORIENTATIONS.append(Surface.ROTATION_180, 270)
            ORIENTATIONS.append(Surface.ROTATION_270, 180)
        }

        fun getInstance(
            context: Context,
            isFront: Boolean,
            uriFront: String,
            uriBack: String,
            label_msg: String
        ) =
            Intent(context, CustomCardCamera::class.java).apply {
                putExtra(IS_FRONT, isFront)
                putExtra(URI_FRONT, uriFront)
                putExtra(URI_BACK, uriBack)
                putExtra(LABEL_MSG, label_msg)
            }
    }

    override fun onBackPressed() {
        finish()
    }
}