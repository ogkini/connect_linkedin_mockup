import { User, JobApply } from '.';

export class JobOffer {
  id: number;
  owner: User;
  text: string;
  createdTime: string;

  applies: Array<JobApply>;

  appliedToJob: boolean;

  appliesCount: number;
}
