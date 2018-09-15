import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { ConnectionConfigService } from "../connection-config.service";


@Injectable()
export class FileUploaderService {

  fileToUpload: File;
  imgFormatsAllowed = ["jpg","png"];

  constructor(
    private httpClient: HttpClient,
    private connConfig: ConnectionConfigService
  ) { }


  public onImageChanged(event) {

    console.debug("Inside 'onImageChanged' in the file-service!");

    this.fileToUpload = event.target.files[0];

    console.debug("After taking the file from event! Filename is: ", this.fileToUpload.name);

    // Test: We want to split the fileName into two strings and take only the fileFormat (the 2nd string). --> It works!
    console.debug("FileType: " + this.fileToUpload.name.split(".", 2)[1])

    if ( !this.imgFormatsAllowed.includes(this.fileToUpload.name.split(".", 2)[1]) ) {
      console.debug("Invalid file detected! Allowed file types are: ");
      for( let fileType of this.imgFormatsAllowed )
        console.debug(fileType);
    }
  }

  public get fileName() : string {
    if ( this.fileToUpload != null )
      return this.fileToUpload.name;
    else
      return null;
  }

  endpoint: string;

  postFile(user_email: string)
  {
      if ( this.fileToUpload == null ) {
        console.warn("FileToUpload was null. No POST was made!");
        return;
      }
      /*else
        console.debug("1 step away from the POST... Our filename is: ", this.fileName);*/

      if ( user_email != null ) // Going to upload a photoProfile..
        this.endpoint = this.connConfig.uploadProfilePhotoEndpoint;
      else
        this.endpoint = this.connConfig.uploadEndpoint;

      const formData: FormData = new FormData();
      formData.append('file', this.fileToUpload, this.fileToUpload.name);
      formData.append("email", user_email); // It's ok to send null.. it's usefull in the backend.

      return this.httpClient.post(this.connConfig.serverUrl + this.endpoint , formData)
                            .subscribe(response => console.log("Response: " + response));
  }

}
