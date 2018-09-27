import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { FormBuilder } from '@angular/forms';
import { first } from "rxjs/operators";

import { User, Post, Like, CreationResponse } from '../../../_models';
import { PostService, LikeService, ConnectionConfigService, AuthenticationService } from '../../../_services';
import { ActivatedRoute } from "@angular/router";

@Component({
  selector: 'app-home-user',
  templateUrl: './home-user.component.html',
  styleUrls: ['./home-user.component.scss']
})
export class HomeUserComponent implements OnInit {

  title = 'Home';
  signedInUser: User;
  posts: Post[] = [];
  submitted = false;
  userId: number;

  public profilePhotosEndpoint: string;

  public constructor(
      private titleService: Title,
      private postService: PostService,
      private likeService: LikeService,
      private formBuilder: FormBuilder,
      private connConfig: ConnectionConfigService,
      private route: ActivatedRoute,
      private authenticationService: AuthenticationService,
  ) {
    this.titleService.setTitle(this.title);
    this.signedInUser = JSON.parse(localStorage.getItem('currentUser'));
  }

  ngOnInit() {
    this.getHomePosts(this.signedInUser.id);
  }

  private getHomePosts(id: number) {
    this.postService.getHome(id).subscribe(posts => { this.posts = posts; });
  }

  // A user likes a post
  createLike(ownerId: number, postId: number) {
    this.likeService.create(ownerId, postId)
      .pipe(first())
      .subscribe((response: CreationResponse) => {
          // Add like to the array
          if (response.object) {
            this.posts.find(post => post.id == postId).likes.push(response.object);
          }

          // Update boolean to indicate that the user likes the post
          this.posts.find(post => post.id == postId).likesPost = true;

          // Update likes counter
          this.posts.find(post => post.id == postId).likesCount++;
        }, error => {
          console.log(error);
        }
      );
  }

  // A user deletes a like
  deleteLike(ownerId: number, postId: number) {
    // Find the id of the like to be deleted
    let likeId = this.posts.find(post => post.id == postId).likes.find(like => like.user.id == this.signedInUser.id).id;

    console.log(likeId);

    this.likeService.delete(ownerId, postId, likeId)
      .pipe(first())
      .subscribe(response => {
          // Remove like from the array
          this.posts.find(post => post.id == postId).likes = this.posts.find(post => post.id == postId)
              .likes.filter(like => like.user.id !== this.signedInUser.id);

          // Update boolean to indicate that the user likes the post
          this.posts.find(post => post.id == postId).likesPost = false;

          // Update likes counter
          this.posts.find(post => post.id == postId).likesCount--;
        }, error => {
          console.log(error);
        }
      );
}

}
