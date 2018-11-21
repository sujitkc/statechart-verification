package testcases;

import java.util.Scanner;

public class Timer {

	public static void timer() {								//	A

		int start = 0;											//	B
		int x5, prev_start, prev_out, out, t_on;				//	B
		int c = 10;												//	B
		prev_start = 0;											//	B
		prev_out = 0;											//	B
		out = 0;												//	B
		Scanner sc = new Scanner(System.in);					//	C
		while (true) {											//	C
			start = sc.nextInt();								//	C
			if (prev_start - start == -1) {						//	D
				x5 = 1;											//	E
			} else {//	Why 'else' has no node? because we can adjust in edges. Note edge 'DE' & 'DF'
				x5 = 0;											//	F
			}

			if (x5 == 1) {										//	G
				out = c;										//	H
			} else if (prev_out != 0) {							//	I
				out = prev_out - 1;								//	J
			} else {
				out = prev_out;									//	K
			}
			if (prev_out != out) {								//	L
				t_on = 1;										//	M
			} else {
				t_on = 0;										//	N
			}
			System.out.println(x5 + "  " + out + "  " + t_on);
			prev_start = start;									//	O
			prev_out = out;										//	O
		}														//	W
	}

	public static void main(String[] args) {

		timer();
	}

}
