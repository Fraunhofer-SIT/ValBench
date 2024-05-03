package de.fraunhofer.sit.sse.valbench.modellingtests.android;

import java.io.IOException;
import java.util.Random;

import android.content.Intent;
import de.fraunhofer.sit.sse.valbench.metadata.ValueComputationTestCase;

public abstract class AndroidIntentTests {

	@ValueComputationTestCase(expectedValues = {
			"Intent { act=android.intent.action.CHOOSER (has extras) }" }, jvmFails = true)
	public static String testIntent() throws IOException {
		Intent intent = new Intent("Foo");
		return Intent.createChooser(intent, "title").toString();
	}

	@ValueComputationTestCase(expectedValues = { "Intent { act=act1 pkg=act1 }",
			"Intent { act=act2 pkg=act2 }" }, jvmFails = true)
	public static String testIntentCompositeValue() throws IOException {
		Intent intent = new Intent();
		if (new Random().nextBoolean()) {
			intent.setAction("act1");
			intent.setPackage("package1");
		} else {
			intent.setAction("act2");
			intent.setPackage("package2");
		}

		return Intent.createChooser(intent, "title").toString();
	}

}