const http = require('http');
const port = 8080;

let requestCounter = 0;

function leftPadInt(num, digits) {
    let numString = num.toString();
    let padding = '';
    while (numString.length + padding.length < digits) {
        padding += '0';
    }
    return padding + numString;
}

http.createServer((_, response) => {
    let curTime = new Date();
    response.end(
		'Speed Test Response\n'+
			`This is request #${leftPadInt(requestCounter++, 6)}\n`+
			`Current time is ${curTime.toISOString()}`,
    );
}).listen(port, err => {
    if (err) {
        console.log(err);
    }
})
