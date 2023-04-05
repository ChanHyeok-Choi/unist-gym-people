package com.unistgympeople.movies.controller;

class MovieNotFoundException extends RuntimeException {

    MovieNotFoundException(String movieId) {
        super("Could not find Movie " + vovieId);
    }
}