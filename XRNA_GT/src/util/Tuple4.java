package util;

public class Tuple4<T0, T1, T2, T3> {
	public T0
		t0;
	public T1
		t1;
	public T2
		t2;
	public T3
		t3;

	public Tuple4(T0 t0, T1 t1, T2 t2, T3 t3) {
		this.t0 = t0;
		this.t1 = t1;
		this.t2 = t2;
		this.t3 = t3;
	}
	
	@Override
	public String toString() {
		return "(" + this.t0 + ", " + this.t1 + ", " + this.t2 + ", " + this.t3 + ")";
	}
}