package fft;

import java.util.concurrent.Callable;

public class CalcThreadRecursive implements Callable<Complex[]> {

	Complex[] buffer;
	public CalcThreadRecursive(Complex[] buffer) {
		this.buffer = buffer;
	}
	
	@Override
	public Complex[] call() throws Exception {
		return recursive_fft_segment(buffer);
	}
	
	public static Complex[] recursive_fft_segment(Complex[] x) {
        int n = x.length;

        if (n == 1) return new Complex[] { x[0] };
        // radix 2 Cooley-Tukey FFT
        if (n % 2 != 0) {
            throw new IllegalArgumentException("n is not a power of 2");
        }
        // compute FFT of even terms
        Complex[] even = new Complex[n/2];
        Complex[] odd = new Complex[n/2];
        for (int k = 0; k < n/2; k++) {
            even[k] = x[2*k];
            odd[k] = x[2*k + 1];
        }
        Complex[] evenFFT = recursive_fft_segment(even);
        Complex[] oddFFT = recursive_fft_segment(odd);
        // combine
        Complex[] y = new Complex[n];
        for (int k = 0; k < n/2; k++) {
            double kth = -2 * k * Math.PI / n;
            Complex wk = new Complex(Math.cos(kth), Math.sin(kth));
            y[k]       = evenFFT[k].plus (wk.times(oddFFT[k]));
            y[k + n/2] = evenFFT[k].minus(wk.times(oddFFT[k]));
        }
        return y;
    }


}
