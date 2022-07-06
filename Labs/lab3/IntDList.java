import sun.applet.resources.MsgAppletViewer_fr;

/**
 * Scheme-like pairs that can be used to form a list of integers.
 *
 * @author P. N. Hilfinger; updated by Linda Deng (1/26/2022)
 */
public class IntDList {

    /**
     * First and last nodes of list.
     */
    protected DNode _front, _back;

    /**
     * An empty list.
     */
    public IntDList() {
        _front = _back = null;
    }

    /**
     * @param values the ints to be placed in the IntDList.
     */
    public IntDList(Integer... values) {
        _front = _back = null;
        for (int val : values) {
            insertBack(val);
        }
    }

    /**
     * @return The first value in this list.
     * Throws a NullPointerException if the list is empty.
     */
    public int getFront() {
        return _front._val;
    }

    /**
     * @return The last value in this list.
     * Throws a NullPointerException if the list is empty.
     */
    public int getBack() {
        return _back._val;
    }

    /**
     * @return The number of elements in this list.
     */
    public int size() {

        DNode node = _front;
        int size = 0;
        while (node != null){
            size++;
            node = node._next;
        }
        return size;
    }

    /**
     * @param index index of node to return,
     *          where index = 0 returns the first node,
     *          index = 1 returns the second node, and so on.
     *          You can assume index will always be a valid index,
     *              i.e 0 <= index < size.
     * @return The node at index index
     */
    private DNode getNode(int index) {
        DNode node = _front;
        while(index > 0){
            node = node._next;
            index--;
        }
        return node;
    }

    /**
     * @param index index of element to return,
     *          where index = 0 returns the first element,
     *          index = 1 returns the second element,and so on.
     *          You can assume index will always be a valid index,
     *              i.e 0 <= index < size.
     * @return The integer value at index index
     */
    public int get(int index) {
        return getNode(index)._val;
    }

    /**
     * @param d value to be inserted in the front
     */
    public void insertFront(int d) {
        DNode node = new DNode(d);
        if(size() == 0){
            _front = node;
            _back = node;
        }
        else{
            node._next = _front;
            _front._prev = node;
            _front = node;
        }
    }

    /**
     * @param d value to be inserted in the back
     */
    public void insertBack(int d) {
        DNode node = new DNode(d);
        if(size() == 0){
            _front = node;
            _back = node;
        }
        else{
            node._prev = _back;
            _back._next = node;
            _back = _back._next;
        }
    }

    /**
     * @param d     value to be inserted
     * @param index index at which the value should be inserted
     *              where index = 0 inserts at the front,
     *              index = 1 inserts at the second position, and so onh.
     *              You can assume index will always be a valid index,
     *              i.e 0 <= index <= size.
     */
    public void insertAtIndex(int d, int index) {
        if(index == 0)
            insertFront(d);
        else  if(index == size())
            insertBack(d);
        else {
            DNode indexNode = _front;
            DNode nodeToAdd = new DNode(d);
            while (index > 0){
                indexNode = indexNode._next;
                index--;
            }
            /* At the correct node to insert */
            indexNode._prev._next = nodeToAdd;
            nodeToAdd._prev = indexNode._prev;
            nodeToAdd._next = indexNode;
            indexNode._prev = nodeToAdd;
        }
    }

    /**
     * Removes the first item in the IntDList and returns it.
     * Assume `deleteFront` is never called on an empty IntDList.
     *
     * @return the item that was deleted
     */
    public int deleteFront() {
        int value = _front._val;
        if(size() == 1){
            _front = null;
            _back = null;
        }
        else {
            _front = _front._next;
            _front._prev = null;
        }

        return value;
    }

    /**
     * Removes the last item in the IntDList and returns it.
     * Assume `deleteBack` is never called on an empty IntDList.
     *
     * @return the item that was deleted
     */
    public int deleteBack() {
        int value = _back._val;
        if(size() == 1){
            _front = null;
            _back = null;
        }
        else {
            _back = _back._prev;
            _back._next = null;
        }

        return value;
    }

    /**
     * @param index index of element to be deleted,
     *          where index = 0 returns the first element,
     *          index = 1 will delete the second element, and so on.
     *          You can assume index will always be a valid index,
     *              i.e 0 <= index < size.
     * @return the item that was deleted
     */
    public int deleteAtIndex(int index) {
        return 0;
    }

    /**
     * @return a string representation of the IntDList in the form
     * [] (empty list) or [1, 2], etc.
     * Hint:
     * String a = "a";
     * a += "b";
     * System.out.println(a); //prints ab
     */
    public String toString() {
        if (size() == 0) {
            return "[]";
        }
        String str = "[";
        DNode curr = _front;
        for (; curr._next != null; curr = curr._next) {
            str += curr._val + ", ";
        }
        str += curr._val +"]";
        return str;
    }

    /**
     * DNode is a "static nested class", because we're only using it inside
     * IntDList, so there's no need to put it outside (and "pollute the
     * namespace" with it. This is also referred to as encapsulation.
     * Look it up for more information!
     */
    static class DNode {
        /** Previous DNode. */
        protected DNode _prev;
        /** Next DNode. */
        protected DNode _next;
        /** Value contained in DNode. */
        protected int _val;

        /**
         * @param val the int to be placed in DNode.
         */
        protected DNode(int val) {
            this(null, val, null);
        }

        /**
         * @param prev previous DNode.
         * @param val  value to be stored in DNode.
         * @param next next DNode.
         */
        protected DNode(DNode prev, int val, DNode next) {
            _prev = prev;
            _val = val;
            _next = next;
        }
    }

}
