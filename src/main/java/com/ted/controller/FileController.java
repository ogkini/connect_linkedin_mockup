package com.ted.controller;

import com.ted.exception.FileStorageException;
import com.ted.repository.UserRepository;
import com.ted.response.UploadFileResponse;
import com.ted.service.FileStorageService;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api")
public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    private static String profilePhotoBaseName = "profile_photo_";

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/uploads/profilePhotos")
    public UploadFileResponse uploadProfilePhoto(@RequestParam("file") MultipartFile file,
                                                 @RequestParam("email") String email) {
        String fileName = file.getOriginalFilename();

        if (fileName == null) {
            logger.error("Failure when retrieving the filename of the incoming file!");
            return new UploadFileResponse(null, /*null,*/null, -1);
        }

        try {   // Wait for the user registration to finish.. before searching for his email.
            Thread.sleep(1000);
        } catch (Exception e) {
            logger.warn("", e);
        }

        // Find ID based on email. Connect with the DB and retrieve it.

        // First check if the user actually exists in DataBase.
        if ( !userRepository.findByEmail(email).isPresent() ) {
            logger.error("The user with the following email doesn't exist! email: " + email);
            return new UploadFileResponse(fileName, /*null,*/null, -1);
        }

        int user_id = 0;
        user_id = userRepository.getId(email); // != 0.
        if ( user_id == 0 ) {
            logger.error("Could not retrieve the id of the user with the email: " + email);
            return new UploadFileResponse(fileName, /*null,*/null, -1);
        }

        fileName = StringUtils  // StringUtils is faster ;-)
                .replace(fileName, fileName, profilePhotoBaseName + "{" + user_id + "}." + FilenameUtils.getExtension(fileName))
                .toLowerCase();

        // Update database with the new "profile_photo"-name..
        if ( userRepository.updatePictureName(fileName, user_id) == 0 ) {
            logger.error("Could not update the picteure for user with id: " + user_id);
            return new UploadFileResponse(fileName, /*null,*/null, -1); // Don't want to store afile having n relation with the database.. so return..
        }

        return uploadFile(file, fileName, "profilePhotos");
    }


    @PostMapping("/uploads")
    public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file, String fileName, String innerDir /* e.g.: "/articles" */) {

        String file_name;
        if ( fileName == null ) {
            file_name = file.getOriginalFilename();
            if ( file_name == null ) {
                logger.error("Failure when retrieving the filename of the incoming file!");
                return new UploadFileResponse(null, /*null,*/null, -1);
            }
        }
        else
            file_name = fileName;

        logger.debug("Incoming fileName is: ", file_name);   // DEBUG!
        //logger.debug("FileContentType: ", file.getContentType());   // DEBUG!

        try {
            file_name = fileStorageService.storeFile(file, file_name, innerDir);
        } catch (IOException e) {
            throw new FileStorageException();
        }

        // Produce download Location..
        /*String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(file_name)
                .toUriString();*/
        //logger.debug("The produced fileDownloadUri is: ", fileDownloadUri);   // DEBUG!

        return new UploadFileResponse(file_name, /*fileDownloadUri,*/ file.getContentType(), file.getSize());
    }


/*
    @GetMapping("/downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        // Load file as Resource
        Resource resource = fileStorageService.loadFileAsResource(fileName);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if ( contentType == null ) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
    */
}
