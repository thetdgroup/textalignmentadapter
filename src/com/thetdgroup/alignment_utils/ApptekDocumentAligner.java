package com.thetdgroup.alignment_utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.json.JSONObject;

public class ApptekDocumentAligner
{
	private static boolean systemLoaded = false;
	
 //
 // JNI Declarations
 //
 public static native void setAlignerLicenseKey(String strLicenseKey);
 public static native void setLexiconLicenseKey(String strLicenseKey);
 public static native void setFilePaths(String strDocumentPath1, int iDocumentLanguage1, String strDocumentPath2, int iDocumentLanguage2, String strLexiconFileName, String strTMXOut);
 public static native void setXML(String strXML);
 public static native String runTMXAlignment();
 public static native String runXLIFFAlignment();
 public static native String runMAPAlignment();
 public static native String runMemoryAlignment();
 public static native void PrintPaths();
 public static native String runLexiconQuery();
 public static native double getAvailableMemory_MB();
 
	//
	// Initialization and shutdown
	//
	public static void initializeApptek(final String lexiconDll, 
																																					final String docAlignDll, 
																																					final String tdgDocAlignDll) throws Exception
	{
	 // Load DocumentAligner DLL
		if(systemLoaded == false)
		{
			System.load(lexiconDll);
			System.load(docAlignDll);
			System.load(tdgDocAlignDll);
			
			systemLoaded = true;
		}
	}
 
	//
	public static JSONObject runMAPAlignment(final String strLicenseKey, 
																																										final String strSource_1, 
																																										final int iSourceLanguage_1, 
																																										final String strSource_2, 
																																										final int iSourceLanguage_2, 
																																										final String strLexiconFileName, 
																																										final String strOutputFile) throws Exception
	{
		//
		// Run Alignment
	 setAlignerLicenseKey(strLicenseKey);
	 setFilePaths(strSource_1, iSourceLanguage_1, strSource_2, iSourceLanguage_2, strOutputFile, strLexiconFileName);
	 
	 String strAlignmentResult = runMAPAlignment();
	 
	 //
		// Read results
	 StringBuffer tmxData = new StringBuffer();
	 FileInputStream fileInputStream = null;
	 
	 File target = new File(strOutputFile);
	 target.createNewFile();
	 
	 if(target.exists())
	 {
	 	try
	 	{
				fileInputStream = new FileInputStream(strOutputFile);
				BufferedReader tmxFileInput = new BufferedReader(new InputStreamReader(fileInputStream, "UTF-8"));
				
		  String line = null;
		  
		  while((line = tmxFileInput.readLine()) != null)
		  {
		  	tmxData.append(line);
		  	tmxData.append(System.getProperty("line.separator"));
		  }
	 	}
	 	finally
	 	{
	 		fileInputStream.close();
	 	}
	 }
  
	 //
  String[] result = strAlignmentResult.split(";");
  
  JSONObject jsonObject = new JSONObject();
  jsonObject.put("alignment_format", "MAP");
  jsonObject.put("aligned_text", tmxData.toString());
  jsonObject.put("status_code", result[0]);
  jsonObject.put("elapse_time", result[1]);

  return jsonObject;
	}
}
