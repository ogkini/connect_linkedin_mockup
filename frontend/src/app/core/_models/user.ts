import { Occupation, Experience, Education, Skill } from '.';

export class User {
  id: number;
  firstname: string;
  lastname: string;
  email: string;
  password: string;
  picture: string;

  occupation: Occupation;
  experience: Array<Experience>;
  education: Array<Education>;
  skills: Array<Skill>;

  newFriendRequests: number;
  newMessages: number;
  newNotifications: number;

  relationshipBetween: boolean;
}
