/*
 * 
 * $Revision$ $Date$
 *
 * This file is part of ***  M y C o R e  ***
 * See http://www.mycore.de/ for details.
 *
 * This program is free software; you can use it, redistribute it
 * and / or modify it under the terms of the GNU General Public License
 * (GPL) as published by the Free Software Foundation; either version 2
 * of the License or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program, in a file called gpl.txt or license.txt.
 * If not, write to the Free Software Foundation Inc.,
 * 59 Temple Place - Suite 330, Boston, MA  02111-1307 USA
 */

package org.mycore.services.fieldquery;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;
import org.jdom.Content;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.output.XMLOutputter;
import org.jdom.transform.JDOMResult;
import org.jdom.transform.JDOMSource;
import org.mycore.common.MCRCache;
import org.mycore.common.MCRConfigurationException;
import org.mycore.common.MCRConstants;
import org.mycore.common.MCRException;
import org.mycore.common.xml.MCRURIResolver;
import org.mycore.datamodel.ifs.MCRFile;
import org.mycore.datamodel.metadata.MCRDerivate;
import org.mycore.datamodel.metadata.MCRMetaISO8601Date;
import org.mycore.datamodel.metadata.MCRObject;

/**
 * Provides methods to automatically extract field values for indexing from
 * MCRObject, MCRFile or any XML document using the definition in
 * searchfields.xml. The buildFields method returns a list of MCRFieldValue
 * objects with values extracted from the object for the given search index.
 * This class supports extracting values from MCRObject metadata, MCRFile
 * metadata, MCRFile xml content. MCRFile additional data, MCRFile text content
 * using the text filter plug-ins, and any plain XML document.
 * 
 * @see MCRSearcher#addToIndex(String, String, List)
 * @author Frank Lützenkirchen
 */
public class MCRData2Fields {

    /** The logger */
    private static final Logger LOGGER = Logger.getLogger(MCRData2Fields.class);

    /** The XSL transformer factory to use */
    private static SAXTransformerFactory factory;

    /** A template element to be used for building individual stylesheet */
    private static Element xslTemplate;

    private static MCRCache stylesheets = new MCRCache(20, "data2searchfields stylesheets");

    static {
        TransformerFactory tf = TransformerFactory.newInstance();
        if (!tf.getFeature(SAXTransformerFactory.FEATURE)) {
            throw new MCRConfigurationException("Could not load a SAXTransformerFactory for use with XSLT");
        }

        factory = (SAXTransformerFactory) tf;
        factory.setURIResolver(MCRURIResolver.instance());

        xslTemplate = new Element("stylesheet");
        xslTemplate.setAttribute("version", "1.0");
        xslTemplate.setNamespace(MCRConstants.XSL_NAMESPACE);
        xslTemplate.addNamespaceDeclaration(Namespace.XML_NAMESPACE);
        xslTemplate.addNamespaceDeclaration(MCRConstants.METS_NAMESPACE);
        xslTemplate.addNamespaceDeclaration(MCRConstants.MODS_NAMESPACE);
        xslTemplate.addNamespaceDeclaration(MCRConstants.XLINK_NAMESPACE);
        xslTemplate.addNamespaceDeclaration(MCRFieldDef.xalanns);
        xslTemplate.addNamespaceDeclaration(MCRFieldDef.extns);
        xslTemplate.setAttribute("extension-element-prefixes", "ext");

        Element param = new Element("param", MCRConstants.XSL_NAMESPACE);
        param.setAttribute("name", "objectType");
        xslTemplate.addContent(param);

        Element template = new Element("template", MCRConstants.XSL_NAMESPACE);
        template.setAttribute("match", "/");
        xslTemplate.addContent(template);

        Element fieldValues = new Element("fieldValues", MCRConstants.MCR_NAMESPACE);
        template.addContent(fieldValues);
    }

    private static Templates buildStylesheet(String index, String source) {
        String key = index + "//" + source;
        Templates stylesheet = (Templates) stylesheets.get(key);
        
        if (stylesheet == null) {
            Element root = (Element) xslTemplate.clone();
            Element fv = root.getChild("template", MCRConstants.XSL_NAMESPACE).getChild("fieldValues", MCRConstants.MCR_NAMESPACE);

            List<MCRFieldDef> fieldDefs = MCRFieldDef.getFieldDefs(index);
            for (int i = 0; i < fieldDefs.size(); i++) {
                MCRFieldDef fieldDef = fieldDefs.get(i);
                if (source.indexOf(fieldDef.getSource()) == -1) {
                    continue;
                }
                List<Content> fragment = fieldDef.getXSL();
                if ((fragment != null) && (fragment.size() > 0)) {
                    fv.addContent(fragment);
                }
            }

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("---------- Stylesheet for \"" + index + "\" / " + source + " ----------");
                XMLOutputter out = new XMLOutputter(org.jdom.output.Format.getPrettyFormat());
                LOGGER.debug("\n" + out.outputString(root));
            }

            try {
                stylesheet = factory.newTemplates(new JDOMSource(new Document(root)));
            } catch (TransformerConfigurationException exc) {
                String msg = "Error while compiling XSL stylesheet: " + exc.getMessageAndLocation();
                throw new MCRConfigurationException(msg, exc);
            }
            stylesheets.put(key, stylesheet);
        }

