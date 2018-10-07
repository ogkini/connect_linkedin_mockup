import { Injectable } from '@angular/core';
import {Validators} from "@angular/forms";
import {TextValidatorDirective} from "../_directives/validators/text_validator.directive";
import {PasswordConfirmValidatorDirective} from "../_directives/validators/password-confirm-validator.directive";

@Injectable()
export class ValidatorService {

  private minTextLength = 2;
  private maxTextLength = 45;
  private maxEmailLength = 65;
  private minPasswordLength = 6;
  private maxPasswordLength = 100;

  private firstnameValidatorsArray = [Validators.required, TextValidatorDirective.validateCharacters, Validators.minLength(this.minTextLength), Validators.maxLength(this.maxTextLength)];
  private lastnameValidatorsArray = [Validators.required, TextValidatorDirective.validateCharacters, Validators.minLength(this.minTextLength), Validators.maxLength(this.maxTextLength)];
  private emailValidatorsArray = [Validators.required, Validators.email, Validators.maxLength(this.maxEmailLength)];
  private passwordValidatorsArray = [Validators.required, Validators.minLength(this.minPasswordLength), Validators.maxLength(this.maxPasswordLength)];
  private confirmPasswordValidatorsArray = [PasswordConfirmValidatorDirective.validatePasswordConfirmation];

  constructor() {}

  public getFirstnameValidatorsArray() {
    return this.firstnameValidatorsArray;
  }

  public getLastnameValidatorsArray() {
    return this.lastnameValidatorsArray;
  }

  public getEmailValidatorsArray() {
    return this.emailValidatorsArray;
  }

  public getPasswordValidatorsArray() {
    return this.passwordValidatorsArray;
  }

  public getConfirmPasswordValidatorsArray() {
    return this.confirmPasswordValidatorsArray;
  }

}
