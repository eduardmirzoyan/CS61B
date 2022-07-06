/**
 * TableFilter to filter for entries equal to a given string.
 *
 * @author Matthew Owen
 */
public class EqualityFilter extends TableFilter {

    public EqualityFilter(Table input, String colName, String match) {
        super(input);
        _input = input;
        _colName = colName;
        _match = match;
    }

    @Override
    protected boolean keep() {
        return _next.getValue(_input.colNameToIndex(_colName)).equals(_match);
    }

    private Table _input;
    private String _colName;
    private String _match;
}
