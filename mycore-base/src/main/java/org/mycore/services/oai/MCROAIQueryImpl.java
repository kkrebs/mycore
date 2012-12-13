/**
 * $RCSfile$ $Revision$ $Date$ This file is part of ** M y C o R e ** Visit our
 * homepage at http://www.mycore.de/ for details. This program is free software;
 * you can use it, redistribute it and / or modify it under the terms of the GNU
 * General Public License (GPL) as published by the Free Software Foundation;
 * either version 2 of the License or (at your option) any later version. This
 * program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with
 * this program, normally in the file license.txt. If not, write to the Free
 * Software Foundation Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307
 * USA
 **/

package org.mycore.services.oai;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.xpath.XPath;
import org.mycore.backend.hibernate.MCRHIBConnection;
import org.mycore.common.MCRConfigurationException;
import org.mycore.common.MCRException;
import org.mycore.datamodel.classifications2.MCRCategory;
import org.mycore.datamodel.classifications2.MCRCategoryDAOFactory;
import org.mycore.datamodel.classifications2.MCRCategoryID;
import org.mycore.datamodel.classifications2.MCRLabel;
import org.mycore.datamodel.classifications2.utils.MCRCategoryTransformer;
import org.mycore.datamodel.metadata.MCRBase;
import org.mycore.datamodel.metadata.MCRMetadataManager;
import org.mycore.datamodel.metadata.MCRObjectID;
import org.mycore.parsers.bool.MCRAndCondition;
import org.mycore.parsers.bool.MCROrCondition;
import org.mycore.services.fieldquery.MCRFieldDef;
import org.mycore.services.fieldquery.MCRQuery;
import org.mycore.services.fieldquery.MCRQueryCondition;
import org.mycore.services.fieldquery.MCRQueryManager;
import org.mycore.services.fieldquery.MCRQueryParser;
import org.mycore.services.fieldquery.MCRResults;

/**
 * @author Heiko Helmbrecht
 * @version $Revision$ $Date$ This is the MyCoRe-Implementation of the
 *          <i>MCROAIQuery </i>-Interface.
 *
 * @deprecated 
 *  code will be removed after the next releasee
 *  use new implementation from MyCoRe oai module
 */
public class MCROAIQueryImpl implements MCROAIQuery {

    private static Logger logger;

    // maximum number of returned list sets
    private static int maxReturns;

    static {
        logger = Logger.getLogger(MCROAIQueryImpl.class.getName());
        maxReturns = MCROAIProvider.getMaximalHitsize();
    }

    private int deliveredResults = 0;

    private int numResults = 0;

    private String lastQuery = "";

    private List<String> resultArray;

    /**
     * Method MCROAIQueryService.
     */
    public MCROAIQueryImpl() {
    }

    /**
     * Method exists. Checks if the given ID exists in the data repository
     * 
     * @param id
     *            The ID to be checked
     * @return boolean
     */
    public boolean exists(String id) {
        return MCRMetadataManager.exists(MCRObjectID.getInstance(id));
    }

    /**
     * Method listSets. Gets a list of classificationId's and Labels for a given
     * ID
     * 
     * @param instance
     *            the Servletinstance
     * @return List a list that contains an array of three Strings: the category
     *         id, the label and a description or NULL, if the server does not
     *         support sets
     */
    public List<String[]> listSets(String instance) {
        String[] classifications = MCROAIProvider.getConfigBean(instance).getClassificationIDs();
        if (classifications.length == 0) {
            return null;
        }
        List<String[]> list = new ArrayList<String[]>();
        for (String classification : classifications) {
            MCRCategory cl = MCRCategoryDAOFactory.getInstance().getCategory(MCRCategoryID.rootID(classification), -1);
            if (cl != null) {
                list.addAll(addXDINI(cl.getChildren()));
            }
        }
        return list;
    }

    private List<String[]> addXDINI(List<MCRCategory> categories) {
        ArrayList<String[]> ar = new ArrayList<String[]>();
        if (categories == null) {
            return ar;
        }
        for (int i = 0; i < categories.size(); i++) {
            MCRCategory category = categories.get(i);
            Set<org.mycore.datamodel.classifications2.MCRLabel> labels = category.getLabels();
            for (MCRLabel label : labels) {
                if ("x-dini".equals(label.getLang())) {
                    String[] set = new String[3];
                    set[0] = label.getText();
                    set[1] = label.getText();
                    set[2] = label.getDescription();
                    ar.add(set);
                }
            }
            ar.addAll(addXDINI(category.getChildren()));
        }
        return ar;
    }

    /**
     * Method listIdentifiers.Gets a list of identifiers
     * 
     * @param set
     *            the category (if known) is in the first element
     * @param from
     *            the date (if known) is in the first element
     * @param until
     *            the date (if known) is in the first element
     * @param metadataPrefix
     *            the requested metadata prefix
     * @param instance
     *            the Servletinstance
     * @return List A list that contains an array of three Strings: the
     *         identifier, a datestamp (modification date) and a string with a
     *         blank separated list of categories the element is classified in
     */
    public List<String> listIdentifiers(String[] set, String[] from, String[] until, String metadataPrefix, String instance) {
        return listRecordsOrIdentifiers(set, from, until, metadataPrefix, instance, false);
    }

