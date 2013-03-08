<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
                xmlns:r="resources"
                xmlns:a="assets"
                xmlns:assets="com.spencernetdevelopment.xsl.Assets"
                xmlns:pp="com.spencernetdevelopment.xsl.ProjectPaths"
                exclude-result-prefixes="r a"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

   <xsl:template match="r:phrase">
      <xsl:message>
Warning, didn't find required attributes on phrase.
Required attributes are: id
      </xsl:message>
   </xsl:template>

   <xsl:template match="r:phrase[@id]">
      <xsl:variable name="rp" select="pp:getXmlResourcesPath()"/>
      <xsl:choose>
         <xsl:when test="assets:assertXmlResourceExists('phrases.xml')">
            <xsl:variable name="id" select="@id"/>
            <xsl:variable name="value" select="document(concat($rp, '/phrases.xml'))/a:phrases/a:phrase[@id=$id]"/>
            <xsl:choose>
               <xsl:when test="string($value) = ''">
                  <xsl:message>
phrases.xml:  Couldn't find phrase with id: <xsl:value-of select="$id"/>
                  </xsl:message>
               </xsl:when>
               <xsl:otherwise>
                  <xsl:value-of select="$value"/>
               </xsl:otherwise>
            </xsl:choose>
         </xsl:when>
         <xsl:otherwise>
            <xsl:message>
Couldn't find <xsl:value-of select="$rp"/>/phrases.xml.
            </xsl:message>

         </xsl:otherwise>
      </xsl:choose>
   </xsl:template>


</xsl:stylesheet>
