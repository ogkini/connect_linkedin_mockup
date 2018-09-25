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

* ```/users/{id}/experience/{id}```

    | Http Method | Result                               |
    | ----------- | ------------------------------------ |
    | **PUT**     | a user updates a specific experience |
    | **DELETE**  | a user deletes a specific experience |

* ```/users/{id}/education```

    | Http Method | Result                       |
    | ----------- | ---------------------------- |
    | **GET**     | returns a user's education   |
    | **POST**    | a user adds education        |

* ```/users/{id}/education/{id}```

    | Http Method | Result                              |
    | ----------- | ----------------------------------- |
    | **PUT**     | a user updates a specific education |
    | **DELETE**  | a user deletes a specific education |

* ```/users/{id}/skills```

    | Http Method | Result                    |
    | ----------- | ------------------------- |
    | **GET**     | returns a user's skills   |
    | **POST**    | a user adds a skill       |

* ```/users/{id}/skills/{id}```

    | Http Method | Result                          |
    | ----------- | ------------------------------- |
    | **PUT**     | a user updates a specific skill |
    | **DELETE**  | a user deletes a specific skill |

* ```/relationships```

    | Http Method | Result                                                     |
    | ----------- | ---------------------------------------------------------- |
    | **POST**    | a user sends a friend request (receiver specified in JSON) |

* ```/relationships/{id}```

    | Http Method | Result                           |
    | ----------- | -------------------------------- |
    | **PUT**     | a user accepts a friend request  |
    | **DELETE**  | a user declines a friend request |

* ```/users/{id}/network```

    | Http Method | Result                                                           |
    | ----------- | ---------------------------------------------------------------- |
    | **GET**     | a user gets his connections, received requests and sent requests |
