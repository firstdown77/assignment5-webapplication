package coreservlets;

import java.awt.geom.Ellipse2D;
import java.util.HashSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import servlets.DatabaseMethods;
import dbObjects.Report;
/**
 * Resource which has only one representation.
 *
 */
public class SearchResource extends ServerResource {
	@Get
	public String represent() throws JSONException {
		Double lat = Double.parseDouble(getQueryValue("lat"));
		Double lon = Double.parseDouble(getQueryValue("lon"));
		Double dist = Double.parseDouble(getQueryValue("dist"));
		Ellipse2D e = new Ellipse2D.Double(lon-dist, lat-dist, dist * 2, dist * 2);
		DatabaseMethods db = new DatabaseMethods();
		HashSet<Report> hsr = db.getAllReports();
		HashSet<Report> toReturn = new HashSet<Report>();
		for (Report r : hsr) {
			if (e.contains(r.getLongitude(), r.getLatitude())) {
				toReturn.add(r);
			}
		}
		JSONObject finalJSON = new JSONObject();
		JSONArray reportsArray = new JSONArray();
		for (Report r : toReturn) {
			JSONObject currReport = new JSONObject();
			currReport.put("user", r.getUser());
			currReport.put("content", r.getTextcontent());
			JSONObject geometryObject = new JSONObject();
			geometryObject.put("type", ((r.getRadius() != null && r.getRadius() > 0) ? "Circle" : "Point"));
			JSONArray coordinatesArray = new JSONArray();
			coordinatesArray.put(r.getLatitude());
			coordinatesArray.put(r.getLongitude());
			geometryObject.put("coordinates", coordinatesArray);
			currReport.put("geometry", geometryObject);
			currReport.put("picture", r.getFilename());
			currReport.put("title", r.getTitle());
			currReport.put("id", r.get_id());
			reportsArray.put(currReport);
		}
		finalJSON.put("reports", reportsArray);
		return finalJSON.toString(2);
	}
}