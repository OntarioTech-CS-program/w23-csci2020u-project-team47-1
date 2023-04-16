package com.example.wschatserverdemo;

import java.io.*;

import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

// Annotation to define the servlet
@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet {
    // Initializing the message variable
    private String message;
    // Initializing the servlet
    public void init() {
        message = "Hello World!";
    }
    // Processing the HTTP GET request and generating the response
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        // Writing the response to the client
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>" + message + "</h1>");
        out.println("</body></html>");
    }
    // Closing the servlet
    public void destroy() {
    }

}