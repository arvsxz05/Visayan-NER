import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class File {
	String FILENAME;
	
	public File() {}

	public File(String path) { FILENAME = path; }

	public String[] getContents() {
		ArrayList<String> list = new ArrayList<>();
		BufferedReader br = null;
		FileReader fr = null;

		try {

			fr = new FileReader(FILENAME);
			br = new BufferedReader(fr);

			String sCurrentLine;

			br = new BufferedReader(new FileReader(FILENAME));

			while ((sCurrentLine = br.readLine()) != null) {

				list.add(sCurrentLine);
			}

			//System.out.println(Arrays.toString(list.toArray(new String[0])));
			return list.toArray(new String[0]);
		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {

				if (br != null)
					br.close();

				if (fr != null)
					fr.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}

		}
		return null;
	}

	public static String[] getContentAsArray(String path) {
		ArrayList<String> list = new ArrayList<>();
		BufferedReader br = null;
		FileReader fr = null;

		try {

			fr = new FileReader(path);
			br = new BufferedReader(fr);

			String sCurrentLine;

			br = new BufferedReader(new FileReader(path));

			while ((sCurrentLine = br.readLine()) != null) {
				sCurrentLine = sCurrentLine.trim();
				list.add(sCurrentLine);
			}

			//System.out.println(Arrays.toString(list.toArray(new String[0])));
			return list.toArray(new String[0]);
		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {

				if (br != null)
					br.close();

				if (fr != null)
					fr.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}

		}
		return null;
	}

	public static void main(String[] args) {
		File.getContentAsArray("C:/Users/Kei/Documents/.UP Files/CMSC/176/176/name_kwords.txt");
	}
}