    /**
     * Method getRecord. Gets a metadata record with the given <i>id </id>.
     * 
     * @param id
     *            The id of the object.
     * @param metadataPrefix
     *            the requested metadata prefix
     * @param instance
     *            the Servletinstance
     * @return List A list that contains an array of three Strings: the
     *         identifier, a datestamp (modification date) and a string with a
     *         blank separated list of categories the element is classified in
     *         and a JDOM element with the metadata of the record
     */
    public List<Object> getRecord(String id, String metadataPrefix, String instance) {
        // TODO List<Object> is really bad return type, a separate class should
        // hole identifier and eMetadata
        List<Object> list = new ArrayList<Object>();

        MCRBase object = null;

        MCRObjectID mcrID = MCRObjectID.getInstance(id);

        if (id.indexOf("derivate") != -1) {
            object = MCRMetadataManager.retrieveMCRDerivate(mcrID);
        } else {
            object = MCRMetadataManager.retrieveMCRObject(mcrID);
        }

        String repositoryId = null;
        try {
            repositoryId = MCROAIProvider.getConfigBean(instance).getRepositoryIdentifier();
        } catch (MCRConfigurationException mcrx) {
            return null;
        } catch (MCRException e) {
            return null;
        }

        String[] identifier = MCROAIProvider.getHeader(object, id, repositoryId, instance);
        list.add(identifier);
        logger.debug("Identifier hinzugefuegt");

        Element eMetadata = (Element) object.createXML().getRootElement().clone();

        list.add(eMetadata);
        logger.debug("Metadaten hinzugefuegt");

        return list;
    }

    /**
     * Method listRecords.Gets a list of metadata records
     * 
     * @param set
     *            the category (if known) is in the first element
     * @param from
     *            the date (if known) is in the first element
     * @param until
     *            the date (if known) is in the first element
     * @param metadataPrefix
     *            the requested metadata prefix
     * @param instance
     *            the Servletinstance
     * @return List A list that contains an array of three Strings: the
     *         identifier, a datestamp (modification date) and a string with a
     *         blank separated list of categories the element is classified in
     */
    public List<String> listRecords(String[] set, String[] from, String[] until, String metadataPrefix, String instance) {
        return listRecordsOrIdentifiers(set, from, until, metadataPrefix, instance, true);
    }

    private List<String> listRecordsOrIdentifiers(String[] set, String[] from, String[] until, String metadataPrefix, String instance,
            boolean listRecords) {
        List<String> list = new ArrayList<String>();

        if (hasMore() && (listRecords == lastQuery.equals("listRecords") || !listRecords == lastQuery.equals("listIdentifiers"))) {
            for (int i = deliveredResults; i < Math.min(maxReturns + deliveredResults, numResults); i++) {
                list.add(resultArray.get(i));
            }
            deliveredResults = Math.min(maxReturns + deliveredResults, numResults);
            return list;
        }

        resetResults(listRecords ? "listRecords" : "listIdentifiers");

        // create query condition
        MCRAndCondition cAnd = new MCRAndCondition();

        String restriction = MCROAIProvider.getConfigBean(instance).getQueryRestriction();
        if (restriction != null) {
            try {
                cAnd.addChild(new MCRQueryParser().parse(restriction));
            } catch (MCRException mcrx) {
                logger.warn("Error in adding OAI restriction: " + restriction, mcrx);
            }
        }

        List<String> searchFields = MCROAIProvider.getConfigBean(instance).getSearchFields();

        MCROrCondition cOr = new MCROrCondition();
        for (String searchField : searchFields) {
            MCRFieldDef field = MCRFieldDef.getDef(searchField);
            if (field != null) {
                if (set == null) {
                    cOr.addChild(new MCRQueryCondition(field, "like", ""));
                } else {
                    String categoryId = set[0].substring(set[0].lastIndexOf(':') + 1);
                    cOr.addChild(new MCRQueryCondition(field, "like", categoryId));
                    generateQueryForDiniLabels(cOr, searchField, set[0], instance);
                }
            }
        }
        if (cOr.getChildren() != null && cOr.getChildren().size() > 0) {
            cAnd.addChild(cOr);
        }

        MCROrCondition dateFrom = new MCROrCondition();

        MCRFieldDef field = MCRFieldDef.getDef("modified");
        if (from != null) {
            String date = getTimeStamp(from[0]);
            dateFrom.addChild(new MCRQueryCondition(field, ">=", date));
        }

        MCRFieldDef fileDateModified = null;
        try {
            fileDateModified = MCRFieldDef.getDef("derivateModificationDate");
        } catch (MCRConfigurationException e) {
            // search field "derivateModificationDate" was not defined
            // fileDateModified will be null
        }

        if (from != null && fileDateModified != null) {
            String date = getTimeStamp(from[0]);
            dateFrom.addChild(new MCRQueryCondition(fileDateModified, ">=", date));
        }
        cAnd.addChild(dateFrom);

        MCROrCondition dateUntil = new MCROrCondition();

        if (until != null) {
            String date = getUntilTimeStamp(until[0]);
            dateUntil.addChild(new MCRQueryCondition(field, "<=", date));
        }

        if (until != null && fileDateModified != null) {
            String date = getUntilTimeStamp(until[0]);
            dateUntil.addChild(new MCRQueryCondition(fileDateModified, "<=", date));
        }
        cAnd.addChild(dateUntil);

        MCRQuery query = new MCRQuery(cAnd);

        logger.debug("OAI-QUERY:" + cAnd);
        MCRResults results = MCRQueryManager.search(query);

        int resultCount = results.getNumHits();
        resultArray = new ArrayList<String>();

        for (int i = 0; i < resultCount; i++) {
            resultArray.add(results.getHit(i).getID());
        }

        // add the deleted items
        String fromDate = from != null ? getTimeStamp(from[0]) : null;
        String untilDate = until != null ? getUntilTimeStamp(until[0]) : null;
        resultArray.addAll(getDeletedItems(fromDate, untilDate));

        numResults = resultArray.size();

        logger.debug("OAIQuery found:" + resultCount + " hits");
        logger.debug(numResults + " hits are publically accessable");
        deliveredResults = Math.min(maxReturns, numResults);
        logger.debug("deliveredResults:" + deliveredResults);

        for (int i = 0; i < deliveredResults; i++) {
            list.add(resultArray.get(i));
        }

        return list;
    }

