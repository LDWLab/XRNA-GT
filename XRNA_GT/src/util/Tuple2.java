package util;

public class Tuple2<T0, T1> {
	public T0
		t0;
	public T1
		t1;

	public Tuple2(T0 t0, T1 t1) {
		this.t0 = t0;
		this.t1 = t1;
	}
	
	@Override
	public String toString() {
		return "(" + this.t0 + ", " + this.t1 + ")";
	}
}