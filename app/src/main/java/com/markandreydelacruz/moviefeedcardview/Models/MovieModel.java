package com.markandreydelacruz.moviefeedcardview.Models;

import java.util.List;

/**
 * Created by mark on 6/1/2017.
 */

public class MovieModel {

        private String movie;
        private int year;
        private double rating;
        private String duration;
        private String director;
        private String tagline;
        private String image;
        private String story;
        private List<CastBean> cast;

        public String getMovie() {
            return movie;
        }

        public void setMovie(String movie) {
            this.movie = movie;
        }

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public double getRating() {
            return rating;
        }

        public void setRating(double rating) {
            this.rating = rating;
        }

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public String getDirector() {
            return director;
        }

        public void setDirector(String director) {
            this.director = director;
        }

        public String getTagline() {
            return tagline;
        }

        public void setTagline(String tagline) {
            this.tagline = tagline;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getStory() {
            return story;
        }

        public void setStory(String story) {
            this.story = story;
        }

        public List<CastBean> getCast() {
            return cast;
        }

        public void setCast(List<CastBean> cast) {
            this.cast = cast;
        }

        public static class CastBean {
            private String name;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
}
