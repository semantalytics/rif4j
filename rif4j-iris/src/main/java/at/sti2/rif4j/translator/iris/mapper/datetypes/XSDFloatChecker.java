package at.sti2.rif4j.translator.iris.mapper.datetypes;

public class XSDFloatChecker {

	public static float valueOf(String value) {
		//INF, -INF and NaN
		if (value.equals("INF"))
		{
			return Float.POSITIVE_INFINITY;
		}
		else if (value.equals("-INF"))
		{
			return Float.NEGATIVE_INFINITY;
		}
		else if (value.equals("NaN"))
		{
			return Float.NaN;
		}
		else
		{
			return Float.valueOf(value);
		}			
	}
}
