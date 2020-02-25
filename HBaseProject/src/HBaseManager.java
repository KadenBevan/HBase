import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.conf.Configuration; 
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable; 
import org.apache.hadoop.hbase.client.Put; 
import org.apache.hadoop.hbase.client.Result; 
import org.apache.hadoop.hbase.client.ResultScanner; 
import org.apache.hadoop.hbase.client.Scan; 
import org.apache.hadoop.hbase.util.Bytes; 

public class HBaseManager {
	
	public HBaseManager() throws MasterNotRunningException, ZooKeeperConnectionException, IOException
	{
	    Configuration config = HBaseConfiguration.create();
	    HBaseAdmin admin = new HBaseAdmin(config);

	    HTableDescriptor table = new HTableDescriptor(Bytes.toBytes("messages"));
	    HColumnDescriptor messageData = new HColumnDescriptor(Bytes.toBytes("message"));
	    table.addFamily(messageData);
	    
	    if (!admin.tableExists("messages")){
	    	admin.createTable(table);
	    }
	}
	
	public Boolean addMessage(String from, String to, String title, String body){
		try{
			Configuration config = HBaseConfiguration.create();
			
		    HBaseAdmin admin = new HBaseAdmin(config);	
		    
		    /*
		     * 'messages' - 'message:from' - 'message:to' - 'message:title' - 'message:body' - 'message:time'
		     */
		    
		    // Instantiating HTable class
		    HTable hTable = new HTable(config, "messages");
		    long timestamp = new Timestamp(System.currentTimeMillis()).getTime();
			Put p = new Put(Bytes.toBytes(new String(from+to+String.valueOf(timestamp))));
			p.add(Bytes.toBytes("message"), Bytes.toBytes("from"), Bytes.toBytes(from));
			p.add(Bytes.toBytes("message"), Bytes.toBytes("to"), Bytes.toBytes(to));
			p.add(Bytes.toBytes("message"), Bytes.toBytes("title"), Bytes.toBytes(title));
			p.add(Bytes.toBytes("message"), Bytes.toBytes("body"), Bytes.toBytes(body));
			p.add(Bytes.toBytes("message"), Bytes.toBytes("time"), Bytes.toBytes(String.valueOf(timestamp)));
			  
			// Saving the put Instance to the HTable.
			hTable.put(p);
		    
			System.out.println("Successfully added message to HBase");
			return true;
			
		} catch (IOException e){
			System.out.println("Failed to add message to HBase\n"+e.toString());
			return false;
		}
	}
	
	public ArrayList<Map<String, String>> getMessages(String from, String to) throws IOException{
		
		ArrayList<Map<String, String>> messages = new ArrayList();
		
		long timestamp = new Timestamp(System.currentTimeMillis()).getTime();
		
		long millsInYear = 31536000000L;		
		Scan s = new Scan(Bytes.toBytes(from+to+(timestamp-millsInYear)), Bytes.toBytes(from+to+timestamp));
		ResultScanner scanner = null;
		
	    try {
	    	Configuration config = HBaseConfiguration.create();
			HTable table = new HTable(config, "messages");
		    s.addColumn(Bytes.toBytes("message"), Bytes.toBytes("from"));
		    s.addColumn(Bytes.toBytes("message"), Bytes.toBytes("to"));
		    s.addColumn(Bytes.toBytes("message"), Bytes.toBytes("body"));
		    s.addColumn(Bytes.toBytes("message"), Bytes.toBytes("title"));
		    s.addColumn(Bytes.toBytes("message"), Bytes.toBytes("time"));
		    scanner = table.getScanner(s);
		    for (Result rr : scanner){
		    	Map<String, String> message = new HashMap();
			    byte [] fromBytes = rr.getValue(Bytes.toBytes("message"), Bytes.toBytes("from"));
			    byte [] toBytes = rr.getValue(Bytes.toBytes("message"), Bytes.toBytes("to"));
			    byte [] bodyBytes = rr.getValue(Bytes.toBytes("message"), Bytes.toBytes("body"));
			    byte [] titleBytes = rr.getValue(Bytes.toBytes("message"), Bytes.toBytes("title"));
			    byte [] timeBytes = rr.getValue(Bytes.toBytes("message"), Bytes.toBytes("time"));
			    
			    // We convert to Strings but we can use the from and to params
			    String body = Bytes.toString(bodyBytes);
			    String title = Bytes.toString(titleBytes);
			    String time = Bytes.toString(timeBytes);
			    String fromStr = Bytes.toString(fromBytes);
			    String toStr = Bytes.toString(toBytes);
			    
			    message.put("from", fromStr);
			    message.put("to", toStr);			    
			    message.put("body", body);
			    message.put("title", title);
			    message.put("time", time);
			    messages.add(message);
		    }	
	    } 
	    finally {
	      scanner.close();
	    }
	    Scan s1 = new Scan(Bytes.toBytes(to+from+(timestamp-millsInYear)), Bytes.toBytes(to+from+timestamp));
		ResultScanner scanner1 = null;
		
	    try {
	    	Configuration config = HBaseConfiguration.create();
			HTable table = new HTable(config, "messages");
		    s1.addColumn(Bytes.toBytes("message"), Bytes.toBytes("from"));
		    s1.addColumn(Bytes.toBytes("message"), Bytes.toBytes("to"));
		    s1.addColumn(Bytes.toBytes("message"), Bytes.toBytes("body"));
		    s1.addColumn(Bytes.toBytes("message"), Bytes.toBytes("title"));
		    s1.addColumn(Bytes.toBytes("message"), Bytes.toBytes("time"));
		    scanner1 = table.getScanner(s1);
		    for (Result rr : scanner1){
		    	Map<String, String> message = new HashMap();
			    byte [] fromBytes = rr.getValue(Bytes.toBytes("message"), Bytes.toBytes("from"));
			    byte [] toBytes = rr.getValue(Bytes.toBytes("message"), Bytes.toBytes("to"));
			    byte [] bodyBytes = rr.getValue(Bytes.toBytes("message"), Bytes.toBytes("body"));
			    byte [] titleBytes = rr.getValue(Bytes.toBytes("message"), Bytes.toBytes("title"));
			    byte [] timeBytes = rr.getValue(Bytes.toBytes("message"), Bytes.toBytes("time"));
			    
			    // We convert to Strings but we can use the from and to params
			    String body = Bytes.toString(bodyBytes);
			    String title = Bytes.toString(titleBytes);
			    String time = Bytes.toString(timeBytes);
			    String fromStr = Bytes.toString(fromBytes);
			    String toStr = Bytes.toString(toBytes);
			    
			    message.put("from", fromStr);
			    message.put("to", toStr);
			    message.put("body", body);
			    message.put("title", title);
			    message.put("time", time);
			    messages.add(message);
		    }	
	    } 
	    finally {
	      scanner.close();
	    }
		return messages;
	}
}
