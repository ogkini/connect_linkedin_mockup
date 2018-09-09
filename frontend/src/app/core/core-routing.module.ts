import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { WelcomeComponent } from './_components/welcome/welcome.component';
import { SignInComponent } from './_components/sign-in/sign-in.component';
import { SignUpComponent } from './_components/sign-up/sign-up.component';
import { HomeAdminComponent } from './_components/admin/home-admin/home-admin.component';
import { HomeUserComponent } from './_components/user/home-user/home-user.component';
import { AuthGuard, RoleGuard } from './_guards/index';

const routes: Routes = [
  {
    path: 'welcome',
    component: WelcomeComponent
  },
  {
    path: 'sign-in',
    component: SignInComponent
  },
  {
    path: 'sign-up',
    component: SignUpComponent
  },
  {
    path: 'home-user',
    component: HomeUserComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'home-admin',
    component: HomeAdminComponent,
    canActivate: [AuthGuard, RoleGuard],
    data: {
      expectedRole: 'ROLE_ADMIN'
    }
  }
];

@NgModule({
  imports: [
    RouterModule.forChild(routes)
  ],
  exports: [
    RouterModule
  ]
})
export class CoreRoutingModule { }
