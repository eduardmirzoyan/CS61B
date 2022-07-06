package gitlet;

import java.io.File;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.HashSet;

public class Gitlet {

    /** Current Working Directory. */
    private static final File CWD = new File(System.getProperty("user.dir"));

    /** Main metadata folder. */
    static final File GITLET_FOLDER = new File(CWD.getPath(), ".gitlet");

    /** Folder of staged files. */
    private static final File STAGING_DIR = new File(GITLET_FOLDER, ".stage");

    /** Folder of added staged files. */
    private static final File STAGING_ADD = new File(STAGING_DIR, ".add");

    /** Folder of removed staged files. */
    private static final File STAGING_REMOVE = new File(STAGING_DIR, ".remove");

    /** Folder of branches. */
    private static final File BRANCHES_FOLDER = new File(GITLET_FOLDER, ".branches");

    /** Folder of the head commit. */
    private static final File HEAD_FOLDER = new File(GITLET_FOLDER, ".head");

    /**
     * The innit logic
     * */
    public static void init() {
        if (GITLET_FOLDER.exists()) {
            System.out.println("A Gitlet version-control system "
                    + "already exists in the current directory.");
            return;
        }


        GITLET_FOLDER.mkdir();
        STAGING_DIR.mkdir();
        STAGING_ADD.mkdir();
        STAGING_REMOVE.mkdir();
        BRANCHES_FOLDER.mkdir();
        HEAD_FOLDER.mkdir();
        Commit.COMMIT_FOLDER.mkdir();
        Blob.BLOB_FOLDER.mkdir();

        Commit initialCommit = new Commit("initial commit", null, null, null);

        File master = new File(BRANCHES_FOLDER, "master.txt");
        Utils.writeContents(master, initialCommit.getID());

        File head = new File(HEAD_FOLDER, "head.txt");
        Utils.writeContents(head, "master.txt");
    }

    /**
     * Print status of repo
     */
    public static void status() {

        if (!GITLET_FOLDER.exists()) {
            System.out.println("Not in an initialized Gitlet directory.");
            return;
        }

        System.out.println("=== Branches ===");
        var branchFileNames = Utils.plainFilenamesIn(BRANCHES_FOLDER);

        File headFile = new File(HEAD_FOLDER, "head.txt");
        String headFileContent = Utils.readContentsAsString(headFile).replace(".txt", "");

        for (String branchFileName : branchFileNames) {
            String name = branchFileName.replace(".txt", "");

            if (headFileContent.equals(name)) {
                name = "*" + name;
            }

            System.out.println(name);
        }
        System.out.println();

        System.out.println("=== Staged Files ===");
        File stageFile = new File(STAGING_ADD, "stage.txt");

        if (stageFile.exists()) {
            String contents = Utils.readContentsAsString(stageFile);
            String[] result = contents.split("\\r?\\n");
            for (String name : result) {
                Blob blob = Blob.load(name);
                System.out.println(blob.getFileName());
            }
        }
        System.out.println();

        System.out.println("=== Removed Files ===");

        stageFile = new File(STAGING_REMOVE, "stage.txt");
        if (stageFile.exists()) {
            String contents = Utils.readContentsAsString(stageFile);
            String[] result = contents.split("\\r?\\n");
            for (String name : result) {
                Blob blob = Blob.load(name);
                System.out.println(blob.getFileName());
            }
        }
        System.out.println();

        System.out.println("=== Modifications Not Staged For Commit ===");
        System.out.println();

        System.out.println("=== Untracked Files ===");
        System.out.println();

    }

