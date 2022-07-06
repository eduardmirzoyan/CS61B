package enigma;

import java.util.ArrayList;

/** An alphabet of encodable characters.  Provides a mapping from characters
 *  to and from indices into the alphabet.
 *  @author Eduard Mirzoyan
 */
class Alphabet {

    /** A new alphabet containing CHARS. The K-th character has index
     *  K (numbering from 0). No character may be duplicated. */
    Alphabet(String chars) {
        _alphabet = new ArrayList<Character>();
        for (char c : chars.toCharArray()) {
            if (_alphabet.contains(c)) {
                throw new EnigmaException("Character " + c
                        + " is a duplicate.");
            }
            _alphabet.add(c);
        }
    }

    /** A default alphabet of all upper-case characters. */
    Alphabet() {
        this("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }

    /** Returns the size of the alphabet. */
    int size() {
        return _alphabet.size();
    }

    /** Returns true if CH is in this alphabet. */
    boolean contains(char ch) {
        return _alphabet.contains(ch);
    }

    /** Returns character number INDEX in the alphabet, where
     *  0 <= INDEX < size(). */
    char toChar(int index) {
        return _alphabet.get(index);
    }

    /** Returns the index of character CH which must be in
     *  the alphabet. This is the inverse of toChar(). */
    int toInt(char ch) {
        if (_alphabet.indexOf(ch) == -1) {
            throw new EnigmaException("Character " + ch
                    + " does not exists in alphabet.");
        }
        return _alphabet.indexOf(ch);
    }

    void print() {
        for (char c : _alphabet) {
            System.out.print(c);
        }
        System.out.println();
    }

    /** An arraylist of characters. */
    private ArrayList<Character> _alphabet;
}
