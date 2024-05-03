package de.fraunhofer.sit.harvester.loggingpoints;

public class LoggingUtils {

	//This has to be kept consistent with de.sit.fraunhofer.remote.Logging
	private static Object convertObjectArray(Object array) {
		if (array instanceof byte[]) {
			byte[] in = (byte[])array;
			Object[] resulting = new Object[in.length];
			for (int i = 0; i < in.length; i++)
				resulting[i] = String.valueOf(in[i]);
			return resulting;
		}
		if (array instanceof short[]) {
			short[] in = (short[])array;
			Object[] resulting = new Object[in.length];
			for (int i = 0; i < in.length; i++)
				resulting[i] = String.valueOf(in[i]);
			return resulting;
		}
		if (array instanceof boolean[]) {
			boolean[] in = (boolean[])array;
			Object[] resulting = new Object[in.length];
			for (int i = 0; i < in.length; i++)
				resulting[i] = String.valueOf(in[i]);
			return resulting;
		}
		if (array instanceof long[]) {
			long[] in = (long[])array;
			Object[] resulting = new Object[in.length];
			for (int i = 0; i < in.length; i++)
				resulting[i] = String.valueOf(in[i]);
			return resulting;
		}
		if (array instanceof int[]) {
			int[] in = (int[])array;
			Object[] resulting = new Object[in.length];
			for (int i = 0; i < in.length; i++)
				resulting[i] = String.valueOf(in[i]);
			return resulting;
		}
		if (array instanceof float[]) {
			float[] in = (float[])array;
			Object[] resulting = new Object[in.length];
			for (int i = 0; i < in.length; i++)
				resulting[i] = String.valueOf(in[i]);
			return resulting;
		}
		if (array instanceof double[]) {
			double[] in = (double[])array;
			Object[] resulting = new Object[in.length];
			for (int i = 0; i < in.length; i++)
				resulting[i] = String.valueOf(in[i]);
			return resulting;
		}

		return array;
	}

	private static String writeObject(Object o) {
		o = convertObjectArray(o);
		if (o instanceof Object[])
		{
			//System.out.println("Detected object-array");
			StringBuilder builder = new StringBuilder();
			builder.append("new Array[] { ");
			Object[] array = (Object[])o;
			for (int i = 0; i < array.length; i++) { 
				Object obj = array[i];
				
				if (obj == null)
					builder.append("<NULLVALUE>");
				else
				{
					//System.out.println("Object value:" + obj);
					builder.append(writeObject(obj));
				}
				if (i != array.length - 1)
					builder.append(", ");
			}
			
			builder.append(" }");
			return builder.toString();
		} else {
			if (o == null)
				return "<NULLVALUE>";
			else
			{
				return o.toString();
			}
		}
	}
	public static String encode(Object... objects) {
		StringBuilder values = new StringBuilder();
		for (Object o : objects)
		{
			String currentObj = writeObject(o);
			values.append(currentObj.length());
			values.append("|");
			values.append(currentObj);
		}
		return values.toString();
	}
}
