package de.fraunhofer.sit.sse.valbench.metadata;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValueComputationTestCase {
	public String[] expectedValues() default {};

	public boolean jvmFails() default false;

	public boolean noEntryPoint() default false;
}
