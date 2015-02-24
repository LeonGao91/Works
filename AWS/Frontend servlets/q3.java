



import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HConnectionManager;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Result;


/**
 * 
 * @author leon
 */
@WebServlet("/q3")
public class q3 extends HttpServlet {

    private HConnection connection;
    private Configuration conf;
    private final String publicDNS = "ec2-54-88-228-11.compute-1.amazonaws.com";
    private static String TEAM;
    private final static String tableName = "retweets";
    private final static String USER = "userid";
    @Override
    public void init() {
        try {
            TEAM = "DEADLINE,276906431060,152339165514,931814217121\n";
            conf = new Configuration();
            conf.set("hbase.master", publicDNS + ":60000");
            conf.set("hbase.zookeeper.quorum", publicDNS);
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
        
        Get g = new Get(request.getParameter(USER).getBytes());
        
        Result result = table.get(g);

        for(KeyValue kv : result.list()){
            sb.append(StringEscapeUtils.unescapeJson(new String(kv.getValue()))).append("\n");
        }
        ServletOutputStream out = response.getOutputStream();
        out.write(sb.toString().getBytes());

    }
}
