<%@page import="servlets.DatabaseMethods"%>
<%@ page import="java.util.*"%>
<%@ page import="javax.xml.transform.TransformerFactory"%>
<%@ page import="javax.xml.transform.stream.StreamSource"%>
<%@ page import="javax.xml.transform.stream.StreamResult"%>
<%@ page import="java.io.StringReader"%>
<%@ page import="java.io.StringWriter"%>
<%@ page import="javax.xml.transform.Transformer"%>
<%@ page import="dbObjects.*"%>
<%@ page language="java" contentType="text/html; charset=US-ASCII"
	pageEncoding="US-ASCII"%>

<?xml version="1.0" encoding="US-ASCII" ?>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<link rel="stylesheet" href="/assignment5-webapplication/css/bootstrap.min.css" type="text/css" />
<link rel="stylesheet" href="/assignment5-webapplication/css/view_report.css" type="text/css" />
<link rel="stylesheet" href="/assignment5-webapplication/css/header.css" type="text/css" />
<link rel="stylesheet" href="/assignment5-webapplication/css/googlemapcode.css" type="text/css" />
<link rel="stylesheet" href="/assignment5-webapplication/js/jquery-ui/jquery-ui.css"/>
<script src="/assignment5-webapplication/js/jquery-1.11.1.min.js"></script>
<script src="/assignment5-webapplication/js/jquery-ui/jquery-ui.js"></script>
<script src="/assignment5-webapplication/js/load_header.js"></script>
<script src="/assignment5-webapplication/js/continentCenterCoords.js"></script>
<script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?
key=AIzaSyA5VLYkZvLXln72Q2FaNEj6O3H2F0yZsVY"></script>
<script src="/assignment5-webapplication/js/geocoder.js"></script>
<script src="/assignment5-webapplication/js/jquery.validate.min.js"></script>
<script src="/assignment5-webapplication/js/mapselect.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII" />
<meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
<title>KML View</title>


</head>
<%

DatabaseMethods db = new DatabaseMethods();
String xml_s = "";
String kml_s = "";

try
{
	ArrayList<Report> reports = db.getAllReports();
	ArrayList<Event> events = db.getUpcomingEvents();
	StringBuilder sb = new StringBuilder();
	sb.append("<?xml version='1.0' encoding='UTF-8'?>\n");
	sb.append("<disasterevacuation>\n");
	sb.append("<reports>\n");
	for (Report r : reports)
	{
		sb.append("<report id='"+r.get_id()+"' username='"+r.getUser()+"'>");
		sb.append("<address>" + r.getAddress() + "</address>");
		sb.append("<longitude>" + r.getLongitude() + "</longitude>");
		sb.append("<latitude>" + r.getLatitude() + "</latitude>");
		sb.append("<radius>" + r.getRadius() + "</radius>");
		sb.append("<title>" + r.getTitle() + "</title>");
		sb.append("<textcontent>" + r.getTextcontent() + "</textcontent>");
		sb.append("</report>");
	}
	sb.append("</reports>\n");
	sb.append("<events>\n");
	for (Event e : events)
	{
		sb.append("<event id='"+e.get_id()+"' username='"+e.getUsername()+"' capacity='"+e.getCapacity()+"'>");
		sb.append("<longitude>" + e.getLongitude() + "</longitude>");
		sb.append("<latitude>" + e.getLatitude() + "</latitude>");
		sb.append("<evacuationmeans>" + e.getEvacuationMeans() + "</evacuationmeans>");
		sb.append("</event>");
	}
	sb.append("</events>\n");
	sb.append("</disasterevacuation>\n");
	
	
	// Use the static TransformerFactory.newInstance() method to instantiate 
	  // a TransformerFactory. The javax.xml.transform.TransformerFactory 
	  // system property setting determines the actual class to instantiate --
	  // org.apache.xalan.transformer.TransformerImpl.
		TransformerFactory tFactory = TransformerFactory.newInstance();
		
		StringReader xml_sr = new StringReader(sb.toString());
        StreamSource xml_ss = new StreamSource(xml_sr);
        StringWriter out_sw = new StringWriter();
        StreamResult out_sr = new StreamResult(out_sw);
        
		// Use the TransformerFactory to instantiate a Transformer that will work with  
		// the stylesheet you specify. This method call also processes the stylesheet
        // into a compiled Templates object.
        String type = request.getParameter("type");
        if (type == null)
        	type = "reports";
        String relativeWebPath;
        if (type.equals("reports"))
        	relativeWebPath = "kml.xsl";
        else
        	relativeWebPath = "kmlevents.xsl";
        
        String absoluteDiskPath = getServletContext().getRealPath(relativeWebPath);
        Transformer transformer = tFactory.newTransformer(new StreamSource(absoluteDiskPath));

		// Use the Transformer to apply the associated Templates object to an XML document
		// (foo.xml) and write the output to a file (foo.out).
		transformer.transform(xml_ss, out_sr);
		xml_s = sb.toString();
		kml_s = out_sw.toString();
}
catch (Exception exc)
{
	response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
}


%>
<body>
	<div id="header"></div>
	<div id="content-body-wrapper" class="content-body-wrapper">
    <div id="content-body" class="content-body">
	<div id="sidebar" class="sidebar"></div>
	<div id="content" class="content">
	<h2 class="text-center">KML View</h2>
	
<form id="kmlview" name="kmlview" action="viewkmlmap.jsp" method="post"
		class="text-center">
		<%if (request.getParameter("type").equals("line")){ %>
		 Your Location:
		<br />
		<div class="text-center">
			Address: <input type="text" name="address" id="address" onkeydown="if (event.keyCode == 13) {$('#btnsearch').click();return false;}"/> <button id="btnsearch" type="button" onclick='translateAddress($("#address").val(), setMapCoords )'>Search</button> 
			
		</div>
		<br />
	<div id="map-canvas"></div>
	<br/>
	
	<%} %>
		<p>XML:</p>
		<p>		<textarea id="xmls" class="code"><%=xml_s %></textarea></p>
		<p>Transformed KML:</p>
		<p><textarea id="kmls" class="code"><%=kml_s %> </textarea></p>
				<input type="hidden" name="latitude" id="latitude"/>
				<input type="hidden" name="longitude" id="longitude"/>
		

	</form>
	</div>
	</div>
	</div>
</body>
</html>