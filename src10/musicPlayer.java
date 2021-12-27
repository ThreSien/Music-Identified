/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cclo;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
//import javax.swing.JLabel;
//import javax.swing.JSlider;

/**
 *
 * 
 */
public class musicPlayer implements LineListener {
	static musicPlayer player = new musicPlayer();

	private boolean playComplete;
	private boolean isStopped;
	private boolean isPause;

	Clip AudiostreamClip;//將路徑取得的音樂檔案變成串流並適用clip liberary

	public static musicPlayer getInstance() {
		return player;
	}

	//路徑在SearchResult的55行，路徑已經寫死了，未來將資料庫選擇加上去
	public void loadMusic(String filepath) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		File musicPath = new File(filepath);
		AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
		AudioFormat format = audioInput.getFormat();
		DataLine.Info info = new DataLine.Info(Clip.class, format);
		AudiostreamClip = (Clip)AudioSystem.getLine(info);
		AudiostreamClip.addLineListener(this);
		AudiostreamClip.open(audioInput);

	}

	// 將取得的音樂檔案轉換為分:秒格式
	public String getClipLengthString() {
		String minutelength = "00";
		String seclength = "00";
		int secondstotal = AudiostreamClip.getFrameLength() / 48_000;// 48000需要依照電腦音訊卡做更換
		int minute = secondstotal / 60;
		int second = secondstotal % 60;

		if (second == 0) {
			if (second < 10) {
				seclength = "0" + Integer.toString(second);
			} else {
				seclength = Integer.toString(second);
			}
		} else {
			if (second < 10) {
				seclength = "0" + Integer.toString(second);
			} else {
				seclength = Integer.toString(second);
			}
			if (minute < 10) {
				minutelength = "0" + Integer.toString(minute);
			} else {
				minutelength = Integer.toString(minute);
			}
		}

		return minutelength + ":" + seclength;
	}

	//將load載入的音樂檔案傳入PlayTimer用來控制labelCounter跟slider
	Clip getClip_ToPlayingTimer(){
		return AudiostreamClip;
	}

	//引用在503行playBack裡面
	void playerStart() throws IOException{
		AudiostreamClip.start();

		playComplete = false;
		isStopped = false;

		while(!playComplete){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException ex) {
//				ex.printStackTrace();
				if(isStopped){
//					System.out.println("Stop Event");
					AudiostreamClip.stop();
					break;
				} 
				if(isPause){
//					System.out.println("Pause Event");
					AudiostreamClip.stop();
				} else {
//					System.out.println("Resume Event");
					AudiostreamClip.start();
				}
			}catch(Exception ex){
                            System.out.println(ex);
                        }
		}
		AudiostreamClip.close();
	}


	public void resume(){
		isPause = false;
	}

	public void stop(){
		isStopped = true;
	}

	void pause(){
		isPause = true;
	}

	//線程控制，偵測音樂播放狀態
	@Override
	public void update(LineEvent event) {
		LineEvent.Type type = event.getType();
		if (type == LineEvent.Type.STOP) {
			System.out.println("STOP EVENT");
			if (isStopped || !isPause) {
				playComplete = true;
			}
		}
	}
}
