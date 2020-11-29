package fft;

import java.util.concurrent.Callable;

public class CombinationThread  implements Callable<Complex[]>{
	Complex[] a;
	Complex[] b;
	public CombinationThread(Complex[] a, Complex[] b) {
		this.a = a;
		this.b = b;
	}
	
	@Override
	public Complex[] call() throws Exception {
		return fft_combination(a, b);
	}
	
	public static Complex[] fft_combination(Complex[] a, Complex[] b) {
		int n = a.length * 2;
        Complex[] buffer = new Complex[n];
        
        for (int k = 0; k < n/2; k++) {
            double kth = -2 * k * Math.PI / n;
            Complex wk = new Complex(Math.cos(kth), Math.sin(kth));
            buffer[k]       = a[k].plus (wk.times(b[k]));
            buffer[k + (n/2)] = a[k].minus(wk.times(b[k]));
        }
        
        return buffer;
    }
}
