package com.ted.controller;

import com.ted.exception.FileNotFoundException;
import com.ted.exception.FileStorageException;
import com.ted.exception.NotAuthorizedException;
import com.ted.repository.UserRepository;
import com.ted.response.UploadFileResponse;
import com.ted.security.CurrentUser;
import com.ted.security.UserDetailsImpl;
import com.ted.service.FileStorageService;
import com.ted.service.SerializationService;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Paths;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api")
public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    FileStorageService fileStorageService;

    @Autowired
    private SerializationService serializationService;

    @Autowired
    private UserRepository userRepository;

    private static String userFileStoragePath;  // Set during run-time.
    public static String currentDirectory = System.getProperty("user.dir");
    public static String localResourcesDirectory = currentDirectory + File.separator + "src" + File.separator + "main" + File.separator + "resources";
    public static String localXMLdirectory = currentDirectory + File.separator + "xml";
    private static String localImageDirectory = localResourcesDirectory + File.separator + "img";
    private static String profilePhotoBaseName = "profile_photo_";
    private static String genericPhotoName = "generic_profile_photo.png";
    private static String imageNotFoundName = "image_not_found.png";

    /*@Autowired
    FileController() {
        this.userFileStoragePath = Paths.get(this.fileStorageService.getFileStorageLocation() + "/users");
    }*/


    @PostMapping("/users/{email}/photos")
    public UploadFileResponse uploadProfilePhoto(@RequestParam("file") MultipartFile file,
                                                 @PathVariable(value = "email") String email) {

        String fileName = file.getOriginalFilename();

        if ( fileName == null ) {
            logger.error("Failure when retrieving the filename of the incoming file!");
            return new UploadFileResponse(null, /*null,*/null, -1);
        }

        try {   // Wait for the user registration to finish.. before hitting the dataBase using his email.
            Thread.sleep(1000);
        } catch (Exception e) {
            logger.warn("", e);
        }

        // Find ID based on email. Connect with the DB and retrieve it.

        int user_id = 0;
        List<BigInteger> userIDs = userRepository.getIdByEmail(email); // != 0.
        if ( userIDs == null || userIDs.isEmpty() ) {
            logger.error("Could not retrieve the id of the user with the email: " + email);
            return new UploadFileResponse(fileName, /*null,*/null, -1);
        }
        else if ( userIDs.size() > 1 ) {    // This should never happen!
            logger.error("Found more than one user with the email: " + email);
            return new UploadFileResponse(fileName, /*null,*/null, -1);
        }
        else
            user_id = ((BigInteger)userIDs.get(0)).intValue();

        fileName = StringUtils  // StringUtils is faster ;-)
                .replace(fileName, fileName, profilePhotoBaseName + "{" + user_id + "}." + FilenameUtils.getExtension(fileName))
                .toLowerCase();

        // Update database with the new "profile_photo"-name..
        if ( userRepository.updatePictureById(fileName, user_id) == 0 ) {
            logger.error("Could not update the picture for user with id: " + user_id);
            return new UploadFileResponse(fileName, /*null,*/null, -1); // Don't want to store afile having n relation with the database.. so return..
        }
        else
            userRepository.flush(); // We want the DB to be updated immediately.

        // Send file to be stored.
        return uploadFile(file, fileName, File.separator + "users" + File.separator + Integer.toString(user_id) + File.separator + "photos");
    }


    @PostMapping("/uploads")
    public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file, String fileName, String innerDir) {

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


    @GetMapping("/users/{userId}/photos")
    public ResponseEntity<Resource> getProfilePhoto(@PathVariable(value = "userId") Long userId, HttpServletRequest request) {
        // Load file as Resource

        if ( userId == null ) {
            logger.warn("Incoming id in \"getProfilePhoto()\" was null!");
            return ResponseEntity.notFound().build();
        }
        else
            userRepository.flush(); // Updated the DB.

        // Get "Users.picture" for the "u.user_id". (from UserRepository)
        List<String> pictureList = userRepository.getPictureById(userId);
        String user_picture;
        String fileFullPath;

        if ( pictureList.isEmpty() ) {
            logger.error("No pictureList was returned for user with \"user_id\": ", userId);
            return ResponseEntity.notFound().build();
        }
        else if ( pictureList.size() > 1 ) {
            logger.error("PictureList returned more than one pictures for user with \"user_id\": ", userId);
            return ResponseEntity.notFound().build();
        }
        else {
            if ( userFileStoragePath == null )
                userFileStoragePath = Paths.get(fileStorageService.getFileStorageLocation() + File.separator + "users").toString();

            user_picture = pictureList.get(0);
            if ( user_picture == null ) {
                logger.debug("No picture was found for user with \"user_id\": {}. Loading the generic one.", userId);
                user_picture = genericPhotoName;
                fileFullPath = localImageDirectory + File.separator + user_picture;
            }
            else {
                fileFullPath = userFileStoragePath + File.separator + userId + File.separator + "photos" + File.separator + user_picture;
            }
        }

        Resource resource;
        try {
            resource = fileStorageService.loadFileAsResource(fileFullPath);
        } catch (FileNotFoundException fnfe) {
            if ( user_picture != null ) {   // If the dataBase says that this user has its own profilePhoto, but it was not found in storage..

                // Wait a bit and retry.. since the user may has just signUp-ed and the file may not be available right-away..
                try {
                    Thread.sleep(5000);
                    resource = fileStorageService.loadFileAsResource(fileFullPath);
                } catch(Exception e) {
                    logger.error("", e);
                    // Loading the "image_not_found", so that the user will be notified that sth's wrong with the storage of its picture, even though one was given.
                    fileFullPath = localImageDirectory + File.separator + imageNotFoundName;
                    try {
                        resource = fileStorageService.loadFileAsResource(fileFullPath);
                    } catch (FileNotFoundException fnfe2) {
                        logger.error("The \"" + imageNotFoundName + "\" was not found in storage!");
                        return ResponseEntity.notFound().build();
                    }
                }
            }
            else {
                logger.error("The \"" + genericPhotoName + "\" was not found in storage!");
                return ResponseEntity.notFound().build();
            }
        }

        return GetFileResponse(request, resource);
    }

    @GetMapping("/users/getXMLdata")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Resource> getUsersXMLdata(@RequestParam("usersIDs") List<String> usersIDs,
                                                    HttpServletRequest request,
                                                    @Valid @CurrentUser UserDetailsImpl currentUser) {

        if (currentUser == null)
            logger.debug("Current user is null");
        else if (!currentUser.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")) ) {
            throw new NotAuthorizedException("You are not authorized to access this resource.");
        }

        String fileFullPath = serializationService.serializeToXML(usersIDs);

        // Get the XML data from storage.
        Resource resource;
        try {
            resource = fileStorageService.loadFileAsResource(fileFullPath);
        } catch (FileNotFoundException e) {
            logger.error("", e);
            return ResponseEntity.notFound().build();
        }

        return GetFileResponse(request, resource);
    }


    private ResponseEntity<Resource> GetFileResponse(HttpServletRequest request, Resource resource) {
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
        /*else
            logger.debug("File: \"" + resource.getFilename() + "\" has contentType: \"" + contentType + "\"");*/

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

}
