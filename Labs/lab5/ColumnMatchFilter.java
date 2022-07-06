/**
 * TableFilter to filter for entries whose two columns match.
 *
 * @author Matthew Owen
 */
public class ColumnMatchFilter extends TableFilter {

    public ColumnMatchFilter(Table input, String colName1, String colName2) {
        super(input);
        _colValue1 = input.colNameToIndex(colName1);
        _colValue2 = input.colNameToIndex(colName2);
    }

    @Override
    protected boolean keep() {
        return _next.getValue(_colValue1).equals(_next.getValue(_colValue2));
    }

    private int _colValue1;
    private int _colValue2;
}
