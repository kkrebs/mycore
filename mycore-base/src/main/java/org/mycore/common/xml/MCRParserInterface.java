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

import java.io.InputStream;
import java.net.URI;

import org.jdom.Document;
import org.mycore.common.MCRException;
import org.xml.sax.SAXParseException;

/**
 * This interface is designed to choose the XML parser. To construct a JDOM you
 * have to methodes, one for a URI input an one for a XML stream.
 * 
 * @author Jens Kupferschmidt
 * @author Thomas Scheffler (yagee)
 * @version $Revision$ $Date$
 */
public interface MCRParserInterface {
    /**
     * Parses an XML file from a URI and returns it as DOM. Use the validation
     * value from mycore.properties.
     * 
     * @param uri
     *            the URI of the XML stream
     * @exception MCRException
     *                general Exception of MyCoRe
     * @return a document object (DOM)
     * @throws SAXParseException 
     */
    public Document parseURI(URI uri) throws MCRException, SAXParseException;

    /**
     * Parses an XML file from a URI and returns it as DOM. Use the given
     * validation flag.
     * 
     * @param uri
     *            the URI of the XML file
     * @param valid
     *            the validation flag
     * @throws MCRException
     *             if XML could not be parsed
     * @return the XML file as a DOM object
     * @throws SAXParseException 
     */
    public Document parseURI(URI uri, boolean valid) throws MCRException, SAXParseException;

    /**
     * Parses an XML String and returns it as DOM. Use the validation value from
     * mycore.properties.
     * 
     * @param xml
     *            the XML data stream
     * @exception MCRException
     *                general Exception of MyCoRe
     * @return a document object (DOM)
     * @throws SAXParseException 
     */
    public Document parseXML(String xml) throws MCRException, SAXParseException;

    /**
     * Parses an XML String and returns it as DOM. Use the given validation
     * flag.
     * 
     * @param xml
     *            the XML String to be parsed
     * @param valid
     *            the validation flag
     * @throws MCRException
     *             if XML could not be parsed
     * @return the XML file as a DOM object
     * @throws SAXParseException 
     */
    public Document parseXML(String xml, boolean valid) throws MCRException, SAXParseException;

    /**
     * Parses an Byte Array and returns it as DOM. Use the validation value from
     * mycore.properties.
     * 
     * @param xml
     *            the XML Byte Array to be parsed
     * @throws MCRException
     *             if XML could not be parsed
     * @return the XML file as a DOM object
     * @throws SAXParseException 
     */
    public Document parseXML(byte[] xml) throws MCRException, SAXParseException;

    /**
     * Parses an Byte Array and returns it as DOM. Use the given validation
     * flag.
     * 
     * @param xml
     *            the XML Byte Array to be parsed
     * @param valid
     *            the validation flag
     * @throws MCRException
     *             if XML could not be parsed
     * @return the XML file as a DOM object
     * @throws SAXParseException 
     */
    public Document parseXML(byte[] xml, boolean valid) throws MCRException, SAXParseException;

    /**
     * Parses an Byte Array and returns it as DOM. Use the validation value from
     * mycore.properties.
     * 
     * @param input
     *            the InputStream to be parsed
     * @throws MCRException
     *             if XML could not be parsed
     * @return the XML file as a DOM object
     * @throws SAXParseException 
     */
    public Document parseXML(InputStream input) throws MCRException, SAXParseException;

    /**
     * Parses an Byte Array and returns it as DOM. Use the given validation
     * flag.
     * 
     * @param input
     *            the InputStream to be parsed
     * @param validate
     *            the validation flag
     * @throws MCRException
     *             if XML could not be parsed
     * @return the XML file as a DOM object
     * @throws SAXParseException 
     */
    public Document parseXML(InputStream input, boolean validate) throws MCRException, SAXParseException;
}