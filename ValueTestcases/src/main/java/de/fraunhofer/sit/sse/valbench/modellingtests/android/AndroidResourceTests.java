package de.fraunhofer.sit.sse.valbench.modellingtests.android;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import android.net.wifi.WifiConfiguration;
import de.fraunhofer.sit.sse.valbench.base.MainActivity;
import de.fraunhofer.sit.sse.valbench.base.R;
import de.fraunhofer.sit.sse.valbench.metadata.ValueComputationTestCase;

public class AndroidResourceTests {

	@ValueComputationTestCase(expectedValues = { "[97, 110, 100, 114, 111]" }, jvmFails = true)
	public static String testResources() throws IOException {
	    InputStream inp = MainActivity.resources.openRawResource(R.raw.androidres);
	    byte[] b = new byte[5];
	    inp.read(b);
		return Arrays.toString(b);
	}
}
