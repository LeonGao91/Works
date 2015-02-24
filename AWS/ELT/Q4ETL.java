import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HConnectionManager;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;

public class Q4ETL {
	static Configuration conf;
	static HConnection connection;
	static String publicDNS = "ec2-54-172-37-151.compute-1.amazonaws.com";
	static String tableName = "q4";
	static HTable table;
	static HBaseAdmin admin;
	static int count;

	public static void main(String[] args) {
		// System.setIn(new FileInputStream("sample.txt"));
		prepareConnection();
		try {
			System.setIn(new java.io.FileInputStream(args[0]));
			// System.setIn(new
			// java.io.FileInputStream("/Users/youming/cc/sampledata/part-00000"));

			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("start import" + tableName);
			String input;
			while ((input = br.readLine()) != null) {
				StringTokenizer tokenizer = new StringTokenizer(input, "\t");
				String locationDate = tokenizer.nextToken();
				int locationIn = locationDate.indexOf("+");
				String location = locationDate.substring(0, locationIn);
				int date = Integer.parseInt(locationDate.substring(locationIn + 1, locationDate.length()).replaceAll("-", ""));
				int rank = Integer.parseInt(tokenizer.nextToken());
				String value = tokenizer.nextToken();

				
				// System.out.println(location+" : "+date+" : "+rank+" : "+value);
				putToHbase(location, date, rank, value);
			}
			System.out.println("out loop");
			table.flushCommits(); // co PutWriteBufferExample-7-Flush Force a
									// flush, this causes an RPC to occur.
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void putToHbase(String location, int date, int rank, String value) throws IOException {
		count++;
		Put put = new Put(Bytes.add(Bytes.toBytes(location.hashCode()), Bytes.toBytes(date), Bytes.toBytes(rank)));
		put.add(Bytes.toBytes("cf"), Bytes.toBytes("n"),
				Bytes.toBytes(value));
		table.put(put); // co PutWriteBufferExample-3-DoPut Store some rows with
						// columns into HBase.
		if (count % 10000 == 0) {
			System.out.println("put" + count);
		}
	}

	public static void prepareConnection() {
		System.out.println("create connection");
		try {
			conf = HBaseConfiguration.create();
			conf = new Configuration();
			conf.set("hbase.master", publicDNS + ":60000");
			conf.set("hbase.zookeeper.quorum", publicDNS);
			conf.setInt("hbase.zookeeper.property.maxClientCnxns", 100);
			connection = HConnectionManager.createConnection(conf);
			admin = new HBaseAdmin(conf);
			
			try {
				admin.disableTable(tableName);
				admin.deleteTable(tableName);
			} catch (Exception e) {
				e.printStackTrace();
			}

			HTableDescriptor desc = new HTableDescriptor( 
					Bytes.toBytes(tableName));
			HColumnDescriptor coldef = new HColumnDescriptor( 
					Bytes.toBytes("cf"));
			desc.addFamily(coldef);
			admin.createTable(desc);
			table = new HTable(conf, tableName);
			System.out.println("Auto flush: " + table.isAutoFlush()); 
			table.setAutoFlush(false); // co PutWriteBufferExample-2-SetFlush
										// Set the auto flush to false to enable
										// the client-side write buffer.
			System.out.println("connection complete");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
