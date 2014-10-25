import java.io.*;
import java.io.File;
import java.io.IOException;

public class generateMatrix {
	 int rows=0;
	 int cols=0;
	 String i;
	 int matrix[][];
	public generateMatrix(String inp,int r,int c) {
		rows = r;
		cols = c;
		i = inp;
		matrix = new int[rows][cols];
		createMatrix(rows,cols);
	}

	 void createMatrix(int rows2, int cols2) {
		int val=0;
		String s = "";
		PrintWriter pf = null;
		File f = new File(i);
		try {
			pf = new PrintWriter(f);
			for(int i=0;i<rows2;i++) {
				s = "";
				for(int j=0;j<cols2;j++) {
					double rand = java.lang.Math.random();
					val = (int)(1+(rand*10));
					s += val+" ";
					//s += ":"+i+","+j+","+val;
				}
				pf.println(s);
				pf.flush();
				
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			pf.close();
		}
				
	}

}
