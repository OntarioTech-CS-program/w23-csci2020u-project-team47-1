
let ws;

async function fetchActiveChatRooms(apiEndpoint, activeChatRoomsContainer) {
    try {
        const response = await fetch(apiEndpoint);
        const data = await response.text();
        //Remove first and last character (square brackets)
        const chatRooms = data.substring(1, data.length - 1).split(',').map(room => room.trim());
        displayActiveChatRooms(chatRooms, activeChatRoomsContainer);
    } catch (error) {
        console.error('Error fetching active chat rooms:', error);
    }
}

function displayActiveChatRooms(chatRooms, activeChatRoomsContainer) {
    console.log('Chat rooms before filtering:', chatRooms);

    //Display all active chat rooms (No duplicates)
    const uniqueChatRooms = [...new Set(chatRooms)];
    console.log('Chat rooms after filtering:', uniqueChatRooms);

    activeChatRoomsContainer.innerHTML = '';

    uniqueChatRooms.forEach(room => {
        addChatRoomToActiveChatRoomsList(room, activeChatRoomsContainer);
    });
}


function addUserToActiveUsersList(username) {
    setTimeout(async () => {
        console.log(username + " added");
        if(username === "Arial") {
            console.log("Arial is not a user");
        } else {
            const activeUsersContainer = document.getElementById('activeUsers'); // Change this line
            const userElement = document.createElement('div');
            userElement.textContent = username;
            userElement.classList.add('user'); // Add this line to style individual users
            activeUsersContainer.appendChild(userElement);
        }

    } , 500);
}


async function enterRoom() {
    let code = document.getElementById("code").value;
    console.log(code);

    ws = new WebSocket("ws://localhost:8080/WSChatServer-1.0-SNAPSHOT/ws/" + code);

    ws.onmessage = async function (event) {
        let message = JSON.parse(event.data);
        console.log("Message received: " + message.type);

        if (message.type === "userJoin" || message.type === "userLeave") {
            await updateActiveUsersList(code);
            await updateActiveRoomsList();
        } else if (message.type === "roomEmpty") {
            // Handle roomEmpty event by updating the list of active chat rooms

            await fetchActiveChatRooms(apiEndpoint, activeChatRoomsContainer);
        } else {
            document.getElementById("log").value += "[" + timestamp() + "] " + message.message + "\n";
        }
    };

    ws.onopen = async function () {
        console.log("WebSocket connection established");
        console.log(code);

        await updateActiveUsersList(code);
    };
}

async function updateActiveUsersList(code) {
    try {
        const response = await fetch("http://localhost:8080/WSChatServer-1.0-SNAPSHOT/api/endpoints/activeUsers/" + code);
        const data = await response.text();
        const activeUsers = data.substring(1, data.length - 1).split(',').map(user => user.trim());
        const filteredUsers = activeUsers.filter(user => user.length < 30);
        const activeUsersContainer = document.getElementById('activeUsers'); // Change this line
        activeUsersContainer.innerHTML = '';

        filteredUsers.forEach(user => {
            addUserToActiveUsersList(user);
        });

    } catch (error) {
        console.error('Error fetching active users:', error);
    }
}

async function updateActiveRoomsList() {
    try {
        const apiEndpoint = 'http://localhost:8080/WSChatServer-1.0-SNAPSHOT/api/endpoints/activeRooms';
        const activeChatRoomsContainer = document.getElementById('activeChatRooms');
        await fetchActiveChatRooms(apiEndpoint, activeChatRoomsContainer);
    } catch (error) {
        console.error('Error updating active rooms list:', error);
    }
}

function reloadRooms() {
    updateActiveRoomsList();
}








const game = () => {
    let pScore = 0;
    let cScore = 0;


    //Start the Game
    const startGame = () => {
        const playBtn = document.querySelector(".intro button");
        const introScreen = document.querySelector(".intro");
        const match = document.querySelector(".match");

        playBtn.addEventListener("click", () => {
            introScreen.classList.add("fadeOut");
            match.classList.add("fadeIn");
        });
    };
    //Play Match
    const playMatch = () => {
        const options = document.querySelectorAll(".options button");
        const playerHand = document.querySelector(".user1");
        const computerHand = document.querySelector(".user2");
        const hands = document.querySelectorAll(".hands img");


        //Computer Options
        const computerOptions = ["rock", "paper", "scissors"];

        options.forEach(option => {
            option.addEventListener("click", function() {
                //Computer Choice
                const computerNumber = Math.floor(Math.random() * 3);
                const computerChoice = computerOptions[computerNumber];

                setTimeout(() => {
                    //Here is where we call compare hands
                    compareHands(this.textContent, computerChoice);
                    //Update Images
                    playerHand.src = `images/${this.textContent}.png`;
                    computerHand.src = `images/${computerChoice}.png`;
                }, 2000);

            });
        });
    };

    const updateScore = () => {
        const playerScore = document.querySelector(".user1-score p");
        const computerScore = document.querySelector(".user2-score p");
        playerScore.textContent = pScore;
        computerScore.textContent = cScore;
    };

    const compareHands = (playerChoice, computerChoice) => {
        //Update Text
        const winner = document.querySelector(".winner");
        //Checking for a tie
        if (playerChoice === computerChoice) {
            winner.textContent = "It is a tie";
            return;
        }
        //Check for Rock
        if (playerChoice === "rock") {
            if (computerChoice === "scissors") {
                winner.textContent = "User 1 Wins";
                pScore++;
                updateScore();
                return;
            } else {
                winner.textContent = "User 2 Wins";
                cScore++;
                updateScore();
                return;
            }
        }
        //Check for Paper
        if (playerChoice === "paper") {
            if (computerChoice === "scissors") {
                winner.textContent = "User 1 Wins";
                cScore++;
                updateScore();
                return;
            } else {
                winner.textContent = "User 2 Wins";
                pScore++;
                updateScore();
                return;
            }
        }
        //Check for Scissors
        if (playerChoice === "scissors") {
            if (computerChoice === "rock") {
                winner.textContent = "User 2  Wins";
                cScore++;
                updateScore();
                return;
            } else {
                winner.textContent = "User 1 Wins";
                pScore++;
                updateScore();
                return;
            }
        }
    };

    //Is call all the inner function
    startGame();
    playMatch();
};

//start the game function
game();