package cclo;

import edu.cmu.sphinx.frontend.*;
import edu.cmu.sphinx.frontend.filter.Preemphasizer;
import edu.cmu.sphinx.frontend.window.RaisedCosineWindower;
// import edu.cmu.sphinx.frontend.transform.DiscreteFourierTransform;
import edu.cmu.sphinx.frontend.util.Microphone;
import java.util.*;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javax.swing.JFrame;
import javax.swing.Timer;
import org.jfree.ui.RefineryUtilities;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Main implements Share {

    Microphone mic;
    DataBlocker dataBlock;
    Preemphasizer preEmp;
    RaisedCosineWindower windower;
    DiscreteFourierTransform dff;
    // public Scorer incFr;
    // public PercentPan percPan;
    public int nodeType;
    public int width, height;
    //public testframe tFrame;
    //public Connection con=null;
    public Connector con = null;
    public static int binaryControl;
    public static ContactEditorUI homePage;
    public static SearchResult resultPage;
    public static boolean startRec = false, finishRec = false;

    public Main() {
    }

    public void initVoice() {
        mic = new Microphone(
                44100, // int sampleRate
                16, // int bitsPerSample, 
                1, // int channels,
                true, // boolean bigEndian, 
                true, // boolean signed
                true, // boolean closeBetweenUtterances, 
                10, // int msecPerRead, 
                false, // boolean keepLastAudio,
                "average", // String stereoToMono
                0, // int selectedChannel, 
                "default", // String selectedMixerIndex
                6400 // int audioBufferSize
        );
        dataBlock = new DataBlocker(10.0);
        preEmp = new Preemphasizer(0.97);
        windower = new RaisedCosineWindower(0.46, 25.625f, 10.0f);
        dff = new DiscreteFourierTransform(2048, false);
        dff.setAED(this);

        mic.initialize();
        dataBlock.initialize();
        preEmp.initialize();
        windower.initialize();
        dff.initialize();

        dff.setPredecessor(windower);
        windower.setPredecessor(preEmp);
        //windower.setPredecessor(dataBlock);
        preEmp.setPredecessor(dataBlock);
        dataBlock.setPredecessor(mic);
    }

    public void initFrames() {
        homePage = new ContactEditorUI(this);
        homePage.setVisible(true);
        homePage.setResizable(false);
        homePage.setLocationRelativeTo(null);
        resultPage = new SearchResult(this);
        
    }
    public void micClear(){
        mic.clear();
    }
    public static void main(String[] args) {
        Main aed = new Main();
        Scanner sc = new Scanner(System.in);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//        double sWidth = screenSize.getWidth();
        double sHeight = screenSize.getHeight();
        aed.initVoice();
        aed.initFrames();
        double empty[]=new double[1024];
        homePage.updateSpec(empty);
        //aed.initConnect();
        //++ Window size

        //start the microphone or exit if the programm if this is not possible
         if (!aed.mic.startRecording()) {
            System.out.println("Cannot start microphone.");
            System.exit(1);
        }

        while (true) {
            
            while (true) {
                System.out.print("");//這行跟鬼一樣 千萬別刪 真的 相信我 
                if (startRec == true && finishRec == false) {
                    binaryControl = 0;
                    
//                    System.out.println("開始錄製");
//                    System.out.println("0:使用者 1:資料庫");
//                    binaryControl = sc.nextInt();
                    break;
                }
            }
            while (!finishRec) {
                try {
                    Data input = aed.dff.getData();
                    DoubleData output = (DoubleData) input;
                } catch (DataProcessingException ex) {
                    System.out.println(ex.getMessage());
                    if (binaryControl == 0) {//使用者
                        aed.dff.voice_Compare(); //開始特徵比對
                        aed.dff.userInput.clear(); //比對完成後清除使用者輸入暫存
                        aed.dff.searchResult.clear();//比對完成後清除搜尋結果暫存
                        aed.dff.RecBegin = false;
                        aed.dff.soundCnt = 0;
                        startRec = false;
                        finishRec = false;
                        homePage.setVisible(false);
                        resultPage.setVisible(true);
                        resultPage.setResizable(true);
                        break;
                    } else if (binaryControl == 1) {
                        break;
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }
    }
}
