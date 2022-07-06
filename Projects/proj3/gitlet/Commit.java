package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;

public class Commit implements Serializable {

    /** Folder that commits live in. */
    static final File COMMIT_FOLDER = new File(Gitlet.GITLET_FOLDER, ".commits");

    /** Folder that commits live in. */
    private String message;

    /** Folder that commits live in. */
    private String id;

    /** Folder that commits live in. */
    private String timestamp;

    /** Folder that commits live in. */
    private HashMap<String, String> blobs;

    /** Folder that commits live in. */
    private String parent1;

    /** Folder that commits live in. */
    private String parent2;

    /** Commit */
    public Commit(String message, HashMap<String, String> blobs, String parent1, String parent2) {
        this.message = message;
        this.blobs = blobs;
        this.parent1 = parent1;
        this.parent2 = parent2;
        timestamp = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy Z").format(new Date());
        if (parent1 == null) {
            this.blobs = new HashMap<>();
            timestamp = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy Z").format(new Date(0));
        }
        id = "commit " + Utils.sha1(message + timestamp + parent1);
        save();
    }

    /** Folder that commits live in. */
    public String getID() {
        return id;
    }

    /** Folder that commits live in. */
    public String getMessage() {
        return message;
    }

    /** Folder that commits live in. */
    public HashMap<String, String> getBlobs() {
        return blobs;
    }

    /** Folder that commits live in. */
    public String getParentId() {
        return parent1;
    }

    /** Folder that commits live in. */
    public String getParent2ID() {
        return parent2;
    }

    /**
     * Saves a commit to a file for future use.
     */
    private void save() {
        File commitFile = new File(COMMIT_FOLDER, id);

        try {
            commitFile.createNewFile();
            Utils.writeObject(commitFile, this);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Reads in and deserializes a commit from a file
     *
     * @param id Name of file to load
     * @return Commit data read from file
     */
    public static Commit load(String id) {
        File commitFile = new File(COMMIT_FOLDER, id);

        if (commitFile.exists()) {
            return Utils.readObject(commitFile, Commit.class);
        } else {
            System.out.println("FILE NOT FOUND: " + id);
        }
        return null;
    }

    /** Folder that commits live in. */
    @Override
    public String toString() {
        return (
                "===" + '\n'
                + id + '\n'
                + "Date: " + timestamp.toString() + '\n'
                + message + '\n'
                );
    }
}
