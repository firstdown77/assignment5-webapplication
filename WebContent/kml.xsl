<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="/">
  <kml xmlns="http://www.opengis.net/kml/2.2">
    <xsl:for-each select="disasterevacuation/reports/report">
    <Placemark>
    	<name><xsl:value-of select="title"/></name>
    	<description><xsl:value-of select="textcontent"/></description>
      	<Point>
      		<coordinates><xsl:value-of select="longitude"/>, <xsl:value-of select="latitude"/></coordinates>
      	</Point>
    </Placemark>
    </xsl:for-each>
  </kml>
</xsl:template>

</xsl:stylesheet>