package org.apache.lucene.demo;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/** Index all text files under a directory.
 * <p>
 * This is a command-line application demonstrating simple Lucene indexing.
 * Run it with no command-line arguments for usage information.
 */
public class IndexFiles {

	private IndexFiles() {}

	/** Index all text files under a directory. */
	public static void main(String[] args) {
		String usage = "java org.apache.lucene.demo.IndexFiles"
				+ " [-index INDEX_PATH] [-docs DOCS_PATH] [-update]\n\n"
				+ "This indexes the documents in DOCS_PATH, creating a Lucene index"
				+ "in INDEX_PATH that can be searched with SearchFiles";
		String indexPath = "index";
		String docsPath = null;
		boolean create = true;
		for(int i=0;i<args.length;i++) {
			if ("-index".equals(args[i])) {
				indexPath = args[i+1];
				i++;
			} else if ("-docs".equals(args[i])) {
				docsPath = args[i+1];
				i++;
			} else if ("-update".equals(args[i])) {
				create = false;
			}
		}

		if (docsPath == null) {
			System.err.println("Usage: " + usage + ":-)");
			System.exit(1);
		}

		final File docDir = new File(docsPath);
		if (!docDir.exists() || !docDir.canRead()) {
			System.out.println("Document directory '" +docDir.getAbsolutePath()+ "' does not exist or is not readable, please check the path");
			System.exit(1);
		}

		Date start = new Date();
		try {
			System.out.println("Indexing to directory '" + indexPath + "'...");

			Directory dir = FSDirectory.open(new File(indexPath));
			// :Post-Release-Update-Version.LUCENE_XY:
			Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_4_10_0);
			IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_4_10_0, analyzer);

			if (create) {
				// Create a new index in the directory, removing any
				// previously indexed documents:
				iwc.setOpenMode(OpenMode.CREATE);
			} else {
				// Add new documents to an existing index:
				iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
			}

			// Optional: for better indexing performance, if you
			// are indexing many documents, increase the RAM
			// buffer.  But if you do this, increase the max heap
			// size to the JVM (eg add -Xmx512m or -Xmx1g):
			//
			// iwc.setRAMBufferSizeMB(256.0);

			IndexWriter writer = new IndexWriter(dir, iwc);
			indexDocs(writer, docDir);

			// NOTE: if you want to maximize search performance,
			// you can optionally call forceMerge here.  This can be
			// a terribly costly operation, so generally it's only
			// worth it when your index is relatively static (ie
			// you're done adding documents to it):
			//
			// writer.forceMerge(1);

			writer.close();

