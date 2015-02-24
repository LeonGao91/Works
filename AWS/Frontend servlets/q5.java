/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HConnectionManager;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Row;
import org.apache.hadoop.hbase.util.Bytes;

/**
 *
 * @author leon
 */
@WebServlet(urlPatterns = {"/q5"})
public class q5 extends HttpServlet {

    private HConnection connection;
    private Configuration conf;
    private final static String publicDNS = "ec2-54-88-228-11.compute-1.amazonaws.com";
    private static String TEAM;
    private static int[] keys;
    private static String tableName;
    private final static String T = "\t";
    private final static String N = "\n";
    private final static String X = "X";
    @Override
    public void init() {
        try {
            TEAM = "DEADLINE,276906431060,152339165514,931814217121\n";
            tableName = "q5-2";
            conf = new Configuration();
            conf.set("hbase.master", publicDNS + ":60000");
            conf.set("hbase.zookeeper.quorum", publicDNS);
            conf.setInt("hbase.zookeeper.property.maxClientCnxns", 100);
            connection = HConnectionManager.createConnection(conf);
            System.out.println("try connecting");
            keys = new int[]{1,2,3,4};
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
        table = connection.getTable(tableName);
        Long m = Long.parseLong(request.getParameter("m"));
        Long n = Long.parseLong(request.getParameter("n"));

        ArrayList<Row> batchJob = new ArrayList<>();
        batchJob.add(new Get(Bytes.toBytes(m)).addFamily("cf".getBytes()));
        batchJob.add(new Get(Bytes.toBytes(n)).addFamily("cf".getBytes()));
        byte[] mValue = new byte[16];
        byte[] nValue = new byte[16];

        try {
            Object[] result = table.batch(batchJob);
            mValue = ((Result) result[0]).value();
            //System.out.println(mValue.length);
            nValue = ((Result) result[1]).value();
        } catch (InterruptedException ex) {
        }

        sb.append(TEAM).append(m).append(T).
                append(n).append(T).
                append("WINNER").append(N);

        for(int i = 0; i <= 3; i++){
            int a = byteToInt(mValue, i);
            int b = byteToInt(nValue, i);
            sb.append(a).append(T).
            append(b).append(T);
            int winner = a - b;
            if(winner < 0) sb.append(n);
            else if(winner > 0) sb.append(m);
            else if(winner == 0) sb.append(X);
            sb.append(N);
        }

        ServletOutputStream out = response.getOutputStream();
        out.write(sb.toString().getBytes());
    }
    
    private int byteToInt(byte[] b, int column) {
        return ((b[4 * column] & 0xff) << 24) |
                 ((b[4 * column+1] & 0xff) << 16) |
                 ((b[4 * column+2] & 0xff) << 8) |
                 (b[4 * column+3] & 0xff);
    }
}
