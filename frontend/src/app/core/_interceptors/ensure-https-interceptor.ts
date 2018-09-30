import {Inject, Injectable} from '@angular/core';
import {
  HttpEvent, HttpInterceptor, HttpHandler, HttpRequest
} from '@angular/common/http';

import { Observable } from 'rxjs';


/** Pass untouched request through to the next request handler. */
@Injectable()
export class EnsureHttpsInterceptor implements HttpInterceptor {

  baseUrl: string = "https://localhost:8443/api";

  intercept(req: HttpRequest<any>, next: HttpHandler):
    Observable<HttpEvent<any>> {
      // clone request and replace 'http://' with 'https://' at the same time

      const fullUrlReq = req.clone( {
        url: this.baseUrl + req.url
        });

      const remDuplReq = fullUrlReq.clone( {
        url: fullUrlReq.url.replace("//localhost:9090/api", "")
      });

      const secureReq = remDuplReq.clone({
        url: remDuplReq.url.replace('http://', 'https://')
      });
      // send the cloned, "secure" request to the next handler.
      return next.handle(secureReq);
    }
}
