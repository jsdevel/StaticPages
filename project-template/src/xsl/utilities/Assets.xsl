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
   xmlns:AM="com.spencernetdevelopment.AssetManager"
   xmlns:AR="com.spencernetdevelopment.AssetResolver"
   xmlns:assets="com.spencernetdevelopment.xsl.Assets"
   xmlns:U="com.spencernetdevelopment.xsl.Utils"
   exclude-result-prefixes="a d string assets groupedAsset U AM AR"
   xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

   <xsl:param name="assetPrefixInBrowser"/>
   <xsl:param name="pagePath"/>
   <xsl:param name="AM"/>
   <xsl:param name="AR"/>

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
      <xsl:variable name="src"
                    select="AM:expandVariables($AM, @src)"/>
      <xsl:value-of select="assets:transferCSS(
            $src,
            @compress
         )"/>
      <link href="{$assetPrefixInBrowser}/{AR:getCSSPath($AR, $src)}"
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
      <xsl:variable name="src"
                    select="AM:expandVariables($AM, @src)"/>
      <xsl:value-of select="assets:validateExternalURL($src)"/>

      <a href="{$src}">
         <xsl:apply-templates select="@*[
            local-name() != 'href' and
            local-name() != 'src' and
            local-name() != 'name'
         ]"/>
         <xsl:choose>
            <xsl:when test="count(node()) = 0 and not(@name)">
               <xsl:value-of select="$src"/>
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

   <!-- TODO The groupdedAsset constructor is broken here.  XSL shouldn't have
   knowledge about configuration.  It should only be a view. -->
   <xsl:template match="a:group[@type = 'js' or @type = 'css']">
      <!-- BROKEN -->
      <xsl:variable name="group" select="groupedAsset:new(
            @type,
            U:getBoolean(@compress)
         )"/>
      <xsl:for-each select="d:url">
         <xsl:value-of select="groupedAsset:addURL(
            $group,
            AM:expandVariables($AM, text())
         )"/>
      </xsl:for-each>
      <xsl:value-of select="groupedAsset:close($group)"/>
      <xsl:variable name="identifier"
                    select="groupedAsset:getIdentifier($group)"/>

      <xsl:value-of select="assets:buildGroupedAsset($group)"/>
      <xsl:choose>
         <xsl:when test="@type = 'css'">
            <xsl:variable name="cssPath"
                          select="AR:getCSSPath($AR, $identifier)"/>
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
                          select="AR:getJSPath($AR, $identifier)"/>
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
      <xsl:variable name="src"
                    select="AM:expandVariables($AM, @src)"/>
      <xsl:variable name="path" select="AR:getImagePath($AR, $src)"/>
      <xsl:value-of select="assets:transferImage(
         AR:getCleanImagePath($AR, $src)
      )"/>
      <img src="{$assetPrefixInBrowser}/{$path}">
         <xsl:apply-templates select="@*[local-name() != 'src']"/>
      </img>
   </xsl:template>

   <xsl:template match="a:include[@asset]">
      <xsl:variable name="asset"
                    select="AM:expandVariables($AM, @asset)"/>
      <xsl:value-of select="string(assets:getAsset($asset))"
                    disable-output-escaping="yes"/>
   </xsl:template>

   <xsl:template match="a:js[@src]">
      <xsl:variable name="src"
                    select="AM:expandVariables($AM, @src)"/>
      <xsl:value-of select="assets:transferJS(
         $src,
         @compress
      )"/>
      <script src="{$assetPrefixInBrowser}/{AR:getJSPath($AR, $src)}">
         <xsl:apply-templates
            select="@*[local-name() != 'src' and local-name() != 'compress']"/>
      </script>
   </xsl:template>

   <xsl:template match="a:pageLink[@src]">
      <xsl:variable name="src"
                    select="AM:expandVariables($AM, @src)"/>
      <xsl:variable name="class"
                    select="AM:expandVariables($AM, @class)"/>
      <xsl:variable name="name"
                    select="AM:expandVariables($AM, @name)"/>

      <xsl:value-of select="assets:validatePageReference($src)"/>
      <xsl:variable name="referencedPageDocument"
                    select="document(AR:getPagePath($AR, $src))/d:page"
         />
      <xsl:variable name="rewritePath"
                    select="
                        AM:expandVariables($AM,
                           $referencedPageDocument/d:seo/d:rewrites/d:url[
                              @default
                           ][1]/text()
                        )
                     "/>
      <xsl:if test="@frag">
         <xsl:value-of select="assets:validateFragmentReference(
            $src,
            AM:expandVariables($AM, @frag)
         )"/>
      </xsl:if>

      <xsl:variable name="frag">
         <xsl:if test="@frag">
            <xsl:value-of select="concat(
               '#',
               AM:expandVariables($AM, @frag)
            )"/>
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
                        AR:getNormalizedRewritePath($AR, $rewritePath)
                     )"/>
               </xsl:when>
               <xsl:otherwise>
                  <xsl:value-of select="concat(
                        $assetPrefixInBrowser,
                        '/',
                        string:replaceAll($src, '%', '%25'),
                        '.html'
                     )"/>
               </xsl:otherwise>
            </xsl:choose>
            <xsl:value-of select="$frag"/>
         </xsl:attribute>
         <xsl:variable name="isCurrentPage"
                       select="string:endsWith(
                           $pagePath,
                           concat($src,'.xml'))
                        "/>
         <xsl:if test="@class or $isCurrentPage">
            <xsl:attribute name="class">
               <xsl:if test="@class">
                  <xsl:value-of select="$class"/>
               </xsl:if>
               <xsl:if test="$isCurrentPage"> selected</xsl:if>
            </xsl:attribute>
         </xsl:if>
         <xsl:choose>
            <xsl:when test="count(node()) = 0 and not(@name)">
               <xsl:value-of select="string:replaceAll($src, '%2F', '/')"/>
            </xsl:when>
            <xsl:when test="@name and count(node()) = 0">
               <xsl:value-of select="$name"/>
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
            AM:expandVariables($AM, @src),
            @compress
         )" disable-output-escaping="yes"/>
      </script>
   </xsl:template>

   <xsl:template match="a:style[@src]">
      <style type="text/css">
         <xsl:value-of select="assets:getCSS(
            AM:expandVariables($AM, @src),
            @compress
         )" disable-output-escaping="yes"/>
      </style>
   </xsl:template>

   <xsl:template match="a:transfer[@src]">
      <xsl:value-of select="assets:transferAsset(
         AM:expandVariables($AM, @src)
      )"/>
   </xsl:template>

   <!-- views -->
   <xsl:template match="a:view">
      <xsl:variable name="viewPath" select="AR:getViewPath(
         $AR,
         AM:expandVariables($AM, text())
      )"/>
      <xsl:apply-templates select="document($viewPath)/d:view"/>
   </xsl:template>

   <xsl:template match="d:view">
      <xsl:apply-templates/>
   </xsl:template>

</xsl:stylesheet>
