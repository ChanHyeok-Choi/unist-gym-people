package com.unistgympeople;

class UserNotFoundException extends RuntimeException {

    UserNotFoundException(String userId) {
        super("Could not find User " + userId);
    }
}