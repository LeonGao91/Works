
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HConnectionManager;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Result;

/**
 *
 * @author leon
 */
@WebServlet("/q2")
public class q2 extends HttpServlet {

    private HConnection connection;
    private Configuration conf;
    private final static String publicDNS = "ec2-54-88-228-11.compute-1.amazonaws.com";
    private static String TEAM;
    private static String tableName;
    private final static String FORMAT = "yyyy-MM-dd HH:mm:ss";
    private final static String TIME = "tweet_time";
    private final static String USER = "userid";
    @Override
    public void init() {
        try {
            TEAM = "DEADLINE,276906431060,152339165514,931814217121\n";
            tableName = "tweets";
            conf = new Configuration();
            conf.set("hbase.master", publicDNS + ":60000");
            conf.set("hbase.zookeeper.quorum", publicDNS);
            conf.setInt("hbase.zookeeper.property.maxClientCnxns", 100);
            connection = HConnectionManager.createConnection(conf);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        Long Time1 = System.currentTimeMillis();
        
        HTableInterface table;
        StringBuilder sb = new StringBuilder();
        sb.append(TEAM);
        table = connection.getTable(tableName);
        try {
            
            Date date = new SimpleDateFormat(FORMAT).parse(request.getParameter(TIME));
            long milliseconds = date.getTime()/1000;
            Get g = new Get((Long.toString(milliseconds) + request.getParameter(USER)).getBytes());
            //System.out.println("Sending query" + (Long.toString(milliseconds) + request.getParameter("userid")));
            Result result = table.get(g);
            for(KeyValue kv : result.list()){
                //System.out.println(new String(kv.getValue()));
                sb.append(StringEscapeUtils.unescapeJson(new String(kv.getValue()))).append("\n");
            }
            ServletOutputStream out = response.getOutputStream();
            out.write(sb.toString().getBytes());
        } catch (ParseException ex) {
        }
    }
}

