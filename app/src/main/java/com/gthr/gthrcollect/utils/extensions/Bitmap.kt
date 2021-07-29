package com.gthr.gthrcollect.utils.extensions


import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import java.io.*


fun Bitmap.rotateImage(rowFile : File?): Bitmap {
    return when (ExifInterface(rowFile?.path!!).getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)) {
        ExifInterface.ORIENTATION_ROTATE_90 -> this.rotateImageAngle(90f)
        ExifInterface.ORIENTATION_ROTATE_180 -> this.rotateImageAngle(180f)
        ExifInterface.ORIENTATION_ROTATE_270 -> this.rotateImageAngle(270f)
        ExifInterface.ORIENTATION_NORMAL -> this
        else -> this
    }
}

fun Bitmap.rotateImageAngle(angle: Float): Bitmap{
    val matrix = Matrix()
    matrix.postRotate(angle)
    return Bitmap.createBitmap(
        this, 0, 0, this.width, this.height,
        matrix, true
    )
}

fun Bitmap.convertBitmapToFile(file: File) : File{
    val bos = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.JPEG, 100 /*ignored for PNG*/, bos)
    val bitMapData = bos.toByteArray()
    var fos: FileOutputStream? = null
    try {
        fos = FileOutputStream(file)
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
    }
    try {
        fos?.write(bitMapData)
        fos?.flush()
        fos?.close()
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return file
}