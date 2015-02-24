/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.util.Arrays;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HConnectionManager;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

/**
 *
 * @author leon
 */
@WebServlet(urlPatterns = {"/q6"})
public class q6 extends HttpServlet {
    
    private HConnection connection;
    private Configuration conf;
    private final static String publicDNS = "ec2-54-88-228-11.compute-1.amazonaws.com";
    private static String TEAM;
    private static String tableName;
    private final static int MAX_VALUE = 48856657;
    private final static int MIN_VALUE = 3;
    private final static long MAX_ID = 2594921025L;
    private final static long MIN_ID = 12L;
    @Override
    public void init() {
        try {
            TEAM = "DEADLINE,276906431060,152339165514,931814217121\n";
            tableName = "q6";
            conf = new Configuration();
            conf.set("hbase.master", publicDNS + ":60000");
            conf.set("hbase.zookeeper.quorum", publicDNS);
            conf.setInt("hbase.hconnection.threads.max", 80);
            conf.setInt("hbase.zookeeper.property.maxClientCnxns", 100);
            connection = HConnectionManager.createConnection(conf);
            System.out.println("try connecting");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     * response)
     */
    @Override
    protected void doGet(HttpServletRequest request,
        HttpServletResponse response) throws ServletException, IOException {
        HTableInterface table;
        StringBuilder sb = new StringBuilder();
        sb.append(TEAM);
        table = connection.getTable(tableName);
        int max = 0;
        int min = 0;
        long m = Long.parseLong(request.getParameter("m"));
        long n = Long.parseLong(request.getParameter("n"));
        if(m > n) {
            sb.append(0).append("\n");
        }
        else { 
            if(m <= MIN_ID)  min = MIN_VALUE; 
            else {
                byte[] key = Bytes.toBytes(Long.MAX_VALUE - m);
                Scan sc = new Scan(key);
                sc.setCaching(1);
                sc.setSmall(true);
                ResultScanner scanner = table.getScanner(sc);
                Result next = scanner.next();
                if(Arrays.equals(key,next.getRow())){
                    min = byteToInt(scanner.next().value());
                }
                else{
                    min = byteToInt(next.value());
                }
                scanner.close();
            }
            if(n >= MAX_ID)  max = MAX_VALUE; 
            else {
                Scan sc = new Scan(Bytes.toBytes(Long.MAX_VALUE - n));
                sc.setCaching(1);
                sc.setSmall(true);
                ResultScanner scanner = table.getScanner(sc);
                max = byteToInt(scanner.next().value());
                scanner.close();
                }
            sb.append(max - min).append("\n");
            }
        
        ServletOutputStream out = response.getOutputStream();
        out.write(sb.toString().getBytes());
    }

    
    private int byteToInt(byte[] b) {
        
        return ((b[0] & 0xff) << 24) |
                 ((b[1] & 0xff) << 16) |
                 ((b[2] & 0xff) << 8) |
                 (b[3] & 0xff);
    }

}

