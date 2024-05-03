package de.fraunhofer.sit.sse.valbench.modellingtests;

import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import de.fraunhofer.sit.sse.valbench.metadata.ValueComputationTestCase;

public class EnumSetTests {

	private static enum TestEnum {
		E1, E2, E3, E4, E5, E6, E7, E8;

		private int x;
		TestEnum y;
	}

	private static String setToString(Set<TestEnum> set) {
		StringBuilder sb = new StringBuilder();
		List<TestEnum> l = new ArrayList<>(set);
		Collections.sort(l);
		for (TestEnum t : l)
			sb.append(t.toString());
		return sb.toString();
	}

	@ValueComputationTestCase
	public static String test1() {
		Set<TestEnum> set = EnumSet.noneOf(TestEnum.class);
		set.add(TestEnum.E1);
		return setToString(set);
	}

	@ValueComputationTestCase
	public static String test2() {
		Set<TestEnum> set = new HashSet<>();
		set.add(TestEnum.E1);
		return setToString(set);
	}

	@ValueComputationTestCase
	public static String test3() {
		Set<TestEnum> set = new LinkedHashSet<>();
		for (TestEnum v : TestEnum.values())
			set.add(v);
		return setToString(set);
	}

	@ValueComputationTestCase
	public static String test4() {
		Set<TestEnum> set = EnumSet.allOf(TestEnum.class);
		return setToString(set);
	}

	@ValueComputationTestCase
	public static String test5() {
		Set<TestEnum> set = EnumSet.range(TestEnum.E2, TestEnum.E5);
		return setToString(EnumSet.copyOf(set));
	}

	@ValueComputationTestCase
	public static String test6() {
		EnumSet<TestEnum> set = EnumSet.range(TestEnum.E2, TestEnum.E5);
		return setToString(EnumSet.complementOf(set));
	}

	@ValueComputationTestCase
	public static String test7() {
		EnumSet<TestEnum> set = EnumSet.noneOf(TestEnum.class).clone();
		set.add(TestEnum.E3);
		set.add(TestEnum.E7);
		return setToString(EnumSet.copyOf(EnumSet.complementOf(set)));
	}

	@ValueComputationTestCase
	public static String test8_0() {
		EnumSet<TestEnum> set = EnumSet.noneOf(TestEnum.class);
		set.add(TestEnum.E1);
		set.add(TestEnum.E4);
		return String.valueOf(set.contains(TestEnum.E6));
	}

	@ValueComputationTestCase
	public static String test8_1() {
		EnumSet<PosixFilePermission> set = EnumSet.noneOf(PosixFilePermission.class);
		set.add(PosixFilePermission.OTHERS_EXECUTE);
		set.add(PosixFilePermission.OWNER_READ);
		return String.valueOf(set.contains(PosixFilePermission.OTHERS_EXECUTE));
	}

	@ValueComputationTestCase
	public static String test8_2() {
		EnumSet<PosixFilePermission> set = EnumSet.noneOf(PosixFilePermission.class);
		set.add(PosixFilePermission.OTHERS_EXECUTE);
		set.add(PosixFilePermission.OWNER_READ);
		return String.valueOf(set.contains(PosixFilePermission.OTHERS_WRITE));
	}

	@ValueComputationTestCase
	public static String test9() {
		EnumSet<PosixFilePermission> set = EnumSet.noneOf(PosixFilePermission.class);
		set.add(PosixFilePermission.OTHERS_EXECUTE);
		set.add(PosixFilePermission.OWNER_READ);
		return PosixFilePermissions.toString(set);
	}

	@ValueComputationTestCase
	public static String test10() {
		EnumSet<PosixFilePermission> set = EnumSet.noneOf(PosixFilePermission.class);
		set.addAll(PosixFilePermissions.fromString("rwx-w--wx"));
		set.add(PosixFilePermission.GROUP_READ);
		return PosixFilePermissions.toString(set);
	}

}