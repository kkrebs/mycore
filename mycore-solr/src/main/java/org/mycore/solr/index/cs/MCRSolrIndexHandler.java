package org.mycore.solr.index.cs;

import java.io.IOException;
import java.util.List;

import org.apache.solr.client.solrj.SolrServerException;

/**
 * 
 * @author Matthias Eichner
 */
public interface MCRSolrIndexHandler {

    /**
     * Commits something to solr.
     * 
     * @throws IOException
     * @throws SolrServerException
     */
    public void index() throws IOException, SolrServerException;

    /**
     * Returns a list of index handlers which should be executed after
     * the default index process. Return an empty list if no sub handlers
     * defined.
     * 
     * @return list of <code>MCRSolrIndexHandler</code>
     */
    public List<MCRSolrIndexHandler> getSubHandlers();

}