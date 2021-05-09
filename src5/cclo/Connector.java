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
    static final String DATABASE_URL = "jdbc:mysql://localhost:3306/music_data?";

    public static Connection connect() {
        Connection con = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(DATABASE_URL
                    + "useUnicode=true&characterEncoding=utf8", "root", "keviny09");
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
            PreparedStatement pstmt = con.prepareStatement("INSERT INTO json_data(id, json) VALUES(?, ?);");
            pstmt.setInt(1,0);//Auto_increment
            pstmt.setString(2,JSONArray.fromObject(arr).toString());
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "呼叫失敗");
        }
    }

    public static void takeout() {
        Connection con = Connector.connect();
        try {
            Statement stat = con.createStatement();
            ResultSet rst = stat.executeQuery("select * from data");
            while (rst.next()) {
                String data = rst.getString("data");
                System.out.println(" data= " + data);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "呼叫失敗");
        }
    }
}
