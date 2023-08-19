package com.example.gymcompanion.utils;

public class HomeFragmentModel {
    private final String /*image,*/ program;
    private final String set;

    public HomeFragmentModel(/*String image, */String program, String set) {
//        this.image = image;
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
}
