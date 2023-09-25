package com.example.gymcompanion.utils;

public class HomeFragmentModel {
    private final String /*image,*/ program;
    private final String set;
    private final String reps;

    public HomeFragmentModel(/*String image, */String program, String set, String reps) {
//        this.image = image;
        this.reps = reps;
        this.program = program;
        this.set = set;
    }

//    public String getImage() {
//        return image;
//    }

    public String getProgram() {
        return program;
    }

    public String getSet() {
        return set;
    }

    public String getReps() {
        return reps;
    }
}
