import java.util.Comparator;


public class MyD implements Comparator<MyD>{
	Double doub;
	String pair;
	public MyD(Double d,String s) {
		doub = d;
		pair = s;
	}
	public int compare(MyD d1,MyD d2) {
		if(d1 instanceof MyD && d2 instanceof MyD) {
			if(d1.doub > d2.doub) {
				return 1;
			}
			else if(d1.doub == d2.doub) {
				return 0;
			}
			else if(d1.doub < d2.doub) {
				return -1;
			}
		}
		return 999;
	}
}
