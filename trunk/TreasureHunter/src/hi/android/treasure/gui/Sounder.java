package hi.android.treasure.gui;

import android.media.SoundPool;
import android.media.AudioManager;


import android.content.Context;


import java.util.HashMap;
 
public class Sounder {    
    
    private SoundPool soundPool; 
    private HashMap<Integer, Integer> soundPoolMap; 
    private int currentID;
    private Context mContext;

    public Sounder(Context context) { 
	this.mContext = context;
	soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100); 
	soundPoolMap = new HashMap<Integer, Integer>(); 
	currentID = 0;
    } 
    
    public int addSound(int resourceID) {
	currentID++;
	soundPoolMap.put(currentID, soundPool.load(mContext, resourceID,1));
	return currentID;
    }
    
    public void playSound(int sound) { 
	AudioManager mgr = (AudioManager)mContext.getSystemService(Context.AUDIO_SERVICE); 
	int streamVolume = mgr.getStreamVolume(AudioManager.STREAM_MUSIC); 
	soundPool.play(soundPoolMap.get(sound), streamVolume, streamVolume, 1, 0, 1f); 
    } 
}