import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

public class CardMapper extends MapReduceBase implements  Mapper<LongWritable, Text, Text, IntWritable> {


	@Override
	public void map(LongWritable key, Text value,
			OutputCollector<Text, IntWritable> arg2, Reporter arg3)
			throws IOException {
		// TODO Auto-generated method stub
		String inputLine = value.toString();
		String [] inparr = inputLine.split(" ");
		for(int i = 0;i<inparr.length;i++) {
			//System.out.println("~~~~~~~~~~~~~~~~~~~~~~~"+inparr[i]);
			int temp = Integer.parseInt(inparr[i]);
			String mykey = "x"+i;
			String mysquare = mykey+"square";
			arg2.collect(new Text(mykey), new IntWritable(temp));
			
			arg2.collect(new Text(mysquare), new IntWritable(temp*temp));
			if(i < inparr.length-1) {
			for(int j=i+1;j<inparr.length;j++) {
			int temp1 = Integer.parseInt(inparr[j]);
			String xy = "x"+i+"x"+j;
			int valxy = temp*temp1;
			arg2.collect(new Text(xy), new IntWritable(valxy));
			}}
		}
		arg2.collect(new Text("count"), new IntWritable(1));
//		Matcher inputMatch = inputPattern.matcher(inputLine);

		// Use regex to throw out Jacks, Queens, Kings, Aces and Jokers
		//if (inputMatch.matches()) {
			// Normalize inconsistent case for card suits
			//String cardSuit = inputMatch.group(1).toLowerCase();
			//int cardValue = Integer.parseInt(inputMatch.group(2));

			
		//}
		
	}
}