        return stylesheet;
    }

    /**
     * Extracts field values for indexing from the given MCRObject's metadata.
     * 
     * @param obj
     *            the MCRObject thats metadata should be indexed
     * @param index
     *            the ID of the index as defined in searchfields.xml
     * @return a List of MCRFieldValue objects that contain field and value
     */
    public static List<MCRFieldValue> buildFields(MCRObject obj, String index) {
        String source = MCRFieldDef.OBJECT_METADATA + " " + MCRFieldDef.OBJECT_CATEGORY;
        Templates stylesheet = buildStylesheet(index, source);
        Document xml = obj.createXML();
        return buildValues(stylesheet, xml, obj.getId().getTypeId());
    }
    
    /**
     * Extracts field values for indexing from the given MCRDerivate metadata.
     * 
     * @param der
     *            the MCRDerivate thats metadata should be indexed
     * @param index
     *            the ID of the index as defined in searchfields.xml
     * @return a List of MCRFieldValue objects that contain field and value
     */
    public static List<MCRFieldValue> buildFields(MCRDerivate der, String index) {
        String source = MCRFieldDef.DERIVATE_METADATA;
        Templates stylesheet = buildStylesheet(index, source);
        Document xml = der.createXML();
        return buildValues(stylesheet, xml, der.getId().getTypeId());
    }

    /**
     * Extracts field values for indexing from the given MCRFile's metadata, xml
     * content or text content.
     * 
     * @param file
     *            the MCRFile thats data should be indexed
     * @param index
     *            the ID of the index as defined in searchfields.xml
     * @return a List of MCRFieldValue objects that contain field and value
     */
    public static List<MCRFieldValue> buildFields(MCRFile file, String index) {
        List<MCRFieldValue> values = new ArrayList<MCRFieldValue>();

        boolean foundSourceXMLContent = false;
        boolean foundSourceFileMetadata = false;
        boolean foundSourceFileAdditional = false;

        // Handle source FILE_TEXT_CONTENT
        LOGGER.debug("Handle source FILE_TEXT_CONTENT");
        List<MCRFieldDef> fieldDefList = MCRFieldDef.getFieldDefs(index);
        for (MCRFieldDef fieldDef : fieldDefList) {
            if (!fieldDef.isUsedForObjectType(file.getContentTypeID())) {
                continue;
            }

            if (MCRFieldDef.FILE_TEXT_CONTENT.equals(fieldDef.getSource())) {
                values.add(new MCRFieldValue(fieldDef, file));
            }

            if (MCRFieldDef.FILE_XML_CONTENT.equals(fieldDef.getSource())) {
                foundSourceXMLContent = true;
            }
            if (MCRFieldDef.FILE_METADATA.equals(fieldDef.getSource())) {
                foundSourceFileMetadata = true;
            }
            if (MCRFieldDef.FILE_ADDITIONAL_DATA.equals(fieldDef.getSource())) {
                foundSourceFileAdditional = true;
            }
        }

        // Handle source FILE_XML_CONTENT
        if (foundSourceXMLContent) {
            LOGGER.debug("Handle source FILE_XML_CONTENT");
            Templates stylesheet = buildStylesheet(index, MCRFieldDef.FILE_XML_CONTENT);
            Document xml = null;
            try {
                xml = file.getContentAsJDOM();
            } catch (Exception ex) {
                String msg = "Exception while building XML content of MCRFile " + file.getOwnerID() + " " + file.getAbsolutePath();
                LOGGER.error(msg, ex);
            }
            if (xml != null) {
                values.addAll(buildValues(stylesheet, xml, file.getContentTypeID()));
            }
        }

        // Handle source FILE_METADATA
        if (foundSourceFileMetadata) {
            LOGGER.debug("Handle source FILE_METADATA");
            Templates stylesheet = buildStylesheet(index, MCRFieldDef.FILE_METADATA);
            Document xml = file.createXML();
            values.addAll(buildValues(stylesheet, xml, file.getContentTypeID()));
        }

        // Handle source FILE_ADDITIONAL_DATA
        if (foundSourceFileAdditional) {
            LOGGER.debug("Handle source FILE_ADDITIONAL_DATA");
            Templates stylesheet = buildStylesheet(index, MCRFieldDef.FILE_ADDITIONAL_DATA);
            Document xml = null;
            try {
                xml = file.getAllAdditionalData();
            } catch (Exception ex) {
                String msg = "Exception while reading additional XML data of MCRFile " + file.getOwnerID() + " " + file.getAbsolutePath();
                LOGGER.error(msg, ex);
            }
            if (xml != null) {
                values.addAll(buildValues(stylesheet, xml, file.getContentTypeID()));
            }
        }

        return values;
    }

    /**
     * Extracts field values for indexing from the given JDOM xml document.
     * 
     * @param doc
     *            the JDOM xml document thats data should be indexed
     * @param index
     *            the ID of the index as defined in searchfields.xml
     * @return a List of MCRFieldValue objects that contain name, type and value
     */
    public static List<MCRFieldValue> buildFields(Document doc, String index) {
        Templates stylesheet = buildStylesheet(index, MCRFieldDef.XML);
        return buildValues(stylesheet, doc, doc.getRootElement().getName());
    }

    /**
     * Extracts field values for indexing from the given JDOM xml document.
     * 
     * @param xml
     *            the xml document thats data should be indexed as byte array
     * @param index
     *            the ID of the index as defined in searchfields.xml
     * @return a List of MCRFieldValue objects that contain name, type and value
     */
    public static List<MCRFieldValue> buildFields(byte[] xml, String index, String source, String objectType) {
        Templates stylesheet = buildStylesheet(index, source);
        return buildValues(stylesheet, xml, objectType);
    }

    /** Transforms xml input to search field values using XSL * */
    private static List<MCRFieldValue> buildValues(Templates stylesheet, Document xml, String objectType) {
        return buildValues(stylesheet, new JDOMSource(xml), objectType);
    }

    /** Transforms xml input to search field values using XSL * */
    private static List<MCRFieldValue> buildValues(Templates stylesheet, byte[] xml, String objectType) {
        return buildValues(stylesheet, new StreamSource(new ByteArrayInputStream(xml)), objectType);
    }

    /** Transforms xml input to search field values using XSL * */
    private static List<MCRFieldValue> buildValues(Templates stylesheet, Source xml, String objectType) {
        List<MCRFieldValue> values = new ArrayList<MCRFieldValue>();

        @SuppressWarnings("rawtypes")
		List fieldValues = null;
        try {
            JDOMResult xmlres = new JDOMResult();
            Transformer transformer = factory.newTransformerHandler(stylesheet).getTransformer();
            transformer.setParameter("objectType", objectType);
            transformer.transform(xml, xmlres);

            @SuppressWarnings("rawtypes")
			List resultList = xmlres.getResult();
            Element root = (Element) resultList.get(0);
            fieldValues = root.getChildren();
        } catch (Exception ex) {
            String msg = "Exception while transforming metadata to search field";
            throw new MCRException(msg, ex);
        }

        if (fieldValues != null) {
            for (int i = 0; i < fieldValues.size(); i++) {
                Element fieldValue = (Element) fieldValues.get(i);
                String value = fieldValue.getTextTrim();
                String name = fieldValue.getName();
                MCRFieldDef def = MCRFieldDef.getDef(name);

                if (value != null && value.length() > 0) {
                    LOGGER.debug("MCRData2Fields " + name + " := " + value);
                    values.add(new MCRFieldValue(def, value));
                }
            }
        }
        return values;
    }

    /**
     * Xalan XSL extension to convert MyCoRe date values to standard format. To
     * be used in a stylesheet or searchfields.xml configuration. Usage example:
     * &lt;field name="date" type="date"
     * xpath="/mycoreobject/metadata/dates/date"
     * value="ext:normalizeDate(string(text()))" &gt;
     * 
     * @param sDate
     *            the date string in a locale-dependent format
     */
    public static String normalizeDate(String sDate) {
        try {
            MCRMetaISO8601Date iDate = new MCRMetaISO8601Date();
            iDate.setDate(sDate.trim());
            String isoDateString =iDate.getISOString();
            if(isoDateString.length()==4){
            	return isoDateString;
            }
            return isoDateString.substring(0, 10);
        } catch (Exception ex) {
            LOGGER.debug(ex);
            return "";
        }
    }
}