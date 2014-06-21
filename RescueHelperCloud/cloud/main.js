var async = require("cloud/async");

// Use Parse.Cloud.define to define as many cloud functions as you want.
// For example:
Parse.Cloud.define("hello", function(request, response) {
  response.success("Hello world!");
});

Parse.Cloud.define("setupusers", function(req, res) {
    var users = {};

    var roleACL = new Parse.ACL();
    var roleDispatcher = new Parse.Role("Dispatcher", roleACL);
    var roleResponder = new Parse.Role("Responder", roleACL);

    async.series([
        function(done) {
            async.eachSeries(["Tom", "Ron", "Bob", "Jon", "BonJovi"],
                function(name, done2) {
                    var user = new Parse.User();
                    user.set("username", name);
                    user.set("password", "letmein");
                    user.set("email", name.toLowerCase() + "@fireman.com");
                    users[name] = user;
                    user.save(null, {
                        success: function() { done2(); },
                        error: function() { done2(); }
                    });
                },
                function() {
                    done();
                }
            );
        },
        function(done) {
            roleDispatcher.getUsers().add(users["Jon"]);
            roleDispatcher.save(null, {
                success: function() {
                    done();
                }
            });
        },
        function(done) {
            roleResponder.getUsers().add(users["BonJovi"]);
            roleResponder.getUsers().add(users["Bob"]);
            roleResponder.getUsers().add(users["Tom"]);
            roleResponder.getUsers().add(users["Ron"]);
            roleResponder.save(null, {
                success: function() {
                    done();
                }
            });
        }
    ], function(err, results) {
        res.success("Done");
    });
});

Parse.Cloud.define("setupevent", function(req, res) {
    var tasks = [];
    var event;
    async.series([
        function(done) {
            async.eachSeries([
                [1, "Forced entry", "Jon", 2],
                [2, "Clear Rooms", "Ron", 1],
                [3, "Locate suspect", "Bob", 1],
                [4, "Occupant safety", "Tom", 1],
                [5, "Witness interview", null, 0]
            ], function(tuple, done2) {
                var task = new Parse.Object("Task");
                task.set("priority", tuple[0]);
                task.set("name", tuple[1]);
                task.set("status", tuple[3]);
                
                var query = new Parse.Query(Parse.User);
                query.equalTo("username", tuple[2]);

                var handler = function(user) {
                    if (user) {
                        task.set("assignedTo", user);
                    }
                    task.save(null, {
                        success: function() { tasks.push(task); done2(); },
                        error: function(err) { res.error(err); done2(); }
                    });
                };

                query.first({
                    success: handler,
                    error: handler
                });
            }, function() {
                done();
            });
        },
        function(done) {
            event = new Parse.Object("Event");
            event.set("name", "Burglary #1012 Elm Street");
            for (var i = 0; i < tasks.length; i++) {
                event.relation("tasks").add(tasks[i]);
            }
            event.save(null, {
                success: function() { done(); },
                error: function() { done(); }
            });
        },
        function(done) {
            async.eachSeries(tasks, function(task, done2) {
                task.set("event", event);
                task.save(null, {
                    success: function() { done2(); },
                    error: function() { done2(); }
                });
            }, function() {
                done();
            });
        }
    ], function() { res.success("Done"); });
});

Parse.Cloud.define("reset", function(req, res) {
    async.eachSeries(["_Role", "_User", "Event", "Task"],
        function(coll, done) {
            var Collection = Parse.Object.extend(coll);
            var query = new Parse.Query(Collection);
            query.find({
                success: function(results) {
                    async.eachSeries(results, function(obj, done2) {
                        obj.destroy({
                            success: function() { done2(); },
                            error: function() { done2(); }
                        });
                    }, function() { done(); });
                },
                error: function() { done(); }
            });
        },
        function() {
            res.success("Done");
        }
    );   
});

