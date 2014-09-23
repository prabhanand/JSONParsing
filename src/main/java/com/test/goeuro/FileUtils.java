package com.test.goeuro;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 
 * File utility class
 *
 */
public class FileUtils {


	public static void saveFile(String absFileName, String data) throws IOException{

		File csvData = new File(absFileName);

		if(!csvData.exists()) {
			csvData.createNewFile();
		}

		FileOutputStream oFile =  null;
		try {
			oFile = new FileOutputStream(csvData, false);
		} catch (FileNotFoundException e) {
			error("Not able to create file "+e.getMessage());
			System.exit(1);
		}

		oFile.write(data.getBytes());
	}

	public static void error(String error){
		//TOOD: Use logger
		System.out.println("ERROR:" + error);
	}

}
