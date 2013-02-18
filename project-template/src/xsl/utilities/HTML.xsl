<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet
   exclude-result-prefixes="d"
   xmlns:d="default"
   xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
   version="1.0"
>
   <xsl:template name="HTML5Doctype">
      <xsl:text disable-output-escaping="yes">&lt;!DOCTYPE html&gt;</xsl:text>
   </xsl:template>
   <!-- reset -->
   <xsl:template match="*"></xsl:template>
   <xsl:template match="d:*"></xsl:template>

   <!-- HTML -->
   <xsl:template match="d:h1"><h1><xsl:apply-templates select="@*"/><xsl:apply-templates/></h1></xsl:template>
   <xsl:template match="d:h2"><h2><xsl:apply-templates select="@*"/><xsl:apply-templates/></h2></xsl:template>
   <xsl:template match="d:h3"><h3><xsl:apply-templates select="@*"/><xsl:apply-templates/></h3></xsl:template>
   <xsl:template match="d:h4"><h4><xsl:apply-templates select="@*"/><xsl:apply-templates/></h4></xsl:template>
   <xsl:template match="d:h5"><h5><xsl:apply-templates select="@*"/><xsl:apply-templates/></h5></xsl:template>
   <xsl:template match="d:p"><p><xsl:apply-templates select="@*"/><xsl:apply-templates/></p></xsl:template>
   <xsl:template match="d:a"><a><xsl:apply-templates select="@*"/><xsl:apply-templates/></a></xsl:template>
   <xsl:template match="d:div"><div><xsl:apply-templates select="@*"/><xsl:apply-templates/></div></xsl:template>
   <xsl:template match="d:span"><span><xsl:apply-templates select="@*"/><xsl:apply-templates/></span></xsl:template>
   <xsl:template match="d:strong"><strong><xsl:apply-templates select="@*"/><xsl:apply-templates/></strong></xsl:template>
   <xsl:template match="d:pre"><pre><xsl:apply-templates select="@*"/><xsl:apply-templates mode="pre"/></pre></xsl:template>
   <xsl:template match="d:pre[@preserve]"><xsl:copy-of select="."/></xsl:template>
   <xsl:template match="d:code"><code><xsl:apply-templates select="@*"/><xsl:apply-templates/></code></xsl:template>
   <xsl:template match="d:button"><button><xsl:apply-templates select="@*"/><xsl:apply-templates/></button></xsl:template>
   <xsl:template match="d:br"><br/></xsl:template>
   <xsl:template match="d:b"><b><xsl:apply-templates/></b></xsl:template>
   <xsl:template match="d:i"><i><xsl:apply-templates/></i></xsl:template>
   <xsl:template match="d:textarea"><textarea><xsl:apply-templates select="@*"/><xsl:apply-templates mode="pre"/></textarea></xsl:template>
   <xsl:template match="d:video"><video><xsl:apply-templates select="@*"/><xsl:apply-templates/></video></xsl:template>
   <xsl:template match="d:source"><source><xsl:apply-templates select="@*"/><xsl:apply-templates/></source></xsl:template>
   <xsl:template match="d:ul"><ul><xsl:apply-templates select="@*"/><xsl:apply-templates/></ul></xsl:template>
   <xsl:template match="d:ol"><ol><xsl:apply-templates select="@*"/><xsl:apply-templates/></ol></xsl:template>
   <xsl:template match="d:li"><li><xsl:apply-templates select="@*"/><xsl:apply-templates/></li></xsl:template>
   <xsl:template match="d:script"><script><xsl:apply-templates select="@*"/><xsl:value-of select="." disable-output-escaping="yes"/></script></xsl:template>
   <xsl:template match="d:style"><style><xsl:apply-templates select="@*"/><xsl:value-of select="." disable-output-escaping="yes"/></style></xsl:template>
   <xsl:template match="text()"><xsl:value-of select="normalize-space(.)"/></xsl:template>
   <xsl:template match="text()" mode="pre"><xsl:value-of select="."/></xsl:template>
   <xsl:template match="*" mode="pre">&lt;<xsl:value-of select="local-name()"/>&gt;<xsl:apply-templates mode="pre"/>&lt;<xsl:value-of select="local-name()"/>&gt;</xsl:template>

   <!--ATTRIBUTES-->
   <xsl:template match="@*">
      <xsl:copy-of select="."/>
   </xsl:template>
</xsl:stylesheet>
