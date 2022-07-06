package enigma;

import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.util.regex.Pattern;

import ucb.util.CommandArgs;

import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author Eduard Mirzoyan
 */
public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            CommandArgs options =
                new CommandArgs("--verbose --=(.*){1,3}", args);
            if (!options.ok()) {
                throw error("Usage: java enigma.Main [--verbose] "
                            + "[INPUT [OUTPUT]]");
            }

            _verbose = options.contains("--verbose");
            new Main(options.get("--")).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Open the necessary files for non-option arguments ARGS (see comment
      *  on main). */
    Main(List<String> args) {
        _config = getInput(args.get(0));

        if (args.size() > 1) {
            _input = getInput(args.get(1));
        } else {
            _input = new Scanner(System.in);
        }

        if (args.size() > 2) {
            _output = getOutput(args.get(2));
        } else {
            _output = System.out;
        }
    }

    /** Return a Scanner reading from the file named NAME. */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return a PrintStream writing to the file named NAME. */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output. */
    private void process() {
        Machine M = readConfig();

        boolean firstLine = true;
        while (_input.hasNextLine()) {
            String input = _input.nextLine();
            if (input.length() == 0) {
                printMessageLine("");
                continue;
            }

            if (input.toCharArray()[0] == '*') {
                setUp(M, input);
            } else if (firstLine) {
                throw new EnigmaException("The input does "
                        + "NOT start with a setting line");
            } else {
                printMessageLine(M.convert(input.replaceAll("\\s+", "")));
            }
            firstLine = false;
        }
    }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {
            String alphabet = _config.nextLine().trim();

            String slotsAndPawls = _config.nextLine().trim();

            _alphabet = new Alphabet(alphabet);

            String[] splitSlotsAndPawls = slotsAndPawls.split("\\s+");
            if (!splitSlotsAndPawls[0].matches("\\d+")
                    || !splitSlotsAndPawls[1].matches("\\d+")) {
                throw error("Slots or pawls are not digits");
            }

            ArrayList<Rotor> allRotors = new ArrayList<Rotor>();
            while (_config.hasNextLine() && _config.hasNext()) {
                Rotor newRotor = readRotor();
                for (Rotor r : allRotors) {
                    if (r.name().equals(newRotor.name())) {
                        throw new EnigmaException("A rotor with "
                                + "this name already exists");
                    }
                }
                allRotors.add(newRotor);
            }

            return new Machine(_alphabet,
                    Integer.parseInt(splitSlotsAndPawls[0]),
                    Integer.parseInt(splitSlotsAndPawls[1]), allRotors);
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }

    /** Return a rotor, reading its description from _config.
     * */
    private Rotor readRotor() {
        try {
            String rotorName = _config.next();
            String rotorNotches = _config.next();

            String rotorPermutations = "";
            while (_config.hasNext(Pattern.compile("\\(\\S+\\)"))) {
                rotorPermutations += _config.next();
            }

            char indicator = rotorNotches.toCharArray()[0];
            if (indicator == 'M') {
                rotorNotches = rotorNotches.substring(1);
                return new MovingRotor(rotorName,
                        new Permutation(rotorPermutations, _alphabet),
                                        rotorNotches);
            } else if (indicator == 'N') {
                return new FixedRotor(rotorName,
                        new Permutation(rotorPermutations, _alphabet));
            } else if (indicator == 'R') {
                return new Reflector(rotorName,
                        new Permutation(rotorPermutations, _alphabet));
            } else {
                throw new NoSuchElementException("The rotor indicator is "
                        + "not M N or R but is: " + indicator);
            }
        } catch (NoSuchElementException excp) {
            excp.printStackTrace();
            throw error("bad rotor description: " + excp.getMessage());
        }
    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {
        String[] settingsArray = settings.split("\\s+");
        if (settingsArray[0].equals('*')) {
            throw new EnigmaException("The first symbol was "
                    + "NOT an asterisk: " + settingsArray[0]);
        }
        if (settings.length() < M.numRotors()) {
            throw new EnigmaException("There are too "
                    + "little settings than the number of motors: "
                    + settings.length() + " " + M.numRotors());
        }
        String[] rotors = new String[M.numRotors()];
        for (int i = 0; i < M.numRotors(); i++) {
            rotors[i] = settingsArray[i + 1];
        }
        M.insertRotors(rotors);
        M.setRotors(settingsArray[1 + M.numRotors()]);

        String plugboard = "";
        for (int i = 2 + M.numRotors(); i < settingsArray.length; i++) {
            plugboard += settingsArray[i];
        }
        M.setPlugboard(new Permutation(plugboard, _alphabet));

    }

    /** Return true iff verbose option specified. */
    static boolean verbose() {
        return _verbose;
    }

    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        int size = 5;
        for (int start = 0; start < msg.length(); start += size) {
            _output.print(msg.substring(start,
                    Math.min(msg.length(), start + size)) + " ");
        }
        _output.println();
    }

    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private PrintStream _output;

    /** True if --verbose specified. */
    private static boolean _verbose;
}
