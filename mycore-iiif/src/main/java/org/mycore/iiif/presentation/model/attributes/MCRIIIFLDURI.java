package org.mycore.iiif.presentation.model.attributes;


import org.mycore.iiif.model.MCRIIIFBase;

public class MCRIIIFLDURI extends MCRIIIFBase {

    public MCRIIIFLDURI(String uri, String type, String format) {
        super(uri, type, MCRIIIFBase.API_PRESENTATION_2);
        this.format = format;
    }

    public MCRIIIFLDURI(String uri, String type) {
        super(uri, type, MCRIIIFBase.API_PRESENTATION_2);
    }

    private String format = null;

}
