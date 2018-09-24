import { User, Relationship } from './index';

export class Network {
  connections: Array<User>;
  receivedRequests: Array<Relationship>;
  sentRequests: Array<Relationship>;
}
