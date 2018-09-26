//export * from './jwt.interceptor';
//export * from './ensure-https-interceptor.ts';

/* "Barrel" of Http Interceptors */
import { HTTP_INTERCEPTORS } from '@angular/common/http';

import { EnsureHttpsInterceptor } from "./ensure-https-interceptor";
import {JwtInterceptor} from "./jwt.interceptor";

/** Http interceptor providers in outside-in order */
export const httpInterceptorProviders = [
  { provide: 'BASE_URL', useValue: 'https://localhost/' },
  { provide: HTTP_INTERCEPTORS, useClass: EnsureHttpsInterceptor, multi: true },
  { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true }
];
