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

package org.mycore.common.xml;

import java.io.IOException;
import java.text.MessageFormat;

import org.apache.log4j.Logger;
import org.apache.xerces.parsers.SAXParser;
import org.jdom.Document;
import org.jdom.input.SAXBuilder;
import org.mycore.common.MCRException;
import org.mycore.common.content.MCRContent;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ext.EntityResolver2;

/**
 * Parses XML content using the default Xerces parser. 
 *  
 * @author Frank L\u00FCtzenkirchen
 */
public class MCRXMLParserXerces implements MCRXMLParser {

    private final static String PARSER_CLASS_NAME = SAXParser.class.getCanonicalName();

    private final static String FEATURE_NAMESPACES = "http://xml.org/sax/features/namespaces";

    private final static String FEATURE_SCHEMA_SUPPORT = "http://apache.org/xml/features/validation/schema";

    private final static String FEATURE_FULL_SCHEMA_SUPPORT = "http://apache.org/xml/features/validation/schema-full-checking";

    private final static String msg = "Error while parsing XML document: ";

    private SAXBuilder builder;

    public void setValidating(boolean validate) {
        builder = new SAXBuilder(PARSER_CLASS_NAME, validate);
        builder.setFeature(FEATURE_NAMESPACES, true);
        builder.setFeature(FEATURE_SCHEMA_SUPPORT, validate);
        builder.setFeature(FEATURE_FULL_SCHEMA_SUPPORT, false);
        builder.setReuseParser(false);
        builder.setErrorHandler(new MCRXMLParserErrorHandler());
        builder.setEntityResolver(new XercesBugFixResolver(MCRURIResolver.instance()));
    }

    public boolean isValidating() {
        return builder.getValidation();
    }

    public Document parseXML(MCRContent content) throws SAXParseException {
        try {
            InputSource source = content.getInputSource();
            return builder.build(source);
        } catch (Exception ex) {
            if (ex instanceof SAXParseException) {
                throw (SAXParseException) ex;
            }
            Throwable cause = ex.getCause();
            if (cause instanceof SAXParseException) {
                throw (SAXParseException) cause;
            }
            throw new MCRException(msg, ex);
        }
    }

    /**
     * Xerces 2.11.0 does not provide a relative systemId if baseURI is a XML file to be validated by a schema specified in systemId.
     * This EntityResolver makes a relative systemId so that the fallback could conform to the defined interface.
     * @author Thomas Scheffler (yagee)
     *
     */
    private static class XercesBugFixResolver implements EntityResolver2 {
        private EntityResolver2 fallback;

        private static Logger LOGGER = Logger.getLogger(MCRXMLParserXerces.class);

        public XercesBugFixResolver(EntityResolver2 fallback) {
            this.fallback = fallback;
        }

        @Override
        public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
            return fallback.resolveEntity(publicId, systemId);
        }

        @Override
        public InputSource getExternalSubset(String name, String baseURI) throws SAXException, IOException {
            return fallback.getExternalSubset(name, baseURI);
        }

        @Override
        public InputSource resolveEntity(String name, String publicId, String baseURI, String systemId) throws SAXException, IOException {
            String prefix = getPrefix(baseURI);
            LOGGER.debug(MessageFormat.format("systemId: {0} prefixed? {1}", systemId, systemId.startsWith(prefix)));
            if (prefix.length() > 0 && systemId.startsWith(prefix)) {
                systemId = systemId.substring(prefix.length());
                LOGGER.debug("new systemId: " + systemId);
            } else {
                LOGGER.debug("Try to use EntityResolver interface");
                InputSource inputSource = resolveEntity(publicId, systemId);
                if (inputSource != null) {
                    LOGGER.debug("Found resource in EntityResolver interface");
                    return inputSource;
                }
            }
            return fallback.resolveEntity(name, publicId, baseURI, systemId);
        }

        private static String getPrefix(String baseURI) {
            if (baseURI == null) {
                return "";
            }
            int pos = baseURI.lastIndexOf('/');
            String prefix = baseURI.substring(0, pos + 1);
            LOGGER.debug(MessageFormat.format("prefix of baseURI ''{0}'' is: {1}", baseURI, prefix));
            return prefix;
        }

    }
}