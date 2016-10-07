package org.mycore.iiif.presentation.model.basic;


import java.util.ArrayList;
import java.util.List;

import org.mycore.iiif.presentation.model.MCRIIIFPresentationBase;
import org.mycore.iiif.presentation.model.attributes.MCRIIIFMetadata;

public class MCRIIIFRange extends MCRIIIFPresentationBase {

    public static final String TYPE = "sc:Range";

    public MCRIIIFRange(String id) {
        super();
        setId(id);
        setType(TYPE);
    }


    private String label;

    public List<MCRIIIFMetadata> metadata = new ArrayList<>();

    // ranges and canvases should be removed!
    public List<String> ranges = new ArrayList<String>();// = new ArrayList<>();
    public List<String> canvases = new ArrayList<>();


    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

}
