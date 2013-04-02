package org.mycore.solr.index.cs;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.jdom2.Document;
import org.mycore.common.content.MCRContent;
import org.mycore.common.content.transformer.MCRContentTransformer;
import org.mycore.datamodel.metadata.MCRBase;

/**
 * Content stream suitable for wrapping {@link MCRBase} and {@link Document} objects.
 * 
 * @author shermann
 * @author Matthias Eichner
 */
public class MCRSolrContentStream extends MCRSolrAbstractContentStream<MCRContent> {

    /**
     * @param objectOrDerivate
     * @param content
     */
    public MCRSolrContentStream(String id, MCRContent content) {
        super(content);
        this.setName(id);
    }

    @Override
    protected void setup() throws IOException {
        MCRContent content = getSource();
        ByteArrayOutputStream out = new ByteArrayOutputStream(64 * 1024);
        getTransformer().transform(content, out);
        byte[] byteArray = out.toByteArray();

        this.setSourceInfo(content.getSystemId());
        this.setContentType(getTransformer().getMimeType());
        this.setSize((long) byteArray.length);
        this.setInputStream(new ByteArrayInputStream(byteArray));
    }

    public MCRContentTransformer getTransformer() {
        return MCRSolrAppender.getTransformer();
    }

}