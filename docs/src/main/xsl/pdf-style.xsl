<?xml version='1.0'?> 
<xsl:stylesheet version="1.0" 
  xmlns:fo="http://www.w3.org/1999/XSL/Format"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  >

  <xsl:import href="classpath:/xslt/org/jboss/pdf.xsl"/>

  <xsl:param name="paper.type" select="'Letter'"/>
  <xsl:param name="double.sided">0</xsl:param>

  <xsl:param name="section.label.includes.component.label" select="0"/>
  <xsl:param name="body.font.master" select="10"/>

  <xsl:param name="body.font.family" select="'Liberation Sans'"/>
  <xsl:param name="monospace.font.family" select="'Liberation Mono'"/>
  <xsl:param name="sans.font.family" select="'Liberation Sans'"/>
  <xsl:param name="title.font.family" select="'Liberation Serif'"/>

  <xsl:attribute-set name="monospace.properties">
    <xsl:attribute name="font-size">90%</xsl:attribute>
    <xsl:attribute name="font-family">
      <xsl:value-of select="$monospace.font.family"/>
    </xsl:attribute>
  </xsl:attribute-set>

  <xsl:attribute-set name="formal.title.properties" use-attribute-sets="normal.para.spacing">
    <xsl:attribute name="font-weight">bold</xsl:attribute>
    <xsl:attribute name="font-size">
      <xsl:value-of select="$body.font.master * 0.9"></xsl:value-of>
      <xsl:text>pt</xsl:text>
    </xsl:attribute>
    <xsl:attribute name="hyphenate">false</xsl:attribute>
    <xsl:attribute name="space-after.minimum">0.0em</xsl:attribute>
    <xsl:attribute name="space-after.optimum">0.2em</xsl:attribute>
    <xsl:attribute name="space-after.maximum">0.4em</xsl:attribute>
  </xsl:attribute-set>

  <xsl:attribute-set name="list.block.spacing">
    <xsl:attribute name="space-before.optimum">1em</xsl:attribute>
    <xsl:attribute name="space-before.minimum">1ex</xsl:attribute>
    <xsl:attribute name="space-before.maximum">1em</xsl:attribute>
    <xsl:attribute name="space-after.optimum">0.1em</xsl:attribute>
    <xsl:attribute name="space-after.minimum">0.1em</xsl:attribute>
    <xsl:attribute name="space-after.maximum">0.1em</xsl:attribute>
  </xsl:attribute-set>

  <xsl:param name="table.frame.border.thickness">1pt</xsl:param>
  <xsl:param name="table.cell.border.thickness">1pt</xsl:param>
  <xsl:param name="table.cell.border.color">#222222</xsl:param>
  <xsl:param name="table.frame.border.color">#222222</xsl:param>

  <xsl:template name="table.cell.block.properties">
    <xsl:attribute name="font-size">90%</xsl:attribute>
    <!-- highlight this entry? -->
    <xsl:if test="ancestor::thead or ancestor::tfoot">
      <xsl:attribute name="font-weight">bold</xsl:attribute>
      <xsl:attribute name="background-color">#cccccc</xsl:attribute>
    </xsl:if>
  </xsl:template>

  <xsl:template name="table.row.properties">
    <xsl:variable name="bgcolor">
      <xsl:call-template name="dbfo-attribute">
        <xsl:with-param name="pis" select="processing-instruction('dbfo')"/>
        <xsl:with-param name="attribute" select="'bgcolor'"/>
      </xsl:call-template>
    </xsl:variable>
    <xsl:if test="$bgcolor != ''">
      <xsl:attribute name="background-color">
        <xsl:value-of select="$bgcolor"/>
      </xsl:attribute>
    </xsl:if>
    <xsl:if test="ancestor::thead or ancestor::tfoot">
      <xsl:attribute name="background-color">#cccccc</xsl:attribute>
    </xsl:if>
  </xsl:template>

  <xsl:template match="appendix//para[count(ancestor::para) = 0]">
    <fo:block xsl:use-attribute-sets="normal.para.spacing">
      <xsl:attribute name="font-size">70%</xsl:attribute>
      <xsl:call-template name="anchor"/>
      <xsl:apply-templates/>
    </fo:block>
  </xsl:template>


  <xsl:template match="appendix/orderedlist/listitem">
    <xsl:variable name="id"><xsl:call-template name="object.id"/></xsl:variable>
  
    <xsl:variable name="item.contents">
      <fo:list-item-label end-indent="label-end()" xsl:use-attribute-sets="orderedlist.label.properties">
        <fo:block>
          <xsl:attribute name="font-size">70%</xsl:attribute>
          <xsl:apply-templates select="." mode="item-number"/>
        </fo:block>
      </fo:list-item-label>
      <fo:list-item-body start-indent="body-start()">
        <fo:block>
          <xsl:apply-templates/>
        </fo:block>
      </fo:list-item-body>
      </xsl:variable>
  
    <xsl:choose>
      <xsl:when test="parent::*/@spacing = 'compact'">
        <fo:list-item id="{$id}" xsl:use-attribute-sets="compact.list.item.spacing">
          <xsl:copy-of select="$item.contents"/>
        </fo:list-item>
      </xsl:when>
      <xsl:otherwise>
        <fo:list-item id="{$id}" xsl:use-attribute-sets="list.item.spacing">
          <xsl:copy-of select="$item.contents"/>
        </fo:list-item>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

</xsl:stylesheet> 

