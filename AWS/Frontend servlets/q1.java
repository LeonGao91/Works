



import java.io.IOException;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

/**
 * 
 * @author leon
 */
@WebServlet("/q1")
public class q1 extends HttpServlet {
    static BigInteger X;
    static char[] TEAM;
    byte[] content;
    HashMap<String, String> IntegerPair;
    BigInteger resultInteger;

    @Override
    public void init() {
        X = DatatypeConverter.parseInteger("6876766832351765396496377534476050002970857483815262918450355869850085167053394672634315391224052153");        
        TEAM = "\nDEADLINE,276906431060,152339165514,931814217121\n".toCharArray();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     * response)
     */
    @Override
    protected void doGet(HttpServletRequest request,
        HttpServletResponse response) throws ServletException, IOException {
        SimpleDateFormat sdf;
        Date d;
        d = new Date();
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        BigInteger k = new BigInteger(request.getParameter("key"));
        resultInteger = k.divide(X);
        StringBuilder sb = new StringBuilder(resultInteger.toString());
        sb.append(TEAM).append(sdf.format(d));
        content = sb.toString().getBytes();
        ServletOutputStream out = response.getOutputStream();
        out.write(content);
    }
}
