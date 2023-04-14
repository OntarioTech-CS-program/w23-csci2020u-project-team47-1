package com.example.finalgroupproject;
import java.io.*;
import java.util.HashSet;
import java.util.Set;

import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.apache.commons.lang3.RandomStringUtils;

@WebServlet(name = "chatServlet", value = "/chat-servlet")
public class GameRoomGenerator extends HttpServlet {
    private String message;
    public static Set<String> rooms = new HashSet<>();

    public static String generatingRandomUpperAlphanumericString(int length) {
        String generatedString = RandomStringUtils.randomAlphanumeric(length).toUpperCase();
        while (rooms.contains(generatedString)){
            generatedString = RandomStringUtils.randomAlphanumeric(length).toUpperCase();
        }
        rooms.add(generatedString);
        return generatedString;
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        out.println(generatingRandomUpperAlphanumericString(5));

    }

    public void destroy() {
    }
}