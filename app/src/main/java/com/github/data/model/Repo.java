package com.github.data.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class Repo {

    public final int id;

    @SerializedName("name")
    public final String name;

    @SerializedName("full_name")
    public final String fullName;

    @SerializedName("description")
    public final String descrption;

    @SerializedName("stargazers_count")
    public final int stars;

    @SerializedName("owner")
    @NonNull
    public final Owner owner;

    public Repo(int id, String name, String fullName, String descrption, int stars, @NonNull Owner owner) {
        this.id = id;
        this.name = name;
        this.fullName = fullName;
        this.descrption = descrption;
        this.stars = stars;
        this.owner = owner;
    }
}
