<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
   xmlns:a="assets"
   xmlns:d="default"
   xmlns:string="java.lang.String"
   xmlns:assets="com.spencernetdevelopment.xsl.Assets"
   xmlns:urlutils="com.spencernetdevelopment.URLUtils"
   exclude-result-prefixes="a d string assets urlutils"
   xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

   <xsl:param name="assetPrefixInBrowser"/>

   <xsl:template match="a:*">
      <xsl:message terminate="yes">
   Unknown asset element 'a:<xsl:value-of select="local-name(.)"/>'.
   The following attributes were found on this element:
         <xsl:for-each select="@*">
            <xsl:value-of select="concat(local-name(.), '=', .)"/>
         </xsl:for-each>
      </xsl:message>
   </xsl:template>

   <xsl:template match="a:css[@src]">
      <xsl:variable name="path" select="concat('css/', @src, '.css')"/>
      <xsl:variable name="foo" select="assets:transferCSS($path, not(contains(@compress, 'false')))"/>
      <link href="{$assetPrefixInBrowser}/{$path}" rel="stylesheet" type="text/css">
         <xsl:apply-templates select="@*[
            not(local-name() = 'rel' or local-name() = 'type' or local-name() = 'href' or local-name() = 'compress' or local-name() = 'src')
         ]"/>
      </link>
   </xsl:template>

   <xsl:template match="a:image[@src]">
      <xsl:variable name="path" select="concat('images/', @src)"/>
      <xsl:value-of select="assets:transferAsset($path)"/>
      <img src="{$assetPrefixInBrowser}/{$path}">
         <xsl:apply-templates select="@*[not(local-name() = 'src')]"/>
      </img>
   </xsl:template>

   <xsl:template match="a:include[@asset]">
      <xsl:value-of select="string(assets:getAsset(@asset))" disable-output-escaping="yes"/>
   </xsl:template>

   <xsl:template match="a:js[@src]">
      <xsl:variable name="path" select="concat('js/', @src, '.js')"/>
      <xsl:value-of select="assets:transferJS($path, not(contains(@compress, 'false')))"/>
      <script src="{$assetPrefixInBrowser}/{$path}">
         <xsl:apply-templates select="@*[local-name() != 'src' and local-name() != 'compress']"/>
      </script>
   </xsl:template>


   <xsl:template match="a:pageLink[@src]">
      <xsl:value-of select="assets:validatePageReference(@src)"/>

      <a href="{$assetPrefixInBrowser}/{string:replaceAll(@src, '%', '%25')}.html">
         <xsl:apply-templates select="@*[name() != 'href' or name() != 'src' or name() != 'name' or name() != 'class']"/>
         <xsl:variable name="isCurrentPage" select="string:endsWith($pagePath, concat(@src,'.xml'))"/>
         <xsl:if test="@class or $isCurrentPage">
            <xsl:attribute name="class">
               <xsl:if test="@class">
                  <xsl:value-of select="@class"/>
               </xsl:if>
               <xsl:if test="$isCurrentPage"> selected</xsl:if>
            </xsl:attribute>
         </xsl:if>
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

   <xsl:template match="a:phrase">
      <xsl:apply-templates/>
   </xsl:template>

   <xsl:template match="a:script[@src]">
      <xsl:variable name="path" select="concat('js/', @src, '.js')"/>
      <script>
         <xsl:value-of select="assets:getJS($path, not(contains(@compress, 'false')))" disable-output-escaping="yes"/>
      </script>
   </xsl:template>

   <xsl:template match="a:style[@src]">
      <xsl:variable name="path" select="concat('css/', @src, '.css')"/>
      <style type="text/css">
         <xsl:value-of select="assets:getCSS($path, not(contains(@compress, 'false')))" disable-output-escaping="yes"/>
      </style>
   </xsl:template>

   <xsl:template match="a:transfer[@src]">
      <xsl:value-of select="assets:transferAsset(@src)"/>
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
