import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

public class Solution {

	static class Lib {
		int id;
		int book; // has # books
		int sign; // signup process takes # days
		int ship; // can ship # books per day
		Set<Integer> bookIds = new HashSet<>();
		Lib(int id, int book, int sign, int ship) { this.id = id; this.book = book; this.sign = sign; this.ship = ship; }
	}

	public static int scan(Lib[] libs, int[] books, int day, String name) throws FileNotFoundException {

		//		System.out.println("There are " + libs.length + " libraries.");
		//		System.out.println("There are " + books.length + " books.");
		//		System.out.println("There are " + day + " days.");
		int result = 0;
		Map<Integer, List<Integer>> map = new HashMap<>();
		List<Integer> list = new ArrayList<>();
		PriorityQueue<Lib> heap = new PriorityQueue<>((a, b) -> a.sign - b.sign);
		for (Lib l : libs) heap.offer(l);
		boolean[] scanned = new boolean[books.length];
		while (day > 0 && !heap.isEmpty()) {
			Lib lib = heap.poll();
			map.put(lib.id, new ArrayList<>());
			list.add(lib.id);
			//			System.out.println("Taking in new a new lib: signup takes " + lib.sign);
			day -= lib.sign;
			PriorityQueue<int[]> bookHeap = new PriorityQueue<>((a, b) -> b[1] - a[1]);
			for (int id : lib.bookIds) bookHeap.offer(new int[] {id, books[id]});
			int perDay = lib.ship;
			int res = day;
			while (!bookHeap.isEmpty() && res > 0) {
				int i = 0;
				while (i < perDay && !bookHeap.isEmpty()) {
					int[] toAdd = bookHeap.poll();
					if (scanned[toAdd[0]]) continue;
					result += toAdd[1];
					scanned[toAdd[0]] = true;
					map.get(lib.id).add(toAdd[0]);
					//					System.out.println("Scanning book " + toAdd[0] + " scoring " + toAdd[1] + " at day (counting down) " + res);
					i++;
				}
				res--;
			}
		}
		PrintWriter pw = new PrintWriter(new File(name));
		if (map.get(list.get(list.size() - 1)).size() == 0) map.remove(list.get(list.size() - 1));
		pw.println(map.size());
		for (int i = 0; i < map.size(); i++) {
//			if (map.get(list.get(i)).size() == 0) continue;
			pw.print(list.get(i) + " ");
			pw.print(map.get(list.get(i)).size());
			
			pw.println();
			
			for (int k : map.get(list.get(i))) {

				pw.print(k + " ");
			}
			pw.println();
		}
		pw.close();

		return result;
	}

	public static void main(String[] args) throws IOException {
		String filename = "c_incunabula.txt";
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
		int numBooks = Integer.parseInt(tokens[0]); // # of books
		int numLibs = Integer.parseInt(tokens[1]); // # of libraries
		int numDays = Integer.parseInt(tokens[2]); // # of days
		tokens = lines[1].split(" ");
		int[] books = new int[numBooks]; // book scores, length numBooks, book ID [0, numBooks)
		for (int i = 0; i < numBooks; i++) {
			books[i] = Integer.parseInt(tokens[i]);
		}
		Lib[] libs = new Lib[numLibs];
		for (int i = 0; i < numLibs; i++) {
			tokens = lines[i * 2 + 2].split(" "); // 2, 4, ..., lib info
			libs[i] = new Lib(i, Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]));
			tokens = lines[i * 2 + 3].split(" ");
			for (int j = 0; j < libs[i].book; j++) {
				libs[i].bookIds.add(Integer.parseInt(tokens[j]));
			}
		}

		//    	for (int i = 0; i < numLibs; i++) {
		//    		System.out.println(libs[i].book);
		//    		System.out.println(libs[i].sign);
		//    		System.out.println(libs[i].ship);
		//    		System.out.println(libs[i].books.toString());
		//    	}

		String name = filename.substring(0, filename.length() - 4) + "_ans.txt";
		int ans = scan(libs, books, numDays, name);
		// Output via console
		    	System.out.println("answer: " + ans);
	}
}