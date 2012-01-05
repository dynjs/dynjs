package org.dynjs.jsr223;

import static org.junit.Assert.assertNotNull;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.junit.Test;

public class DynJSEngineFactoryTest {

	@Test
	public void should_discover_engine_factory() {
		ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
		ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("dynjs");
		assertNotNull(scriptEngine);
	}
	
	@Test
	public void should_discover_engine_factory1() throws Exception {
		ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
		ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("dynjs");
		scriptEngine.eval("var a = 1;");
	}
	

}
