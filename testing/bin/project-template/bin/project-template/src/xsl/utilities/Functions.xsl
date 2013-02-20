<?xml version="1.0" encoding="UTF-8"?>
<!--
    Description:
      Provides basic functionality, to make the xml markup more powerful.
-->
<xsl:stylesheet version="1.0"
   xmlns:d="default"
   xmlns:fn="functions"
   xmlns:ffns="java:com.spencernetdevelopment.xsl.FileFunctions"
   exclude-result-prefixes="d fn ffns"
   xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

   <xsl:template match="fn:include[@asset]">
      <xsl:value-of select="string(ffns:getAsset(@asset))" disable-output-escaping="yes"/>
   </xsl:template>

   <xsl:template match="fn:outputCSS[@href]">
      <style type="text/css">
         <xsl:value-of select="string(ffns:getAsset(@href))" disable-output-escaping="yes"/>
      </style>
   </xsl:template>

   <xsl:template match="fn:outputJS[@href]">
      <script>
         <xsl:value-of select="string(ffns:getAsset(@href))" disable-output-escaping="yes"/>
      </script>
   </xsl:template>

</xsl:stylesheet>
