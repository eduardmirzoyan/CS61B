/**
 * TableFilter to filter for entries greater than a given string.
 *
 * @author Matthew Owen
 */
public class GreaterThanFilter extends TableFilter {

    public GreaterThanFilter(Table input, String colName, String ref) {
        super(input);
        _input = input;
        _colName = colName;
        _ref = ref;
    }

    @Override
    protected boolean keep() {
        return _next.getValue(_input.colNameToIndex(_colName)).compareTo(_ref) > 0;
    }

    private Table _input;
    private String _colName;
    private String _ref;
}
