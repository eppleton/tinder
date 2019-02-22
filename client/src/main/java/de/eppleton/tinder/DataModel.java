package de.eppleton.tinder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.java.html.BrwsrCtx;
import net.java.html.json.Function;
import net.java.html.json.Model;
import net.java.html.json.Models;
import net.java.html.json.Property;

@Model(className = "Root", targetId = "",
        instance = true, properties = {
            @Property(name = "profileDetailsShowing", type = boolean.class)
            , @Property(name = "profiles", type = Profile.class, array = true)
            , @Property(name = "activeProfile", type = Profile.class)
            , @Property(name = "nextProfile", type = Profile.class)
            , @Property(name = "loaded", type = boolean.class)
        })
final class DataModel {

    public static enum Status {
        LIKED, SUPERLIKED, DISMISSED, EMPTY
    }

    /**
     * Called when the page is ready.
     */
    static void onPageLoad() {
        Root ui = new Root();
        ArrayList<Profile> arrayList = new ArrayList<>();
        
        try {
            Models.parse(BrwsrCtx.findDefault(DataModel.class),
                    Profile.class,
                    DataModel.class.getResourceAsStream("profiles.json")
                    , arrayList);
            ui.getProfiles().addAll(arrayList);
        } catch (IOException ex) {
            Logger.getLogger(DataModel.class.getName()).log(Level.SEVERE, null, ex);
        }

        ui.setActiveProfile(ui.getProfiles().get(0));
        ui.setNextProfile(ui.getProfiles().get(1));
        ui.setLoaded(true);
        ui.applyBindings();
    }

    @Function
    public static void showDetails(Root root) {
        root.setProfileDetailsShowing(true);
    }

    @Function
    public static void hideDetails(Root root) {
        root.setProfileDetailsShowing(false);
    }

    @Function
    public static void newTop(Root root) {
        Profile remove = root.getProfiles().remove(0);
        root.setActiveProfile(root.getNextProfile());
        if (root.getProfiles().size() > 0) {
            root.setNextProfile(root.getProfiles().get(1));
        }
        remove.setStatus(Status.EMPTY);
        root.getProfiles().add(remove);
    }

    @Function
    public static void swipeLeft(Root root) {
        root.setProfileDetailsShowing(false);
        Profile top = root.getActiveProfile();
        top.setStatus(Status.DISMISSED);
    }

    @Function
    public static void swipeRight(Root root) {
        root.setProfileDetailsShowing(false);
        Profile top = root.getActiveProfile();
        top.setStatus(Status.LIKED);
    }

    @Function
    public static void swipeUp(Root root) {
        root.setProfileDetailsShowing(false);
        Profile top = root.getActiveProfile();
        top.setStatus(Status.SUPERLIKED);
    }

    @Model(className = "Profile", targetId = "", instance = true, properties = {
        @Property(name = "id", type = long.class)
        ,@Property(name = "status", type = Status.class)
        ,@Property(name = "name", type = String.class)
        ,@Property(name = "technology", type = String.class)
        ,@Property(name = "description", type = String.class)
        ,@Property(name = "affiliation", type = String.class)
        ,@Property(name = "quote", type = String.class)
        ,@Property(name = "img", type = String.class)
    })
    static class ProfileVMD {

    }

}