    /**
     * @param from
     * @param until
     */
    @SuppressWarnings("unchecked")
    public List<String> getDeletedItems(String from, String until) {
        logger.info("Getting identifiers of deleted items");
        List<String> deletedItems = new Vector<String>();
        try {
            MCRHIBConnection conn = MCRHIBConnection.instance();
            String q = "SELECT DISTINCT identifier FROM mcrdeleteditems WHERE ";
            if (from != null && until != null) {
                q += "date_deleted >= '" + from + "' and date_deleted <= '" + until + "'";
            } else if (from != null) {
                q += "date_deleted >= '" + from + "'";
            } else if (until != null) {
                q += "date_deleted <= '" + until + "'";
            } else {
                q += "true";
            }
            deletedItems = conn.getSession().createSQLQuery(q).list();
        } catch (Exception ex) {
            logger.warn("Could not retrieve identifiers of deleted objects", ex);
        }

        return deletedItems;
    }

    /**
     * Method hasMore.
     * 
     * @return true, if more results for the last query exists, else false
     */
    public boolean hasMore() {
        return deliveredResults < numResults;
    }

    private String getTimeStamp(String isoDate) {
        int len = isoDate.length();
        if (len == 10) {
            return isoDate + " 00:00:00";
        } else if (len == 20) {
            return isoDate.substring(0, 10) + " " + isoDate.substring(11, 19);
        }
        logger.warn("unallowed iso date format:" + isoDate);
        return null;
    }

    private String getUntilTimeStamp(String isoDate) {
        int len = isoDate.length();
        if (len == 10) {
            return isoDate + " 23:59:59";
        } else if (len == 20) {
            return isoDate.substring(0, 10) + " " + isoDate.substring(11, 19);
        }
        logger.warn("unallowed iso date format:" + isoDate);
        return null;
    }

    private void resetResults(String query) {
        deliveredResults = 0;
        numResults = 0;
        resultArray = null;
        lastQuery = query;
    }

    @SuppressWarnings("unchecked")
    private void generateQueryForDiniLabels(MCROrCondition cOr, String searchField, String set, String instance) {
        // expected searchfields: "format", "type", "subject"
        // mapping to DINI sets: "doc-type", "pub-type", "ddc"
        MCRFieldDef field = MCRFieldDef.getDef(searchField);

        String[] classification = MCROAIProvider.getConfigBean(instance).getClassificationIDsForSearchField(searchField);

        for (String element : classification) {
            // TODO: maybe query this directly from backend
            MCRCategory cl = MCRCategoryDAOFactory.getInstance().getCategory(MCRCategoryID.rootID(element), -1);
            org.jdom.Document jDomDoc = MCRCategoryTransformer.getMetaDataDocument(cl, false);
            try {
                // could be improved: return only <label> under a <categegory>
                // but //category/label[..] does not work here
                XPath xpathExpr = XPath.newInstance("//label[@xml:lang='x-dini' and @text='" + set + "']/..//@ID");
                List<Attribute> resultList = xpathExpr.selectNodes(jDomDoc);
                for (Attribute id : resultList) {
                    cOr.addChild(new MCRQueryCondition(field, "like", id.getValue()));
                }
            } catch (JDOMException e) {
                logger.error(e);
            }
        }
    }
}