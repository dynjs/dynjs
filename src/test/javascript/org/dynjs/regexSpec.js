describe("regular expressions", function() {

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
        expect(y).toBe("bar ohé barbar");
    });
  });

  describe("multiline", function() {
    it("should not match newlines with dot character", function() {
      var multiline = "FOO=bar\nFOOBAR=boom\n";
      var regex = new RegExp(/([\w+]+)\s*\=\s*(.*)/gi);
      var regexWithM = new RegExp(/([\w+]+)\s*\=\s*(.*)/gmi);
      var verifyExec = function(arr) {
        expect(arr.length).toBe(3);
        expect(arr[0]).toBe("FOO=bar");
        expect(arr[1]).toBe("FOO");
        expect(arr[2]).toBe("bar");
      };
      var verifyMatch = function(arr) {
        expect(arr.length).toBe(2);
        expect(arr[0]).toBe("FOO=bar");
        expect(arr[1]).toBe("FOOBAR=boom");
      };
      verifyExec(regex.exec(multiline));
      verifyExec(regexWithM.exec(multiline));
      verifyMatch(multiline.match(regex));
      verifyMatch(multiline.match(regexWithM));

    });

    it("should match ^ for first line without multiline flag", function() {
      var multiline = "FOO=bar\nFOOBAR=boom\n";
      var regex = new RegExp(/^([\w+]+)\s*\=\s*(.*)/gi);
      var arr = multiline.match(regex);
      expect(arr.length).toBe(1);
      expect(arr[0]).toBe("FOO=bar");
    });

    it("should match ^ for all lines with multiline flag", function() {
      var multiline = "FOO=bar\nFOOBAR=boom\n";
      var regex = new RegExp(/^([\w+]+)\s*\=\s*(.*)/gim);
      var arr = multiline.match(regex);
      expect(arr.length).toBe(2);
      expect(arr[0]).toBe("FOO=bar");
      expect(arr[1]).toBe("FOOBAR=boom");
    });
  });

});

describe("String.prototype.match", function() {
  it("should work with multi line input", function() {
    var line = "FOO=bar\nFOOBAR=barfoo";
    var regex = new RegExp(/([\w+]+)\s*\=\s*(.*)/gmi);
    var lines = regex.exec(line);

    expect(lines).toBeTruthy();
    expect(lines.length).toBe(3);
  });
});

describe("Nested regex", function() {

  it("should work", function() {

    var regex = new RegExp("^((?:(?:[0-9a-f]+::?)+)?)");
    var match = "FE80:0000:0000:0000:0202:B3FF:FE1E:8329";
    var result = match.match(regex);
    expect(result.length).toBe(2);
    expect(result[0]).toBe('');
    expect(result[1]).toBe('');
  });
});

