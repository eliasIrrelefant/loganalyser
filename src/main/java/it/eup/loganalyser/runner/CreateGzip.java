package it.eup.loganalyser.runner;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.io.IOUtils;

public class CreateGzip {
	public static void main(String[] args) throws Exception {
		FileInputStream inputStream = new FileInputStream(args[0]);
		GZIPOutputStream outputStream = new GZIPOutputStream(new FileOutputStream(args[0] + ".gz"));

		IOUtils.copy(inputStream, outputStream);

		IOUtils.closeQuietly(outputStream);
	}
}
