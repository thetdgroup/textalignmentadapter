package com.thetdgroup;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.xpath.XPath;
import org.json.JSONObject;

import com.thetdgroup.alignment_utils.ApptekDocumentAligner;
import com.thetdgroup.AdapterConstants;

public class TextAlignmentAdapter extends BaseTextAlignmentAdapter
{
	private String adapterConfigurationFile = "";
	private String lexiconLibraryPath = "";
	private String alignerLibraryPath = "";
	private String jniLibraryPath = "";
	
	private String alignementWorkDirectory = "";
	
	private String alignerLicenseKey = "";
	private String lexiconLicenseKey = "";
	
	private FuzeInCommunication fuzeInCommunication = new FuzeInCommunication();
	
	//
	public void initialize(final JSONObject configurationObject) throws Exception
	{
		if(configurationObject.has("adapter_configuration_file") == false)
		{
			throw new Exception("The adapter_configuration_file parameter was not found");
		}
		
		//
		if(configurationObject.has("fuzein_connection_info"))
		{
			JSONObject jsonCommParams = configurationObject.getJSONObject("fuzein_connection_info");
			
			fuzeInCommunication.setFuzeInConnection(jsonCommParams.getString("service_url"), 
																																											jsonCommParams.getInt("service_socket_timeout"), 
																																											jsonCommParams.getInt("service_connection_timeout"), 
																																											jsonCommParams.getInt("service_connection_retry"));
		}
		
		//
		adapterConfigurationFile = configurationObject.getString("adapter_configuration_file");
		parseAdapterConfiguration(configurationObject.getString("adapter_configuration_file"));
		
		//
		// Init the aligner
	 ApptekDocumentAligner.initializeApptek(lexiconLibraryPath, alignerLibraryPath, jniLibraryPath);
	}
	
	//
	public void destroy()
	{
	 if(fuzeInCommunication != null)
	 {
	 	fuzeInCommunication.releaseConnection();
	 }
	}
	
	//
	public JSONObject wordAlign(JSONObject parameters) throws Exception
	{
		//
		// Validate that all required params are present
		//
		if(parameters.has("source_file_name") == false)
		{
			throw new Exception("The 'source_file_name' parameter is required.");
		}
		
		if(parameters.has("target_file_name") == false)
		{
			throw new Exception("The 'target_file_name' parameter is required.");
		}
		
		if(parameters.has("source_language") == false)
		{
			throw new Exception("The 'source_language' parameter is required.");
		}
		
		if(parameters.has("target_language") == false)
		{
			throw new Exception("The 'target_language' parameter is required.");
		}
		
	 //
		//
		//
		JSONObject jsonObject = new JSONObject(); 
		jsonObject.put(AdapterConstants.ADAPTER_STATUS, AdapterConstants.status.UNSUPPORTED);
		jsonObject.put(AdapterConstants.ADAPTER_DATA, "");
	 
		return jsonObject;
	}
	
	//
	public JSONObject sentenceAlign(JSONObject parameters) throws Exception
	{
		//
		// Validate that all required params are present
		//
		if(parameters.has("source_file_name") == false)
		{
			throw new Exception("The 'source_file_name' parameter is required.");
		}
		
		if(parameters.has("target_file_name") == false)
		{
			throw new Exception("The 'target_file_name' parameter is required.");
		}
		
		if(parameters.has("source_language") == false)
		{
			throw new Exception("The 'source_language' parameter is required.");
		}
		
		if(parameters.has("target_language") == false)
		{
			throw new Exception("The 'target_language' parameter is required.");
		}
		
	 //
		//
		//
		JSONObject jsonObject = new JSONObject(); 
		jsonObject.put(AdapterConstants.ADAPTER_STATUS, AdapterConstants.status.UNSUPPORTED);
		jsonObject.put(AdapterConstants.ADAPTER_DATA, "");
	 
		return jsonObject;
	}
	
