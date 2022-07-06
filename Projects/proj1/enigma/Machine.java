package enigma;

import java.util.ArrayList;
import java.util.Collection;

/** Class that represents a complete enigma machine.
 *  @author Eduard Mirzoyan
 */
class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        if (1 >= numRotors || 0 > pawls || pawls >= numRotors) {
            throw new EnigmaException("Precondition "
                    + "not met");
        }

        _alphabet = alpha;
        _numRotors = numRotors;
        _numPawls = pawls;
        _allRotors = new ArrayList<Rotor>();
        for (Rotor r : allRotors) {
            _allRotors.add(r);
        }
        _insertedRotors = new ArrayList<Rotor>();
    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        return _numRotors;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return _numPawls;
    }

    /** Return Rotor #K, where Rotor #0 is the reflector, and Rotor
     *  #(numRotors()-1) is the fast Rotor.  Modifying this Rotor has
     *  undefined results. */
    Rotor getRotor(int k) {
        if (_insertedRotors.size() < k) {
            throw new EnigmaException("The rotor you want "
                    + "to get does not exist for: " + k);
        }
        return _insertedRotors.get(k);
    }

    Alphabet alphabet() {
        return _alphabet;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        _insertedRotors.clear();

        Rotor addedRotor;
        for (String rotorName : rotors) {
            addedRotor = null;
            for (Rotor rotor : _allRotors) {
                if (rotor.name().equals(rotorName)) {
                    if (!rotor.reflecting()) {
                        rotor.set(0);
                    }
                    _insertedRotors.add(rotor);
                    addedRotor = rotor;
                }
            }
            if (addedRotor == null) {
                throw new EnigmaException("Bad rotor name");
            }
        }

        if (!_insertedRotors.get(0).reflecting()) {
            throw new EnigmaException("The first rotation is not a reflector!");
        }
    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        char[] settingArray = setting.toCharArray();
        for (int i = 0; i < numRotors() - 1; i++) {
            if (!_insertedRotors.get(i + 1).reflecting()) {
                int index = _alphabet.toInt(settingArray[i]);
                _insertedRotors.get(i + 1).set(index);
            }
        }
    }

    /** Return the current plugboard's permutation. */
    Permutation plugboard() {
        return _plugboard;
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        _plugboard = plugboard;
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing
     *  the machine. */
    int convert(int c) {
        advanceRotors();
        if (Main.verbose()) {
            System.err.printf("[");
            for (int r = 1; r < numRotors(); r += 1) {
                System.err.printf("%c",
                        alphabet().toChar(getRotor(r).setting()));
            }
            System.err.printf("] %c -> ", alphabet().toChar(c));
        }

        c = plugboard().permute(c);
        if (Main.verbose()) {
            System.err.printf("%c -> ", alphabet().toChar(c));
        }
        c = applyRotors(c);

        c = plugboard().permute(c);
        if (Main.verbose()) {
            System.err.printf("%c%n", alphabet().toChar(c));
        }
        return c;
    }

    /** Advance all rotors to their next position. */
    private void advanceRotors() {
        ArrayList<Integer> rotorsAtNotch = new ArrayList<Integer>();
        for (int i = 1; i < numRotors(); i++) {
            if (_insertedRotors.get(i).atNotch()
                    && _insertedRotors.get(i - 1).rotates()) {
                if (!rotorsAtNotch.contains(i - 1)) {
                    rotorsAtNotch.add(i - 1);
                }
                if (!rotorsAtNotch.contains(i)) {
                    rotorsAtNotch.add(i);
                }
            }
        }
        if (!rotorsAtNotch.contains(numRotors() - 1)) {
            rotorsAtNotch.add(numRotors() - 1);
        }
        for (int i : rotorsAtNotch) {
            _insertedRotors.get(i).advance();
        }
    }

    /** Return the result of applying the rotors to the character C (as an
     *  index in the range 0..alphabet size - 1). */
    private int applyRotors(int c) {
        for (int i = numRotors() - 1; i >= 0; i--) {
            c = _insertedRotors.get(i).convertForward(c);
        }
        for (int i = 1; i < numRotors(); i++) {
            c = _insertedRotors.get(i).convertBackward(c);
        }
        return c;
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        String result = "";
        for (char c : msg.toCharArray()) {
            result += _alphabet.toChar(convert(_alphabet.toInt(c)));
        }
        return result;
    }

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;

    /** List of all available rotors. */
    private ArrayList<Rotor> _allRotors;

    /** List of all inserted rotors. */
    private ArrayList<Rotor> _insertedRotors;

    /** Number of total rotor slots. */
    private int _numRotors;

    /** Number of pawls. */
    private int _numPawls;

    /** The machine's plugboard permutation. */
    private Permutation _plugboard;

}
