describe("Java language interop", function() {

  describe("instanceof operator", function() {
    it("should work with POJOs", function() {
      var list = new java.util.ArrayList();
      expect(list instanceof java.util.ArrayList).toBe(true);
    });
  });

  describe("Type coercion between JavaScript and Java", function() {
    it("should pass JavaScript Types.NULL as Java null to Java method calls", function() {
      expect(org.dynjs.runtime.java.SayHiToJava.isNull(null)).toBe(true);
    });

    it("should pass JavaScript Types.NULL as Java null to Java constructors", function() {
      thing = new org.dynjs.runtime.java.Thing(null);
      expect(thing.gotNull()).toBe(true);
    });

    it("should treat Java Floats as JS Numbers", function() {
      var foo = new org.dynjs.runtime.java.DefaultFoo();
      expect((typeof foo.getSomeFloat())).toBe('number');
      expect(foo.getSomeFloat().toString()).toBe('3.14');
      //expect(foo.getSomeFloat() === 3.14).toBe(true);
    });

    it("should treat Java Doubles as JS Numbers", function() {
      var foo = new org.dynjs.runtime.java.DefaultFoo();
      expect((typeof foo.getSomeDouble())).toBe('number');
      expect(foo.getSomeDouble().toString()).toBe('3.14');
      //expect(foo.getSomeFloat() === 3.14).toBe(true);
    });
  });

  describe("Dynamic dispatch of implemented interface functions should work", function() {
    var actions = {
      doIt: function() {
        return "doIt";
      }
    };

    function lookup(name, defaultReturn) {
      return function() {
        return (actions[name] ? actions[name].apply(null, arguments) : defaultReturn);
      };
    }

    var foo = new org.dynjs.runtime.java.Foobar({
      doIt: lookup("doIt", "Default"),
      doItDifferently: lookup("doItDifferently", "Default"),
      doItWithParameters: lookup("doItWithParameters", "Default"),
      doItWithInt: lookup("doItWithInt", -1),
      doItWithPrimitiveInt: lookup("doItWithPrimitiveInt", -1),
      doItWithBoolean: lookup("doItWithBoolean", false),
      doItWithPrimitiveBoolean: lookup("doItWithBoolean", false),
      doItWithObjectReturningBoolean: lookup("doItWithObjectReturningBoolean", false)
    });

    it("should handle defaults", function() {
      expect(foo.doIt()).toBe("doIt");
      expect(foo.doItDifferently()).toBe("Default");
      expect(foo.doItWithParameters("Fajitas!", true)).toBe("Default");
    });
    
    it("should handle changes to the defaults", function() {
      actions.doItDifferently = function() {
        return "doItDifferently";
      };
      expect(foo.doItDifferently()).toBe("doItDifferently");
    });
    
    it("should handle parameters supplied to the function", function() {
      actions.doItWithParameters = function() {
        return Array.prototype.slice.apply(arguments)[0];
      };
      expect(foo.doItWithParameters("Fajitas!", true)).toBe("Fajitas!");
      expect(foo.callWithParameters("Nachos!", true)).toBe("Nachos!");
    });

    it("should handle primitive parameters and return types", function() {
      actions.doItWithInt = function(param) {
        return param + 1;
      };
      actions.doItWithPrimitiveInt = function(param) {
        return param + 1;
      };
      actions.doItWithBoolean = function(param) {
        return param;
      };
      actions.doItWithPrimitiveBoolean = function(param) {
        return param;
      };
      actions.doItWithObjectReturningBoolean = function(param) {
        return true;
      };

      expect(foo.doItWithInt(5)).toBe(6);
      expect(foo.callWithInt(5)).toBe(6);

      expect(foo.doItWithPrimitiveInt(5)).toBe(6);
      expect(foo.callWithPrimitiveInt(6)).toBe(7);

      expect(foo.doItWithBoolean(true)).toBe(true);
      expect(foo.callWithBoolean(true)).toBe(true);

      expect(foo.doItWithPrimitiveBoolean(true)).toBe(true);
      expect(foo.callWithPrimitiveBoolean(true)).toBe(true);

      expect(foo.doItWithObjectReturningBoolean(new java.lang.String("foo"))).toBe(true);
    });

  });

});

