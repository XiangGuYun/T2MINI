package com.yp.baselib.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.NinePatchDrawable
import android.media.MediaMetadataRetriever
import android.media.ThumbnailUtils
import android.os.Environment
import android.provider.MediaStore
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.load.resource.bitmap.VideoDecoder.FRAME_OPTION
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import java.io.*
import java.nio.ByteBuffer
import java.security.MessageDigest
import java.util.*

/**
 * 位图工具类
 */
object BmpUtils {

    /**
     * 显示网络图片
     */
    @SuppressLint("CheckResult")
    fun show(ctx: Context,
             iv: ImageView,
             imgUrl: String?,
             placeholderDrawable: Triple<Int, Int, Int>? = null,
             placeholderRes: Int = -1,
             override: Pair<Int, Int> = 0 to 0,
             errorDrawable: Drawable? = null,
             errorRes: Int = -1,
             priority: Priority = Priority.NORMAL) {
        if (imgUrl == null) return
        val req = RequestOptions()
        if (placeholderDrawable != null) {
            val bmp = Bitmap.createBitmap(placeholderDrawable.first,
                    placeholderDrawable.second, Bitmap.Config.RGB_565)
            val canvas = Canvas(bmp)
            canvas.drawColor(placeholderDrawable.third)
            req.placeholder(BitmapDrawable(bmp))
        }
        if (placeholderRes != -1) {
            req.placeholder(placeholderRes)
        }
        if (errorDrawable != null) {
            req.error(errorDrawable)
        }
        if (errorRes != -1) {
            req.error(errorRes)
        }
        if (override != 0 to 0) {
            req.override(override.first, override.second)
        }
        req.priority(priority)
        Glide.with(ctx).load(imgUrl).apply(req)
                .transition(DrawableTransitionOptions.withCrossFade()).addListener(object : RequestListener<Drawable> {
                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?,
                                                 dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        return false
                    }

                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                        LogUtils.d("Glide_Url", "==== " + e?.message + "===== " + e?.localizedMessage)
                        return false
                    }

                })
                .into(iv)
    }

    /**
     * 显示网络图片，并在回调中获取Bitmap
     */
    @SuppressLint("CheckResult")
    fun show(ctx: Context,
             iv: ImageView,
             imgUrl: String?,
             placeholderDrawable: Triple<Int, Int, Int>? = null,
             placeholderRes: Int = -1,
             override: Pair<Int, Int> = 0 to 0,
             errorDrawable: Drawable? = null,
             errorRes: Int = -1,
             priority: Priority = Priority.NORMAL,
             getBitmap: (bmp: Bitmap) -> Unit) {
        if (imgUrl == null) return
        val req = RequestOptions()
        if (placeholderDrawable != null) {
            val bmp = Bitmap.createBitmap(placeholderDrawable.first,
                    placeholderDrawable.second, Bitmap.Config.RGB_565)
            val canvas = Canvas(bmp)
            canvas.drawColor(placeholderDrawable.third)
            req.placeholder(BitmapDrawable(bmp))
        }
        if (placeholderRes != -1) {
            req.placeholder(placeholderRes)
        }
        if (errorDrawable != null) {
            req.error(errorDrawable)
        }
        if (errorRes != -1) {
            req.error(errorRes)
        }
        if (override != 0 to 0) {
            req.override(override.first, override.second)
        }
        req.priority(priority)
        Glide.with(ctx).load(imgUrl).apply(req)
                .transition(DrawableTransitionOptions.withCrossFade())
                .addListener(object : RequestListener<Drawable> {
                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?,
                                                 dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        return false
                    }

                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                        LogUtils.d("Glide_Url", "==== " + e?.message + "===== " + e?.localizedMessage)
                        return false
                    }

                })
                .into(object : SimpleTarget<Drawable>() {
                    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                        iv.setImageDrawable(resource)
                        getBitmap.invoke(drawable2Bitmap(resource)!!)
                    }
                })
    }

    /**
     * 将图片地址转换为Bitmap
     */
    fun url2Bmp(ctx: Context, imgUrl: String, getBitmap: (bmp: Bitmap) -> Unit) {
        Glide.with(ctx).load(imgUrl).apply(RequestOptions()
//                .override(320, 240).
                .priority(Priority.IMMEDIATE)
                .fitCenter())
                .addListener(object : RequestListener<Drawable> {
                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?,
                                                 dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        return false
                    }

                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                        LogUtils.d("Glide_Url", "==== " + e?.message + "===== " + e?.localizedMessage)
                        return false
                    }

                })
                .into(object : SimpleTarget<Drawable>() {
                    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                        if (imgUrl.contains(".gif")) {
                            val res = (resource as GifDrawable)
                            getBitmap.invoke(res.firstFrame)
                        } else {
                            getBitmap.invoke(drawable2Bitmap(resource)!!)
                        }
                    }
                })
    }

    /**
     * 图片模糊处理
     */
    fun rsBlur(context: Context, source: Bitmap, radius: Int): Bitmap {
        val inputBmp = source
        //(1)
        val renderScript = RenderScript.create(context)

        Log.i("Test", "scale size:" + inputBmp.getWidth() + "*" + inputBmp.getHeight())

        // Allocate memory for Renderscript to work with
        //(2)
        val input = Allocation.createFromBitmap(renderScript, inputBmp)
        val output = Allocation.createTyped(renderScript, input.getType())
        //(3)
        // Load up an instance of the specific script that we want to use.
        val scriptIntrinsicBlur = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript))
        //(4)
        scriptIntrinsicBlur.setInput(input)
        //(5)
        // Set the blur radius
        scriptIntrinsicBlur.setRadius(radius.toFloat())
        //(6)
        // Start the ScriptIntrinisicBlur
        scriptIntrinsicBlur.forEach(output)
        //(7)
        // Copy the output to the blurred bitmap
        output.copyTo(inputBmp)
        //(8)
        renderScript.destroy()

        return inputBmp
    }

    /**
     * 获取文件路径下的Bitmap
     * @param filePath 文件路径
     * @param targetW 想要的宽度值
     * @param targetH 想要的高度值
     * @return
     */
    fun decodeBitmap(filePath: String, targetW: Int, targetH: Int): Bitmap {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true//设置只解码图片的边框（宽高）数据，只为测出采样率
        options.inPreferredConfig = Bitmap.Config.RGB_565//设置图片像素格式的首选配置
        BitmapFactory.decodeFile(filePath, options)//预加载
        //获取图片的原始宽高
        val originalW = options.outWidth
        val originalH = options.outHeight
        //设置采样大小
        options.inSampleSize = getSimpleSize(originalW, originalH, targetW, targetH)
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeFile(filePath, options)
    }

    /**
     * 获取本地资源下的Bitmap
     */
    fun decodeBitmap(ctx: Context, id: Int, targetW: Int, targetH: Int): Bitmap {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true//设置只解码图片的边框（宽高）数据，只为测出采样率
        options.inPreferredConfig = Bitmap.Config.RGB_565//设置图片像素格式的首选配置
        BitmapFactory.decodeResource(ctx.resources, id, options)//预加载
        //获取图片的原始宽高
        val originalW = options.outWidth
        val originalH = options.outHeight
        //设置采样大小
        options.inSampleSize = getSimpleSize(originalW, originalH, targetW, targetH)
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeResource(ctx.resources, id, options)
    }

    /**
     * 将Bitmap保存到SD卡中
     */
    fun saveBitmapToSDCard(bitmap: Bitmap, fileName: String, quality: Int) {
        val f = File(Environment.getExternalStorageDirectory().toString(), fileName)
        var out: FileOutputStream? = null
        try {
            out = FileOutputStream(f)
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out)
            try {
                out.flush()
                out.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
    }

    /**
     * 将Bitmap保存到缓存中
     */
    fun saveBitmapToCache(ctx: Context, bitmap: Bitmap, fileName: String, quality: Int) {
        val f = File("/data/data/${ctx.packageName}/cache", fileName)
        var out: FileOutputStream? = null
        try {
            out = FileOutputStream(f)
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out)
            try {
                out.flush()
                out.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
    }

    /**
     * 计算采样率
     */
    private fun getSimpleSize(originalW: Int, originalH: Int, targetW: Int, targetH: Int): Int {
        var sampleSize = 1
        if (originalW > originalH && originalW > targetW) {//以宽度来计算采样值
            sampleSize = originalW / targetW
        } else if (originalW < originalH && originalH > targetH) {
            sampleSize = originalH / targetH
        }
        if (sampleSize <= 0) {
            sampleSize = 1
        }
        return sampleSize
    }


    /**
     * 去除Bitmap的透明背景，并替换颜色
     */
    fun drawBg4Bitmap(color: Int, originBitmap: Bitmap): Bitmap {
        val paint = Paint().apply { setColor(color) }
        val bitmap = Bitmap.createBitmap(originBitmap.width, originBitmap.height, originBitmap.config)
        val canvas = Canvas(bitmap)
        canvas.drawRect(0f, 0f, originBitmap.width.toFloat(), originBitmap.height.toFloat(), paint)
        canvas.drawBitmap(originBitmap, 0f, 0f, paint)
        return bitmap
    }

    /**
     * Bitmap转ByteArray
     */
    fun bitmap2Bytes1(bmp: Bitmap, isPng: Boolean = true, quality: Int = 100): ByteArray {
        val o = ByteArrayOutputStream()
        if (isPng)
            bmp.compress(Bitmap.CompressFormat.PNG, quality, o)
        else
            bmp.compress(Bitmap.CompressFormat.JPEG, quality, o)
        return o.toByteArray()
    }

    /**
     * Bitmap转ByteArray
     */
    fun bitmap2Bytes2(bmp: Bitmap): ByteArray {
        val bytes = bmp.byteCount
        val buf = ByteBuffer.allocate(bytes)
        bmp.copyPixelsToBuffer(buf)
        return buf.array()
    }


    /**
     * ByteArray转Bitmap
     */
    fun bytes2Bitmap(ba: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(ba, 0, ba.size)
    }

    /**
     * Bitmap转Drawable
     */
    fun bitmap2Drawable(bmp: Bitmap): Drawable {
        return BitmapDrawable(bmp)
    }

    /**
     * Drawable转Bitmap
     *
     * @param drawable
     * @return
     */
    fun drawable2Bitmap(drawable: Drawable?): Bitmap? {
        if (drawable == null) return null
        return if (drawable is BitmapDrawable) {
            drawable.bitmap
        } else if (drawable is NinePatchDrawable) {
            val bitmap = Bitmap
                    .createBitmap(
                            drawable.getIntrinsicWidth(),
                            drawable.getIntrinsicHeight(),
                            if (drawable.getOpacity() != PixelFormat.OPAQUE) Bitmap.Config.ARGB_8888 else Bitmap.Config.RGB_565)
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight())
            drawable.draw(canvas)
            bitmap
        } else {
            null
        }
    }

    /**
     * 压缩Bitmap质量
     * @param bitmap
     * @param quality 1-100
     * @return
     */
    fun compressQuality1(bitmap: Bitmap, quality: Int): Bitmap? {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos)
        val bytes = baos.toByteArray()
        try {
            baos.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }


    /**
     * 压缩Bitmap质量，直到文件小于某个目标KB大小为止
     * @param image
     * @param targetSize
     * @return
     */
    fun compressQuality2(image: Bitmap, targetSize: Int = 100): Bitmap {
        val baos = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos) //质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        var options = 100
        while (baos.toByteArray().size / 1024 > targetSize) { //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset() //重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos) //这里压缩options%，把压缩后的数据存放到baos中
            options -= 10 //每次都减少10
            if (options == 0) {
                break
            }
        }
        val isBm = ByteArrayInputStream(baos.toByteArray()) //把压缩后的数据baos存放到ByteArrayInputStream中
        return BitmapFactory.decodeStream(isBm, null, null)!!
    }

    /**
     * 获取视频文件的第一帧截图
     * @param path 视频文件的路径
     * @return Bitmap 返回获取的Bitmap
     */
    fun getVideoThumb(path: String, compress: Boolean = true): Bitmap {
        val media = MediaMetadataRetriever()
        media.setDataSource(path)
        val bitmap = media.frameAtTime
        if (!compress) {
            return bitmap
        }
        return compressQuality2(bitmap)
    }

    /**
     * 获取本地视频信息
     */
    fun getVideoInfo(path: String): Triple<String, String, String> {
        val media = MediaMetadataRetriever()
        media.setDataSource(path)
        val duration = media.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_DURATION)
        val width = media.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)
        val height = media.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)
        return Triple(width, height, duration)
    }

    /**
     * 获取本地视频缩略图
     * @param filePath
     * @param kind
     * @return
     */
    fun createVideoThumbnail(filePath: String, kind: Int): Bitmap? {
        var bitmap: Bitmap? = null
        val retriever = MediaMetadataRetriever()
        try {
            if (filePath.startsWith("http://")
                    || filePath.startsWith("https://")
                    || filePath.startsWith("widevine://")) {
                retriever.setDataSource(filePath, Hashtable())
            } else {
                retriever.setDataSource(filePath)
            }
            bitmap = retriever.getFrameAtTime(0, MediaMetadataRetriever.OPTION_CLOSEST_SYNC) //retriever.getFrameAtTime(-1);
        } catch (ex: IllegalArgumentException) {
            // Assume this is a corrupt video file
            ex.printStackTrace()
        } catch (ex: RuntimeException) {
            // Assume this is a corrupt video file.
            ex.printStackTrace()
        } finally {
            try {
                retriever.release()
            } catch (ex: RuntimeException) {
                // Ignore failures while cleaning up.
                ex.printStackTrace()
            }
        }
        if (bitmap == null) {
            return null
        }
        if (kind == MediaStore.Images.Thumbnails.MINI_KIND) { //压缩图片 开始处
            // Scale down the bitmap if it's too large.
            val width = bitmap.width
            val height = bitmap.height
            val max = Math.max(width, height)
            if (max > 512) {
                val scale = 512f / max
                val w = Math.round(scale * width)
                val h = Math.round(scale * height)
                bitmap = Bitmap.createScaledBitmap(bitmap, w, h, true)
            } //压缩图片 结束处
        } else if (kind == MediaStore.Images.Thumbnails.MICRO_KIND) {
            bitmap = ThumbnailUtils.extractThumbnail(bitmap,
                    96,
                    96,
                    ThumbnailUtils.OPTIONS_RECYCLE_INPUT)
        }
        return bitmap
    }


    /**
     * 加载网络视频缩略图
     */
    @SuppressLint("CheckResult")
    fun loadVideoScreenshot(context: Context, uri: String, imageView: ImageView, frameTimeMicros: Long) {
        val requestOptions = RequestOptions.frameOf(frameTimeMicros)
        requestOptions.set(FRAME_OPTION, MediaMetadataRetriever.OPTION_CLOSEST)
        requestOptions.transform(object : BitmapTransformation() {
            override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
                return toTransform
            }

            override fun updateDiskCacheKey(messageDigest: MessageDigest) {
                try {
                    messageDigest.update((context.packageName + "RotateTransform").toByteArray(charset("utf-8")))
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        })
        Glide.with(context).load(uri).apply(requestOptions).into(imageView)
    }


}