describe("Java language interop", function() {

  describe("instanceof operator", function() {
    it("should work with POJOs", function() {
      var list = new java.util.ArrayList();
      expect(list instanceof java.util.ArrayList).toBe(true);
    });
  });

});

