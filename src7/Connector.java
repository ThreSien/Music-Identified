/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cclo;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.ResultSetMetaData;
import javax.swing.JOptionPane;
import java.sql.PreparedStatement;
import java.util.LinkedList;
import net.sf.json.JSONArray;

/**
 *
 * @author k9073
 */
public class Connector {

    static final String DRIVER = "com.mysql.jdbc.Driver";
    static final String DATABASE_URL = "jdbc:mysql://localhost/music_data?";

    //static final String DATABASE_URL = "jdbc:mysql://120.110.113.205:3517/music_data?";
    public static Connection connect() {
        Connection con = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(DATABASE_URL
                    + "useUnicode=true&characterEncoding=utf8", "root", "sien");
            //+ "useUnicode=true&characterEncoding=utf8", "user", "lab517");
            JOptionPane.showMessageDialog(null, "Connected!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return con;
    }

    public static void insert(LinkedList arr) {
        Connection con = Connector.connect();
        try {
            int id = 1;//範例裡就給個1，就會返回id為1的user的各種資訊
            String data = "";
            PreparedStatement pstmt = con.prepareStatement("INSERT INTO two_song(id, scale) VALUES(?, ?);");
            pstmt.setInt(1, 0);//Auto_increment
            pstmt.setObject(2, JSONArray.fromObject(arr).toString());
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "呼叫失敗");
        }
    }

    public static LinkedList take_All_Out() {
        Connection con = Connector.connect();
        String json = "";
        LinkedList<String> take = new LinkedList<>();
        try {
            Statement stat = con.createStatement();
            ResultSet rst = stat.executeQuery("select * from two_song");
            while (rst.next()) {
                json = rst.getString("scale");
                take.offer(json);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "呼叫失敗");
        }
        return take;
    }

}
