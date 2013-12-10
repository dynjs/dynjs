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
      doItWithParameters: lookup("doItWithParameters", "Default")
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

    xit("should handle primitive parameters and return types", function() {
      actions.doItWithInt = function() {
        return Array.prototype.slice.apply(arguments)[0];
      };
      actions.doItWithBoolean = function(param) {
        return param;
      };
      expect(foo.doItWithInt(5)).toBe(5);
      expect(foo.callWithInt(5)).toBe(5);
      expect(foo.doItWithBoolean(true)).toBe(true);
      expect(foo.callWithBoolean(true)).toBe(true);
    });
    
  });

});

