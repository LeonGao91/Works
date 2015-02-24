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

public class Q5ETL {
	static Configuration conf;
	static HConnection connection;
	static String publicDNS = "ec2-54-88-228-11.compute-1.amazonaws.com";
	static String tableName = "q5-2";
	static HTable table;
	static HBaseAdmin admin;
	static int count;

	public static void main(String[] args) {
		// System.setIn(new FileInputStream("sample.txt"));
		prepareConnection();
		try {
			System.setIn(new java.io.FileInputStream(args[0]));
//			 System.setIn(new java.io.FileInputStream("/Users/youming/cc/sampledata/part-00000"));
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("start import " + tableName);
			String input;
			while ((input = br.readLine()) != null) {
				StringTokenizer tokenizer = new StringTokenizer(input, "\t");
				long userid = Long.parseLong(tokenizer.nextToken());
				int s1 = Integer.parseInt(tokenizer.nextToken());
				int s2 = Integer.parseInt(tokenizer.nextToken());
				int s3 = Integer.parseInt(tokenizer.nextToken());
				int s4 = Integer.parseInt(tokenizer.nextToken());
				// //to timestamp
//				 System.out.println(userid+" : "+s1+" : "+s2+" : "+s3+" : "+s4);
				putToHbase(userid, s1, s2, s3, s4);
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

	public static void putToHbase(long userid, int s1, int s2, int s3, int s4) throws IOException {
		count++;
		Put put = new Put(Bytes.toBytes(userid));
		byte[] s123 = Bytes.add(Bytes.toBytes(s1), Bytes.toBytes(s2), Bytes.toBytes(s3));
		byte[] s1234 = Bytes.add(s123,Bytes.toBytes(s4));
//		System.out.println(s1234.length);
		put.add(Bytes.toBytes("cf"), Bytes.toBytes(1),
				s1234);
//		put.add(Bytes.toBytes("cf"), Bytes.toBytes(2),
//				Bytes.toBytes(s2));
//		put.add(Bytes.toBytes("cf"), Bytes.toBytes(3),
//				Bytes.toBytes(s3));
//		put.add(Bytes.toBytes("cf"), Bytes.toBytes(4),
//				Bytes.toBytes(s4));
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
