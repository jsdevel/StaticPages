<?xml version="1.0" encoding="UTF-8"?>
<!--
   Copyright 2012 Joseph Spencer.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
-->
<xsl:stylesheet version="1.0"
   xmlns:a="assets"
   xmlns:d="default"
   xmlns:string="java.lang.String"
   xmlns:groupedAsset="com.spencernetdevelopment.GroupedAssetTransaction"
   xmlns:assets="com.spencernetdevelopment.xsl.Assets"
   xmlns:pp="com.spencernetdevelopment.xsl.ProjectPaths"
   exclude-result-prefixes="a d string assets pp groupedAsset"
   xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

   <xsl:param name="assetPrefixInBrowser"/>
   <xsl:param name="pagePath"/>

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
      <xsl:value-of select="assets:transferCSS(
            @src,
            @compress
         )"/>
      <link href="{$assetPrefixInBrowser}/{assets:getCSSPath(@src)}"
            rel="stylesheet" type="text/css"
      >
         <xsl:apply-templates select="@*[
            local-name() != 'rel' and
            local-name() != 'type' and
            local-name() != 'href' and
            local-name() != 'compress' and
            local-name() != 'src'
         ]"/>
      </link>
   </xsl:template>

   <xsl:template match="a:externalLink[@src]">
      <xsl:value-of select="assets:validateExternalURL(@src)"/>

      <a href="{@src}">
         <xsl:apply-templates select="@*[
            local-name() != 'href' and
            local-name() != 'src' and
            local-name() != 'name'
         ]"/>
         <xsl:choose>
            <xsl:when test="count(node()) = 0 and not(@name)">
               <xsl:value-of select="@src"/>
            </xsl:when>
            <xsl:when test="@name and count(node()) = 0">
               <xsl:value-of select="@name"/>
            </xsl:when>
            <xsl:otherwise>
               <xsl:apply-templates select="node()"/>
            </xsl:otherwise>
         </xsl:choose>
      </a>
   </xsl:template>

   <xsl:template match="a:group[@type = 'js' or @type = 'css']">
      <xsl:variable name="group" select="groupedAsset:new(
            @type,
            assets:getEnableCompression(@compress)
         )"/>
      <xsl:for-each select="d:url">
         <xsl:value-of select="groupedAsset:addURL($group, text())"/>
      </xsl:for-each>
      <xsl:value-of select="groupedAsset:close($group)"/>
      <xsl:variable name="identifier"
                    select="groupedAsset:getIdentifier($group)"/>

      <xsl:value-of select="assets:buildGroupedAsset($group)"/>
      <xsl:choose>
         <xsl:when test="@type = 'css'">
            <xsl:variable name="cssPath"
                          select="assets:getCSSPath($identifier)"/>
            <link href="{$assetPrefixInBrowser}/{$cssPath}" rel="stylesheet"
                  type="text/css"
            >
               <xsl:apply-templates select="@*[
                  local-name() != 'rel' and
                  local-name() != 'type' and
                  local-name() != 'href' and
                  local-name() != 'compress' and
                  local-name() != 'src'
               ]"/>
            </link>
         </xsl:when>
         <xsl:otherwise>
            <xsl:variable name="jsPath"
                          select="assets:getJSPath($identifier)"/>
            <script src="{$assetPrefixInBrowser}/{$jsPath}"
                    type="text/javascript"
            >
               <xsl:apply-templates select="@*[
                     local-name() != 'src' and
                     local-name() != 'type' and
                     local-name() != 'compress'
                  ]"/>
            </script>
         </xsl:otherwise>
      </xsl:choose>
   </xsl:template>

   <xsl:template match="a:image[@src]">
      <xsl:variable name="path" select="assets:getImagePath(@src)"/>
      <xsl:value-of select="assets:transferImage(
         assets:getCleanImagePath(@src)
      )"/>
      <img src="{$assetPrefixInBrowser}/{$path}">
         <xsl:apply-templates select="@*[local-name() != 'src']"/>
      </img>
   </xsl:template>

   <xsl:template match="a:include[@asset]">
      <xsl:value-of select="string(assets:getAsset(@asset))"
                    disable-output-escaping="yes"/>
   </xsl:template>

   <xsl:template match="a:js[@src]">
      <xsl:value-of select="assets:transferJS(
         @src,
         @compress
      )"/>
      <script src="{$assetPrefixInBrowser}/{assets:getJSPath(@src)}">
         <xsl:apply-templates
            select="@*[local-name() != 'src' and local-name() != 'compress']"/>
      </script>
   </xsl:template>

   <xsl:template match="a:pageLink[@src]">
      <xsl:value-of select="assets:validatePageReference(@src)"/>
      <xsl:variable name="referencedPagePath"
                    select="concat(pp:getXmlPagesPath(), '/', @src, '.xml')"
         />
      <xsl:variable name="referencedPageDocument"
                    select="document($referencedPagePath)/d:page"
         />
      <xsl:variable name="rewritePath"
                    select="$referencedPageDocument/d:seo/d:rewrites/d:url[
                           @default
                        ][1]/text()
                     "/>
      <xsl:if test="@frag">
         <xsl:value-of select="assets:validateFragmentReference(@src, @frag)"/>
      </xsl:if>

      <xsl:variable name="frag">
         <xsl:if test="@frag">
            <xsl:value-of select="concat('#', @frag)"/>
         </xsl:if>
      </xsl:variable>

      <a>
         <xsl:apply-templates select="@*[
            local-name() != 'class' and
            local-name() != 'frag' and
            local-name() != 'href' and
            local-name() != 'src' and
            local-name() != 'name'
         ]"/>
         <xsl:attribute name="href">
            <xsl:choose>
               <xsl:when
                  test="string($rewritePath) != ''">
                  <xsl:value-of select="concat(
                        $assetPrefixInBrowser,
                        assets:getNormalizedRewritePath($rewritePath)
                     )"/>
               </xsl:when>
               <xsl:otherwise>
                  <xsl:value-of select="concat(
                        $assetPrefixInBrowser,
                        '/',
                        string:replaceAll(@src, '%', '%25'),
                        '.html'
                     )"/>
               </xsl:otherwise>
            </xsl:choose>
            <xsl:value-of select="$frag"/>
         </xsl:attribute>
         <xsl:variable name="isCurrentPage"
                       select="string:endsWith(
                           $pagePath,
                           concat(@src,'.xml'))
                        "/>
         <xsl:if test="@class or $isCurrentPage">
            <xsl:attribute name="class">
               <xsl:if test="@class">
                  <xsl:value-of select="@class"/>
               </xsl:if>
               <xsl:if test="$isCurrentPage"> selected</xsl:if>
            </xsl:attribute>
         </xsl:if>
         <xsl:choose>
            <xsl:when test="count(node()) = 0 and not(@name)">
               <xsl:value-of select="string:replaceAll(@src, '%2F', '/')"/>
            </xsl:when>
            <xsl:when test="@name and count(node()) = 0">
               <xsl:value-of select="@name"/>
            </xsl:when>
            <xsl:otherwise>
               <xsl:apply-templates select="node()"/>
            </xsl:otherwise>
         </xsl:choose>
      </a>
   </xsl:template>

   <xsl:template match="a:phrase">
      <xsl:apply-templates/>
   </xsl:template>

   <xsl:template match="a:script[@src]">
      <script>
         <xsl:value-of select="assets:getJS(
            @src,
            @compress
         )" disable-output-escaping="yes"/>
      </script>
   </xsl:template>

   <xsl:template match="a:style[@src]">
      <style type="text/css">
         <xsl:value-of select="assets:getCSS(
            @src,
            @compress
         )" disable-output-escaping="yes"/>
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
