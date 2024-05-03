package de.fraunhofer.sit.sse.valbench;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@SelectClasses(value = { CheckForRequirements.class, GroundTruthTests.class, DeterminismTests.class, CreateJavaEntryPoint.class
})
@Suite
public class TestSuite {

}
