/*
 * $Revision$ 
 * $Date$
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

package org.mycore.frontend.export;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.mycore.common.content.MCRContent;
import org.mycore.common.content.transformer.MCRContentTransformer;
import org.mycore.common.content.transformer.MCRContentTransformerFactory;
import org.mycore.frontend.basket.MCRBasket;
import org.mycore.frontend.basket.MCRBasketManager;
import org.mycore.frontend.servlets.MCRServlet;
import org.mycore.frontend.servlets.MCRServletJob;

/**
 * Provides functionality to export content. 
 * The content to export can be selected by specifying one or more 
 * URIs to read from, or by giving the ID of a basket to export. 
 * The selected content is collected as MCRExportCollection thats
 * root element name can be specified. 
 * The content is then transformed using an MCRContentTransformer instance
 * and forwarded to the requesting client.
 * 
 * Request Parameters:
 *   uri=... 
 *     can be repeated to include content from one or more URIs to read XML from
 *   basket=...
 *     the ID of a basket to read XML from  
 *   root=...
 *     optional, name of the root element that wraps the selected content
 *   ns=...
 *     optional, URI of the namespace of the root element
 *   transformer=...
 *     the ID of the transformer to use to export the selected content.
 *          
 * @see MCRExportCollection
 * @see MCRContentTransformer
 * 
 * @author Frank L\u00FCtzenkirchen
 */
public class MCRExportServlet extends MCRServlet {

    private final static Logger LOGGER = Logger.getLogger(MCRExportServlet.class);

    @Override
    public void doGetPost(MCRServletJob job) throws Exception {
        String transformerID = job.getRequest().getParameter("transformer");
        String filename = getProperty(job.getRequest(), "filename");
        if (filename == null) {
            filename = "export-" + System.currentTimeMillis();
        }
        MCRContentTransformer transformer = MCRContentTransformerFactory.getTransformer(transformerID);
        if (transformer == null) {
            job.getResponse().sendError(HttpServletResponse.SC_BAD_REQUEST, "Transformer " + transformerID + " is not configured.");
            return;
        }
        MCRExportCollection collection = createCollection(job.getRequest());
        fillCollection(job.getRequest(), collection);
        sendResponse(job.getResponse(), collection.getContent(), transformer, filename + "." + transformer.getFileExtension());
    }

    /**
     * Fills the collection with the XML data requested by URIs or basket ID.
     */
    private void fillCollection(HttpServletRequest req, MCRExportCollection collection) throws Exception {
        String basketID = req.getParameter("basket");
        if (basketID != null) {
            MCRBasket basket = MCRBasketManager.getOrCreateBasketInSession(basketID);
            collection.add(basket);
            LOGGER.info("exporting basket " + basketID + " via " + req.getParameter("transformer"));
        }

        if (req.getParameter("uri") != null)
            for (String uri : req.getParameterValues("uri")) {
                if (isAllowed(uri)) {
                    collection.add(uri);
                    LOGGER.info("exporting " + uri + " via " + req.getParameter("transformer"));
                }
            }
    }

    /** URIs beginning with these prefixes are forbidden for security reasons */
    private final static String[] forbiddenURIs = { "file", "webapp", "resource" };

    private boolean isAllowed(String uri) {
        for (String prefix : forbiddenURIs)
            if (uri.startsWith(prefix)) {
                LOGGER.warn("URI " + uri + " is not allowed for security reasons");
                return false;
            }
        return true;
    }

    /**
     * Creates a new, empty MCRExportCollection, optionally with the requested root element name and namespace.
     */
    private MCRExportCollection createCollection(HttpServletRequest req) {
        MCRExportCollection collection = new MCRExportCollection();
        String root = req.getParameter("root");
        String ns = req.getParameter("ns");
        if (!((root == null) || root.isEmpty()))
            collection.setRootElement(root, ns);
        return collection;
    }

    /**
     * Sends the resulting, transformed MCRContent to the client
     */
    private void sendResponse(HttpServletResponse res, MCRContent content, MCRContentTransformer transformer, String filename) throws IOException {
        res.setHeader("Content-Disposition", "inline;filename=\"" + filename + "\"");
        res.setContentType(transformer.getMimeType());
        OutputStream out = res.getOutputStream();
        transformer.transform(content, out);
        out.flush();
        out.close();
    }
}