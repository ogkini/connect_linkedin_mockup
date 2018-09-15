import { Attribute, Directive, Input } from '@angular/core';
import { AbstractControl, ValidationErrors, NG_VALIDATORS, FormControl, Validator } from "@angular/forms";
import { Subscription } from "rxjs";


@Directive({
  exportAs: 'compare_pass',
  selector: '[compare_pass]',
  providers: [{ provide: NG_VALIDATORS, useExisting: PasswordConfirmValidatorDirective, multi: true }]
})
export class PasswordConfirmValidatorDirective implements Validator {
  //@Input('compare_pass') public validateEqual: string;
  //constructor( @Attribute('compare_pass') public compare_pass: string) {}


  validate(c: AbstractControl): ValidationErrors | null {

    /*console.debug("Inside password validate..");
    var retVal = PasswordConfirmValidatorDirective.validatePasswordConfirmation(c);
    console.debug("'validatePasswordConfirmation' returned: ", retVal);
    return retVal;*/

    return PasswordConfirmValidatorDirective.validatePasswordConfirmation(c);
  }


  static validatePasswordConfirmation(c: AbstractControl): ValidationErrors | null {

    //console.debug("Inside validatePasswordConfirmation !");

    if (c.value == null || c.value.length == 0) {
      //console.warn("Empty password field.");
      return null; // Skip empty values.
    }

    const controlToCompare = c.parent.get('password');

    //console.debug("'controlToCompare' returned: ", controlToCompare);

    if ( controlToCompare ) {
      const subscription: Subscription = controlToCompare.valueChanges.subscribe(() => {
        c.updateValueAndValidity();
        subscription.unsubscribe();
      });
    }

    return controlToCompare && controlToCompare.value != c.value ? { compare_pass: true } : null;
  }

}
