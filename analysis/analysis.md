# REST api Specifications

Add ```/api``` at the front of every uri.

* ```/users```

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

* ```/users/{id}/experience```

    | Http Method | Result                        |
    | ----------- | ----------------------------- |
    | **GET**     | returns a user's experience   |
    | **POST**    | a user adds experience        |

* ```/users/{id}/education```

    | Http Method | Result                       |
    | ----------- | ---------------------------- |
    | **GET**     | returns a user's education   |
    | **POST**    | a user adds education        |

* ```/users/{id}/skills```

    | Http Method | Result                    |
    | ----------- | ------------------------- |
    | **GET**     | returns a user's skills   |
    | **POST**    | a user adds a skill       |