    /**
     * Add to stage
     * @param fileName name of file
     */
    public static void addToStage(String fileName) {
        File fileToStage = new File(CWD, fileName);

        if (fileToStage.exists()) {

            String contents = Utils.readContentsAsString(fileToStage);
            Blob blob = new Blob(fileName, contents);

            File removeStageFile = new File(STAGING_REMOVE, "stage.txt");
            if (removeStageFile.exists()) {
                String stageContents = Utils.readContentsAsString(removeStageFile);

                if (stageContents.contains(blob.getId())) {
                    stageContents = stageContents.replace(blob.getId(), "");

                    if (stageContents.isBlank()) {
                        removeStageFile.delete();
                    } else {
                        Utils.writeContents(removeStageFile, stageContents);
                    }

                    return;
                }
            }
            File stagingFile = new File(STAGING_ADD, "stage.txt");
            File head = new File(HEAD_FOLDER, "head.txt");
            String headBranchName = Utils.readContentsAsString(head);
            File headBranch = new File(BRANCHES_FOLDER, headBranchName);
            String headCommitID = Utils.readContentsAsString(headBranch);
            Commit headCommit = Commit.load(headCommitID);

            for (String blobId : headCommit.getBlobs().values()) {
                Blob test = Blob.load(blobId);
                if (test.getFileName().equals(fileName)
                        && test.getContents().equals(blob.getContents())) {
                    return;
                }
            }
            if (!stagingFile.exists()) {
                Utils.writeContents(stagingFile, "");
            }
            if (Utils.readContentsAsString(stagingFile).contains(blob.getId())) {
                System.out.println("File already staged: " + fileName);

                return;
            }

            blob.save();
            Utils.writeContents(stagingFile,
                    Utils.readContentsAsString(stagingFile) + blob.getId() + '\n');
        } else {
            System.out.println("File does not exist.");
        }
    }

    /**
     * Remove from stage
     * @param fileName name of file
     */
    public static void removeFromStage(String fileName) {
        File fileToRemove = new File(CWD, fileName);
        if (!fileToRemove.exists()) {
            File stagingFile = new File(STAGING_REMOVE, "stage.txt");
            if (!stagingFile.exists()) {
                Utils.writeContents(stagingFile, "");
            }
            Blob blob = new Blob(fileName, "");
            blob.save();
            Utils.writeContents(stagingFile, Utils.readContentsAsString(stagingFile)
                    + blob.getId() + '\n');
            return;
        }
        String fileContents = Utils.readContentsAsString(fileToRemove);
        Blob blob = new Blob(fileName, fileContents);
        File stageFile = new File(STAGING_ADD, "stage.txt");
        if (stageFile.exists()) {
            String stageContents = Utils.readContentsAsString(stageFile);
            if (stageContents.contains(blob.getId())) {
                stageContents = stageContents.replace(blob.getId(), "");
                File blobFile = new File(Blob.BLOB_FOLDER, blob.getId());
                blobFile.delete();
                if (stageContents.isBlank()) {
                    stageFile.delete();
                } else {
                    Utils.writeContents(stageFile, stageContents);
                }
                return;
            }
        }
        File head = new File(HEAD_FOLDER, "head.txt");
        String headBranchName = Utils.readContentsAsString(head);
        File headBranch = new File(BRANCHES_FOLDER, headBranchName);
        String headCommitID = Utils.readContentsAsString(headBranch);

        Commit headCommit = Commit.load(headCommitID);

        if (headCommit.getBlobs() != null) {
            for (String id : headCommit.getBlobs().values()) {
                if (id.equals(blob.getId())) {
                    File stagingFile = new File(STAGING_REMOVE, "stage.txt");
                    if (!stagingFile.exists()) {
                        Utils.writeContents(stagingFile, "");
                    }

                    if (Utils.readContentsAsString(stagingFile).contains(blob.getId())) {
                        System.out.println("File already staged: " + fileName);

                        return;
                    }

                    blob.save();

                    Utils.writeContents(stagingFile,
                            Utils.readContentsAsString(stagingFile) + blob.getId() + '\n');

                    fileToRemove.delete();

                    return;
                }
            }

            System.out.println("No reason to remove the file.");
        }

    }

