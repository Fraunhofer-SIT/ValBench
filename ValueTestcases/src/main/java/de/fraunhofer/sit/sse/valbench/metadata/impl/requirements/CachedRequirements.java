package de.fraunhofer.sit.sse.valbench.metadata.impl.requirements;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.HashSet;

import com.google.common.reflect.ClassPath;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.fraunhofer.sit.sse.valbench.RuntimeTypeAdapterFactory;
import de.fraunhofer.sit.sse.valbench.metadata.interfaces.IRequirement;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.io.InputStreamReader;

public class CachedRequirements {
	public CachedRequirements(String name, Set<IRequirement> exp) {
		this.testCase = name;
		requirements = exp.toArray(new IRequirement[exp.size()]);

	}
	String testCase;
	IRequirement[] requirements;

	
	private static final Map<String, Set<IRequirement>> REQUIREMENTS;
	
	static {
		Map<String, Set<IRequirement>> reqs = null;
		try (InputStream rs = CachedRequirements.class.getResourceAsStream("/Requirements.json");
				) {
			if (rs != null) {
				try (InputStreamReader reader = new InputStreamReader(rs)) {
					Gson gson = createGSON();
					reqs = new HashMap<>();
					for (CachedRequirements i : gson.fromJson(reader, CachedRequirements[].class)) {
						Set<IRequirement> m = new HashSet<>();
						for (IRequirement d : i.requirements) {
							m.add(d);
						}
						reqs.put(i.testCase, m);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			REQUIREMENTS = reqs;
		}
		
	}
	
	public IRequirement[] getAllRequirements() {
		return requirements;
	}
	
	public static Set<IRequirement> getRequirementsFor(String signature) {
		if (REQUIREMENTS == null)
			throw new RuntimeException("You need to recompile the test library. Requirements file not found in JAR.");
		Set<IRequirement> s = REQUIREMENTS.get(signature);
		if (s == null)
			//see CheckForRequirements class
			throw new RuntimeException("You may need to recompile the test library. Could not find a requirement for " + signature);
		return s;
	}

	public static Gson createGSON() {
		Set<Class<?>> classes;
		try {
			classes = ClassPath.from(ClassLoader.getSystemClassLoader()).getAllClasses().stream()
			.filter(clazz -> clazz.getPackageName().startsWith(CachedRequirements.class.getPackage().getName())).map(clazz -> clazz.load())
			.filter(clazz -> IRequirement.class.isAssignableFrom(clazz))
			.collect(Collectors.toSet());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		GsonBuilder gson = new GsonBuilder();


        final RuntimeTypeAdapterFactory<IRequirement> typeFactory = RuntimeTypeAdapterFactory
                .of(IRequirement.class, "requirementType");
        for (Class<?> d : classes)
			typeFactory.registerSubtype((Class<? extends IRequirement>) d);
        gson.registerTypeAdapterFactory(typeFactory);
		return gson.create();
	}
}