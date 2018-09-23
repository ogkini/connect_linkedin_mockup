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


  public onImageChange($event) {

    //console.debug("Inside 'onImageChange' in the file-service!");

    this.onFileChange($event);

    //console.debug("After taking the file from event! Filename is: ", this.fileToUpload.name);

    // Take the file-extension.
    let curFileExtension = this.fileToUpload.name.split(".", 2)[1];
    //console.debug("FileType: " + curFileExtension)
    if ( !this.imgFormatsAllowed.includes(curFileExtension) ) {  // If it's not an imageType-file..
      this.fileToUpload = null; // Don't accept this file (don't send it to the backend).
      console.warn("Invalid file detected: " + curFileExtension + "\nAllowed file types are: ");
      for( let fileType of this.imgFormatsAllowed )
        console.debug(fileType);
    }
  }

  public onFileChange($event) {
    this.fileToUpload = $event.target.files[0];
  }

  public onFileReset() {
    this.fileToUpload = null;
  }

  public get fileName() : string {
    if ( this.fileToUpload != null )
      return this.fileToUpload.name;
    else
      return null;
  }

  endpoint: string;

  public postFile(user_email: string)
  {
      if ( this.fileToUpload == null ) {
        console.warn("FileToUpload was null. No POST was made!");
        return;
      }
      /*else
        console.debug("1 step away from the POST... Our filename is: ", this.fileName);*/

      if ( user_email != null ) // Going to upload to UsersEndpoint..
        this.endpoint = this.connConfig.userFilesEndpoint;
      else
        this.endpoint = this.connConfig.filesEndpoint;

      const formData: FormData = new FormData();
      formData.append('file', this.fileToUpload, this.fileToUpload.name);
      formData.append("email", user_email); // It's ok to send null.. it's useful in the backend.

      return this.httpClient.post(this.connConfig.serverUrl + this.endpoint , formData)
                            .subscribe(response => console.log("Response: " + response),
                                                        this.fileToUpload = null);  // Reset value.
  }

}