    /**
     * Commits changes and moves up head
     * @param message message of commit
     @param secondParentID parent of commit
     */
    public static void commit(String message, String secondParentID) {
        if (message.isBlank()) {
            System.out.println("Please enter a commit message.");
            return;
        }

        File addStageFile = new File(STAGING_ADD, "stage.txt");
        File removeStageFile = new File(STAGING_REMOVE, "stage.txt");

        HashMap<String, String> result = new HashMap<>();
        if (addStageFile.exists() || removeStageFile.exists()) {

            if (addStageFile.exists()) {
                String contents = Utils.readContentsAsString(addStageFile);
                String[] ids = contents.split("\\r?\\n");

                for (String id : ids) {
                    Blob blob = Blob.load(id);
                    result.put(blob.getFileName(), id);
                }


                addStageFile.delete();
            }


            File headFile = new File(HEAD_FOLDER, "head.txt");
            String headBranchName = Utils.readContentsAsString(headFile);

            File currentBranchFile = new File(BRANCHES_FOLDER, headBranchName);
            String headCommitID = Utils.readContentsAsString(currentBranchFile);
            Commit headCommit = Commit.load(headCommitID);

            ArrayList<String> idsNotToAdd = new ArrayList<>();

            if (removeStageFile.exists()) {


                String contents = Utils.readContentsAsString(removeStageFile);
                String[] ids = contents.split("\\r?\\n");


                for (String blobName : ids) {

                    idsNotToAdd.add(blobName);


                }

                removeStageFile.delete();
            }

            for (String s : headCommit.getBlobs().values()) {
                if (!idsNotToAdd.contains(s)) {
                    Blob b = Blob.load(s);
                    result.put(b.getFileName(), b.getId());
                }
            }


            Commit commit = new Commit(message, result, headCommitID, secondParentID);

            Utils.writeContents(currentBranchFile, commit.getID());
        } else {
            System.out.println("No changes added to the commit.");
        }
    }

    /**
     * Prints a log for current branch
     */
    public static void log() {

        File headFile = new File(HEAD_FOLDER, "head.txt");
        String headBranchName = Utils.readContentsAsString(headFile);
        File branchFile = new File(BRANCHES_FOLDER, headBranchName);
        String currentCommitID = Utils.readContentsAsString(branchFile);


        while (currentCommitID != null) {
            Commit currentCommit = Commit.load(currentCommitID);

            System.out.println(currentCommit.toString());

            currentCommitID = currentCommit.getParentId();
        }
    }


    /**
     * Prints a log of ALL commits
     */
    public static void globalLog() {
        var fileNames = Utils.plainFilenamesIn(Commit.COMMIT_FOLDER);
        Commit commit;
        for (String fileName : fileNames) {
            commit = Commit.load(fileName);

            System.out.println(commit.toString());
        }
    }


    /**
     * Checkout a file at current commit
     * @param fileName commit id
     */
    public static void checkout(String fileName) {

        File headFile = new File(HEAD_FOLDER, "head.txt");
        String headBranchName = Utils.readContentsAsString(headFile);
        File headBranchFile = new File(BRANCHES_FOLDER, headBranchName);

        File headCommitFile = new File(Commit.COMMIT_FOLDER,
                Utils.readContentsAsString(headBranchFile));
        Commit headCommit = Utils.readObject(headCommitFile, Commit.class);

        var blobIDs = headCommit.getBlobs();
        for (String blobID : blobIDs.values()) {
            Blob blob = Blob.load(blobID);
            if (blob.getFileName().equals(fileName)) {
                File directoryFile = new File(CWD, blob.getFileName());

                Utils.writeContents(directoryFile, blob.getContents());

                return;
            }

        }

        System.out.println("File does not exist in that commit.");
    }

    /**
     * Checkout a file at a commit
     * @param commitID file name
     * @param fileName commit id
     */
    public static void checkout(String commitID, String fileName) {

        File file = new File(CWD, fileName);
        if (!file.exists()) {
            System.out.println("File does not exist in that commit.");
            return;
        }

        Blob fileBlob = new Blob(fileName, Utils.readContentsAsString(file));
        File blobFile = new File(Blob.BLOB_FOLDER, fileBlob.getId());
        if (!blobFile.exists()) {
            System.out.println("There is an untracked file in the way; "
                    + "delete it, or add and commit it first.");
            return;
        }

        File commitFile = new File(Commit.COMMIT_FOLDER, commitID);
        if (!commitFile.exists()) {
            System.out.println("No commit with that id exists.");
            return;
        }
        Commit commit = Utils.readObject(commitFile, Commit.class);


        var blobIDs = commit.getBlobs();
        for (String blobID : blobIDs.values()) {
            Blob blob = Blob.load(blobID);

            if (blob.getFileName().equals(fileName)) {

                File directoryFile = new File(CWD, blob.getFileName());


                Utils.writeContents(directoryFile, blob.getContents());


                return;
            }
        }


    }


