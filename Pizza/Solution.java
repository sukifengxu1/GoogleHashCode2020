import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

class Solution {
	public static int morePizza(int M, int[] items, String name) throws FileNotFoundException {
		int N = items.length;
		
		int[][] maxPizza = new int[N][M + 1];
		int[][] track = new int[N][M + 1];
		for (int m = 1; m <= M; m++) {
			if (items[0] <= m) {
				maxPizza[0][m] = items[0];
			} else {
				track[0][m] = -1;
			}
		}
		for (int n = 1; n < N; n++) {
			for (int m = 1; m <= M; m++) {
				if (items[n] > m) {
					track[n][m] = track[n - 1][m];
					maxPizza[n][m] = maxPizza[n - 1][m];
					continue;
				}
				if (maxPizza[n - 1][m - items[n]] + items[n] > maxPizza[n - 1][m]) {
					maxPizza[n][m] = maxPizza[n - 1][m - items[n]] + items[n];
					track[n][m] = n;
				} else {
					track[n][m] = track[n - 1][m];
					maxPizza[n][m] = maxPizza[n - 1][m];
				}
			}
		}
		int total = maxPizza[N - 1][M];
		List<Integer> ans = new ArrayList<>();
		int currIndex = track[N - 1][M];
		ans.add(currIndex);
		total -= items[currIndex];
		
		while (total > 0) {
			currIndex = track[currIndex][total];
			ans.add(currIndex);
			total -= items[currIndex];
		}
		
		PrintWriter pw = new PrintWriter(name);
		pw.println(ans.size());
		for (int i = ans.size() - 1; i >= 0; i--) pw.print(ans.get(i) + " ");
		pw.close();
		
		return maxPizza[N - 1][M];
    }

    public static void main(String[] args) throws IOException {
    	String filename = "d_quite_big.in";
    	InputStream stream = new FileInputStream(filename);
    	StringBuffer buffer = new StringBuffer();
    	int ch;
    	while ((ch = stream.read()) > 0) {
    		buffer.append((char) ch);
    	}
    	stream.close();
    	String input = buffer.toString();
    	String[] lines = input.split("\n");
    	String[] tokens = lines[0].split(" ");
    	int M = Integer.parseInt(tokens[0]); // capacity
    	int N = Integer.parseInt(tokens[1]); // # of items
    	tokens = lines[1].split(" ");
    	int[] items = new int[N];
    	for (int i = 0; i < N; i += 2) {
    		items[i] = Integer.parseInt(tokens[i]);
    	}
    	
    	String name = filename.substring(0, filename.length() - 2) + "txt";
    	int ans = morePizza(M, items, name);
    	// Output via console
    	System.out.println(ans);
    }
}