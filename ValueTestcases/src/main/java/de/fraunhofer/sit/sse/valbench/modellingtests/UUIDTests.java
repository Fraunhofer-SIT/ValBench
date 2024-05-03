package de.fraunhofer.sit.sse.valbench.modellingtests;

import java.io.IOException;
import java.util.UUID;

import de.fraunhofer.sit.sse.valbench.metadata.ValueComputationTestCase;

public class UUIDTests {

	@ValueComputationTestCase
	public static String testUUID() throws IOException {
		UUID u = UUID.fromString("123e4567-e89b-42d3-a456-556642440000");
		UUID u2 = UUID.fromString("123e4567-e89b-42d3-a456-556642440001");
		String s = "" + u.getLeastSignificantBits() + u.getMostSignificantBits() + u.variant() + u.version()
				+ u.hashCode();
		s += "" + u.compareTo(u2) + u.equals(u2) + u2.hashCode() + u.toString();
		return s;
	}

}
