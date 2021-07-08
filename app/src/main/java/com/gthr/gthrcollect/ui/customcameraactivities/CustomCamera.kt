package com.gthr.gthrcollect.ui.customcameraactivities

import android.Manifest
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
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.HandlerThread
import android.provider.Settings
import android.util.Size
import android.util.SparseIntArray
import android.view.*
import android.view.TextureView.SurfaceTextureListener
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.utils.enums.CameraViews
import com.gthr.gthrcollect.utils.extensions.gone
import com.gthr.gthrcollect.utils.extensions.visible
import com.gthr.gthrcollect.utils.logger.GthrLogger
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*

class CustomCamera : AppCompatActivity() {

    lateinit var mIdLayout: View
    private lateinit var mCardLayout: View
    lateinit var mPreview_layout: RelativeLayout
    lateinit var mBtn_camera: ImageView
    private var mTextureView: TextureView? = null
    private var mCameraDevice: CameraDevice? = null
    private var cameraId: String? = null
    private var mImageDimensions: Size? = null
    private var mCameraCaptureSession: CameraCaptureSession? = null
    private var mCaptureRequestBuilder: CaptureRequest.Builder? = null
    private var mImageReader: ImageReader? = null
    private val flashSupported = false
    private var mBackgroundThread: HandlerThread? = null
    private var mBackgroundHandler: Handler? = null
    var mStateCallback: CameraDevice.StateCallback? = null
    var mFile: File? = null
    private var mPreviewTextView: TextView? = null


