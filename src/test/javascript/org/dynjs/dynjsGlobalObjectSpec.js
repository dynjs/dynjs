describe("DynJS global object", function() {
    it("should allow to add entries to classpath during runtime", function() {
        var me = Packages.me;

        expect(typeof me.qmx.vraptor.authz.AuthzInfo).not.toMatch(/Class/);
        dynjs.classpath.push("./target/fixtures/vraptor-authz-0.2.jar")
        expect(typeof me.qmx.vraptor.authz.AuthzInfo).toMatch(/Class/);
    });
});