<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
                xmlns:p="phrases"
                xmlns:d="default"
                xmlns:assets="com.spencernetdevelopment.xsl.Assets"
                xmlns:pp="com.spencernetdevelopment.xsl.ProjectPaths"
                exclude-result-prefixes="p d assets pp"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

   <xsl:template match="p:*">
      <xsl:variable name="rp" select="pp:getXmlResourcesPath()"/>
      <xsl:choose>
         <xsl:when test="assets:assertXmlResourceExists('phrases.xml')">
            <xsl:variable name="id" select="local-name()"/>
            <xsl:variable name="phrase" select="document(concat($rp, '/phrases.xml'))/d:phrases/d:phrase[@id=$id]"/>
            <xsl:choose>
               <xsl:when test="string($phrase) = ''">
                  <xsl:message>
phrases.xml:  Couldn't find phrase with id: <xsl:value-of select="$id"/>
                  </xsl:message>
               </xsl:when>
               <xsl:otherwise>
                  <span>
                     <xsl:apply-templates select="@*[not(local-name() = 'id' or local-name() = 'a' or local-name() = 'b')]"/>
                     <xsl:if test="@b">&#160;</xsl:if>
                        <xsl:apply-templates select="$phrase"/>
                     <xsl:if test="@a">&#160;</xsl:if>
                  </span>
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
