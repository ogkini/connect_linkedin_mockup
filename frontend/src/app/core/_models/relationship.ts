import { User } from './index';

export class Relationship {
  id: number;
  sender: User;
  receiver: User;
  status: number;
  seen: boolean;
}
