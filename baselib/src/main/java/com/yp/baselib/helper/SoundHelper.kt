package com.yp.baselib.helper

import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import com.yp.baselib.Ctx

/**
 * 音效助手类
 */
class SoundHelper private constructor() {

    private var voiceId = 0
    private var soundPool: SoundPool?=null

    companion object {

        private var helper: SoundHelper? = null

        fun getInstance(): SoundHelper {
            if (helper == null) {
                helper = SoundHelper()
            }
            return helper!!
        }
    }

    init {
        val builder = SoundPool.Builder()
        //传入音频数量
        builder.setMaxStreams(1)
        //AudioAttributes是一个封装音频各种属性的方法
        val attrBuilder = AudioAttributes.Builder()
        //设置音频流的合适的属性
        attrBuilder.setLegacyStreamType(AudioManager.STREAM_ALARM) //STREAM_MUSIC
        //加载一个AudioAttributes
        builder.setAudioAttributes(attrBuilder.build())
        soundPool = builder.build()
    }

    fun play(ctx: Ctx, soundId: Int) {
        soundPool?.let {
            voiceId = it.load(ctx, soundId, 1)
            //异步需要等待加载完成，音频才能播放成功
            //异步需要等待加载完成，音频才能播放成功
            it.setOnLoadCompleteListener { soundPool, sampleId, status ->
                if (status == 0) {
                    //第一个参数soundID
                    //第二个参数leftVolume为左侧音量值（范围= 0.0到1.0）
                    //第三个参数rightVolume为右的音量值（范围= 0.0到1.0）
                    //第四个参数priority 为流的优先级，值越大优先级高，影响当同时播放数量超出了最大支持数时SoundPool对该流的处理
                    //第五个参数loop 为音频重复播放次数，0为值播放一次，-1为无限循环，其他值为播放loop+1次
                    //第六个参数 rate为播放的速率，范围0.5-2.0(0.5为一半速率，1.0为正常速率，2.0为两倍速率)
                    soundPool.play(voiceId, 1f, 1f, 1, 0, 1f)
                }
            }
        }
    }

    fun release(){
        soundPool?.autoPause();
        soundPool?.unload(voiceId);
        soundPool?.release();
    }

}