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
  xit("should be 'subclassable'", function() {
    function BetterArray() {
        Array.call(this);
    }
    BetterArray.super_ = Array;
    BetterArray.prototype = Object.create(Array.prototype, {
              constructor: {
                value: BetterArray,
                enumerable: false,
                writable: true,
                configurable: true
              }});
    var x = new BetterArray(1, 2, 3, 4);
    expect(x[0]).toBe(1);
  });
});
