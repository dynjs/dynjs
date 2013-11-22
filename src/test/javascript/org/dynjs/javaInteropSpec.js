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

});