    public static void checkoutBranch(String branchName) {
        branchName += ".txt";

        File branchHeadFile = new File(BRANCHES_FOLDER,  branchName);
        if (!branchHeadFile.exists()) {
            System.out.println("No such branch exists.");
            return;
        }

        File headFile = new File(HEAD_FOLDER, "head.txt");
        String headBranch = Utils.readContentsAsString(headFile);

        if (headBranch.equals(branchName)) {
            System.out.println("No need to checkout the current branch.");
            return;
        }


        File branchFile = new File(BRANCHES_FOLDER, headBranch);
        String headCommitID = Utils.readContentsAsString(branchFile);
        Commit headCommit = Commit.load(headCommitID);


        String commitID = Utils.readContentsAsString(branchHeadFile);
        File commitFile = new File(Commit.COMMIT_FOLDER, commitID);
        Commit commit = Utils.readObject(commitFile, Commit.class);


        List<String> fileNames = Utils.plainFilenamesIn(CWD);

        for (String fName : fileNames) {
            if (!headCommit.getBlobs().containsKey(fName) && commit.getBlobs().containsKey(fName)) {
                String blobID = commit.getBlobs().get(fName);
                String cont = Utils.readContentsAsString(new File(CWD, fName));
                if (!cont.equals(Blob.load(blobID).getContents())) {
                    System.out.println("There is an untracked file in the way; "
                            + "delete it, or add and commit it first.");
                    return;
                }
            }
        }

        for (String blobID : headCommit.getBlobs().values()) {
            Blob blob = Blob.load(blobID);
            File file = new File(CWD, blob.getFileName());
            if (file.exists()) {
                file.delete();
            }
        }



        var blobIDs = commit.getBlobs();


        for (String blobID : blobIDs.values()) {

            Blob blob = Blob.load(blobID);

            File oldFile = new File(CWD, blob.getFileName());

            String contents = blob.getContents();

            Utils.writeContents(oldFile, contents);
        }


        Utils.writeContents(headFile, branchName);


        File stageFile = new File(STAGING_ADD, "stage.txt");
        if (stageFile.exists()) {
            stageFile.delete();
        }

        stageFile = new File(STAGING_REMOVE, "stage.txt");
        if (stageFile.exists()) {
            stageFile.delete();
        }
    }

    /**
     * Finds a commit with a message
     * @param message message of commit
     */
    public static void find(String message) {

        File head = new File(HEAD_FOLDER, "head.txt");
        String headBranchName = Utils.readContentsAsString(head);
        File headBranch = new File(BRANCHES_FOLDER, headBranchName);


        boolean found = false;


        List<String> commitIDs = Utils.plainFilenamesIn(Commit.COMMIT_FOLDER);

        for (String commitID : commitIDs) {
            Commit currentCommit = Commit.load(commitID);


            if (currentCommit.getMessage().equals(message)) {
                System.out.println(commitID.replace("commit ", ""));
                found = true;
            }
        }


        if (!found) {
            System.out.println("Found no commit with that message.");
        }
    }

    /**
     * Creates a new branch
     * @param branchName name of branch
     */
    public static void createBranch(String branchName) {

        branchName += ".txt";


        File branchFile = new File(BRANCHES_FOLDER, branchName);


        if (branchFile.exists()) {
            System.out.println("A branch with that name already exists.");
            return;
        }


        File headFile = new File(HEAD_FOLDER, "head.txt");
        String currBranch = Utils.readContentsAsString(headFile);
        File currentBranch = new File(BRANCHES_FOLDER, currBranch);
        String currentCommitID = Utils.readContentsAsString(currentBranch);


        Utils.writeContents(branchFile, currentCommitID);
    }

