import { Component, OnInit } from '@angular/core';
import { Title } from "@angular/platform-browser";
import { FormGroup, FormBuilder, Validators } from "@angular/forms";
import { first } from "rxjs/operators";

import { User, Conversation, Message, CreationResponse } from '../../../_models';
import { ConversationService, ConnectionConfigService } from '../../../_services';

@Component({
  selector: 'app-messages',
  templateUrl: './messages.component.html',
  styleUrls: ['./messages.component.scss']
})
export class MessagesComponent implements OnInit {

  title: string = 'My Messages';
  signedInUser: User;
  conversations: Conversation[] = [];
  messages: Message[] = [];
  activeConversationId: number;

  sendMessageForm: FormGroup;

  public profilePhotosEndpoint: string;

  constructor(
      private titleService: Title,
      private formBuilder: FormBuilder,
      private conversationService: ConversationService,
      private connConfig: ConnectionConfigService
  ) {
    this.titleService.setTitle(this.title);
    this.signedInUser = JSON.parse(localStorage.getItem('currentUser'));
    this.profilePhotosEndpoint = this.connConfig.usersEndpoint;
  }

  ngOnInit() {
    this.getConversations();

    // Initialise form contents
    this.sendMessageFormInit();
  }

  // Returns all conversations of a user
  private getConversations() {
    this.conversationService.getAll().subscribe(conversations => { this.conversations = conversations; });
  }

  // Initiliases the form to send a new message
  sendMessageFormInit() {
    this.sendMessageForm = this.formBuilder.group({
      text: ['']
    });
  }

  // Convenience getters for easy access to form fields
  get getSendMessageForm() { return this.sendMessageForm.controls; }

  // A user creates a post
  sendMessage() {
    // Create a new Post object
    const newMessage: Message = {
      text: this.getSendMessageForm.text.value
    } as Message;

    // Submit the post to the server
    this.conversationService.createMessage(newMessage, this.activeConversationId)
      .pipe(first())
      .subscribe((response: CreationResponse) => {
          // Add the message to the array
          if (response.object) {
            this.conversations.find(conversation => conversation.id == this.activeConversationId).messages.push(response.object);
            this.messages.push(response.object);
          }

          // Clear the form
          this.sendMessageFormInit();
        }, error => {
          console.error(error);
        }
      );
  }

  // Copies the messages of the selected conversation
  // to a temporary array for display purposes
  public showMessages(conversationId: number) {
    let conversation = this.conversations.find(conversation => conversation.id == conversationId);
    this.messages = JSON.parse(JSON.stringify(conversation.messages));

    this.activeConversationId = conversationId;
  }

}
