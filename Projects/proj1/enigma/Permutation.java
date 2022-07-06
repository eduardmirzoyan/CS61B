package enigma;

import java.util.ArrayList;

import static enigma.EnigmaException.*;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Eduard Mirzoyan
 */
class Permutation {

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        _cycles = new ArrayList<String>();
        cycles = cycles.trim();

        boolean cycleFlag = false;
        String cycleWord = "";

        for (char c : cycles.toCharArray()) {
            if (c == '(') {
                cycleFlag = true;
            } else if (c == ')') {
                _cycles.add(cycleWord);
                cycleFlag = false;
                cycleWord = "";
            } else if (cycleFlag) {
                if (!alphabet.contains(c)) {
                    throw new EnigmaException("Character " + c
                            + " does not exists in alphabet "
                            + "but is in a cycle.");
                }
                cycleWord += c;
            }
        }

        for (int i = 0; i < alphabet.size(); i++) {
            if (!cycles.contains("" + alphabet.toChar(i))) {
                _cycles.add("" + alphabet.toChar(i));
            }
        }
    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void addCycle(String cycle) {
        if (_cycles.contains(cycle)) {
            throw new EnigmaException("Cycle " + cycle + " already exists.");
        }
        _cycles.add(cycle);
    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return _alphabet.size();
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        return _alphabet.toInt(permute(_alphabet.toChar(wrap(p))));
    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        return _alphabet.toInt(invert(_alphabet.toChar(wrap(c))));
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        char result = ' ';
        for (String s : _cycles) {
            if (s.contains("" + p)) {
                if (s.length() == 1) {
                    return s.charAt(0);
                }

                int newIndex = betterWrap(s.indexOf("" + p) + 1, s.length());
                result = s.charAt(newIndex);
                break;
            }
        }

        if (result == ' ') {
            throw new EnigmaException("Char " + p
                    + " not found in any cycles.");
        }
        return result;
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        char result = ' ';
        for (String s : _cycles) {
            if (s.contains("" + c)) {
                if (s.length() == 1) {
                    return s.charAt(0);
                }

                int newIndex = betterWrap(s.indexOf("" + c) - 1, s.length());
                result = s.charAt(newIndex);
                break;
            }
        }

        if (result == ' ') {
            throw new EnigmaException("Char " + c
                    + " not found in any cycles.");
        }
        return result;
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        for (String s : _cycles) {
            if (s.length() > 1) {
                return false;
            }
        }
        return true;
    }

    private int betterWrap(int p, int d) {
        int r = p % d;
        if (r < 0) {
            r += d;
        }
        return r;
    }

    void print() {
        for (String s : _cycles) {
            System.out.print(s + " ");
        }
        System.out.println();
    }

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;

    /** List of all cycles in this permutation. */
    private ArrayList<String> _cycles;
}
