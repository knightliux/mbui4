package com.moonX.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class POIOuput {

	private static boolean write2007Excel(String filePath, List<Item> list) {
		try {
			XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(filePath));
			XSSFSheet sheet = wb.getSheetAt(0);
			for (int i = 0; i < list.size(); i++) {
				XSSFRow row = sheet.createRow(sheet.getLastRowNum() + 1);
				XSSFCell cellMac = row.createCell(0);
				XSSFCell cellConcertMac = row.createCell(1);
				cellMac.setCellValue(list.get(i).mac);
				cellConcertMac.setCellValue(list.get(i).convertMac);
			}

			FileOutputStream out = new FileOutputStream(filePath);
			wb.write(out);
			out.close();
			return true;
		} catch(FileNotFoundException e){
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private static boolean write2003Excel(String filePath, List<Item> list) {
		try {
			HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(filePath));
			HSSFSheet sheet = wb.getSheetAt(0);
			for (int i = 0; i < list.size(); i++) {
				HSSFRow row = sheet.createRow(sheet.getLastRowNum() + 1);
				HSSFCell cellMac = row.createCell(0);
				HSSFCell cellConcertMac = row.createCell(1);
				cellMac.setCellValue(list.get(i).mac);
				cellConcertMac.setCellValue(list.get(i).convertMac);
			}

			FileOutputStream out = new FileOutputStream(filePath);
			wb.write(out);
			out.close();
			return true;
		} catch(FileNotFoundException e){
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return false;
	}

	private static boolean writeExcel(File file, List<Item> mList)
			throws IOException {
		String fileName = file.getName();
		String extension = fileName.lastIndexOf(".") == -1 ? "" : fileName
				.substring(fileName.lastIndexOf(".") + 1);

		if ("xls".equals(extension)) {
			System.out.println("write xls");
			return write2003Excel(file.getPath(), mList);
		} else if ("xlsx".equals(extension)) {
			return write2007Excel(file.getPath(), mList);
		} else {
			throw new IOException("ioexception");
		}
	}

	private static class Item {
		String mac;
		String convertMac;
		public Item(String mac, String convertMac) {
			super();
			this.mac = mac;
			this.convertMac = convertMac;
		}
	}
	
	public static boolean saveMac(String mac,String convertMac){
		
		final Item item = new Item(mac,convertMac);
		List<Item> list = new ArrayList<Item>(){
			/**
			 * 
			 */
			private static final long serialVersionUID = -8764521259926357226L;

			{
				add(item);
			}
		};
		try {
			boolean status = writeExcel(new File("D:/test.xls"), list);
			return status;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

}