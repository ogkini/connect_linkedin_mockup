import {Component, Input, OnInit} from '@angular/core';
import {Comment, CreationResponse, Post, User} from "../../../_models";
import {first} from "rxjs/operators";
import {FormBuilder, FormGroup} from "@angular/forms";
import {
  AuthenticationService,
  CommentService,
  ConnectionConfigService, DataService,
  LikeService,
  UserService
} from "../../../_services";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-post',
  templateUrl: './post.component.html',
  styleUrls: ['./post.component.scss']
})
export class PostComponent implements OnInit {

  @Input() public postToShow: Post;

  signedInUser: User;
  user: User = {} as User;
  submitted = false;
  userId: number;
  showComments = false;
  message: string;

  addCommentForm: FormGroup;

  public profilePhotosEndpoint: string;

  public constructor(
    private userService: UserService,
    private likeService: LikeService,
    private commentService: CommentService,
    private formBuilder: FormBuilder,
    private connConfig: ConnectionConfigService,
    private route: ActivatedRoute,
    private authenticationService: AuthenticationService,
    private dataService: DataService
  ) {
    this.signedInUser = JSON.parse(localStorage.getItem('currentUser'));

    this.route.params.subscribe(params => {
      this.userId = +params['id'];
      this.authenticationService.forbidUnauthorizedAccess(this.signedInUser, this.userId);
    });
    this.profilePhotosEndpoint = this.connConfig.usersEndpoint;

    // Initialise form contents
    this.addCommentFormInit();
  }

  ngOnInit() {
    this.dataService.currentMessage.subscribe(message => this.message = message);
  }

  // Initiliases the form to add a new comment
  addCommentFormInit() {
    this.addCommentForm = this.formBuilder.group({
      text: ['']
    });
  }

  // Convenience getters for easy access to form fields
  get getAddCommentForm() { return this.addCommentForm.controls; }

  // A user likes a post
  addLike(ownerId: number, postId: number) {
    this.likeService.create(ownerId, postId)
      .pipe(first())
      .subscribe((response: CreationResponse) => {
          // Add like to the array
          if (response.object) {
            this.postToShow.likes.push(response.object);
          }

          // Update boolean to indicate that the user likes the post
          this.postToShow.likesPost = true;

          // Update likes counter
          this.postToShow.likesCount++;
        }, error => {
          console.error(error);
        }
      );
  }

  // A user deletes a like
  deleteLike(ownerId: number, postId: number) {
    // Find the id of the like to be deleted
    let likeId = this.postToShow.likes.find(like => like.user.id == this.signedInUser.id).id;

    this.likeService.delete(ownerId, postId, likeId)
      .pipe(first())
      .subscribe(response => {
          // Remove like from the array
          this.postToShow.likes = this.postToShow.likes.filter(like => like.user.id !== this.signedInUser.id);

          // Update boolean to indicate that the user likes the post
          this.postToShow.likesPost = false;

          // Update likes counter
          this.postToShow.likesCount--;
        }, error => {
          console.error(error);
        }
      );
  }

  // A user comments on a post
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
            this.postToShow.comments.push(response.object);
          }

          // Update comments counter
          this.postToShow.commentsCount++;

          // Clear the form
          this.addCommentFormInit();
        }, error => {
          console.error(error);
        }
      );
  }

  showCommentsToggle() {
    this.showComments = !this.showComments;
  }

}
