/**
 * Copyright 2013 Jesse Anderson
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;

public class CardDriver {
	static Map<String,Integer> m;
	static ArrayList<MyD> top10;
	static int rows=4;
	static int cols=4;
	static double matrix [][];
/*	public int run(String[] args) throws Exception {
		String input, output;
		if (args.length > 2) {
			input = args[0];
			output = args[1];
		} else {
			System.err.println("Incorrect number of arguments.  Expected: input output");
			return -1;
		}

		Job job = new Job(getConf());
		job.setJarByClass(CardDriver.class);
		job.setJobName(this.getClass().getName());

		FileInputFormat.setInputPaths(job, new Path(input));
		FileOutputFormat.setOutputPath(job, new Path(output));

		job.setMapperClass(CardMapper.class);
		job.setReducerClass(CardTotalReducer.class);

		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(IntWritable.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);

		boolean success = job.waitForCompletion(true);
		return success ? 0 : 1;
	}
*/
	public static void main(String[] args) throws Exception {
		String str=null;
		Long bt=0l,at=0l,bef=0l;
		String input, output;
		if (args.length > 2) {
			input = args[0];
			output = args[1];
		} else {
			System.err.println("Incorrect number of arguments.  Expected: input output");
			return;
		}
		JobClient client = new JobClient();
		JobConf conf = new JobConf(CardDriver.class);
	    conf.setJobName("corr");

	    // the keys are words (strings)
	    conf.setOutputKeyClass(Text.class);
	    // the values are counts (ints)
	    conf.setOutputValueClass(IntWritable.class);

	    conf.setMapperClass(CardMapper.class);
	    conf.setReducerClass(CardTotalReducer.class);

	    FileInputFormat.setInputPaths(conf, new Path(input));
	    FileOutputFormat.setOutputPath(conf, new Path(output));
	    client.setConf(conf);
		FileSystem fs = FileSystem.get(conf);
		m = new HashMap<String,Integer>();
		top10 = new ArrayList<MyD>();
		rows = Integer.parseInt(args[2]);
		cols = Integer.parseInt(args[3]);
		matrix = new double[cols][cols];
		bt = System.currentTimeMillis();
		bef = System.currentTimeMillis();
		new generateMatrix(input,rows, cols);
		at = System.currentTimeMillis();
		System.out.println("Generation Time:"+(at-bt));
		fs.copyFromLocalFile(new Path(input), new Path(input));
	    bt = System.currentTimeMillis();
		try {
	        JobClient.runJob(conf);
	      } catch (Exception e) {
	        e.printStackTrace();
	      }
		System.out.println("Hadoop Time:"+(System.currentTimeMillis() - bt));
	    fs.copyToLocalFile(new Path(output), new Path(output));
		File f = new File(output+"/part-00000");
		BufferedReader br = new BufferedReader(new FileReader(f));
		while((str=br.readLine())!=null) {
			StringTokenizer s = new StringTokenizer(str);
			m.put(s.nextToken(), Integer.parseInt(s.nextToken()));
		}
		bt = System.currentTimeMillis();
		try {
		buildMatrix(args);
		}
		catch(Exception e) {
			System.out.println("Exception Thrown");
		}
		System.out.println("Matrix Time:"+(System.currentTimeMillis() - bt));
		System.out.println("OverAll Time:"+(System.currentTimeMillis() - bef));
	}

	 static void buildMatrix(String [] o) throws FileNotFoundException {
		 File f = new File(o[1]+"corrMatrix.txt");
		 PrintWriter pw = new PrintWriter(f);
		//System.out.println("size"+m.size());
		 
		 for(int i = 0; i<cols;i++) {
			 String result = "";	 
			 for(int j=i;j<cols;j++) {
				 if(i == j) {
					// System.out.println("into if");
					 matrix[i][j] = 1.0;
					 
				 }
				 else {
					// System.out.println("into else");
					 String xkey = "x"+i;
					 String ykey = "x"+j;
					 String xsquare = xkey+"square";
					 String xy = "x"+i+"x"+j;
					 String ysquare = ykey+"square";
					 int count = Integer.parseInt(o[2]);//m.get("count");
					 int xi = m.get(xkey);
					 int yi = m.get(ykey);
					 int xiyi = m.get(xy);
					 int xisquare = m.get(xsquare);
					 int yisquare = m.get(ysquare);
					 double xmean = (double) xi/count;
					 double ymean = (double) yi/count;
					 // numerator = (double)valueXY - (valueC*xmean*ymean);
					 // double denomone = (double)(xisquare - (count*(xmean*xmean)));
					 // double denomtwo = (double)(yisquare - (valueC*(ymean*ymean)));
					  //denominator = (double)sqrt(denomone*denomtwo);

					 double numerator = (double)(xiyi - (double)((xmean*ymean)*rows));					 
					 //System.out.println("numerator = "+numerator+" xmean = "+xmean+"ymean = "+ymean);
					 double denom = (double)Math.sqrt(((double)(xisquare - rows*(xmean*xmean)))*((double)(yisquare - rows*(ymean*ymean))));
					 //System.out.println("denom"+denom);
					 //System.out.println("corr"+numerator/denom);
					 double corr = numerator/denom;
					 matrix[i][j] = corr;
					 matrix[j][i] = corr;
				 }
	//			 result+=matrix[i][j]+" ";
			 }
			 
	//		 pw.println(result);
	//		 pw.flush();
		 }
		 for(int i = 0; i<cols;i++) {
			 StringBuffer result = new StringBuffer();
			 StringBuffer sp = new StringBuffer("  ");
			 for(int j=0;j<cols;j++) {
				 result.append(matrix[i][j]);
				 if(j>i )
				 { 
					 MyD p = new MyD(matrix[i][j],""+i+""+j);
					 top10.add(p);
				 }
				 result.append(sp);
			 }	 
			 	pw.println(result);
			 	pw.flush();
 }

		Collections.sort(top10,new MyD(-1.0,"d"));
		 for(int i = 0; i<(top10.size() > 10?10:top10.size());i++) {
			 System.out.println(top10.get(top10.size()-1-i).doub+" "+(top10.get(top10.size()-1-i).pair));
 }		 
		 
		 pw.close();		
	}
}