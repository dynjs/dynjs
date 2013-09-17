describe("CommonJS require()", function() {

  describe("load paths", function(){

    it("should have a pushLoadPath() function", function(){
      expect(typeof require.pushLoadPath).toBe('function');
      require.pushLoadPath('/foo/bar');
      expect(require.paths.get(0)).toBe('/foo/bar');
      expect(require.paths.size() > 1).toBe(true);
    });

  });

  describe("global module", function() {
    it("should have an id and exports property", function() {
      expect(typeof module).toBe('object');
      expect(typeof module.id).toBe('string');
      expect(typeof module.exports).toBe('object');
      expect(typeof exports).toBe('object');
    });
  });

});

