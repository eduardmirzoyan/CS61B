package gitlet;

/** Driver class for Gitlet, the tiny stupid version-control system.
 *  @author Eduard Mirzoyan
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND> .... */
    public static void main(String... args) {
        // Error handling
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            return;
        }

        switch (args[0]) {
        case "init":

            Gitlet.init();
            break;
        case "add":

            Gitlet.addToStage(args[1]);
            break;
        case "checkout":

            if (args.length == 2) {
                Gitlet.checkoutBranch(args[1]);
            } else if (args.length == 3) {
                Gitlet.checkout(args[2]);
            } else if (args.length == 4) {
                if (!args[2].equals("--")) {
                    System.out.println("Incorrect operands.");
                    return;
                }
                Gitlet.checkout("commit " + args[1], args[3]);
            }

            break;
        case "commit":
            if (args.length < 2) {
                System.out.println("Please enter a commit message.");
            } else {
                Gitlet.commit(args[1], null);
            }
            break;
        case "log":
            Gitlet.log();
            break;
        case "global-log":

            Gitlet.globalLog();
            break;
        case "rm":

            Gitlet.removeFromStage(args[1]);
            break;
        case "find":

            Gitlet.find(args[1]);
            break;
        case "status":

            Gitlet.status();
            break;
        case "branch":

            Gitlet.createBranch(args[1]);
            break;
        case "rm-branch":

            Gitlet.removeBranch(args[1]);
            break;
        case "reset":
            Gitlet.reset(args[1]);
            break;
        case "merge":
            Gitlet.mergeBranch(args[1]);
            break;
        default:
            System.out.println("No command with that name exists.");
        }
    }

    /**
     * Prints out MESSAGE and exits with error code -1.
     * Note:
     *     The functionality for erroring/exit codes is different within Gitlet
     *     so DO NOT use this as a reference.
     *     Refer to the spec for more information.
     * @param message message to print
     */
    public static void exitWithError(String message) {
        if (message != null && !message.equals("")) {
            System.out.println(message);
        }
        System.exit(-1);
    }

}