    /**
     * Remove's a branch
     * @param branchName name of branch
     */
    public static void removeBranch(String branchName) {

        branchName += ".txt";

        File headFile = new File(HEAD_FOLDER, "head.txt");
        String currBranch = Utils.readContentsAsString(headFile);

        if (branchName.equals(currBranch)) {
            System.out.println("Cannot remove the current branch.");
            return;
        }


        File branchFile = new File(BRANCHES_FOLDER, branchName);


        if (branchFile.exists()) {

            branchFile.delete();
        } else {
            System.out.println("A branch with that name does not exist.");
        }
    }

    /**
     * Resets to the state of commit
     * @param commitID name of commit
     */
    public static void reset(String commitID) {
        File commitFile = new File(Commit.COMMIT_FOLDER, "commit " + commitID);

        if (!commitFile.exists()) {
            System.out.println("No commit with that id exists.");
            return;
        }


        File headFile = new File(HEAD_FOLDER, "head.txt");
        String headBranchName = Utils.readContentsAsString(headFile);
        File currentBranchFile = new File(BRANCHES_FOLDER, headBranchName);
        String currentCommitID = Utils.readContentsAsString(currentBranchFile);
        Commit headCommit = Commit.load(currentCommitID);


        Commit commit = Utils.readObject(commitFile, Commit.class);

        List<String> fileNames = Utils.plainFilenamesIn(CWD);

        for (String fName : fileNames) {
            if (!headCommit.getBlobs().containsKey(fName) && commit.getBlobs().containsKey(fName)) {
                String blobID = commit.getBlobs().get(fName);
                String cont = Utils.readContentsAsString(new File(CWD, fName));
                if (!cont.equals(Blob.load(blobID).getContents())) {
                    System.out.println("There is an untracked file in the way; "
                            + "delete it, or add and commit it first.");
                    return;
                }
            }
        }
        var blobIDs = commit.getBlobs();
        for (String blobID : blobIDs.values()) {

            Blob blob = Blob.load(blobID);

            File oldFile = new File(CWD, blob.getFileName());

            String contents = blob.getContents();

            Utils.writeContents(oldFile, contents);
        }


        Utils.writeContents(currentBranchFile, commit.getID());


        File stageFile = new File(STAGING_ADD, "stage.txt");
        if (stageFile.exists()) {
            stageFile.delete();
        }

    }

    /**
     * Merges two branches
     * @param branchName name branch
     */
    public static void mergeBranch(String branchName) {
        branchName += ".txt";

        File stageFile = new File(STAGING_ADD, "stage.txt");
        if (stageFile.exists()) {
            System.out.println("You have uncommitted changes.");
            return;
        }
        stageFile = new File(STAGING_REMOVE, "stage.txt");
        if (stageFile.exists()) {
            System.out.println("You have uncommitted changes.");
            return;
        }

        File headFile = new File(HEAD_FOLDER, "head.txt");
        String headBranchName = Utils.readContentsAsString(headFile);

        if (headBranchName.equals(branchName)) {
            System.out.println("Cannot merge a branch with itself.");
            return;
        }

        File branchFile = new File(BRANCHES_FOLDER, branchName);

        if (!branchFile.exists()) {
            System.out.println("A branch with that name does not exist.");
            return;
        }

        File headBranchFile = new File(BRANCHES_FOLDER, headBranchName);
        String commitID = Utils.readContentsAsString(headBranchFile);
        Commit headCommit = Commit.load(commitID);

        commitID = Utils.readContentsAsString(branchFile);
        Commit branchCommit = Commit.load(commitID);

        List<String> fileNames = Utils.plainFilenamesIn(CWD);

        for (String fName : fileNames) {
            if (!headCommit.getBlobs().containsKey(fName)
                    && branchCommit.getBlobs().containsKey(fName)) {
                String blobID = branchCommit.getBlobs().get(fName);
                String cont = Utils.readContentsAsString(new File(CWD, fName));
                if (!cont.equals(Blob.load(blobID).getContents())) {
                    System.out.println("There is an untracked file in the way; "
                            + "delete it, or add and commit it first.");
                    return;
                }
            }
        }
        Commit splitCommit = findSplit(headCommit, branchCommit);

        if (splitCommit == null) {
            System.out.println("There was an issue finding split");
            return;
        }

        headCommit = Commit.load(Utils.readContentsAsString(headBranchFile));
        branchCommit = Commit.load(Utils.readContentsAsString(branchFile));

        HashSet<String> blobHashSet = new HashSet<>();
        blobHashSet.addAll(splitCommit.getBlobs().keySet());
        blobHashSet.addAll(headCommit.getBlobs().keySet());
        blobHashSet.addAll(branchCommit.getBlobs().keySet());

        boolean result = helper(blobHashSet, splitCommit, headCommit, branchCommit);

        if (result) {
            System.out.println("Encountered a merge conflict.");
        }
        branchName = branchName.replace(".txt", "");
        headBranchName = headBranchName.replace(".txt", "");
        commit("Merged " + branchName + " into " + headBranchName + ".", branchCommit.getID());
    }

