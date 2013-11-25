package com.thetdgroup.alignment_utils;

public class LexiconObject
{
 private String sourceLanguageID = "";
 private String targetLanguageID = "";
 private String lexiconPath = "";
 
 //
 public LexiconObject(final String sourceID, final String targetID, final String lexiconFilePath)
 {
 	sourceLanguageID = sourceID;
 	targetLanguageID = targetID;
 	lexiconPath = lexiconFilePath;
 }
 
 public String getKey()
 {
 	return sourceLanguageID + "_" + targetLanguageID;
 }
 
 public String getLexiconFilePath()
 {
 	return lexiconPath;
 }
}
