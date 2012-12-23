package org.mycore.migration21_22.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.xpath.XPath;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mycore.common.MCRConfiguration;
import org.mycore.common.content.MCRStreamContent;
import org.mycore.datamodel.common.MCRXMLMetadataManager;
import org.mycore.datamodel.metadata.MCRObjectID;
import org.mycore.frontend.cli.MCRCommandLineInterface;
import org.mycore.migration21_22.cli.MCRMigrationCommands22;

public class MCRMigrationCommands22Test {
    private static Logger LOGGER = Logger.getLogger(MCRMigrationCommands22Test.class);

    @Rule
    public TemporaryFolder tmpFolder = new TemporaryFolder();

    @Before
    public void init() throws MalformedURLException{
        File baseDir = tmpFolder.newFolder("baseDir");
        File svnBase = tmpFolder.newFolder("SVNBase");
        MCRConfiguration.instance().set("MCR.CLI.Classes.Annotated", MCRMigrationCommands22.class.getName());
        MCRConfiguration.instance().set("MCR.Metadata.Store.BaseDir", baseDir.getAbsolutePath());
        MCRConfiguration.instance().set("MCR.Metadata.Store.SVNBase", "file:///"+ svnBase.getAbsolutePath().replace('\\', '/'));
        MCRConfiguration.instance().set("MCR.Metadata.Type.cbu", true);
        MCRConfiguration.instance().set("MCR.Metadata.Type.mods", true);
        MCRConfiguration.instance().set("MCR.CommandLineInterface.unitTest", true);
        InputStream cbuXML = getClass().getResourceAsStream("/xml/ArchNachl_cbu_00000001.xml");
        MCRXMLMetadataManager.instance().create(MCRObjectID.getInstance("ArchNachl_cbu_00000001"), new MCRStreamContent(cbuXML), new Date());
        InputStream cbuXML1 = getClass().getResourceAsStream("/xml/ArchNachl_cbu_00000002.xml");
        MCRXMLMetadataManager.instance().create(MCRObjectID.getInstance("ArchNachl_cbu_00000002"), new MCRStreamContent(cbuXML1), new Date());
        InputStream bmelvXML = getClass().getResourceAsStream("/xml/bmelv_mods_00000052.xml");
        MCRXMLMetadataManager.instance().create(MCRObjectID.getInstance("bmelv_mods_00000052"), new MCRStreamContent(bmelvXML), new Date());
    }
    
    @Test
    public void test() throws JDOMException, IOException {
        MCRCommandLineInterface.main(new String[]{"migrate xlink label"});
        
        MCRXMLMetadataManager xmlMetaManager = MCRXMLMetadataManager.instance();
        List<String> listIDs = xmlMetaManager.listIDs();
        
        XPath xlinkLabel = XPath.newInstance("/mycoreobject/*/*[starts-with(@class,'MCRMetaLink')]/*/@xlink:label");
        XPath xlinkTitle = XPath.newInstance("/mycoreobject/*/*[starts-with(@class,'MCRMetaLink')]/*/@xlink:title");
        for (String ID : listIDs) {
            MCRObjectID mcrid = MCRObjectID.getInstance(ID);
            Document mcrObjXML = xmlMetaManager.retrieveXML(mcrid);
            
            assertTrue("there should be no more xlink:label in MCRMetaLink. ", xlinkLabel.selectNodes(mcrObjXML).isEmpty());
            
            if(ID.equals("ArchNachl_cbu_0000000002")){
                assertEquals("No xlink label please!", ((Attribute)xlinkTitle.selectSingleNode(mcrObjXML)).getValue());
            }
        }
    }
}