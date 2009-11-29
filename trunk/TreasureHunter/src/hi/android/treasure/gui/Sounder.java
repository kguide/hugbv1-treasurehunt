package hi.android.treasure.gui;

import android.media.SoundPool;
import android.media.AudioManager;


import android.content.Context;


import java.util.HashMap;
 
public class Sounder {    
    
    private SoundPool soundPool; 
    private HashMap<Integer, Integer> soundPoolMap; 

    private AudioManager mAudioManager;
    private Context mContext;

    public Sounder(Context context) { 
	this.mContext = context;
	soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100); 
	soundPoolMap = new HashMap<Integer, Integer>(); 

    } 
    
    public void addSound(int resourceID) {
	soundPoolMap.put(resourceID, soundPool.load(mContext, resourceID,1));
    }
    
    public void playSound(int sound) { 
	mAudioManager = (AudioManager)mContext.getSystemService(Context.AUDIO_SERVICE); 
	int streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC); 
	soundPool.play(soundPoolMap.get(sound), streamVolume,streamVolume, 1, 0, 1f); 
    } 
}