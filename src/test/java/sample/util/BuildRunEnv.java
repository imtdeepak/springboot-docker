package sample.util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BuildRunEnv {
    // For testing Local
    static String[] envFiles = {".env.local.pcs"};

    static String   ideaWorkspace    = ".idea/workspace.xml";
    static String   patternString    = "^export ([^=]+)=(\')?(.+)(\')?$";
    static String[] xPathConfigs     = {
            "/project/component[@name='RunManager']/configuration[@type='JUnit']",
            "/project/component[@name='RunManager']/configuration[@type='SpringBootApplicationConfigurationType']",
            "/project/component[@name='RunManager']/configuration[@type='Application']"
    };
    static String   xPathEnvNodeName = "envs";

    static HashMap<String, String> env;

    public static void main(String[] args) {
        try {
            readEnv();
            setIdeaEnvironment();
        } catch (Throwable e) {
            System.out.println(e.toString());
        }
    }

    public static void readEnv() throws IOException {
        env = new HashMap<>();
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher;
        String key, value;
        for (String fileName : envFiles) {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                matcher = pattern.matcher(line);
                if (matcher.find()) {
                    value = matcher.group(3).replaceAll("(^')|('$)", "");
                    env.put(matcher.group(1), value);
                }
            }
            bufferedReader.close();
        }
    }

    public static Document readIdeaWorkspace() throws IOException, SAXException, ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setIgnoringElementContentWhitespace(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        File file = new File(ideaWorkspace);
        return builder.parse(file);
    }

    public static Node buildEnvsNode(Document document) {
        Node newEnvsNode = document.createElement("envs");
        //env.forEach(k, v) ->
        Iterator it = env.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            Element envNode = (Element) document.createElement("env");
            envNode.setAttribute("name", pair.getKey().toString());
            envNode.setAttribute("value", pair.getValue().toString());
            newEnvsNode.appendChild(envNode);
        }
        return newEnvsNode;
    }

    public static void saveIdeaWorkspace(Document document) throws TransformerException {
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        Result output = new StreamResult(new File(ideaWorkspace));
        Source input = new DOMSource(document);

        transformer.transform(input, output);
    }

    public static void setIdeaEnvironment() throws ParserConfigurationException, SAXException, IOException, XPathExpressionException, TransformerException {
        Document doc = readIdeaWorkspace();

        XPath xPath = XPathFactory.newInstance().newXPath();
        for (String xPathConfig : xPathConfigs) {
            NodeList configNodes = (NodeList) xPath.compile(xPathConfig).evaluate(doc, XPathConstants.NODESET);

            for (int i = 0; i < configNodes.getLength(); i++) {
                Element configNode = (Element) configNodes.item(i);
                if (configNode != null && configNode.getNodeType() == Node.ELEMENT_NODE) {
                    Node oldEnvsNode = (Node) xPath.compile(xPathEnvNodeName).evaluate(configNode, XPathConstants.NODE);
                    Node newEnvsNode = buildEnvsNode(doc);

                    if (oldEnvsNode != null) {
                        configNode.removeChild(oldEnvsNode);
                    }
                    if (newEnvsNode != null) {
                        configNode.appendChild(newEnvsNode);
                    }
                }
            }
        }
        saveIdeaWorkspace(doc);
    }
}