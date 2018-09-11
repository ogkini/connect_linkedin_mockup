# REST api Specifications

Add ```/api``` at the front of every uri.

* ```diff
  - /users
  ```

    | Http Method | Result                                   |
    | ----------- | ---------------------------------------- |
    | **GET**     | returns a list of all users (admin only) |
    | **POST**    | creates a new user                       |

* ```/users/{id}```

    | Http Method | Result                                                              |
    | ----------- | ------------------------------------------------------------------- |
    | **GET**     | returns a specific user                                             |
    | **PUT**     | a user updates his account information                              |
    | **DELETE**  | a user deletes his account (admin can delete a user's account, too) |

* ```/users/{id}/occupation```

    | Http Method | Result                        |
    | ----------- | ----------------------------- |
    | **GET**     | returns a user's occupation   |
    | **POST**    | a user adds his occupation    |
    | **PUT**     | a user updates his occupation |
    | **DELETE**  | a user deletes his occupation |
