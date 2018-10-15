import { Injectable } from '@angular/core';

@Injectable()
export class UsersCheckBoxService {

  private userIDs: string[] = [];
  public selectedAllUsers = false;

  public getUserIDsList(): string[] {
    return this.userIDs;
  }

  public getUserIDsListLength(): number {
    // Bypass getting the whole list just for checking its length.
    return this.userIDs.length;
  }

  public clearUserIDsList(): void {
    this.userIDs = [];
  }

}
