package junitToHtml;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;

/**
 * Class to generate an HTML test report from an Junit XML test report.
 * This was created because with large log files, Junit will often run out of memory before
 *  successfully generating the HTML test report- however this is not an issue for XML test reports.
 */
public class JunitToHTML {

    /**
     * Takes one argument in the form of an absoute path to an Intellij XML test report.
     * @param args - One argument, being the abs path to the XML test report.
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException {

        ClassPathResource r = new ClassPathResource("test-results-template.html");
        File htmltemplateFile = r.getFile();
        String xmlFileName = "C:\\POCs_Automation\\CBJ-Automation\\TestSuite_Demo.xml";
        String htmlString = FileUtils.readFileToString(htmltemplateFile);

        File XMLFile = new File(xmlFileName);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(XMLFile);

        doc.getDocumentElement().normalize();

        //Loop tests in suite
        NodeList testSuiteList = doc.getElementsByTagName("testsuite");
        String content = "";

        for (int i = 0; i < testSuiteList.getLength(); i++) {

        	String testcaseTmpl = "<ul>";
            String testTmpl = "<ul>"
                    + "<li class=\"level suite\">"
                    + "<span><em class=\"time\">"
                    + "<div class=\"time\">$runduration s</div>"
                    + "</em><em class=\"status\">$status</em>$runname</span>";
                                                     
            
            Node testSuiteNode = testSuiteList.item(i);

            if (testSuiteNode.getNodeType() == Node.ELEMENT_NODE) {
            	String failedFlag = "Passed"; 
                String runBody = "";
                Element eElement = (Element) testSuiteNode;
                NodeList testCaseList = ((Element) testSuiteNode).getElementsByTagName("testcase");                
                
                //for Loop to populate child testcases
                for(int j=0;j<testCaseList.getLength();j++) {                
                    Node testCaseNode = testCaseList.item(j);
                    Element eTestcaseElement = (Element) testCaseNode;
                    runBody = testCaseNode.getTextContent();  
                    
                    String status = "";
                    if(!runBody.equals(""))
                    {
                    	status = "<font color=\"red\" size=\"3\">failed </font>";
                    	failedFlag = "<font color=\"red\" size=\"4\">Failed </font>";
                    	
                    }
                    else 
                    {
                    	status = "passed";
                    }
                    
                    testcaseTmpl += "<li class=\"level test\">"
                    + "		<span><em class=\"time\">"
                    + "		<div class=\"time\">$testrunduration s</div>"
                    + "		</em><em class=\"status\">$status</em>$testcase</span>"                   
                    + "		</li>"
                    + "		<ul><li>$runbody</li></ul>";
                
                    testcaseTmpl = testcaseTmpl.replace("$testrunduration", eTestcaseElement.getAttribute("time"));
                    testcaseTmpl = testcaseTmpl.replace("$testcase", eTestcaseElement.getAttribute("name"));
                    testcaseTmpl = testcaseTmpl.replace("$status", status);
                    testcaseTmpl = testcaseTmpl.replace("$runbody", runBody);
                    
                }
                
                testTmpl +=  testcaseTmpl + "</ul>"
                        + "</li>"
                        + "</ul>";
                
                testTmpl = testTmpl.replace("$runduration", eElement.getAttribute("time"));
                testTmpl = testTmpl.replace("$runname", eElement.getAttribute("name"));
                testTmpl = testTmpl.replace("$status", failedFlag);
                testTmpl = testTmpl.replace("$runbody", runBody);
                content += testTmpl;
            }

        }

        // Get suite attributes
        testSuiteList = doc.getElementsByTagName("testsuite");
        String title = "Junit - Test Results Summary";
        String duration = "";
        for (int i = 0; i < testSuiteList.getLength(); i++) {

            Node nNode = testSuiteList.item(i);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                Element eElement = (Element) nNode;
                //title = eElement.getAttribute("name");
                duration = Float.toString(Float.parseFloat(eElement.getAttribute("time"))/60);

            }
        }

        //Loop counts
        testSuiteList = doc.getElementsByTagName("testsuites");
        String total = "0";
        String passed = "0";
        String failed = "0";
        String skipped = "0";
        for (int i = 0; i < testSuiteList.getLength(); i++) {

            Node nNode = testSuiteList.item(i);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                  Element eElement = (Element) nNode;
                  total = eElement.getAttribute("tests");                
                  passed = Integer.toString(Integer.parseInt(eElement.getAttribute("tests")) - Integer.parseInt(eElement.getAttribute("skipped")) - Integer.parseInt(eElement.getAttribute("failures")));                
                  failed = eElement.getAttribute("failures");
                  skipped = eElement.getAttribute("skipped");
                  duration = Float.toString(Float.parseFloat(eElement.getAttribute("time"))/60);
                }
            }
        
        htmlString = htmlString.replace("$testname", title)
                                .replace("$duration", duration)
                                .replace("$total", total)
                                .replace("$passed", passed)
                                .replace("$failed", failed)
                                .replace("$skipped", skipped)
                                .replace("$content", content);

        File newHtmlFile = new File(title + ".html");
        System.out.println("The File path is : " + newHtmlFile.getAbsolutePath());
        FileUtils.writeStringToFile(newHtmlFile, htmlString);
    }

}