let ws;
let game_code;
function enterRoom() {
    let code = document.getElementById("room-code").value;
    game_code = code;
    ws = new WebSocket("ws://localhost:8080/WSChatServerDemo-1.0-SNAPSHOT/ws/" + code);

    ws.onmessage = function (event) {
        console.log(event.data);
        let message = JSON.parse(event.data);
        document.getElementById("log").value += "[" + timestamp() + "] " + message.message + "\n";
    }
}
document.getElementById("input").addEventListener("keyup", function (event) {
    if (event.keyCode === 13) {
        let request = {"type":"chat", "msg":event.target.value};
        ws.send(JSON.stringify(request));
        event.target.value = "";
    }
});


function startNewGame()
{
    socket = new WebSocket("ws://localhost:8080/WSChatServerDemo-1.0-SNAPSHOT/ws/game/" + game_code);

// When the socket connection is established, prompt the user for their name
    socket.onopen = function() {
        var username = prompt("Enter your name:");
        socket.send(username);
    }

// When the user clicks the submit button, send their choice to the server
    var submitButton = document.getElementById("submit-button");
    submitButton.addEventListener("click", function() {
        var userInput = document.getElementById("user-input").value.toUpperCase();
        if (userInput === "R" || userInput === "P" || userInput === "S") {
            socket.send(userInput);
        }
    });

// When a message is received from the server, update the game result
    socket.onmessage = function(event) {
        var message = JSON.parse(event.data);
        if (message.type === "game-result") {
            var resultElement = document.getElementById("result");
            resultElement.innerHTML = message.result;
        }
    };
}

function timestamp() {
    var d = new Date(), minutes = d.getMinutes();
    if (minutes < 10) minutes = '0' + minutes;
    return d.getHours() + ':' + minutes;
}