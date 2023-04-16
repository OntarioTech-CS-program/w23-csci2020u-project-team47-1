# INTERACTIVE CLIENT ROOM
> Course: CSCI 2020U: Software Systems Development and Integration

**The following is the backend for the Chat Server logic**

## ChatServer
* The @ServerEndpoint annotation specifies the endpoint URL for the WebSocket connection, which is /ws/{roomID}. The {roomID} is a parameter that is passed in as a PathParam.
* The ChatServer class has three member variables: usernames, roomList, and roomHistoryList. usernames is a HashMap that maps a user's session ID to their username. roomList is a HashMap that maps a user's session ID to the chat room they are in. roomHistoryList is a HashMap that maps a chat room ID to its chat history.
* The class has three methods annotated with @OnOpen, @OnClose, and @OnMessage. These annotations are used by the WebSocket API to denote methods that should be called when a WebSocket connection is opened, closed, or when a message is received.
* The open method is called when a WebSocket connection is opened. It takes in two parameters: the roomID passed in as a PathParam and the session object representing the WebSocket session. The method adds the user's session ID to the roomList for the chat room they joined, loads the chat room's history, and sends a welcome message to the user. If the chat room has history, it sends it to the user and adds it to the roomHistoryList. If the chat room has no history, it initializes it.
* The close method is called when a WebSocket connection is closed. It takes in a session object representing the WebSocket session. The method removes the user's username from the usernames map and removes the user's session ID from the roomList. It adds a log to the chat room's history that the user left and broadcasts the message to other users in the same chat room. If there are no other users in the chat room, it saves the chat room's history.
* The handleMessage method is called when a message is received. It takes in the message as a String and the session object representing the WebSocket session. If the user has already set their username, it adds the user's message to the chat room's history and broadcasts the message to other users in the chat room. If the user has not set their username, it sets their username and sends a welcome message. It also adds a log to the chat room's history that the user joined and broadcasts the message to other users in the chat room.

## FileReaderWriter 
* "saveNewFile" method: This method takes in a directory, a filename, and a string content as input parameters. It creates a new file with the specified name in the specified directory, writes the input string content to the file, and closes the file.
* "readHistoryFile" method: This method takes in a directory and a filename as input parameters. It reads the content of the specified file, if it exists, and returns it as a string. If the file does not exist, it creates a new file with the specified name in the specified directory and returns null.

## HistoryResource
* The class uses the Jakarta RESTful Web Services API to handle HTTP requests, which allows it to respond to requests made to a specified URL path. The @Path annotation is used to specify the base path for the HTTP requests handled by this class.
* The class has three methods that correspond to three different HTTP request methods: GET, POST, and PUT. The @GET, @POST, and @PUT annotations are used to indicate which HTTP methods each method should handle.
* The hello() method is a simple method that returns a string "Hello, World!" when the base path /history is accessed using the GET HTTP method with the "text/plain" content type.
* The getRoomHistory() method handles GET HTTP requests to /history/{roomID} path with "application/json" content type. This method reads the content of a JSON file whose name is specified by the roomID parameter and returns its contents as a JSON object with the room and log fields. The room field contains the value of the roomID parameter, and the log field contains the contents of the JSON file.
* The saveRoomHistory() method handles POST HTTP requests to /history/{roomID} path with "application/json" content type. This method receives a JSON object containing chat room history as a parameter and saves it to a JSON file in a directory called /chatHistory. The name of the file is specified by the room field in the received JSON object. This method returns a 200 HTTP status code if the operation is successful.

## ResourceAPI
* This code is a Java class named ResourceAPI, which provides methods for accessing a web API for a chat application. The class contains two methods, saveChatRoomHistory and loadChatRoomHistory, both of which interact with the web API to send or receive data.
* The saveChatRoomHistory method sends a POST request to the API to save a chat room's history. It takes two parameters, roomID and log, which represent the ID of the chat room and the log of messages to be saved, respectively. The method constructs a URL to the appropriate API endpoint using the roomID parameter and sets the necessary headers for the request to be sent as JSON data. It then writes the JSON data to the request's output stream using the roomID and log parameters, and reads the response from the input stream.
* The loadChatRoomHistory method sends a GET request to the API to load a chat room's history. It takes one parameter, roomID, which represents the ID of the chat room whose history is to be loaded. The method constructs a URL to the appropriate API endpoint using the roomID parameter and sets the necessary headers for the request to be sent as JSON data. It then reads the response from the input stream and converts it to a string, which is then transformed into objects using the org.json library. The log data is then extracted from the objects and returned as a string.

## HelloServlet
* The package statement at the beginning of the code specifies the Java package where this servlet class resides.
* The imports at the beginning of the code import classes that are used by the servlet, including classes from the Jakarta Servlet API.
* The @WebServlet annotation specifies the name and URL pattern of the servlet. In this case, the name of the servlet is "helloServlet" and the URL pattern is "/hello-servlet".
* The class definition starts with the keyword "public" followed by the name of the class, "HelloServlet", and the keyword "extends" followed by "HttpServlet". This means that the class is a subclass of the HttpServlet class, which provides methods for processing HTTP requests and generating HTTP responses.
* The message variable is declared and initialized to "Hello World!" in the init() method, which is called when the servlet is first loaded.
* The doGet() method is the main method that processes HTTP GET requests. It takes two arguments: a HttpServletRequest object that represents the incoming HTTP request, and a HttpServletResponse object that represents the outgoing HTTP response. The method sets the content type of the response to "text/html" and writes an HTML page with the message to the response output stream.
* The destroy() method is called when the servlet is unloaded, and can be used to release any resources held by the servlet. In this case, the method is empty because the servlet doesn't hold any resources that need to be released.

