import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Conversation, ConversationRequest, Message } from '../_models';
import { ConnectionConfigService } from './connection-config.service';

@Injectable()
export class ConversationService {

  constructor(
    private httpClient: HttpClient,
    private connConfig: ConnectionConfigService
  ) { }

  // Returns all conversations of a user
  getAll() {
    return this.httpClient.get<Conversation[]>(this.connConfig.conversationsEndpoint);
  }

  // A user initiates a conversation
  create(conversationRequest: ConversationRequest) {
    return this.httpClient.post(this.connConfig.conversationsEndpoint, conversationRequest);
  }

  // A user sends a message in a conversation
  createMessage(message: Message, conversationId: number) {
    return this.httpClient.post(this.connConfig.conversationsEndpoint + '/' +
        conversationId + '/' + this.connConfig.messagesEndpoint, message);
  }

}
