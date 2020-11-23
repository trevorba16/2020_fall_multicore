package fft;

import static java.lang.Math.*;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.awt.Color;

import javax.swing.JFrame;

import org.math.plot.Plot2DPanel;

public class ParallelFFT {

    // display an array of Complex numbers to standard output
    public static void show(Complex[] x, String title) {
        System.out.println(title);
        System.out.println("-------------------");
        for (int i = 0; i < x.length; i++) {
        	System.out.println(x[i]);
        }
        System.out.println();
    }
    
    public static Complex[] generateSignal(int N)
	{
			Complex[] signal = new Complex[N];
			for (int i = 0; i < N; i++)
			{
				signal[i] = new Complex(i, 0);
//				signal[i] = new Complex(Math.cos((2 * i * 1 * Math.PI) / N) + 0.5
//						* Math.cos(((2 * i * 2 * Math.PI) / N) + Math.PI / 4), 0);
			}
		return signal;
	}
    
    public static int bitReverse(int n, int bits) {
        int reversedN = n;
        int count = bits - 1;
 
        n >>= 1;
        while (n > 0) {
            reversedN = (reversedN << 1) | (n & 1);
            count--;
            n >>= 1;
        }
 
        return ((reversedN << count) & ((1 << bits) - 1));
    }
 
    static void iterative_fft(Complex[] buffer) {
 
        int bits = (int) (log(buffer.length) / log(2));
        for (int j = 1; j < buffer.length / 2; j++) {
 
            int swapPos = bitReverse(j, bits);
            Complex temp = buffer[j];
            buffer[j] = buffer[swapPos];
            buffer[swapPos] = temp;
        }
 
        System.out.println("N\t\ti\t\tk\t\teven\t\todd");
        for (int N = 2; N <= buffer.length; N <<= 1) {
            for (int i = 0; i < buffer.length; i += N) {
                for (int k = 0; k < N / 2; k++) {
 
                    int evenIndex = i + k;
                    int oddIndex = i + k + (N / 2);
                    System.out.println(N +"\t\t" + i + "\t\t" + k+ "\t\t" + evenIndex + "\t\t" + oddIndex);
                    Complex even = buffer[evenIndex];
                    Complex odd = buffer[oddIndex];
 
                    double term = (-2 * PI * k) / (double) N;
                    Complex exp = (new Complex(cos(term), sin(term)).times(odd));
 
                    buffer[evenIndex] = even.plus(exp);
                    buffer[oddIndex] = even.minus(exp);
                }
            }
            System.out.println("----------------------------------------------------------");
        }
    }
    
    static void fft_segment(Complex[] buffer, int numThreads) {
    	int itemCount = buffer.length / (numThreads * 2);
    	
    	System.out.println("itemCount: " + itemCount);
    	int run = 1;
    	int[] evenIndexes = new int[itemCount];
    	int[] oddIndexes = new int[itemCount];
    	int[] kValues = new int[itemCount];
    	
    	
    	
    	for (int N = 2; N <= buffer.length; N <<= 1) {
    		ExecutorService executor = Executors.newFixedThreadPool(4);
            for (int i = 0; i < buffer.length; i += N) {
                for (int k = 0; k < N / 2; k++) {
                    int evenIndex = i + k;
                    int oddIndex = i + k + (N / 2);
                    
                    evenIndexes[run - 1] = evenIndex;
                    oddIndexes[run - 1] = oddIndex;
                    kValues[run - 1] = k;
                    
                    if (run % itemCount == 0) {
                    	run = 0;
                    	CalcThread c = new CalcThread(buffer, evenIndexes, oddIndexes, kValues, N);
                    	executor.execute(c);
                    	evenIndexes = new int[itemCount];
                    	oddIndexes = new int[itemCount];
                    	kValues = new int[itemCount];
                    }
                    run++;
                }
            }
            executor.shutdown();
            while (!executor.isTerminated()) {   }  
    	}
    }
    
    public static void main(String[] args) { 
//    	CooleyTukeySeries();
    	Complex[] x = generateSignal(16);
//    	iterative_fft(x);
//    	show(x, "");
    	fft_segment(x,4);
    	show(x, "");
    }

}
