/**
 * TableFilter to filter for containing substrings.
 *
 * @author Matthew Owen
 */
public class SubstringFilter extends TableFilter {

    public SubstringFilter(Table input, String colName, String subStr) {
        super(input);
        _input = input;
        _colName = colName;
        _subStr = subStr;
    }

    @Override
    protected boolean keep() {
        return _next.getValue(_input.colNameToIndex(_colName)).contains(_subStr);
    }

    private Table _input;
    private String _colName;
    private String _subStr;
}
