import { User, JobApply } from '.';

export class JobOffer {
  id: number;
  owner: User;
  text: string;
  createdTime: string;

  jobApplies: Array<JobApply>;

  appliedToJobOffer: boolean;
  jobAppliesCount: number;
}
