import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;

public class CardTotalReducer extends MapReduceBase implements Reducer<Text, IntWritable, Text, IntWritable> {


	@Override
	public void reduce(Text key, Iterator<IntWritable> values,
			OutputCollector<Text, IntWritable> arg2, Reporter arg3)
			throws IOException {
		int sum = 0;

		// Go through all values to sum up card values for a key
	
		while(values.hasNext()) {
			IntWritable i = values.next();
			sum += i.get();
		}

		arg2.collect(key, new IntWritable(sum));		
	}
}
