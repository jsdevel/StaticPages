<?xml version="1.0" encoding="UTF-8"?>
<!--
    Description:
      This is the default page for all xml pages.
-->
<xsl:stylesheet version="1.0"
   xmlns:d="default"
   xmlns:saxon="http://icl.com/saxon"
   exclude-result-prefixes="d saxon"
   xmlns:xsl="http://www.w3.org/1999/XSL/Transform" >

   <xsl:import href="../utilities/HTML.xsl"/>
   <xsl:import href="../utilities/Functions.xsl"/>
   <xsl:import href="../utilities/Assets.xsl"/>

   <xsl:output method="html" indent="no" saxon:omit-meta-tag="yes"/>

   <xsl:template match="/">
      <xsl:variable name="numberOfPages" select="count(//d:page)"/>

      <xsl:choose>
         <xsl:when test="$numberOfPages = 0">
            <xsl:message terminate="yes">
               There must be at least one page defined with xmlns="default".
            </xsl:message>
         </xsl:when>
         <xsl:when test="$numberOfPages &gt; 1">
            <xsl:message terminate="yes">
               Only one page may be defined.
            </xsl:message>
         </xsl:when>
         <xsl:when test="count(d:page) = 0">
            <xsl:message terminate="yes">
               page with xmlns="default" must be the root element.
            </xsl:message>
         </xsl:when>
         <xsl:otherwise>
            <xsl:apply-templates select="d:page"/>
         </xsl:otherwise>
      </xsl:choose>
   </xsl:template>

   <xsl:template match="d:page">
      <xsl:call-template name="HTML5Doctype"/>
      <html class="no-js">
         <head>
            <meta charset="utf-8"/>
            <xsl:apply-templates select="d:seo" mode="seo"/>
            <xsl:apply-templates select="d:head" mode="head"/>
         </head>
         <body>
            <xsl:apply-templates/>
         </body>
      </html>
   </xsl:template>

</xsl:stylesheet>
