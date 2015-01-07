describe("String.prototype.replace with regular expressions", function() {
  it("should handle simple string replacements with functions", function() {
    var x = ":res[content-length] :referrer[google.com]";
    var y = x.replace(/:([-\w]{2,})(?:\[([^\]]+)\])?/, function(_, a, b, c, d) {
      expect(_).toBe(":res[content-length]");
      expect(a).toBe("res");
      expect(b).toBe("content-length");
      expect(c).toBe(0);
      expect(d).toBe(":res[content-length] :referrer[google.com]");
      return a + "." + b;
    });
    expect(y).toBe("res.content-length :referrer[google.com]");
  });

  it("should handle global string replacements with functions", function() {
    var x = ":remote-addr - - [:date] :res[content-length] - - :foo[bar] :referrer[google.com]";
    var y = x.replace(/:([-\w]{2,})(?:\[([^\]]+)\])?/g, function(_, a, b, c, d) {
      return a + "." + b;
    });
    print(y);
    expect(y).toBe("remote-addr.undefined - - [date.undefined] res.content-length - - foo.bar referrer.google.com");
  });

  it("should work fine with multi-byte UTF-8 characters", function() {
    var x = "foo ohé foobar";
    var y = x.replace(/foo/g, "bar");
    print(y);
    expect(y).toBe("bar ohé barbar");
  });
});

