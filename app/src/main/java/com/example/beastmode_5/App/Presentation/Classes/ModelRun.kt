package com.example.beastmode_5.App.Presentation.Classes

import android.content.Context
import android.graphics.Bitmap
import android.graphics.RectF
import androidx.compose.ui.graphics.Matrix
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.channels.FileChannel

class ModelRun(private val context: Context) {
    val interpreter: Interpreter

    init {
        val model = context.assets.openFd("pothole_model.tflite")
        val inputstream = FileInputStream(model.fileDescriptor)
        val modelbuffer = inputstream.channel.map(
            FileChannel.MapMode.READ_ONLY,
            model.startOffset,
            model.declaredLength
        )
        interpreter = Interpreter(modelbuffer)
    }

    fun understand_input_stream() {
        val inputtensor = interpreter.getInputTensor(0)
        println("Input tensorshape ${inputtensor.shape().contentToString()}")
        println("Input tensor datatype ${inputtensor.dataType()}")
    }

    fun undertand_output_tensor() {
        for (i in 0 until interpreter.getOutputTensorCount()) {
            val outputtensor = interpreter.getOutputTensor(i)
            println("Output tensorshape ${outputtensor.shape().contentToString()}")
            println("Output tensor datatype ${outputtensor.dataType()}")
        }
    }

    fun runstaticimg(bitmap: Bitmap):List<DetectionBox>{
        val tempresult = mutableListOf<DetectionBox>()
        val final_result= mutableListOf<DetectionBox>()
        val resizedbitmap = Bitmap.createScaledBitmap(
            bitmap,
            640,
            640,
            true
        )
        val inputbuffer = ByteBuffer.allocateDirect(1 * 640 * 640 * 3 * 4)
        inputbuffer.order(ByteOrder.nativeOrder())
        for (y in 0 until 640) {
            for (x in 0 until 640) {
                val pixel = resizedbitmap.getPixel(x, y)
                val r = pixel.red
                val g = pixel.green
                val b = pixel.blue
                inputbuffer.putFloat(r.toFloat() / 255f)
                inputbuffer.putFloat(g.toFloat() / 255f)
                inputbuffer.putFloat(b.toFloat() / 255f)
            }
        }
        val output = Array(1) {
            Array(5) {
                FloatArray(8400)
            }
        }
        interpreter.run(inputbuffer, output)
        for (i in 0 until 8400) {
            if (output[0][4][i] > 0.5) {
                /*    val x=output[0][0][i]
            val y=output[0][1][i]
            val w=output[0][2][i]
            val h=output[0][3][i]
            val score=output[0][4][i]
            println("Prediction $i")
            println("x:$x y:$y w:$w h:$h score:$score")removed this as it was only for testing */

                val centerx = output[0][0][i] * 640
                val centery = output[0][1][i] * 640
                val width = output[0][2][i] * 640
                val height = output[0][3][i] * 640
                val left = centerx - width / 2
                val top = centery - height / 2
                val right = centerx + width / 2
                val bottom = centery + height / 2
                /*   val scale=maxOf(
            bitmap.width/resizedbitmap.width,
            bitmap.height/resizedbitmap.height
        )
        val scale_width=bitmap.width*resizedbitmap.width
        val horizontaloffset=(scale_width-bitmap.width)/2f we dont need this bcz we have same scale */
                //making the coordinates correcty for an img of width and height of bitmap ,we can skip this and return box coordinated of 640x640 directly and then in canvas we can scale for img size displayed in canvas rather than resizing the box coordinates to orginal img
                //and after that in canvas we again scale it to draw bounding box for that particular height and width
                val scalex = bitmap.width / resizedbitmap.width.toFloat()
                val scaley = bitmap.height / resizedbitmap.height.toFloat()
                val newleft = left * scalex
                val newtop = top * scaley
                val newright = right * scalex
                val newbottom = bottom * scaley
                val box = RectF(newleft, newtop, newright, newbottom)
                val score = output[0][4][i]
                val detectionbox = DetectionBox(
                    box,
                    score
                )
                tempresult.add(detectionbox)
            }
        }
        val alldetections=tempresult
        val allscores_sorted=alldetections.sortedByDescending { it.score }.toMutableList()
        while(allscores_sorted.isNotEmpty()){
            val best=allscores_sorted.removeAt(0)
            final_result.add(best)
            val iterator=allscores_sorted.iterator()
            while(iterator.hasNext()){
                val candidate=iterator.next()
                val iou=calculateIoU(best.box,candidate.box)
                if(iou>0.5){
                    iterator.remove()
                }
            }
        }
        return final_result
    }

        fun calculateIoU(a: RectF, b: RectF): Float {

            val left = maxOf(a.left, b.left)
            val top = maxOf(a.top, b.top)

            val right = minOf(a.right, b.right)
            val bottom = minOf(a.bottom, b.bottom)

            val intersectionWidth = maxOf(0f, right - left)
            val intersectionHeight = maxOf(0f, bottom - top)

            val intersectionArea =
                intersectionWidth * intersectionHeight

            val areaA = a.width() * a.height()
            val areaB = b.width() * b.height()

            val unionArea =
                areaA + areaB - intersectionArea

            return intersectionArea / unionArea
        }

