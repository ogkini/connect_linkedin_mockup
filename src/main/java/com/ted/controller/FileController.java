package com.ted.controller;

import com.ted.exception.FileStorageException;
import com.ted.repository.UserRepository;
import com.ted.response.UploadFileResponse;
import com.ted.service.FileStorageService;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api")
public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    FileStorageService fileStorageService;

    @Autowired
    private UserRepository userRepository;


    private static String userFileStoragePath;  // Set during run-time.
    private static String localImageDirectory = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "img";
    private static String profilePhotoBaseName = "profile_photo_";
    private static String genericPhotoName = "generic_profile_photo.png";

    /*@Autowired
    FileController() {
        this.userFileStoragePath = Paths.get(this.fileStorageService.getFileStorageLocation() + "/users");
    }*/

    @PostMapping("/uploads/users")
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
        user_id = userRepository.getIdByEmail(email); // != 0.
        if ( user_id == 0 ) {
            logger.error("Could not retrieve the id of the user with the email: " + email);
            return new UploadFileResponse(fileName, /*null,*/null, -1);
        }

        fileName = StringUtils  // StringUtils is faster ;-)
                .replace(fileName, fileName, profilePhotoBaseName + "{" + user_id + "}." + FilenameUtils.getExtension(fileName))
                .toLowerCase();

        // Update database with the new "profile_photo"-name..
        if ( userRepository.updatePictureById(fileName, user_id) == 0 ) {
            logger.error("Could not update the picture for user with id: " + user_id);
            return new UploadFileResponse(fileName, /*null,*/null, -1); // Don't want to store afile having n relation with the database.. so return..
        }

        // Send file to be stored.
        return uploadFile(file, fileName, File.separator + "users" + File.separator + Integer.toString(user_id) + File.separator + "photos");
    }


    @PostMapping("/uploads")
    public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file, String fileName, String innerDir /* e.g.: "/users", "/articles", ect. */) {

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

        //logger.debug("Incoming fileName is: ", file_name);   // DEBUG!
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


    @GetMapping("/uploads/users")
    public ResponseEntity<Resource> LoadUserFile(@RequestParam("user_id") String user_id, HttpServletRequest request) {
        // Load file as Resource

        if ( user_id == null ) {
            logger.warn("Incoming id in \"LoadUserFile()\" was null!");
            return ResponseEntity.notFound().build();
        }

        // Get "Users.picture" for the "u.user_id". (from UserRepository)
        List<String> pictureList = userRepository.getPictureById(Integer.parseInt(user_id));
        String user_picture;
        String fileFullPath;

        if ( pictureList.isEmpty() ) {
            logger.error("No pictureList was returned for user with \"user_id\": ", user_id);
            return ResponseEntity.notFound().build();
        }
        else if ( pictureList.size() > 1 ) {
            logger.error("PictureList returned more than one pictures for user with \"user_id\": ", user_id);
            return ResponseEntity.notFound().build();
        }
        else {
            if ( userFileStoragePath == null )
                userFileStoragePath = Paths.get(fileStorageService.getFileStorageLocation() + File.separator + "users").toString();

            user_picture = pictureList.get(0);
            if ( user_picture == null ) {
                logger.debug("No picture was found for user with \"user_id\": {}. Loading the generic one.", user_id);
                user_picture = genericPhotoName;
                fileFullPath = localImageDirectory + File.separator + user_picture;
            }
            else {
                fileFullPath = userFileStoragePath + File.separator + user_id + File.separator + "photos" + File.separator + user_picture;
            }
        }

        Resource resource = fileStorageService.loadFileAsResource(fileFullPath);

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
}
