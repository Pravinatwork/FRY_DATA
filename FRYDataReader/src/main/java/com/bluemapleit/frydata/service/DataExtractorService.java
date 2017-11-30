package com.bluemapleit.frydata.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.apache.pdfbox.pdmodel.interactive.form.PDNonTerminalField;

import com.bluemapleit.frydata.meta.MetaData;

public class DataExtractorService {

	/**
	 * This method will extract the data's from all available PDF from the given
	 * location and it will return as List of Map. Map has key values pairs that
	 * needs to be extracted from one PDF.
	 * 
	 * @param filePath
	 * @return List<Map<String,String>>
	 * @throws InvalidPasswordException
	 * @throws IOException
	 */
	public List<Map<String, String>> readAndExtractTheData(String filePath)
			throws InvalidPasswordException, IOException {

		List<Map<String, String>> returnDataList = new ArrayList<Map<String, String>>();
		Map<String, String> dataMap;
		PDDocument document;
		PDDocumentCatalog catalog;
		PDAcroForm form;
		File folder = new File(filePath);

		for (final File file : folder.listFiles()) {
			document = PDDocument.load(file);
			catalog = document.getDocumentCatalog();
			form = catalog.getAcroForm();
			List<PDField> fields = form.getFields();
			dataMap = new HashMap<String, String>();
			dataMap.put(MetaData.FILE_NAME, file.getName());
			for (PDField field : fields) {
				this.nodeList(field, dataMap);
			}
			returnDataList.add(dataMap);
			document.close();
		}
		return returnDataList;
	}

	/**
	 * this method is used to traverse all the available tree nodes inside the
	 * PDF. will list out all possible tree nodes name and its values
	 * 
	 * @param field
	 * @param dataMap
	 */
	private void nodeList(PDField field, Map<String, String> dataMap) {
		System.out.println(field.getFullyQualifiedName() + " = > " + field.getValueAsString());
		dataMap.put(field.getFullyQualifiedName(), field.getValueAsString());
		if (field instanceof PDNonTerminalField) {
			PDNonTerminalField nonTerminalField = (PDNonTerminalField) field;
			for (PDField child : nonTerminalField.getChildren()) {
				this.nodeList(child, dataMap);
			}
		}
	}
}
