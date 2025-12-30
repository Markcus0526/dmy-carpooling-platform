package com.damytech.Utils.mutil;

import java.util.HashMap;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

public class SoundUtil {
	private Context context;
	private SoundPool soundPool;
	private HashMap<Integer, Integer> soundPoolMap;
	private int streamVolume;
	private AudioManager am;

	public SoundUtil(Context context) {
		super();
		this.context = context;
		initSound();
	}
	
	private void initSound(){
		soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 10);
		soundPoolMap = new HashMap<Integer, Integer>();
		am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        streamVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
		am.setStreamVolume(AudioManager.STREAM_MUSIC,
                streamVolume,
				AudioManager.FLAG_PLAY_SOUND);
	}
	public void loadFx(int rawid, int id){
		soundPoolMap.put(id, soundPool.load(context, rawid, id));
	}
	public void play(int sound, int uloop){
		soundPool.play(soundPoolMap.get(sound), streamVolume, streamVolume, 1, uloop, 1);
	}

}