    /**
     * Merges two branches.
     * @param blobHashSet name branch
     * @param splitCommit name branch
     * @param headCommit name branch
     * @param branchCommit name branch
     */
    private static boolean helper(HashSet<String> blobHashSet, Commit splitCommit, Commit headCommit, Commit branchCommit) {
        boolean mergeConflict = false;

        for (var fileName : blobHashSet) {

            String splitID = splitCommit.getBlobs().get(fileName);
            String headID = headCommit.getBlobs().get(fileName);
            String branchID = branchCommit.getBlobs().get(fileName);

            if ((splitID == null && headID == null)
                    || (splitID != null && headID != null && splitID.equals(headID))) {

                File file = new File(CWD, fileName);

                if (branchID == null) {
                    removeFromStage(fileName);
                } else {
                    Utils.writeContents(file, Blob.load(branchID).getContents());
                    addToStage(fileName);
                }
                continue;
            }

            if ((splitID == null && branchID == null)
                    || (splitID != null && branchID != null && splitID.equals(branchID))) {
                if (headID != null) {
                    File file = new File(CWD, fileName);

                    Utils.writeContents(file, Blob.load(headID).getContents());
                }
                continue;
            }
            if (!((headID == null && branchID == null)
                    || (headID != null && branchID != null && headID.equals(branchID)))) {
                mergeConflict = true;

                String result = "<<<<<<< HEAD\n";
                result += Blob.load(headID).getContents();
                result += "\n=======\n";
                result += Blob.load(branchID).getContents();
                result += "\n>>>>>>>";
                File file = new File(CWD, fileName);
                Utils.writeContents(file, result);
                addToStage(fileName);
            }
        }

        return mergeConflict;
    }


    /**
     * Merges two branches.
     * @param headCommit name branch
     * @param branchCommit name branch
     */
    private static Commit findSplit(Commit headCommit, Commit branchCommit) {
        ArrayList<String> visited = new ArrayList<>();
        LinkedList<String> nodes = new LinkedList<>();
        nodes.add(branchCommit.getID());
        while (!nodes.isEmpty()) {
            Commit currentFirst = Commit.load(nodes.removeFirst());

            if (visited.contains(currentFirst)) {
                continue;
            }
            visited.add(currentFirst.getID());
            if (currentFirst.getParentId() != null) {
                nodes.add(currentFirst.getParentId());
            }
            if (currentFirst.getParent2ID() != null) {
                nodes.add(currentFirst.getParent2ID());
            }
        }


        nodes = new LinkedList<>();
        nodes.add(headCommit.getID());
        Commit splitCommit = null;


        while (!nodes.isEmpty()) {

            Commit currentFirst = Commit.load(nodes.removeFirst());
            if (visited.contains(currentFirst.getID())) {

                splitCommit = currentFirst;
                break;
            }

            if (currentFirst.getParentId() != null) {
                nodes.add(currentFirst.getParentId());
            }

            if (currentFirst.getParent2ID() != null) {
                nodes.add(currentFirst.getParent2ID());
            }
        }

        return splitCommit;
    }
}
