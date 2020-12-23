package test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Test3 {
	public static void main(String[] args) {
		String fileName = ("C:\\Users\\caede\\Desktop\\XRNA\\lines.txt");
		try {
			String csvContents = "";
			BufferedReader reader = new BufferedReader(new FileReader(new File(fileName)));
			for (String line = reader.readLine(); line != null; line = reader.readLine()) {
				if (line.startsWith("l ") || line.startsWith("s ")) {
					String[] cells = line.split(" ");
					for (String cell : cells) {
						csvContents += cell + ", ";
					}
					csvContents += "\r\n";
				}
			}
			reader.close();
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File("C:\\Users\\caede\\Desktop\\lines.xlsx")));
			writer.write(csvContents);
			writer.close();
		} catch (IOException ex) {
			
		}
	}
}
