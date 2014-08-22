package coreservlets;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/search")
public class Search {
	
	private String doSearch() {
		return null;
	}	
	
	// Called for plain text requests.
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String plainTextSearch() {
		String data = doSearch();
		return "Whoa Text.";		
	}

	// Called for XML requests.
	@GET
	@Produces(MediaType.TEXT_XML)
	public String xmlSearch() {
		String data = doSearch();
		return "Whoa XML.";		
	}
	
	// Called for HTML requests.
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String htmlSearch() {
		String data = doSearch();
		return "Whoa HTML.";		
	}
	
}
