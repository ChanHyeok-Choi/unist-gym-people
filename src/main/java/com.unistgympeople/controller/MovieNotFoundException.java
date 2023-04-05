package com.unistgympeople.controller;

class MovieNotFoundException extends RuntimeException {

    MovieNotFoundException(String movieId) {
        super("Could not find Movie " + movieId);
    }
}