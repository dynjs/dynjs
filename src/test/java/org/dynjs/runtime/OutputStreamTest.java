package org.dynjs.runtime;

import static org.fest.assertions.Assertions.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.io.IOException;

import org.dynjs.Config;
import org.junit.Test;

public class OutputStreamTest {
    @Test
    public void testOutputStream() throws UnsupportedEncodingException, IOException {
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      PrintStream ps = new PrintStream(bos);

      Config config = new Config();
      config.setOutputStream(ps);
      DynJS dynjs = new DynJS(config);

      dynjs.evaluate("print(\"hello world\");");

      ps.flush();
      bos.flush();
      String result = bos.toString("UTF-8");

      assertThat(result).isEqualTo("hello world\n");

    }
}
