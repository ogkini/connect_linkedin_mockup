import {Attribute, Directive, Input} from '@angular/core';
import { AbstractControl, ValidationErrors, NG_VALIDATORS, Validator } from "@angular/forms";


@Directive({
  exportAs: 'text_val',
  selector: '[text_val]',
  providers: [{ provide: NG_VALIDATORS, useExisting: TextValidatorDirective, multi: true }]
})
export class TextValidatorDirective implements Validator {
  //@Input('text_val') text: string;
  //constructor( @Attribute('text_val') public text_val: string) {}

  validate(c: AbstractControl): ValidationErrors | null {

    return TextValidatorDirective.validateCharacters(c);
  }


  static validateCharacters(c: AbstractControl): ValidationErrors | null {
    
    // first check if the control has a value
    if ( c.value && c.value.length == 0 ) {
      //console.warn("Empty text-field.");
      return null;
    }
    /*else
      console.debug("'validateCharacters' received NOT NULL!");*/

    // setup simple regex for white listed characters
    var validCharacters: RegExp = /[^a-zA-Z\s]/;

    // match the control value against the regular expression
    const matches = c.value.match(validCharacters);

    // if there are matches return an object, else return null.
    return matches && matches.length ? { text_val: true } : null;
  }

}
