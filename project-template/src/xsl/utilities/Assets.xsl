<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
   xmlns:a="assets"
   xmlns:d="default"
   xmlns:string="java.lang.String"
   xmlns:assets="com.spencernetdevelopment.xsl.Assets"
   xmlns:urlutils="com.spencernetdevelopment.URLUtils"
   exclude-result-prefixes="a d string assets"
   xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

   <xsl:template match="a:image[@src]">
      <xsl:value-of select="assets:assetReference(@src)"/>
      <img>
         <xsl:apply-templates select="@*"/>
      </img>
   </xsl:template>

   <xsl:template match="a:pageLink[@src]">
      <xsl:value-of select="assets:validatePageReference(@src)"/>

      <a href="{string:replaceAll(@src, '%', '%25')}.html">
         <xsl:apply-templates select="@*[name() != 'href' or name() != 'src' or name() != 'name' ]"/>
         <xsl:choose>
            <xsl:when test="not(@name)">
               <xsl:value-of select="string:replaceAll(@src, '%2F', '/')"/>
            </xsl:when>
            <xsl:otherwise>
               <xsl:value-of select="@name"/>
            </xsl:otherwise>
         </xsl:choose>
      </a>
   </xsl:template>

   <!-- views -->
   <xsl:template match="a:view">
      <xsl:variable name="viewPath" select="assets:getViewPath(text())"/>
      <xsl:apply-templates select="document($viewPath)/d:view"/>
   </xsl:template>

   <xsl:template match="d:view">
      <xsl:apply-templates/>
   </xsl:template>

</xsl:stylesheet>
