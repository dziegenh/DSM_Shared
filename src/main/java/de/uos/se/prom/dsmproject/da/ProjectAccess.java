package de.uos.se.prom.dsmproject.da;

import de.uos.se.prom.dsmproject.da.entity.PersistedArtifact;
import de.uos.se.prom.dsmproject.da.entity.PersistedArtifacttype;
import de.uos.se.prom.dsmproject.da.entity.PersistedDependency;
import de.uos.se.prom.dsmproject.da.entity.PersistedProject;
import de.uos.se.prom.dsmproject.da.entity.PersistedTypesDefactory;
import de.uos.se.prom.dsmproject.da.entity.PersistedTypesFactory;
import de.uos.se.prom.dsmproject.da.entity.PersistedView;
import de.uos.se.prom.dsmproject.entity.Project;
import de.uos.se.utilities.XmlUtilities;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class ProjectAccess {

    private static final String TAG_ROOT = "DsmProject";
    private static final String TAG_PROJECT = "persistedProject";

    private static final String ATTRIBUTE_VERSION = "version";
    
    
    private static final String TAG_SERVERTIMESTAMP = "serverTimestamp";
    private static final String ATTRIBUTE_TIMESTAMP = "timestamp";

    /**
     * TODO get value from properties!
     */
    private static final String ATTRIBUTE_VERSION_VALUE = "0.0";
    static final String ATTRIBUTE_VERSION_MIN_VALUE = "0.0";

    @Deprecated
    private final boolean COMPRESS_IMAGES = false;

    /**
     * FA02
     */
    public Project loadProject(File file) throws SAXException, ParserConfigurationException, IOException, JAXBException {

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(file);

        //optional, but recommended
        //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
        doc.getDocumentElement().normalize();

        JAXBContext jaxbContext = JAXBContext.newInstance(
                PersistedProject.class,
                PersistedArtifact.class,
                PersistedArtifacttype.class,
                PersistedDependency.class,
                PersistedView.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        // Get file version from root tag
        Element root = (Element) doc.getElementsByTagName(TAG_ROOT).item(0);
        String fileVersion = root.getAttribute(ATTRIBUTE_VERSION);
        if (fileVersion.isEmpty()) {
            fileVersion = ATTRIBUTE_VERSION_MIN_VALUE;
        }
        

        // TODO unmarshall objects depending on file version!
        if (fileVersion.equals(ATTRIBUTE_VERSION_MIN_VALUE)) {
            // load model data
            NodeList projectNodes = doc.getElementsByTagName(TAG_PROJECT);
            Element projectNode = (Element) projectNodes.item(0);
            PersistedProject pProject = XmlUtilities.nodeToObject(projectNode, PersistedProject.class, unmarshaller);

            return new PersistedTypesDefactory().create(pProject);
        }

        return null;
    }
    
    /**
     * Load Project represented as String used for Webserver
     * @param project
     * @return Project
     * @throws SAXException
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws JAXBException
     */
    public Project loadProject(String project) throws SAXException, ParserConfigurationException, IOException, JAXBException {

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(new InputSource(new StringReader(project)));

        //optional, but recommended
        //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
        doc.getDocumentElement().normalize();

        JAXBContext jaxbContext = JAXBContext.newInstance(
                PersistedProject.class,
                PersistedArtifact.class,
                PersistedArtifacttype.class,
                PersistedDependency.class,
                PersistedView.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        // Get file version from root tag
        Element root = (Element) doc.getElementsByTagName(TAG_ROOT).item(0);
        String fileVersion = root.getAttribute(ATTRIBUTE_VERSION);
        if (fileVersion.isEmpty()) {
            fileVersion = ATTRIBUTE_VERSION_MIN_VALUE;
        }
        

        // TODO unmarshall objects depending on file version!
        if (fileVersion.equals(ATTRIBUTE_VERSION_MIN_VALUE)) {
            // load model data
            NodeList projectNodes = doc.getElementsByTagName(TAG_PROJECT);
            Element projectNode = (Element) projectNodes.item(0);
            PersistedProject pProject = XmlUtilities.nodeToObject(projectNode, PersistedProject.class, unmarshaller);

            return new PersistedTypesDefactory().create(pProject);
        }

        return null;
    }
    
    /**
     * save Project without Timestamp
     * @param project Project
     * @param file File
     * @throws TransformerException
     * @throws ParserConfigurationException
     */
    public void saveProject(Project project, File file) throws TransformerException, ParserConfigurationException {
    	saveProject(project,file,null);
    }
    
    /**
     * FA04
     */
    public void saveProject(Project project, File file, String timestamp) throws TransformerException, ParserConfigurationException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        Document doc = docBuilder.newDocument();

        Element root = doc.createElement(TAG_ROOT);

        // add file version to root tag
        root.setAttribute(ATTRIBUTE_VERSION, ATTRIBUTE_VERSION_VALUE);
        doc.appendChild(root);
        
        //Add Server Timestamp
        if(timestamp != null) {
        	Element serverTimestamp = doc.createElement(TAG_SERVERTIMESTAMP);
            serverTimestamp.setAttribute(ATTRIBUTE_TIMESTAMP, timestamp);
            root.appendChild(serverTimestamp);
        }
        
        
        PersistedProject pProject = new PersistedTypesFactory().create(project);

        // serialize model
        //XmlUtilities.addToXml(root, sTimestamp, ServerTimestamp.class);
        XmlUtilities.addToXml(root, pProject, PersistedProject.class);

        // save XML file
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(file);
        transformer.transform(source, result);
    }
    
    /**
     * Get Timestamp of serialized Project represented as String
     * @param project Project
     * @return Timestamp
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public String getProjectTimestamp(String project) throws ParserConfigurationException, SAXException, IOException {
    	  DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
          DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
          Document doc = dBuilder.parse(new InputSource(new StringReader(project)));

          //optional, but recommended
          //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
          doc.getDocumentElement().normalize();
          
          //Default Value for Timestamp is 0
          String t = "0";
          // Get server Timestamp if exists
          Element serverTimestamp = (Element) doc.getElementsByTagName(TAG_SERVERTIMESTAMP).item(0);
          if(serverTimestamp != null) {
        	  String timestamp = serverTimestamp.getAttribute(ATTRIBUTE_TIMESTAMP);
        	  t = timestamp;
          }
          
    	return t;
    }
    
    /**
     * Get Timestamp of serialized Project represented as File
     * @param project Project
     * @return Timestamp
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public String getProjectTimestamp(File project) throws ParserConfigurationException, SAXException, IOException {
    	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(project);

        //optional, but recommended
        //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
        doc.getDocumentElement().normalize();
        
      //Default Value for Timestamp is 0
        String t = "0";
        // Get server Timestamp if exists
        Element serverTimestamp = (Element) doc.getElementsByTagName(TAG_SERVERTIMESTAMP).item(0);
        if(serverTimestamp != null) {
        	String timestamp = serverTimestamp.getAttribute(ATTRIBUTE_TIMESTAMP);
      	  	t = timestamp;
        }
        
        return t;
    }
    
    

}