	//
	public JSONObject paragraphAlign(JSONObject parameters) throws Exception
	{
		//
		// Validate that all required params are present
		//
		if(parameters.has("source_text") == false)
		{
			throw new Exception("The 'source_text' parameter is required.");
		}
		
		if(parameters.has("target_text") == false)
		{
			throw new Exception("The 'target_text' parameter is required.");
		}
		
		if(parameters.has("source_language") == false)
		{
			throw new Exception("The 'source_language' parameter is required.");
		}
		
		if(parameters.has("target_language") == false)
		{
			throw new Exception("The 'target_language' parameter is required.");
		}
		
		//
		// Get appropriate Lexicon based on source and target languages
		String lexiconFile = getLexicon(parameters.getString("source_language"),
																																		parameters.getString("target_language"));

		// Write source text buffer to file
		String sourceFile = "c:\\temp\\source.txt";
		FileUtils.writeStringToFile(new File(sourceFile), parameters.getString("source_text"), "UTF-8");

		// Write target text buffer to file
		String targetFile = "c:\\temp\\target.txt";
		FileUtils.writeStringToFile(new File(targetFile), parameters.getString("target_text"), "UTF-8");
		
		String outputFile = "c:\\temp\\apptek.txt";
		String alignmentOutputString = FileUtils.readFileToString(new File(outputFile), "UTF-8");
		
		//
		// Run alignment
		/*JSONObject jsonAlignResult = ApptekDocumentAligner.runMAPAlignment(alignerLicenseKey, 
																																								sourceFile,
				                                    getLanguageID(parameters.getString("source_language")),
				                                    targetFile,
				                                    getLanguageID(parameters.getString("target_language")),
				                                    lexiconFile,
				                                    outputFile);*/
		
		// FAKE BELOW - START
		JSONObject jsonAlignResult = new JSONObject();
		jsonAlignResult.put("aligned_text", "fake");
		jsonAlignResult.put("alignment_format", "MAP");
		jsonAlignResult.put("status_code", "1");
		jsonAlignResult.put("elapse_time", "1.02");
		// FAKE BELOW - END
		
	 //
		JSONObject jsonObject = new JSONObject(); 
		jsonObject.put(AdapterConstants.ADAPTER_STATUS, AdapterConstants.status.SUCCESS);
		jsonObject.put(AdapterConstants.ADAPTER_DATA, jsonAlignResult);
	 
		return jsonObject;
	}
	
 //
	private void parseAdapterConfiguration(String adapterConfigurationFile) throws Exception
	{
		//
		// Parse Configuration
		Document configurationDocument = saxBuilder.build(adapterConfigurationFile);
		
		//
		// Get the AppTekLexicon.dll location 
		XPath xPath = XPath.newInstance("adapter_configuration/lexicon_library");
		Element element = (Element) xPath.selectSingleNode(configurationDocument);
		lexiconLibraryPath = element.getText();
		
		//
		// Get the ApptekDocAlignDll location 
		xPath = XPath.newInstance("adapter_configuration/aligner_library");
		element = (Element) xPath.selectSingleNode(configurationDocument);
		alignerLibraryPath = element.getText();
		
		//
		// Get the TDGDocumentAligner.dll location 
		xPath = XPath.newInstance("adapter_configuration/jni_library");
		element = (Element) xPath.selectSingleNode(configurationDocument);
		jniLibraryPath = element.getText();
		
		//
		// Get the Aligner license key
		xPath = XPath.newInstance("adapter_configuration/aligner_license_key");
		element = (Element) xPath.selectSingleNode(configurationDocument);
		alignerLicenseKey = element.getText();
		
		//
		// Get the Lexicon license key
		xPath = XPath.newInstance("adapter_configuration/lexicon_license_key");
		element = (Element) xPath.selectSingleNode(configurationDocument);
		lexiconLicenseKey = element.getText();
		
		//
		// Get the work directory location
		xPath = XPath.newInstance("adapter_configuration/alignment_work_directory");
		element = (Element) xPath.selectSingleNode(configurationDocument);
		alignementWorkDirectory = element.getText();
	}
	
	//
	private int getLanguageID(final String languageId) throws Exception
	{
		//
		// Parse Configuration
		Document configurationDocument = saxBuilder.build(adapterConfigurationFile);
		
		//
		XPath xPath = XPath.newInstance("adapter_configuration/apptek_languages/language[@iso='" + languageId + "']");
		Element apptekLanguageElement = (Element) xPath.selectSingleNode(configurationDocument);
		
		if(apptekLanguageElement != null)
		{
			return apptekLanguageElement.getAttribute("apptek_id").getIntValue();
		}
		
		return 0;
	}
	
	//
	private String getLexicon(final String sourceLanguageID, final String targetLanguageID) throws Exception
	{
		//
		// Parse Configuration
		Document configurationDocument = saxBuilder.build(adapterConfigurationFile);
		
		//
		String query = "adapter_configuration/lexicons/lexicon[@source_lang_iso='" + sourceLanguageID + "'] | " +
																	"adapter_configuration/lexicons/lexicon[@target_lang_iso='" + targetLanguageID + "']";
		
		XPath xPath = XPath.newInstance(query);
		Element apptekLanguageElement = (Element) xPath.selectSingleNode(configurationDocument);

		if(apptekLanguageElement != null)
		{
			return apptekLanguageElement.getText();
		}
		
		//
		return "";
	}
}
