package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class IOProcessor {

	public static void writeStringToFile(String str, String filenameInclPath) {

		File file = new File(filenameInclPath);
		String content = str;

		try (FileOutputStream fop = new FileOutputStream(file)) {

			// if file doesn't exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			// get the content in bytes
			byte[] contentInBytes = content.getBytes();

			fop.write(contentInBytes);
			fop.flush();
			fop.close();

			System.out.println("Created file: " + filenameInclPath);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param filenameInclPath
	 * @return
	 */
	public static String readFileToString(String filenameInclPath) {
		String str = null;
		BufferedReader br = null;

		try {

			String sCurrentLine;

			br = new BufferedReader(new FileReader(filenameInclPath));
			str = "";
			while ((sCurrentLine = br.readLine()) != null) {
				// System.out.println(sCurrentLine);
				str += sCurrentLine + "\n";
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return str;

	}
}
