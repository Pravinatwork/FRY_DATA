package com.bluemapleit.frydata.run;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;

import com.bluemapleit.frydata.service.DataExtractorService;

public class Runner {

	// static final String
	// PDF_PATH="C:\\PravinWorkSpace\\Projects\\FRYData\\DataFolder\\ReaderModifiedForm.pdf";
	static final String PDF_PATH = "C:\\PravinWorkSpace\\Projects\\FRYData\\DataFolder";

	public static void main(String args[]) throws InvalidPasswordException, IOException {

		DataExtractorService service = new DataExtractorService();
		List<Map<String, String>> fryData = service.readAndExtractTheData(PDF_PATH);

	}
}
