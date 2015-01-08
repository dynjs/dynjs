describe("Java Collections objects", function() {

  describe("java.util.List", function(){

    xit("should act like a Javascript Array", function(){
      var list = new java.util.ArrayList();
      list.add("Foo");
      expect(list.length).toBe(1);
      expect(list[0]).toBe("Foo");
    });

  });

});

describe("Javascript Arrays", function() {
  it("should be 'subclassable'", function() {
    function BetterArray() {
        Array.apply(this, arguments);
    }
    BetterArray.prototype = Object.create(Array.prototype, {
              constructor: {
                value: BetterArray,
                enumerable: false,
                writable: true,
                configurable: true
              }});
   var x = new BetterArray(1, 2, 3, 4);
   expect(x.length).toBe(4);
   expect(x[0]).toBe(1);
   expect(x[1]).toBe(2);
   expect(x[2]).toBe(3);
   expect(x[3]).toBe(4);
  });
});
