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


function addChatRoomToActiveChatRoomsList(room, ulElement) {
    const ul = document.createElement('ul');
    ul.textContent = room;
    ulElement.appendChild(ul);
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


document.addEventListener('DOMContentLoaded', () => {
    const activeChatRoomsContainer = document.getElementById('activeChatRooms');
    const apiEndpoint = 'http://localhost:8080/WSChatServer-1.0-SNAPSHOT/api/endpoints/activeRooms';

    fetchActiveChatRooms(apiEndpoint, activeChatRoomsContainer);
    updateActiveRoomsList();

});


const chatLog = document.getElementById("log");
document.getElementById("input").addEventListener("keyup", function (event) {
    if (event.keyCode === 13) {
        let request = {"type":"chat", "msg":event.target.value};
        ws.send(JSON.stringify(request));
        event.target.value = "";
        chatLog.scrollTop = chatLog.scrollHeight;

    }
});

function timestamp() {
    var d = new Date(), minutes = d.getMinutes();
    if (minutes < 10) minutes = '0' + minutes;
    return d.getHours() + ':' + minutes;
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

