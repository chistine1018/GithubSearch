package com.github.data.model;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Index;

import com.google.gson.annotations.SerializedName;

/**
 * Using name/owner_login as primary key instead of id since name/owner_login is always available
 * vs id is not.
 */
@Entity(indices = {@Index("id"), @Index("owner_login")},
        primaryKeys = {"name", "owner_login"})
public class Repo {

    public final int id;

    @SerializedName("name")
    @NonNull
    public final String name;

    @SerializedName("full_name")
    public final String fullName;

    @SerializedName("description")
    public final String descrption;

    @SerializedName("stargazers_count")
    public final int stars;

    @SerializedName("owner")
    @Embedded(prefix = "owner_")
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
