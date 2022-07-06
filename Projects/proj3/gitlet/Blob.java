package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

public class Blob implements Serializable {

    /** Folder that blobs live in. */
    static final File BLOB_FOLDER = new File(Gitlet.GITLET_FOLDER, ".blobs");

    /** Id of blob. */
    private String id;

    /** filename of blob. */
    private String fileName;

    /** contents of file. */
    private String contents;

    /**
     * Creates a blob.
     * @param fileName name of file
     * @param contents contents of ile
     */
    public Blob(String fileName, String contents) {
        this.fileName = fileName;
        this.contents = contents;


        id = "blob " + Utils.sha1(fileName + contents);
    }

    /**
     * Saves a commit to a file for future use.
     */
    public void save() {

        File blobFile = new File(BLOB_FOLDER, id);


        try {
            blobFile.createNewFile();
            Utils.writeObject(blobFile, this);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Reads in and deserializes a blob from a file.
     *
     * @param id Name of file to load
     */
    public static Blob load(String id) {
        File blobFile = new File(BLOB_FOLDER, id);
        if (blobFile.exists()) {
            return Utils.readObject(blobFile, Blob.class);
        } else {
            System.out.println("FILE NOT FOUND: " + id);
        }
        return null;
    }

    /**
     * Return id.
     */
    public String getId() {
        return id;
    }

    /**
     * Return filename.
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Return contents of file.
     */
    public String getContents() {
        return contents;
    }

}
