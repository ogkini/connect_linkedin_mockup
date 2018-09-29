import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { WelcomeComponent } from './_components/welcome/welcome.component';
import { SignInComponent } from './_components/sign-in/sign-in.component';
import { SignUpComponent } from './_components/sign-up/sign-up.component';
import { HomeAdminComponent } from './_components/admin/home-admin/home-admin.component';
import { HomeUserComponent } from './_components/user/home-user/home-user.component';
import { NetworkComponent } from './_components/user/network/network.component';
import { MessagesComponent } from './_components/user/messages/messages.component';

import { AuthGuard, RoleGuard } from './_guards';
import { PageNotFoundComponent } from "./_components/navigation-error/page-not-found/page-not-found.component";
import { UserInfoComponent } from "./_components/user/user-info/user-info.component";
import { UserSettingsComponent } from "./_components/user/user-settings/user-settings.component";
import { UserProfileComponent } from "./_components/user/user-profile/user-profile.component";
import { ForbiddenComponent } from "./_components/navigation-error/forbidden/forbidden.component";

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
    path: 'home-admin',
    component: HomeAdminComponent,
    canActivate: [AuthGuard, RoleGuard],
    data: {
      expectedRole: 'ROLE_ADMIN'
    }
  },
  {
    path: 'users/:id',
    canActivate: [AuthGuard],
    children: [
      { path: '', redirectTo: 'home', pathMatch: 'full' },
      { path: 'home', component:  HomeUserComponent },
      { path: 'network', component:  NetworkComponent },
      //{ path: 'job-offers', component:  JobOffersComponent },
      { path: 'messages', component:  MessagesComponent },
      //{ path: 'notifications', component:  NotificationsComponent },
      { path: 'info', component:  UserInfoComponent },
      { path: 'profile', component:  UserProfileComponent },
      { path: 'settings', component:  UserSettingsComponent, runGuardsAndResolvers: 'always' }
    ],
    runGuardsAndResolvers: 'always'
  },
  {
    path: 'forbidden',
    component: ForbiddenComponent,
  },
  {
    path: '**',
    component: PageNotFoundComponent
  }
];

@NgModule({
  imports: [
    //RouterModule.forRoot(routes, {onSameUrlNavigation: 'reload'})
    RouterModule.forChild(routes)
  ],
  exports: [
    RouterModule
  ]
})
export class CoreRoutingModule { }
