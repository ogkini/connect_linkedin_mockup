package com.ted.service;

import com.ctc.wstx.api.WstxInputProperties;
import com.ctc.wstx.api.WstxOutputProperties;
import com.ctc.wstx.stax.WstxInputFactory;
import com.ctc.wstx.stax.WstxOutputFactory;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlFactory;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import com.ted.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;
import java.io.File;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static com.ted.controller.FileController.localXMLdirectory;


@Service
public class SerializationService {

    private static final Logger logger = LoggerFactory.getLogger(SerializationService.class);

    @Autowired
    private UserService userService;

    private XmlMapper xmlMapper;
    private XMLStreamWriter streamWriter;

    private String xmlFullFilePath = localXMLdirectory + File.separator + "usersData.xml";

    public SerializationService()
    {
        // Input factory
        XMLInputFactory ifactory = new WstxInputFactory(); // Woodstox XMLInputFactory impl
        ifactory.setProperty(WstxInputProperties.P_MAX_ATTRIBUTE_SIZE, 32000);

        StringWriter stringWriter = new StringWriter();

        // Output Factory
        XMLOutputFactory ofactory = new WstxOutputFactory(); // Woodstox XMLOutputfactory impl
        try {
            this.streamWriter = ofactory.createXMLStreamWriter(stringWriter);
        } catch (Exception e) {
            logger.error("", e);
            return;
        }

        ofactory.setProperty(WstxOutputProperties.P_OUTPUT_CDATA_AS_TEXT, true);

        // I/O Factory
        XmlFactory xf = new XmlFactory(ifactory, ofactory);

        this.xmlMapper = new XmlMapper(xf); // there are other overloads too

        this.xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);

        this.xmlMapper.registerModule(new JaxbAnnotationModule());

        try {
            Path path = Paths.get(xmlFullFilePath);
            Files.deleteIfExists(path); // Delete the file
            Files.createDirectories(Paths.get(localXMLdirectory));  // Create the directories (not the file)
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public String serializeToXML(List<Integer> usersIDs)
    {
        String xmlFulPath = xmlFullFilePath;

        File xmlFile = new File(xmlFulPath);

        try {
            // For the given users-IDs retrieve the user's data frm the dataBase (with SELECT-queries)
            // Start from just the "Users"-table and then we will see how to take everything else as well.
            List<User> users = new ArrayList<User>();
            for ( Integer userId: usersIDs ) {
                users.add(this.userService.getByIdCustom(userId.longValue()));
            }

            // Write to XML file
            this.streamWriter.writeStartDocument();
            this.xmlMapper.getFactory().createGenerator(this.streamWriter);
            this.streamWriter.writeStartElement("root");

            this.xmlMapper.writeValue(xmlFile, users);

            this.streamWriter.writeEndElement();
            this.streamWriter.writeEndDocument();
            /*

            //this.xmlMapper.writeValue(streamWriter, employeeBean);

            this.streamWriter.writeComment("Some insightful commentary here");

            // Take input String from memory, transform and return to memory.
            //String xml = this.xmlMapper.writeValueAsString(new SpanShapeRenderer.Simple());

            // Or read from somewhere and write to file.
            //this.xmlMapper.readValue(...);  // Read XML-data.
            this.xmlMapper.writeValue(new File(xmlFulPath), new User());  // Write to jsonFile.*/

            return xmlFulPath;

        }catch (Exception e) {
            logger.error("", e);
            return null;
        }
    }

}