    var mCameraViews: String? = null;
    var mIsFrontView: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.custom_camera)

        mStateCallback = object : CameraDevice.StateCallback() {
            override fun onOpened(camera: CameraDevice) {
                mCameraDevice = camera
                createCameraPreview()
            }

            override fun onDisconnected(mCameraDevice: CameraDevice) {
                this@CustomCamera.mCameraDevice!!.close()
            }

            override fun onError(mCameraDevice: CameraDevice, i: Int) {
                this@CustomCamera.mCameraDevice!!.close()
                this@CustomCamera.mCameraDevice = null // Change to global
            }
        }
        mTextureView = findViewById(R.id.textureView)
        mTextureView?.setSurfaceTextureListener(textureListener)
        mIdLayout = findViewById(R.id.id_layout)
        mCardLayout = findViewById(R.id.card_layout)
        mPreview_layout = findViewById(R.id.preview_layout)
        mPreviewTextView = findViewById(R.id.tv_preview_text)

        mBtn_camera = findViewById(R.id.btn_camera)

        mCameraViews = intent.getStringExtra(CAMERA_VIEW)
        mIsFrontView = intent.getBooleanExtra(IS_FRONT, false)

        if (mCameraViews!!.equals(CameraViews.ID_VERIFICATION.toString())) {
            mPreviewTextView?.text =
                if (mIsFrontView) getString(R.string.preview_note_front) else getString(R.string.preview_note_back)
            mIdLayout.visible()
            mCardLayout.gone()
        } else {
            mIdLayout.gone()
            mCardLayout.visible()
        }

        mBtn_camera.setOnClickListener { takePicture() }
    }

    fun takePicture() {
        if (mCameraDevice == null) return
        val cameraManager = getSystemService(CAMERA_SERVICE) as CameraManager
        try {
            val cameraCharacteristics = cameraManager.getCameraCharacteristics(
                mCameraDevice!!.id
            )
            var jpegSizes: Array<Size>? = null
            if (cameraCharacteristics == null) jpegSizes =
                cameraCharacteristics.get<StreamConfigurationMap>(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)!!
                    .getOutputSizes(ImageFormat.JPEG)

            // Capture image with custom size
            var width = 640
            var height = 480
            if (jpegSizes != null && jpegSizes.size > 0) {
                width = jpegSizes[0].width
                height = jpegSizes[0].height
            }
            mImageReader = ImageReader.newInstance(width, height, ImageFormat.JPEG, 1)
            val outputSurface: MutableList<Surface> = ArrayList(2)
            outputSurface.add(mImageReader!!.surface)
            outputSurface.add(Surface(mTextureView!!.surfaceTexture))
            val captureBuilder =
                mCameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)
            captureBuilder.addTarget(mImageReader!!.surface)
            captureBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO)

            // Check orientation base on device
            val rotation = windowManager.defaultDisplay.rotation
            captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS[rotation])
            val readerListener = OnImageAvailableListener { mImageReader ->
                mFile = File(
                    Environment.getExternalStorageDirectory().toString() + "/" + UUID.randomUUID()
                        .toString() + ".jpg"
                )
                var image: Image? = null
                try {
                    image = mImageReader.acquireLatestImage()
                    val buffer = image.planes[0].buffer
                    buffer.rewind()
                    val bytes = ByteArray(buffer.capacity())
                    buffer[bytes]
                    //    processImage(image);
                    val bitmapPicture = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                    var croppedBitmap: Bitmap? = null
                    val display = (getSystemService(WINDOW_SERVICE) as WindowManager).defaultDisplay
                    if (display.rotation == Surface.ROTATION_0) {

                        //rotate bitmap, because camera sensor usually in landscape mode
                        val matrix = Matrix()
                        matrix.postRotate(0f)
                        val rotatedBitmap = Bitmap.createBitmap(
                            bitmapPicture, 0, 0,
                            bitmapPicture.width, bitmapPicture.height, matrix, true
                        )
                        //save mFile
                        //    createImageFile(rotatedBitmap);

                        //calculate aspect ratio
                        val koefX = rotatedBitmap.width
                            .toFloat() / mPreview_layout.width.toFloat()
                        val koefY = rotatedBitmap.height
                            .toFloat() / mPreview_layout.height


                        var x1 = 0
                        var y1 = 0
                        var x2 = 0
                        var y2 = 0

                        //get viewfinder border size and position on the screen


                        if (mCameraViews!!.equals(CameraViews.ID_VERIFICATION.toString())) {

                            x1 = mIdLayout.left - mIdLayout.paddingLeft - mIdLayout.paddingRight
                            y1 = mIdLayout.top - mIdLayout.paddingTop - mIdLayout.paddingBottom
                            x2 = mIdLayout.width
                            y2 = mIdLayout.height
                        } else {
                            x1 =
                                mCardLayout.left - mCardLayout.paddingLeft - mCardLayout.paddingRight
                            y1 =
                                mCardLayout.top - mCardLayout.paddingTop - mCardLayout.paddingBottom
                            x2 = mCardLayout.width
                            y2 = mCardLayout.height
                        }

                        //calculate position and size for cropping
                        val cropStartX = Math.round(x1 * koefX)
                        val cropStartY = Math.round(y1 * koefY)
                        val cropWidthX = Math.round(x2 * koefX)
                        val cropHeightY = Math.round(y2 * koefY)

                        //check limits and make crop
                        if (cropStartX + cropWidthX <= rotatedBitmap.width &&
                            cropStartY + cropHeightY <= rotatedBitmap.height
                        ) {
                            croppedBitmap = Bitmap.createBitmap(
                                rotatedBitmap, cropStartX,
                                cropStartY, cropWidthX, cropHeightY
                            )

                        } else {
                            croppedBitmap = null
                        }

                        //save result
                        croppedBitmap?.let { createImageFile(it) }
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
                    Toast.makeText(
                        this@CustomCamera,
                        getString(R.string.storage_permission),
                        Toast.LENGTH_SHORT
                    ).show()

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

                    override fun onConfigureFailed(mCameraCaptureSession: CameraCaptureSession) {
                        Toast.makeText(
                            this@CustomCamera,
                            getString(R.string.change),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                null
            )
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    private fun updatePreview() {
        if (mCameraDevice == null) Toast.makeText(
            this@CustomCamera,
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
            cameraId = cameraManager.cameraIdList[0]
            val cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraId!!)
            val map =
                cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)!!
            mImageDimensions = map.getOutputSizes(SurfaceTexture::class.java)[0]


            //  val []permissions={Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE}
            if (ContextCompat.checkSelfPermission(
                    this@CustomCamera,
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    this@CustomCamera,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                // Permission to Camera is not granted, request for permission

                ActivityCompat.requestPermissions(
                    this@CustomCamera, arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ), MY_PERMISSIONS_REQUEST_CAMERA
                )
                return
            }



            cameraManager.openCamera(cameraId!!, mStateCallback!!, null)
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    var textureListener: SurfaceTextureListener = object : SurfaceTextureListener {
        override fun onSurfaceTextureAvailable(surfaceTexture: SurfaceTexture, i: Int, i1: Int) {
            openCamera()
        }

        override fun onSurfaceTextureSizeChanged(surfaceTexture: SurfaceTexture, i: Int, i1: Int) {}


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
                this@CustomCamera,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
            &&
            ContextCompat.checkSelfPermission(
                this@CustomCamera,
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

            openCamera();

        }


/*
        when (requestCode) {
            102 ->



        }
*/
    }

    private fun openSettings() {


        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivity(intent)
        Toast.makeText(
            this,
            getString(R.string.permission_denied),
            Toast.LENGTH_SHORT
        ).show()
    }


    fun createImageFile(bitmap: Bitmap) {
        val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val timeStamp = SimpleDateFormat("MMdd_HHmmssSSS").format(Date())
        val imageFileName = "region_$timeStamp.jpg"
        val mFile = File(path, imageFileName)
        try {
            // Make sure the Pictures directory exists.
            if (path.mkdirs()) {
                Toast.makeText(this, getString(R.string.not_exist) + path.name, Toast.LENGTH_SHORT)
                    .show()
            }
            val os: OutputStream = FileOutputStream(mFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os)
            os.flush()
            os.close()
            GthrLogger.i("ExternalStorage", "Writed " + path + mFile.name)

            MediaScannerConnection.scanFile(
                this, arrayOf(mFile.toString()), null
            ) { path, uri ->
                GthrLogger.i("ExternalStorage", "Scanned $path:")
                GthrLogger.i("ExternalStorage", "-> uri=$uri")
                GthrLogger.e("URL", uri.toString())
            }
            Toast.makeText(this, mFile.name, Toast.LENGTH_SHORT).show()




            startActivityForResult(
                ImagePreview.getInstance(this, mFile.path, mCameraViews!!), REQUEST_CODE_PREVIEW
            )
        } catch (e: Exception) {
            e.printStackTrace()
            // Unable to create mFile, likely because external storage is
            // not currently mounted.
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null) {
            val returnIntent = Intent()
            returnIntent.putExtra(INTENT_KEY_URL, data.getStringExtra(INTENT_KEY_URL))
            setResult(RESULT_OK, returnIntent)
            finish()
        }
    }

    companion object {
        private val ORIENTATIONS = SparseIntArray()
        const val INTENT_KEY_URL = "url"
        const val MY_PERMISSIONS_REQUEST_CAMERA = 102
        const val HANDLER_NAME = "Camera Background"

        const val CAMERA_VIEW = "camera_view"
        const val IS_FRONT = "is_front"

        init {
            ORIENTATIONS.append(Surface.ROTATION_0, 90)
            ORIENTATIONS.append(Surface.ROTATION_90, 0)
            ORIENTATIONS.append(Surface.ROTATION_180, 270)
            ORIENTATIONS.append(Surface.ROTATION_270, 180)
        }

        fun getInstance(context: Context, cameraViewType: CameraViews, isFront: Boolean) =
            Intent(context, CustomCamera::class.java).apply {
                putExtra(CAMERA_VIEW, cameraViewType.toString())
                putExtra(IS_FRONT, isFront)
            }

        const val REQUEST_CODE_PREVIEW = 1

    }
}