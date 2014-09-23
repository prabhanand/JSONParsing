package com.test.goeuro;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * 
 * Entry point to app
 *
 */
public class App {
	/*
	 Functionality
	 ------------------
	 The endpoint always responds with a JSON array that contains JSON objects as elements.
	 Each object, among other keys, has a name and a geo_position key. 
	 The geo_position key is an object with latitude and longitude fields. 
	 If no matches are found an empty JSON array is returned. 
	 The program should query the API with the user input and create a CSV file from it. 
	 The CSV file should have the form: _id, name, type, latitude,longitude
	 */

	public static void main(String[] args) {
		String cmdParameter = null;
		if(null != args && args.length > 0){
			cmdParameter = args[0];
		}

		if(null == cmdParameter || cmdParameter.length() <= 0){
			error("The input string can't be null or empty");
			printUsage();
			//TODO: what error code?
			System.exit(1); 
		}

		//TODO:Get this url from config properties file
		final String baseUrl = "http://api.goeuro.com/api/v2/position/suggest/en/";
		String url = baseUrl + cmdParameter;

		//TODO:User config properties file to find output Folder for CSV file
		final String userDir = System.getProperty("user.dir");
		final String ext = ".csv"; //File extension

		info("Using dir = " + userDir+" to write output file");

		File userDirFile = new File(userDir);

		//This Folder should have write permission else error out and exit
		if(!userDirFile.canWrite()){
			error("On current folder this user should have File write permission");
			//TODO: What error code?
			System.exit(1);
		}

		String outPutFile = userDir + "/" + cmdParameter + ext;
		
		//Call functionality
		callGoEuro(outPutFile, url);
	}


	public static void callGoEuro(String outPutFileName, String url){
		try
		{
			//Get data from url
			String citydata = IOUtils.toString(new URL(url).openStream());

			//Prepare JSON object 
			JSONArray json = new JSONArray(citydata);

			//Decide what data we are interested in from complete set
			JSONArray names = new JSONArray();
			names.put("_id");
			names.put("name");
			names.put("type");
			names.put("geo_position");

			//Header info in csv file
			StringBuilder output = new StringBuilder("_id,").append("name,").append("type,").append("latitude,").append("longitude").append("\n");

			//Append output of JSON parsing
			output.append(JSONUtils.toString(names,json));

			//Save data to csv file
			FileUtils.saveFile(outPutFileName, output.toString());

			info("Saved output to file - "+outPutFileName);

		}catch(JSONException ex){
			error("Error occered while parsing output data "+ex.getMessage());
			System.exit(1);
		}catch (IOException e) {
			error("Error occered while reading or writing data "+e.getMessage());
			System.exit(1);
		}
		System.exit(0);
	}


	public static void error(String error){
		//TOOD: Use logger
		System.out.println("ERROR:" + error);
	}

	public static void info(String log){
		//TOOD: Use logger
		System.out.println("INFO:" + log);
	}

	public static void printUsage(){
		System.out.println("Please make sure that Java is in your classpath");
		System.out.println("Usage: java -jar GoEuroTest.jar <non empty string>");
	}	



}
