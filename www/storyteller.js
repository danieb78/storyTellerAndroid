var exec = require('cordova/exec');

var Storyteller = {
    initialize: function(apiKey, userId, success, error) {
        exec(success, error, "Storyteller", "initializeSDK", [apiKey, userId]);
    },
    showStorytellerView: function(success, error) {
        exec(success, error, "Storyteller", "showStorytellerView", []);
    }
};

module.exports = Storyteller;