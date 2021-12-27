/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cclo;

/**
 *
 * @author Ray Hu
 * This class counts playing time in the form of HH:mm:ss
 * It also updates the time slider
 */
import javax.sound.sampled.Clip;
import javax.swing.JLabel;
import javax.swing.JSlider;

public class PlayingTimer extends Thread {
	//private DateFormat dateFormater = new SimpleDateFormat("HH:mm:ss");	
	private boolean isRunning = false;
	private boolean isPause = false;
	private boolean isReset = false;

	
	private JLabel labelRecordTime;
	private JSlider slider;
	private Clip audioClip;
	

	//將Clip設定到PlayTimer
	public void setAudioClip(Clip audioClip) {
		this.audioClip = audioClip;
	}

	
        PlayingTimer(JLabel labelRecordTime, JSlider slider) {
		this.labelRecordTime = labelRecordTime;
		this.slider = slider;
	}
	
	//控制labelRecordTime跟slider的變化
	public void run() {
		isRunning = true;
		
		while (isRunning) {
			try {
				Thread.sleep(100);
				if (!isPause) {
					if (audioClip != null && audioClip.isRunning()) {
						int currentSecond = (int) audioClip.getFramePosition() / 48_000; 
						labelRecordTime.setText(secondToMinuteSecond(currentSecond));
						slider.setValue(currentSecond);
					}
				} 
			} catch (InterruptedException ex) {
//				ex.printStackTrace();
				if (isReset) {//當暫停之後會重製所有labelRecordertime跟slider的狀態
					slider.setValue(0);
					labelRecordTime.setText("00:00");
					isRunning = false;		
					break;
				}
			}catch(Exception ex){
                            
                        }
		}
	}
	
	
	/**
	 * Reset counting to "00:00:00"
	 */
	void reset() {
		isReset = true;
		isRunning = false;
	}
	
	void pauseTimer() {
		isPause = true;
	}
	
	void resumeTimer() {
		isPause = false;
	}
	
	public String secondToMinuteSecond(int currentsecond) {
		String minuteLength = "00";
		String secondLength = "00";
		int minute = currentsecond / 60;
		int second = currentsecond % 60;


		if (second == 0) { // 未滿一分鐘
			if (second < 10) {
				secondLength = "0" + Integer.toString(second);
			} else {
				secondLength = Integer.toString(second);
			}

		} else {
			if (second < 10) {
				secondLength = "0" + Integer.toString(second);
			} else {
				secondLength = Integer.toString(second);
			}
			if (minute < 10) {
				minuteLength = "0" + Integer.toString(minute);
			} else {
				minuteLength = Integer.toString(minute);
			}
		}
		return minuteLength + ":" + secondLength;
	}

}
