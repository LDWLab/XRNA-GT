package util;

public class Tuple3<T0, T1, T2> {
	public T0
		t0;
	public T1
		t1;
	public T2
		t2;

	public Tuple3(T0 t0, T1 t1, T2 t2) {
		this.t0 = t0;
		this.t1 = t1;
		this.t2 = t2;
	}
	
	@Override
	public String toString() {
		return "(" + this.t0 + ", " + this.t1 + ", " + this.t2 + ")";
	}
}