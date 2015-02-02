package ir.assignments.three;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class Controller {
	public static void main(String[] args) throws Exception{
		String crawlStorageFolder = "C:/cs121hw3";
		final int politenessDelay = 300;
		final String userAgentString = "UCI Inf141-CS121 crawler 95812659 30062256";
		final int numberOfCrawlers = 8;
		
		
		CrawlConfig config = new CrawlConfig();
		config.setCrawlStorageFolder(crawlStorageFolder);
		config.setPolitenessDelay(politenessDelay);
		config.setUserAgentString(userAgentString);
		//config.setResumableCrawling(true);
		
		System.out.println(config.toString());
		
		// Instantiate the controller for web crawl
		PageFetcher pageFetcher = new PageFetcher(config);
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
		
	
		CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
		// Add seed URLs
		controller.addSeed("http://www.ics.uci.edu/");
		
		// Start crawling
		controller.start(Crawler.class, numberOfCrawlers);	
	}
}