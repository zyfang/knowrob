/*
 * Copyright (c) 2012 Stefan Profanter
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the Technische Universiteit Eindhoven nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
*/

package org.knowrob.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * File Utils for reading text files and unzip.
 * 
 * @author Stefan Profanter
 * 
 */
public class FileUtil {

	/**
	 * Convert a file path into a File object with an absolute path relative to a passed in root. If
	 * path is absolute then a file object constructed from new File(path) is returned, otherwise a
	 * file object is returned from new File(root, path) if root is not null, otherwise null is
	 * returned.
	 * 
	 * @param root
	 *            root file path
	 * @param path
	 *            relative file path
	 * @return the absolute path to the file or null if root is null
	 */
	public static String getAbsoluteFilePath(String root, String path) {
		File file = new File(path);
		if (file.isAbsolute())
			return file.getAbsolutePath();

		if (root == null)
			return null;

		return new File(new File(root), path).getAbsolutePath();
	}

	/**
	 * Read a text file and return it as a String.
	 * 
	 * @param file
	 *            File to read
	 * @return the content of the file
	 * @throws FileNotFoundException
	 *             If file not found or invalid path
	 * @throws IOException
	 *             if there was an error reading the file
	 */
	public static String readTextFile(File file) throws FileNotFoundException, IOException {
		FileReader fr = new FileReader(file);
		char[] cbuf = new char[(int) file.length()];
		fr.read(cbuf);
		String content = new String(cbuf);
		fr.close();
		return content;
	}

	/**
	 * Read a text file and return it as a String.
	 * 
	 * @param filename
	 *            Filename as String. File must exist otherwise a FileNotFound exception will be
	 *            thrown.
	 * @return the content of the file
	 * @throws FileNotFoundException
	 *             If file not found or invalid path
	 * @throws IOException
	 *             if there was an error reading the file
	 */
	public static String readTextFile(String filename) throws FileNotFoundException, IOException {
		return readTextFile(new File(filename));
	}

	/**
	 * Unzip a zipped file (eg. kmz, zip) into given directory
	 * 
	 * @param zipFile
	 *            zipped file to unzip
	 * @param outputDirectory
	 *            destination directory for unzipped content
	 * @return true if successfully unzipped
	 */
	public static boolean Unzip(String zipFile, String outputDirectory) {
		String outDir = outputDirectory;
		if (!outDir.endsWith("/") && !outDir.endsWith("\\"))
			outDir += File.separator;

		BufferedOutputStream dest = null;
		BufferedInputStream is = null;
		int BUFFER = 2048;
		ZipEntry entry;
		ZipFile zipfile;
		try {
			zipfile = new ZipFile(zipFile);
			Enumeration<? extends ZipEntry> e = zipfile.entries();
			while (e.hasMoreElements()) {
				entry = e.nextElement();
				if (entry.isDirectory()) {
					(new File(outDir + entry.getName())).mkdir();
					continue;
				}

				String name;

				if (File.separator.equals("\\")) {
					name = entry.getName()
							.replaceAll("/", Matcher.quoteReplacement(File.separator));
				} else {
					name = entry.getName().replaceAll("\\\\",
							Matcher.quoteReplacement(File.separator));
				}

				String filename = outDir + name;
				String filePath = filename.substring(0, filename.lastIndexOf(File.separator));

				// Create directory if not existing
				if (!(new File(filePath)).exists()) {
					(new File(filePath)).mkdirs();
				}
				is = new BufferedInputStream(zipfile.getInputStream(entry));
				int count;
				byte data[] = new byte[BUFFER];
				FileOutputStream fos = new FileOutputStream(filename);
				dest = new BufferedOutputStream(fos, BUFFER);
				while ((count = is.read(data, 0, BUFFER)) != -1) {
					dest.write(data, 0, count);
				}
				dest.flush();
				dest.close();
				is.close();
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			System.err.println("Couldn't unzip file: " + zipFile);
			e1.printStackTrace();
			return false;
		}
		return true;
	}
}
