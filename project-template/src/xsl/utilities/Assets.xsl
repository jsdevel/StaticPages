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

   <xsl:template match="a:image[@src]">
      <xsl:variable name="path" select="concat('images/', @src)"/>
      <xsl:value-of select="assets:transferAsset($path)"/>
      <img src="{$assetPrefixInBrowser}{$path}">
         <xsl:apply-templates select="@*[not(local-name() = 'src')]"/>
      </img>
   </xsl:template>

   <xsl:template match="a:css[@href]">
      <xsl:variable name="path" select="concat('css/', @href, '.css')"/>
      <xsl:value-of select="assets:validateAssetReference($path)"/>
      <xsl:value-of select="assets:transferAsset($path)"/>
      <link href="{$assetPrefixInBrowser}{$path}" rel="stylesheet" type="text/css">
         <xsl:apply-templates select="@*[
            not(local-name() = 'rel' or local-name() = 'type' or local-name() = 'href')
         ]"/>
      </link>
   </xsl:template>

   <xsl:template match="a:js[@src]">
      <xsl:variable name="path" select="concat('js/', @src, '.js')"/>
      <xsl:value-of select="assets:validateAssetReference($path)"/>
      <xsl:value-of select="assets:transferAsset($path)"/>
      <script src="{$assetPrefixInBrowser}{$path}">
         <xsl:apply-templates select="@*[not(local-name() = 'src')]"/>
      </script>
   </xsl:template>


   <xsl:template match="a:pageLink[@src]">
      <xsl:value-of select="assets:validatePageReference(@src)"/>

      <a href="{$assetPrefixInBrowser}{string:replaceAll(@src, '%', '%25')}.html">
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

   <xsl:template match="a:include[@asset]">
      <xsl:value-of select="string(assets:getAsset(@asset))" disable-output-escaping="yes"/>
   </xsl:template>

   <xsl:template match="a:style[@href]">
      <style type="text/css">
         <xsl:value-of select="string(assets:getAsset(@href))" disable-output-escaping="yes"/>
      </style>
   </xsl:template>

   <xsl:template match="a:script[@href]">
      <script>
         <xsl:value-of select="string(assets:getAsset(@href))" disable-output-escaping="yes"/>
      </script>
   </xsl:template>

   <!-- views -->
   <xsl:template match="a:view">
      <xsl:variable name="viewPath" select="assets:getViewPath(text())"/>
      <xsl:apply-templates select="document($viewPath)/d:view"/>
   </xsl:template>

   <xsl:template match="d:view">
      <xsl:apply-templates/>
   </xsl:template>

   <xsl:template match="a:phrase">
      <xsl:apply-templates/>
   </xsl:template>

</xsl:stylesheet>
