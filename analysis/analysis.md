# REST api Specifications

Add ```/api``` at the front of every uri.

* ```/users```

    | Http Method | Result                      |
    | ----------- | ----------------------------|
    | **GET**     | returns a list of all users |
    | **POST**    | creates a new user          |

* ```/users/{id}```

    #### Http Methods
    **GET** returns a specific user    
    **PUT** a user updates his account information    
    **DELETE** a user (or the admin) deletes his account

* ```/users/{id}/occupation```

    #### Http Methods
    ```GET``` returns a user's occupation  
    ```PUT``` a user updates his occupation  
    ```DELETE``` a user (or the admin) deletes his account
