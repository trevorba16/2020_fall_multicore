package fft;

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class CalcThread implements Runnable {
	
	Complex[] buffer;
	int[] evenIndexes; 
	int[] oddIndexes;
	int[] kValues;
	int N;
	
	CalcThread(Complex[] buffer, int[] evenIndexes, int[] oddIndexes, int[] kValues, int N) {
		this.buffer = buffer;
		this.evenIndexes = evenIndexes;
		this.oddIndexes = oddIndexes;
		this.kValues = kValues;
		this.N = N;
	}
	
	public void run() {
		for (int i = 0; i < evenIndexes.length; i++) {
			int evenIndex = evenIndexes[i];
			int oddIndex = oddIndexes[i];
			int k = kValues[i];
			Complex even = buffer[evenIndex];
            Complex odd = buffer[oddIndex];

            double term = (-2 * PI * k) / (double) N;
            Complex exp = (new Complex(cos(term), sin(term)).times(odd));

            buffer[evenIndex] = even.plus(exp);
            buffer[oddIndex] = even.minus(exp);
		}
	                
	}

}
