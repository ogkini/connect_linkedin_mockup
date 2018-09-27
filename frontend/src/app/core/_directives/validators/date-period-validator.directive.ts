import { Attribute, Directive, Input } from '@angular/core';
import { AbstractControl, ValidationErrors, NG_VALIDATORS, FormControl, Validator } from "@angular/forms";
import { Subscription } from "rxjs";
import {DateService} from "../../_services/date.service";


@Directive({
  exportAs: 'date_check_fail',
  selector: '[date_check_fail]',
  providers: [{ provide: NG_VALIDATORS, useExisting: DatePeriodValidatorDirective, multi: true }]
})
export class DatePeriodValidatorDirective implements Validator {
  //@Input('compare_pass') public validateEqual: string;
  //constructor( @Attribute('date_check') public date_check: any) {}

  constructor() {}

  validate(c: AbstractControl): ValidationErrors | null {

    return DatePeriodValidatorDirective.validateDatePeriod(c);
  }


  static validateDatePeriod(c: AbstractControl): ValidationErrors | null {

    if (c.value == null || c.value.length == 0) {
      //console.warn("Empty endYear field.");
      return null; // Skip empty values.
    }

    const endYear = c.value;
    // c.value is the "endYear", so we have to take the endMonth from "parent", to construct the endDate.
    const endMonthControl = c.parent.get('endMonth');

    // Take the "startDate".

    const startMonthControl = c.parent.get('startMonth');
    const startYearControl = c.parent.get('startYear');

    // Verify we got all the values.. if not, return null.
    let haveControl;
    if ( endMonthControl && startYearControl && startMonthControl ) {
      haveControl = true;
      const subscription1: Subscription = endMonthControl.valueChanges.subscribe(() => {
        endMonthControl.updateValueAndValidity();
        subscription1.unsubscribe();
      });
      const subscription2: Subscription = startYearControl.valueChanges.subscribe(() => {
        startYearControl.updateValueAndValidity();
        subscription2.unsubscribe();
      });
      const subscription3: Subscription = startMonthControl.valueChanges.subscribe(() => {
        startMonthControl.updateValueAndValidity();
        subscription3.unsubscribe();
      });
    }

    // Create the start and end dates
    let endDate = DateService.createDate(endYear, endMonthControl.value);
    let startDate = DateService.createDate(startYearControl.value, startMonthControl.value);

    //console.debug("DateObjects:\nStartDate:", startDate, "\nEndDate:", endDate);

    /*if ( startDate < endDate )
      console.debug("StartDate is earlier than the endDate, all good!");
    else
      console.debug("EndDate is earlier than the startDate, not good!");*/

    return haveControl && startDate > endDate ? { date_check_fail: true } : null;
  }

}
