package com.bluemapleit.frydata.run;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.bluemapleit.frydata.config.FRYDataConfig;
import com.bluemapleit.frydata.io.ExcelExporterService;
import com.bluemapleit.frydata.service.DataExtractorService;

public class Runner {

	// static final String
	// PDF_PATH="C:\\PravinWorkSpace\\Projects\\FRYData\\DataFolder\\ReaderModifiedForm.pdf";
	static final String PDF_PATH = "C:\\PravinWorkSpace\\Projects\\FRYData\\DataFolder";

	public static void main(String args[]) throws InvalidPasswordException, IOException {

		ApplicationContext context = new AnnotationConfigApplicationContext(FRYDataConfig.class);

		// DataExtractorService service = new DataExtractorService();

		DataExtractorService service = context.getBean(DataExtractorService.class);
		ExcelExporterService excelService = context.getBean(ExcelExporterService.class);

		List<Map<String, String>> fryData = service.readAndExtractTheData(PDF_PATH);

		excelService.exportToExcel("C:\\PravinWorkSpace\\Projects\\FRYData\\FRYDataOut", "FRY_DATA", fryData);

	}
}