    fun RunLiveDetection(bitmap: Bitmap, rotationDegrees: Int):List<DetectionBox>{
        val tempresult=mutableListOf<DetectionBox>()
        val finalresult=mutableListOf<DetectionBox>()
        val rotatedbitmap=rotateBitmap(bitmap,rotationDegrees)
        val resizedbitmap= Bitmap.createScaledBitmap(
            rotatedbitmap,
            640,
            640,
            true
        )
        val inputbuffer= ByteBuffer.allocateDirect(
            1*640*640*3*4)
        inputbuffer.order(ByteOrder.nativeOrder())
        for(y in 0 until 640){
            for(x in 0 until 640){
                val pixel=resizedbitmap.getPixel(x,y)
                val r=pixel.red
                val g=pixel.green
                val b=pixel.blue
                inputbuffer.putFloat(r.toFloat()/255f)
                inputbuffer.putFloat(g.toFloat()/255f)
                inputbuffer.putFloat(b.toFloat()/255f)
            }
        }
        val output= Array(1){
            Array(5){
                FloatArray(8400)
            }
        }
        inputbuffer.rewind()
        interpreter.run(inputbuffer,output)

        for(i in 0 until 8400){
            if(output[0][4][i]>0.5){

                val center_x=output[0][0][i]*640
                val center_y=output[0][1][i]*640
                val width=output[0][2][i]*640
                val height=output[0][3][i]*640

                val left=center_x-width/2
                val right=center_x+width/2
                val top=center_y-height/2
                val bottom=center_y+height/2

                val scale_x=rotatedbitmap.width/640f
                val scale_y=rotatedbitmap.height/640f

                val box=RectF(left*scale_x,
                    top*scale_y,
                    right*scale_x,
                    bottom*scale_y)
                val score=output[0][4][i]
                val detectionbox=DetectionBox(box,score)
                tempresult.add(detectionbox)
            }
        }
        val sorted_pothole_scores=tempresult.sortedByDescending { it.score }.toMutableList()
        while(sorted_pothole_scores.isNotEmpty()){
            val best=sorted_pothole_scores.removeAt(0)
            finalresult.add(best)
            val iterator=sorted_pothole_scores.iterator()
            while(iterator.hasNext()){
                val candiate=iterator.next()
                val iou=calculateIoU(best.box,candiate.box)
                if(iou>0.5){
                    iterator.remove()
                }
            }
        }
        return finalresult
    }

    fun rotateBitmap(bitmap: Bitmap, rotationDegrees: Int): Bitmap{
        if(rotationDegrees==0) return bitmap
        val matrix= android.graphics.Matrix()
        matrix.postRotate(rotationDegrees.toFloat())
        return Bitmap.createBitmap(
            bitmap,
            0,
            0,
            bitmap.width,
            bitmap.height,
            matrix,
            true
        )
    }

    }
// ITERATOR IMPORTANT POINTS:
//
// iterator() gives us a way to move through the list one element at a time.
//
// hasNext()
// → Checks whether there is another element available to read.
// → It does NOT mean there must be 2 elements.
// → If even 1 element is available, it returns true.
//
// next()
// → Gives me the next/current element from the iterator.
// → We store that element in `candidate`.
// → After giving the element, the iterator moves forward to the next position.
//
// IMPORTANT:
// next() gives me B, then the iterator moves toward C.
// But `remove()` still removes B, NOT C.
//
// remove()
// → Removes the element that was returned by the MOST RECENT `next()` call.
// → So if next() gave me B, remove() removes B.
// → If next() then gives me C, remove() removes C.
//
// In NMS:
// → next() gives us the candidate box to compare with `best`.
// → If IoU is too high, iterator.remove() removes that candidate
//   because it is considered a duplicate of `best`.
// → If IoU is low, we DON'T remove it.
//   It stays in the list so it can become `best` in a later iteration.
//
// Simple memory:
// hasNext() → "Is there another element?"
// next()    → "Give me that element."
// remove()  → "Remove the element you just gave me."




// IMPORTANT:
// The YOLO model uses a 640 × 640 input image,
// but the actual rotated camera image may have a different size,
// for example 480 × 640.
//
// Therefore, we cannot directly use:
// preview_view.width / 640f
//
// We first need to convert the YOLO box coordinates
// from the 640 × 640 MODEL coordinate system
// back to the ROTATED CAMERA IMAGE coordinate system.
//
// Example:
// Model input      = 640 × 640
// Rotated image    = 480 × 640
//
// X scale = 480 / 640 = 0.75
// Y scale = 640 / 640 = 1.0
//
// After that, we convert the camera-image coordinates
// to PreviewView coordinates using the FILL_CENTER scaling.
//
// So the complete transformation is:
//
// YOLO 640 × 640
//       ↓
// Rotated camera image (e.g. 480 × 640)
//       ↓
// FILL_CENTER transformation
//       ↓
// PreviewView coordinates
//
// We cannot treat the 640 × 640 model image as the image
// displayed by PreviewView because the model input was created
// by stretching the camera image to 640 × 640.




//The model stretches/resizes the camera image, so first we convert the detected box back to the original camera image coordinates.
//Then we convert those camera-image coordinates to PreviewView coordinates so the box appears correctly on the screen.
//This applies to any model that returns bounding boxes based on a resized/stretched input image.
