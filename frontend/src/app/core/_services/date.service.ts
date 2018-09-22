import { Injectable } from '@angular/core';

@Injectable()
export class DateService {

  constructor() {}

// Creates and returns a valid date string
  public static createDate(year: number, month: number) {
    if (month < 10)
      return `${year}-0${month}-01`;
    else
      return `${year}-${month}-01`;
  }
}
