
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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class Q6ETL {
	static Configuration conf;
	static HConnection connection;
	static String publicDNS = "ec2-54-172-37-151.compute-1.amazonaws.com";
	static String tableName = "q6";
	static HTable table;
	static HBaseAdmin admin;
	static int count;
	public static void main(String[] args) {
//		 System.setIn(new FileInputStream("sample.txt"));
		prepareConnection();
			long key;
			int value;
			 try {
				System.setIn(new java.io.FileInputStream(args[0]));
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				
				String input;
				System.out.println("start import");
				while ((input = br.readLine()) != null) {
					int split = input.indexOf('\t');
					key = Long.MAX_VALUE-Long.parseLong(input.substring(0,split));
					value = Integer.parseInt(input.substring(split+1,input.length()));
					putToHbase(key, value);
				}
				System.out.println("out loop");
				 table.flushCommits(); // co PutWriteBufferExample-7-Flush Force a flush, this causes an RPC to occur.
//				 Result res2 = table.get(get);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
				
			
	}

	
	public static void prepareConnection(){

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
	    	    //  PutWriteBufferExample
	    		HTableDescriptor desc = new HTableDescriptor( // co CreateTableExample-2-CreateHTD Create the table descriptor instance.
	            Bytes.toBytes(tableName));
	    		HColumnDescriptor coldef = new HColumnDescriptor( // co CreateTableExample-3-CreateHCD Create a column family descriptor and add it to the table descriptor.
        		Bytes.toBytes("cf"));
        	    desc.addFamily(coldef);
        	    admin.createTable(desc); 
	    	    table = new HTable(conf, tableName);
	    	    System.out.println("Auto flush: " + table.isAutoFlush());  // co PutWriteBufferExample-1-CheckFlush Check what the auto flush flag is set to, should print "Auto flush: true".
	    	    table.setAutoFlush(false); // co PutWriteBufferExample-2-SetFlush Set the auto flush to false to enable the client-side write buffer.
	    	    System.out.println("connection complete");
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	}
	
	
	public static void putToHbase(long key, int value) throws IOException{
		count++;
	    Put put = new Put(Bytes.toBytes(key));
	    put.add(Bytes.toBytes("cf"), Bytes.toBytes("n"),
	    Bytes.toBytes(value));
	    table.put(put); // co PutWriteBufferExample-3-DoPut Store some rows with columns into HBase.
	    if(count%10000==0){
		    System.out.println("put"+count);
	    }
	}

}


