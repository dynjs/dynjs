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
