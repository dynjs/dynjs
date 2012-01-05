package org.dynjs.jsr223;

import static org.junit.Assert.*;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.junit.Test;

public class DynJSEngineFactoryTest {

	@Test
	public void test() {
		ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
		ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("dynjs");
		assertNotNull(scriptEngine);
	}

}
