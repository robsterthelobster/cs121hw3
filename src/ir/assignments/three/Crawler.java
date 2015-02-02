// Robin Chen    #95812659
// Valentin Yang #30062256

// Credits to Yasser Ganjisaffar & Avi Hayun for BasicCrawler.java from crawler4j example

package ir.assignments.three;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class Crawler extends WebCrawler{
	/**
	 * This method is for testing purposes only. It does not need to be used
	 * to answer any of the questions in the assignment. However, it must
	 * function as specified so that your crawler can be verified programatically.
	 * 
	 * This methods performs a crawl starting at the specified seed URL. Returns a
	 * collection containing all URLs visited during the crawl.
	 */
	
	private final static Pattern BINARY_FILES_EXTENSIONS =
		        Pattern.compile(".*\\.(bmp|gif|jpe?g|png|tiff?|pdf|ico|xaml|pict|rif|pptx?|ps" +
		        "|mid|mp2|mp3|mp4|wav|wma|au|aiff|flac|ogg|3gp|aac|amr|au|vox" +
		        "|avi|mov|mpe?g|ra?m|m4v|smil|wm?v|swf|aaf|asf|flv|mkv" +
		        "|zip|rar|gz|7z|aac|ace|alz|apk|arc|arj|dmg|jar|lzip|lha|css)" +
		        "(\\?.*)?$"); // For url Query parts ( URL?q=... )
	
	@Override
	public boolean shouldVisit(WebURL url){
		String href = url.getURL().toLowerCase();
		
		return(!BINARY_FILES_EXTENSIONS.matcher(href).matches() && href.contains(".ics.uci.edu") &&
				!href.contains("calendar") && !href.contains("archive"));
	}
	
	@Override
	public void visit(Page page){
//		String url = page.getWebURL().getURL();
//        System.out.println("URL: " + url);
//
//        if (page.getParseData() instanceof HtmlParseData) {
//                HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
//                String text = htmlParseData.getText();
//                String html = htmlParseData.getHtml();
//                List<WebURL> links = htmlParseData.getOutgoingUrls();
//
//                System.out.println("Text length: " + text.length());
//                System.out.println("Html length: " + html.length());
//                System.out.println("Number of outgoing links: " + links.size());
		
	
		// CREDITS TO MadProgrammer on his StackOverflow response on writing to a text file in Java.
		try {
			PrintStream printer = new PrintStream(new FileOutputStream("printdata.txt", true));
		
			int docid = page.getWebURL().getDocid();
			String url = page.getWebURL().getURL();
			String domain = page.getWebURL().getDomain();
			String path = page.getWebURL().getPath();
			String subDomain = page.getWebURL().getSubDomain();
			String parentUrl = page.getWebURL().getParentUrl();
			String anchor = page.getWebURL().getAnchor();
			
			printer.println("Docid: " + docid);
			printer.println("URL: " + url);
			printer.println("Domain: '" + domain + "'");
			printer.println("Sub-domain: '" + subDomain + "'");
			printer.println("Path: '" + path + "'");
			printer.println("Parent page: " + parentUrl);
			printer.println("Anchor text: " + anchor);
			
			System.out.println("Docid: " + docid);
			System.out.println("URL: " + url);
			System.out.println("Domain: '" + domain + "'");
			System.out.println("Sub-domain: '" + subDomain + "'");
			System.out.println("Path: '" + path + "'");
			System.out.println("Parent page: " + parentUrl);
			System.out.println("Anchor text: " + anchor);
			
			if(page.getParseData() instanceof HtmlParseData) {
				HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
				String text = htmlParseData.getText();
			    String html = htmlParseData.getHtml();
			    List<WebURL> links = htmlParseData.getOutgoingUrls();
			
			    printer.println("Text length: " + text.length());
			    printer.println("Html length: " + html.length());
			    printer.println("Number of outgoing links: " + links.size());
			    
		        System.out.println("Text length: " + text.length());
		        System.out.println("Html length: " + html.length());
		        System.out.println("Number of outgoing links: " + links.size());
			         
		        String outputFormat = url + "," + links.size() + "\n";
		        printer.println(outputFormat);
		        System.out.println(outputFormat);
			}
			printer.println("-----");
			System.out.println("-----");
			
//			File logFile = new File("data.txt");
//			System.out.println(logFile.getCanonicalPath());
//			writer = new BufferedWriter(new FileWriter(logFile, true));		// allows program to append text to the file
//			int docid = page.getWebURL().getDocid();
//			String url = page.getWebURL().getURL();
//			String domain = page.getWebURL().getDomain();
//			String path = page.getWebURL().getPath();
//			String subDomain = page.getWebURL().getSubDomain();
//			String parentUrl = page.getWebURL().getParentUrl();
//			String anchor = page.getWebURL().getAnchor();
//			
//			writer.write("Docid: " + docid);
//			writer.write("URL: " + url);
//			writer.write("Domain: '" + domain + "'");
//			writer.write("Sub-domain: '" + subDomain + "'");
//			writer.write("Path: '" + path + "'");
//			writer.write("Parent page: " + parentUrl);
//			writer.write("Anchor text: " + anchor);
//			
//			if(page.getParseData() instanceof HtmlParseData) {
//				HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
//				String text = htmlParseData.getText();
//			    String html = htmlParseData.getHtml();
//			    List<WebURL> links = htmlParseData.getOutgoingUrls();
//			
//			    writer.write("Text length: " + text.length());
//			    writer.write("Html length: " + html.length());
//			    writer.write("Number of outgoing links: " + links.size());
//			         
//		        String outputFormat = url + "," + links.size() + "\n";
//		        writer.write("Output Format: " + outputFormat);
//			} 
//			
//			writer.write("-----");
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	public static Collection<String> crawl(String seedURL) {
		// TODO implement me
		return null;
	}
	
	
}


