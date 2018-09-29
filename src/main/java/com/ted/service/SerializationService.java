package com.ted.service;

import com.ctc.wstx.api.WstxInputProperties;
import com.ctc.wstx.api.WstxOutputProperties;
import com.ctc.wstx.stax.WstxInputFactory;
import com.ctc.wstx.stax.WstxOutputFactory;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlFactory;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.ted.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import java.io.File;
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

    private String xmlFullFilePath = localXMLdirectory + File.separator + "usersData.xml";

    public SerializationService()
    {
        // Input factory
        XMLInputFactory ifactory = new WstxInputFactory();
        ifactory.setProperty(WstxInputProperties.P_MAX_ATTRIBUTE_SIZE, 32000);

        // Output Factory
        XMLOutputFactory ofactory = new WstxOutputFactory();
        ofactory.setProperty(WstxOutputProperties.P_OUTPUT_CDATA_AS_TEXT, true);

        // I/O Factory
        XmlFactory xf = new XmlFactory(ifactory, ofactory);
        this.xmlMapper = new XmlMapper(xf); // there are other overloads too
        this.xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
        this.xmlMapper.registerModule(new JacksonXmlModule());
    }

    public String serializeToXML(List<String> usersIDs)
    {
        try {
            Path path = Paths.get(this.xmlFullFilePath);
            Files.deleteIfExists(path); // Delete the file
            Files.createDirectories(Paths.get(localXMLdirectory));  // Create the directories (not the file)
            File xmlFile = new File(this.xmlFullFilePath);
            //JsonGenerator gen = this.xmlMapper.getFactory().createGenerator(new FileOutputStream(xmlFile));

            // For the given users-IDs retrieve the user's data from the dataBase (with SELECT-queries).
            List<User> users = new ArrayList<User>();
            for ( String userId: usersIDs ) {
                users.add(this.userService.getByIdCustom(Long.parseLong(userId)));
                //this.xmlMapper.writeValue(gen, this.userService.getByIdCustom(userId.longValue()));   // It gives exception: "com.fasterxml.jackson.core.JsonGenerationException: Trying to output second root, <User>"
            }

            // Write to XML file
            this.xmlMapper.writeValue(xmlFile, users);

            return this.xmlFullFilePath;

        }catch (Exception e) {
            logger.error("", e);
            return null;
        }
    }

}
