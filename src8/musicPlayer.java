/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cclo;


import java.io.File;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

/**
 *
 * @author Ray Hu
 */
public class musicPlayer {
    static musicPlayer player = new musicPlayer();
    
    static Clip clip;
    
    //private Clip audioClip;
    private static final int SECONDS_IN_HOUR = 60 * 60;
    private static final int SECONDS_IN_MINUTE = 60;
    
    
    
    private musicPlayer()
    {
        
    }
    
    public static musicPlayer getInstance(){
        return player;
    }
    
    public void loadMusic(String filepath)
    {
        try{
            File musicPath = new File(filepath);
            
            if(musicPath.exists()){
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
                clip=AudioSystem.getClip();
                clip.open(audioInput);
                
               
                
                System.out.println("Initilised");
            }
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
    public String getClipLengthString() {
		String length = "";
		long hour = 0;
		long minute = 0;
		long seconds = clip.getMicrosecondLength() / 1_000_000;
		
		System.out.println(seconds);
		
		if (seconds >= SECONDS_IN_HOUR) {
			hour = seconds / SECONDS_IN_HOUR;
			length = String.format("%02d:", hour);
		} else {
			length += "00:";
		}
		
		minute = seconds - hour * SECONDS_IN_HOUR;
		if (minute >= SECONDS_IN_MINUTE) {
			minute = minute / SECONDS_IN_MINUTE;
			length += String.format("%02d:", minute);
			
		} else {
			minute = 0;
			length += "00:";
		}
		
		long second = seconds - hour * SECONDS_IN_HOUR - minute * SECONDS_IN_MINUTE;
		
		length += String.format("%02d", second);
		
		return length;
	}
    
    public Clip getAudioClip() {
		return clip;
	}	

  
    
    
}
