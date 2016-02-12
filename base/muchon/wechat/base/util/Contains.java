package muchon.wechat.base.util;

import org.nutz.dao.entity.Entity;
import org.nutz.dao.jdbc.ValueAdaptor;
import org.nutz.dao.util.cri.AbstractSqlExpression;

public class Contains extends AbstractSqlExpression{
	
	public static Contains create(String name, boolean and, String[] values){
		Contains ct = new Contains(name);
		ct.values = values;
		ct.and = and;
		return ct;
	}
	
	protected Contains(String name) {
		super(name);
	}

	private String[] values;
	private boolean and = true;

	@Override
	public void joinSql(Entity<?> en, StringBuilder sb) {
		if(values != null && values.length > 0){
			String colName = _fmtcol(en);
			sb.append(" ( ");
			String andOr = " and ";
			if(!and){
				andOr = " or ";
			}
			for(int i=0; i<values.length-1; i++){
				sb.append(" CONTAINS(").append(colName).append(", '").append(values[i]).append("')");
				sb.append(andOr);
			}
			sb.append(" CONTAINS(").append(colName).append(", '").append(values[values.length-1]).append("')");
			sb.append(" ) ");
		}
	}

	@Override
	public int joinAdaptor(Entity<?> en, ValueAdaptor[] adaptors, int off) {
        return off;
	}

	@Override
	public int joinParams(Entity<?> en, Object obj, Object[] params, int off) {
        return off;
	}

	@Override
	public int paramCount(Entity<?> en) {
		return 0;
	}

}