			Date end = new Date();
			System.out.println(end.getTime() - start.getTime() + " total milliseconds");

		} catch (IOException e) {
			System.out.println(" caught a " + e.getClass() +
					"\n with message: " + e.getMessage());
		}
	}

	/**
	 * Indexes the given file using the given writer, or if a directory is given,
	 * recurses over files and directories found under the given directory.
	 * 
	 * NOTE: This method indexes one document per input file.  This is slow.  For good
	 * throughput, put multiple documents into your input file(s).  An example of this is
	 * in the benchmark module, which can create "line doc" files, one document per line,
	 * using the
	 * <a href="../../../../../contrib-benchmark/org/apache/lucene/benchmark/byTask/tasks/WriteLineDocTask.html"
	 * >WriteLineDocTask</a>.
	 *  
	 * @param writer Writer to the index where the given file/dir info will be stored
	 * @param file The file to index, or the directory to recurse into to find files to index
	 * @throws IOException If there is a low-level I/O error
	 */
	static void indexDocs(IndexWriter writer, File file)
			throws IOException {
		// do not try to index files that cannot be read
		if (file.canRead() && !file.getPath().contains("ETEXT")) {
			if (file.isDirectory()) {
				String[] files = file.list();
				// an IO error could occur
				if (files != null) {
					for (int i = 0; i < files.length; i++) {
						indexDocs(writer, new File(file, files[i]));
					}
				}
			} else{
				BufferedReader br = null;
				String filepath = file.getPath();
				if(isZipFile(file)){
					ZipFile fis = new ZipFile(file);
					for (Enumeration e = fis.entries(); e.hasMoreElements();) {
						ZipEntry entry = (ZipEntry) e.nextElement();
						filepath = filepath+"\\"+entry.toString();
						if(entry.toString().endsWith(".txt")){
							InputStream input = fis.getInputStream(entry);
							br = new BufferedReader(new InputStreamReader(input, "UTF-8"));
						}else{
							return;
						}
					}
				}else if(file.toString().endsWith(".txt")){
					FileInputStream fis;
					try {
						fis = new FileInputStream(file);
					} catch (FileNotFoundException fnfe) {
						return;
					}
					br = new BufferedReader(new InputStreamReader(fis, StandardCharsets.UTF_8));
				}else{
					// if not a zip file OR not a text file
					return;
				}

				try{
					Document doc = new Document();

					Field pathField = new StringField("path", filepath, Field.Store.YES);
					doc.add(pathField);
					
					doc.add(new LongField("modified", file.lastModified(), Field.Store.NO));
					
					TextField fieldContents = new TextField("contents", br);
					//fieldContents.setBoost(0.5f);
					doc.add(fieldContents);
					
					String author = "";
					String title = "";
					String language = "";
					String release = "";
					String line = "";
					try{
						while((line = br.readLine()) != null){
							if(line.contains("Author:")){
								author = line.substring(8, line.length());
							}else if(line.contains("Title:")){
								title = line.substring(7, line.length());
							}else if(line.contains("Language:")){
								language = line.substring(10, line.length());
							}else if(line.contains("Release Date:")){
								release = line.substring(14, line.length());
							}else if(line.contains("*** START OF THIS PROJECT")){
								break;
							}
						}
					}catch(Exception e){
						System.out.println(e.getStackTrace());
					}
					printLog(author);
					printLog(title);
					printLog(language);
					printLog(release);
					
					TextField fieldAuthor = new TextField("author", author, Field.Store.YES);
					TextField fieldTitle = new TextField("title", title, Field.Store.YES);
					TextField fieldRelease = new TextField("release", release, Field.Store.YES);
					TextField fieldLanguage = new TextField("language", language, Field.Store.YES); 
					
					fieldAuthor.setBoost(4f);
					fieldTitle.setBoost(4f);
					
					doc.add(fieldAuthor);
					doc.add(fieldTitle);
					doc.add(fieldLanguage);
					doc.add(fieldRelease);

					//doc.add(new TextField("Author", new BufferedReader(new InputStreamReader(fis, StandardCharsets.UTF_8))));

					if (writer.getConfig().getOpenMode() == OpenMode.CREATE) {
						// New index, so we just add the document (no old document can be there):
						System.out.println("adding " + filepath);
						writer.addDocument(doc);
					} else {
						// Existing index (an old copy of this document may have been indexed) so 
						// we use updateDocument instead to replace the old one matching the exact 
						// path, if present:
						System.out.println("updating " + file);
						writer.updateDocument(new Term("path", filepath), doc);
					}
				}
				finally{
					br.close();
				}
			}
		}
	}

	//SOURCE : http://www.java2s.com/Code/Java/File-Input-Output/DeterminewhetherafileisaZIPFile.htm
	/**
	 * Determine whether a file is a ZIP File.
	 */
	public static boolean isZipFile(File file) throws IOException {
		if(file.isDirectory()) {
			return false;
		}
		if(!file.canRead()) {
			throw new IOException("Cannot read file "+file.getAbsolutePath());
		}
		if(file.length() < 4) {
			return false;
		}
		DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
		int test = in.readInt();
		in.close();
		return test == 0x504b0304;
	}
	
	private static void printLog(String s){
		// comment to remove print lines
		System.out.println(s);
	}
}
