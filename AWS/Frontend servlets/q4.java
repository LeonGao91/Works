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
import org.apache.commons.lang3.StringEscapeUtils;
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
@WebServlet(urlPatterns = {"/q4"})
public class q4 extends HttpServlet {
    private HConnection connection;
    private Configuration conf;
    private static final String publicDNS = "ec2-54-88-228-11.compute-1.amazonaws.com";
    private static String TEAM;
    private static String tableName;
    @Override
    public void init() {
        try {
            TEAM = "DEADLINE,276906431060,152339165514,931814217121\n";
            conf = new Configuration();
            conf.set("hbase.master", publicDNS + ":60000");
            conf.set("hbase.zookeeper.quorum", publicDNS);
            conf.setInt("hbase.zookeeper.property.maxClientCnxns", 100);
            connection = HConnectionManager.createConnection(conf);
            tableName = "q4";
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
        int m = Integer.parseInt(request.getParameter("m"));
        int n = Integer.parseInt(request.getParameter("n"));

        
        byte[] location = Bytes.toBytes(request.getParameter("location").hashCode());
        byte[] date = Bytes.toBytes(Integer.parseInt(request.getParameter("date").replace("-", "")));
        try {
            ArrayList<Row> batchJob = new ArrayList<>();
            for(int i = m; i <= n; i++) {
                batchJob.add(new Get(Bytes.add(location, 
                    date, 
                    Bytes.toBytes(i))));
            }
            Object[] result = table.batch(batchJob);
            for(Object r : result) {
                if(r instanceof Result){
                    if(((Result)r).value() != null){
                        //System.out.println(new String(((Result)r).value()));
                        sb.append(StringEscapeUtils.unescapeJson(new String(((Result)r).value()))).append("\n");
                    }
                }
            }
        } catch (InterruptedException ex) {
        }
        
        ServletOutputStream out = response.getOutputStream();
        out.write(sb.toString().getBytes());
    }
}
