// Robin Chen    #95812659
// Valentin Yang #30062256

// Credits to Yasser Ganjisaffar & Avi Hayun for BasicCrawler.java from crawler4j example

package ir.assignments.three;

import ir.assignments.helper.Utilities;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class Crawler extends WebCrawler{

	BufferedWriter writer;
	ArrayList<String> urls = new ArrayList<String>();

	private final static Pattern BINARY_FILES_EXTENSIONS =
			Pattern.compile(".*\\.(bmp|gif|jpe?g|png|tiff?|pdf|ico|xaml|pict|rif|pptx?|ps" +
					"|mid|mp2|mp3|mp4|wav|wma|au|aiff|flac|ogg|3gp|aac|amr|au|vox" +
					"|avi|mov|mpe?g|ra?m|m4v|smil|wm?v|swf|aaf|asf|flv|mkv" +
					"|zip|rar|gz|7z|aac|ace|alz|apk|arc|arj|dmg|jar|lzip|lha|css)" +
					"(\\?.*)?$"); // For url Query parts ( URL?q=... )

	@Override
	public boolean shouldVisit(WebURL url){
		String href = url.getURL().toLowerCase();

		//		return(!BINARY_FILES_EXTENSIONS.matcher(href).matches() && href.contains(".ics.uci.edu") &&
		//				!href.contains("calendar") && !href.contains("archive"));
		return(!BINARY_FILES_EXTENSIONS.matcher(href).matches() && href.contains(".ics.uci.edu/~kay"));

	}

	@Override
	public void visit(Page page){
		//common words
		if (page.getParseData() instanceof HtmlParseData) {
			HtmlParseData parseData = (HtmlParseData) page.getParseData();
			Controller.tokens.addAll(Utilities.tokenizeString(parseData.getText()));
		}

		try {

			System.out.println(Controller.logFile.getCanonicalPath());
			writer = new BufferedWriter(new FileWriter(Controller.logFile, true));

			int docid = page.getWebURL().getDocid();
			String url = page.getWebURL().getURL();
			String domain = page.getWebURL().getDomain();
			String path = page.getWebURL().getPath();
			String subDomain = page.getWebURL().getSubDomain();
			String parentUrl = page.getWebURL().getParentUrl();
			String anchor = page.getWebURL().getAnchor();

			write("Docid: " + docid);
			write("URL: " + url);
			write("Domain: '" + domain + "'");
			write("Sub-domain: '" + subDomain + "'");
			write("Path: '" + path + "'");
			write("Parent page: " + parentUrl);
			write("Anchor text: " + anchor);

			if(page.getParseData() instanceof HtmlParseData) {
				HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
				String text = htmlParseData.getText();
				String html = htmlParseData.getHtml();
				List<WebURL> links = htmlParseData.getOutgoingUrls();

				if(text.length() > Controller.length){
					Controller.length = text.length();
					Controller.longest_page = url;
				}
				
				write("Text length: " + text.length());
				write("Html length: " + html.length());
				write("Number of outgoing links: " + links.size());

				String outputFormat = url + "," + links.size() + "\n";
				write(outputFormat);      

			}

			write("-----");
			writer.close();

		} catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * This method is for testing purposes only. It does not need to be used
	 * to answer any of the questions in the assignment. However, it must
	 * function as specified so that your crawler can be verified programatically.
	 * 
	 * This methods performs a crawl starting at the specified seed URL. Returns a
	 * collection containing all URLs visited during the crawl.
	 */
	public static Collection<String> crawl(String seedURL) {
		// TODO implement me
		return null;
	}

	private void write(String line){
		System.out.println(line);
		try {
			writer.write(line + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}


