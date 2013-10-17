var page = require('webpage').create(),
    system = require('system'),
    t, address, returnCode = null;

if (system.args.length === 1) {
    console.log('Usage: crawl.js <some URL>');
    phantom.exit();
}

address = system.args[1];
page.onResourceReceived = function(res) {
    if (res.stage === 'end' && returnCode == null) {
        returnCode = res.status;
        console.log(returnCode);
    }
};
page.open(address, function (status) {
    if (status !== 'success') {
        console.log('FAIL to load the address');
    } else {
        var p = page.content;
        console.log(p);
    }
    phantom.exit();
});