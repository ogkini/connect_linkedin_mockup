import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { FormBuilder, FormGroup } from "@angular/forms";
import { first } from "rxjs/operators";

import { User, Post, Like, Comment, CreationResponse } from '../../../_models';
import { PostService, LikeService, CommentService } from '../../../_services';
import { ConnectionConfigService, AuthenticationService } from '../../../_services';
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
  showComments = false;
  addCommentForm: FormGroup;

  public profilePhotosEndpoint: string;

  public constructor(
      private titleService: Title,
      private postService: PostService,
      private likeService: LikeService,
      private commentService: CommentService,
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

    // Initialise form contents
    this.addCommentFormInit();
  }

  private getHomePosts(id: number) {
    this.postService.getHome(id).subscribe(posts => { this.posts = posts; });
  }

  // Initiliases the form to add a new comment
  addCommentFormInit() {
    this.addCommentForm = this.formBuilder.group({
      text: ['']
    });
  }

  // Convenience getter for easy access to form fields
  get getAddCommentForm() { return this.addCommentForm.controls; }

  // A user likes a post
  addLike(ownerId: number, postId: number) {
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
          console.error(error);
        }
      );
  }

  // A user deletes a like
  deleteLike(ownerId: number, postId: number) {
    // Find the id of the like to be deleted
    let likeId = this.posts.find(post => post.id == postId).likes.find(like => like.user.id == this.signedInUser.id).id;

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
          console.error(error);
        }
      );
  }

  addComment(ownerId: number, postId: number) {
    // Create a new Comment object
    const newComment: Comment = {
      text: this.getAddCommentForm.text.value
    } as Comment;

    // Submit the comment to the server
    this.commentService.create(newComment, ownerId, postId)
      .pipe(first())
      .subscribe((response: CreationResponse) => {
          // Add comment to the array
          if (response.object) {
            this.posts.find(post => post.id == postId).comments.push(response.object);
          }

          // Update comments counter
          this.posts.find(post => post.id == postId).commentsCount++;
        }, error => {
          console.error(error);
        }
      );
  }

  showCommentsToggle() {
    this.showComments = !this.showComments;
  }

}
