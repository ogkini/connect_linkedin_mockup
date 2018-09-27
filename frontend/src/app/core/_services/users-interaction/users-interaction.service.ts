import { Injectable } from '@angular/core';
import {CreationResponse, FriendRequest} from "../../_models";
import {first} from "rxjs/operators";
import {RelationshipService} from "../relationship.service";

@Injectable()
export class UsersInteractionService {

  constructor(
    private relationshipService: RelationshipService,
  ) { }

  sendFriendRequest(userId: number, ) {
    // Create a new Friend Request object
    const newFriendRequest: FriendRequest = {
      receiver: userId,
    } as FriendRequest;

    // Submit the Friend Request to the server
    this.relationshipService.create(newFriendRequest)
      .pipe(first())
      .subscribe(error => {
          console.error(error);
        }
      );
  }

  sendMessage() {
    console.debug("Going to send a message..");
  }


}
