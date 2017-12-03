package com.bluemapleit.frydata.io;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bluemapleit.frydata.meta.MetaData;

@Component
public class ExcelExporterService {

	@Autowired
	MetaData metaDataIns;

	private Workbook workbook;

	public boolean exportToExcel(String fileURI, String fileName, List<Map<String, String>> mapList)
			throws IOException {

		System.out.println("Begining the write process - URL " + fileURI + " fileName-" + fileName);
		System.out.println("List of files - " + mapList.size());

		if (fileName == null || fileURI == null || mapList == null || mapList.isEmpty()) {
			System.out.println("Incorrect parameters for excel export!");
			return false;
		}

		/**
		 * Preparing the meta data columns for extract the values from read PDF
		 * map.
		 */
		Set<Entry<String, String>> mapEntry = metaDataIns.getDataColumnMap().entrySet();
		Iterator<Entry<String, String>> mapEntryIterator;

		workbook = new HSSFWorkbook();

		Sheet sheet = workbook.createSheet(MetaData.FRY_DATA_SHEET);

		Iterator<Map<String, String>> iterator = mapList.iterator();
		int rowIndex = 0;
		int cellIndex = 0;
		Row row;
		Cell cell1;
		Map<String, String> valuesMap;
		while (iterator.hasNext()) {
			valuesMap = iterator.next();
			row = sheet.createRow(rowIndex);

			System.out.println("writing row at - " + rowIndex);

			cellIndex = 0;
			mapEntryIterator = mapEntry.iterator();
			while (mapEntryIterator.hasNext()) {
				Entry<String, String> entry = mapEntryIterator.next();
				cell1 = row.createCell(cellIndex);

				if (rowIndex == 0) {
					cell1.setCellValue(entry.getKey());
				} else {

					if (entry.getValue().equals(MetaData.NA)) {
						cell1.setCellValue("NULL");
						cellIndex += 1;
						continue;
					}
					// System.out.println("writing cell at - " + cellIndex + "
					// => " + valuesMap.get(entry.getValue()));
					cell1.setCellValue(valuesMap.get(entry.getValue()));
				}
				cellIndex += 1;
			}
			if (rowIndex == 0) {
				iterator = mapList.iterator();
			}

			rowIndex += 1;
		}
		for (int i = 0; i < rowIndex; i++) {
			sheet.autoSizeColumn(i);
		}

		FileOutputStream fos = new FileOutputStream(
				fileURI + "/" + fileName + Calendar.getInstance().getTimeInMillis() + ".xls");
		workbook.write(fos);
		fos.close();
		System.out.println(fileName + " written successfully");

		return true;
	}

}
