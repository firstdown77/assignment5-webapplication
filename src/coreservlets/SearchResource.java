package coreservlets;

import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.HashSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import servlets.DatabaseMethods;
import dbObjects.Event;
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
		ArrayList<Report> hsr = db.getAllReports();
		HashSet<Report> toReturn = new HashSet<Report>();
		for (Report r : hsr) {
			if (Math.acos(Math.sin(lat) * Math.sin(r.getLatitude()) + Math.cos(lat)
					* Math.cos(r.getLatitude()) * Math.cos(r.getLongitude() - (lon)))
					* 6371 * 1000 <= dist) {
				toReturn.add(r);
			}
		}
		ArrayList<Event> hse = db.getAllEvents();
		HashSet<Event> toReturnEvents = new HashSet<Event>();

		for (Event ev : hse) {
			if (Math.acos(Math.sin(lat) * Math.sin(ev.getLatitude()) + Math.cos(lat)
					* Math.cos(ev.getLatitude()) * Math.cos(ev.getLongitude() - (lon)))
					* 6371 * 1000 <= dist) {
				toReturnEvents.add(ev);
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
		JSONArray eventsArray = new JSONArray();
		for (Event ev : toReturnEvents) {
			JSONObject currEvent = new JSONObject();
			currEvent.put("id", ev.get_id());
			JSONObject geometryObject = new JSONObject();
			geometryObject.put("type", "Point");
			JSONArray coordinatesArray = new JSONArray();
			coordinatesArray.put(ev.getLatitude());
			coordinatesArray.put(ev.getLongitude());
			geometryObject.put("coordinates", coordinatesArray);
			currEvent.put("geometry", geometryObject);
			currEvent.put("estimatedTime", ev.getDate());
			currEvent.put("meanOfEvacuation", ev.getEvacuationMeans());
			currEvent.put("capacity", ev.getCapacity());
			int registeredCount = db.getRegisteredCount(ev.get_id());
			currEvent.put("registrationCount", registeredCount);
			eventsArray.put(currEvent);
		}
		finalJSON.put("evacuationEvents", eventsArray);
		return finalJSON.toString(2);
	}

	public static float distFrom(float lat1, float lng1, float lat2, float lng2) {
		double earthRadius = 6371; //kilometers
		double dLat = Math.toRadians(lat2-lat1);
		double dLng = Math.toRadians(lng2-lng1);
		double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
				Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
				Math.sin(dLng/2) * Math.sin(dLng/2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
		float dist = (float) (earthRadius * c);

		return dist;
	}
}