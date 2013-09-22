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
   xmlns:d="default"
   xmlns:e="extension"
   xmlns:string="java.lang.String"
   xmlns:BC="com.spencernetdevelopment.Breadcrumb"
   xmlns:BCS="com.spencernetdevelopment.Breadcrumbs"
   xmlns:E="com.spencernetdevelopment.Extensions"
   xmlns:LV="com.spencernetdevelopment.LinkValidator"
   exclude-result-prefixes="BC BCS d e E LV string"
   xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

   <xsl:param name="assetPrefixInBrowser"/>
   <xsl:param name="domainRelativePagePath"/>
   <xsl:param name="xmlPagePath"/>
   <xsl:param name="E"/>
   <xsl:param name="LV"/>

   <xsl:template match="e:breadcrumbs">
      <xsl:variable name="crumbs" select="E:makeBreadcrumbs($E,
         $assetPrefixInBrowser, $domainRelativePagePath
      )"/>

      <xsl:if test="BCS:isEmpty($crumbs) = false()">
         <xsl:apply-templates mode="Extensions_buildCrumbs">
            <xsl:with-param name="crumbs" select="$crumbs"/>
         </xsl:apply-templates>
      </xsl:if>
   </xsl:template>

   <xsl:template match="text()" mode="Extensions_buildCrumbs">
      <xsl:apply-templates/>
   </xsl:template>
   <xsl:template match="text()" mode="Extensions_buildCrumb">
      <xsl:apply-templates/>
   </xsl:template>

   <xsl:template match="d:*" mode="Extensions_buildCrumbs">
      <xsl:param name="crumbs"/>

      <xsl:value-of select="concat( '&lt;', local-name())"
                     disable-output-escaping="yes"/>
      <xsl:for-each select="@*">
         <xsl:value-of select="concat(
                              ' ', local-name(), '=&#34;', ., '&#34;')"
                        disable-output-escaping="yes"/>
      </xsl:for-each>
      <xsl:choose>
         <xsl:when test="count(*) > 0">
            <xsl:text disable-output-escaping="yes">&gt;</xsl:text>
            <xsl:apply-templates mode="Extensions_buildCrumbs">
               <xsl:with-param name="crumbs" select="$crumbs"/>
            </xsl:apply-templates>
            <xsl:value-of select="concat('&lt;/', local-name(), '&gt;')"
                           disable-output-escaping="yes"/>
         </xsl:when>
         <xsl:otherwise>
            <xsl:value-of select="'/&gt;'" disable-output-escaping="yes"/>
         </xsl:otherwise>
      </xsl:choose>
   </xsl:template>

   <xsl:template match="e:crumb" mode="Extensions_buildCrumbs">
      <xsl:param name="crumbs"/>

      <xsl:if test="BCS:isEmpty($crumbs) = false()">
         <xsl:variable name="crumb" select="BCS:take($crumbs)"/>
         <xsl:apply-templates mode="Extensions_buildCrumb">
            <xsl:with-param name="crumb" select="$crumb"/>
            <xsl:with-param name="isLast" select="BCS:isEmpty($crumbs)"/>
         </xsl:apply-templates>
         <xsl:apply-templates select="." mode="Extensions_buildCrumbs">
            <xsl:with-param name="crumbs" select="$crumbs"/>
         </xsl:apply-templates>
      </xsl:if>
   </xsl:template>

   <xsl:template match="d:*" mode="Extensions_buildCrumb">
      <xsl:param name="crumb"/>
      <xsl:param name="isLast"/>

      <xsl:value-of select="concat( '&lt;', local-name())"
                     disable-output-escaping="yes"/>
      <xsl:for-each select="@*">
         <xsl:value-of select="concat(
                              ' ', local-name(), '=&#34;', ., '&#34;')"
                        disable-output-escaping="yes"/>
      </xsl:for-each>
      <xsl:choose>
         <xsl:when test="count(*) > 0">
            <xsl:text disable-output-escaping="yes">&gt;</xsl:text>
            <xsl:apply-templates mode="Extensions_buildCrumb">
               <xsl:with-param name="crumb" select="$crumb"/>
               <xsl:with-param name="isLast" select="$isLast"/>
            </xsl:apply-templates>
            <xsl:value-of select="concat('&lt;/', local-name(), '&gt;')"
                           disable-output-escaping="yes"/>
         </xsl:when>
         <xsl:otherwise>
            <xsl:value-of select="'/&gt;'" disable-output-escaping="yes"/>
         </xsl:otherwise>
      </xsl:choose>
   </xsl:template>

   <xsl:template match="e:link" mode="Extensions_buildCrumb">
      <xsl:param name="crumb"/>
      <xsl:param name="isLast" select="false()"/>

      <xsl:value-of select="LV:validatePageReference($LV,
         BC:getXmlPagesRelativePath($crumb))"/>

      <xsl:choose>
         <xsl:when test="$isLast">
            <xsl:value-of select="BC:getDisplayName($crumb)"/>
         </xsl:when>
         <xsl:otherwise>
            <a href="{BC:getDomainRelativeLink($crumb)}">
               <xsl:value-of select="BC:getDisplayName($crumb)"/>
            </a>
         </xsl:otherwise>
      </xsl:choose>
   </xsl:template>
</xsl:stylesheet>
