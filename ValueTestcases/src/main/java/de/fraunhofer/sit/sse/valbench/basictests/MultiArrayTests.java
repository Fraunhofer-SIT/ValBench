package de.fraunhofer.sit.sse.valbench.basictests;

import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

import de.fraunhofer.sit.sse.valbench.metadata.ValueComputationTestCase;

public class MultiArrayTests {
	@ValueComputationTestCase
	public static String testSimple() throws IOException {
		Random rand = new Random();
		int[][][] b = new int[2][3][4];
		rand.setSeed(4);
		for (int x = 0; x < 2; x++) {
			for (int y = 0; y < 3; y++) {
				for (int z = 0; z < 4; z++) {
					b[x][y][z] = rand.nextInt();
				}
			}
		}
		return Arrays.deepToString(b);
	}

	@ValueComputationTestCase
	public static String testSimple2() throws IOException {
		Random rand = new Random();
		int[][][] b = new int[2][3][4];
		rand.setSeed(4);
		for (int x = 0; x < 2; x++) {
			b[x] = new int[4][4];
			for (int y = 0; y < 3; y++) {
				for (int z = 0; z < 4; z++) {
					b[x][y][z] = rand.nextInt();
				}
			}
		}
		return Arrays.deepToString(b);
	}

	@ValueComputationTestCase
	public static String testEmpty() throws IOException {
		int[][][][] b = new int[2][3][4][5];
		return Arrays.deepToString(b);
	}

}
