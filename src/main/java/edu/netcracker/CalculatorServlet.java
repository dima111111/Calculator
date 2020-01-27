package edu.netcracker;

import java.io.*;
import java.util.Map;
import java.util.LinkedHashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpSession;
import com.google.gson.*;
import java.util.HashMap;
import java.io.FileReader;
import au.com.bytecode.opencsv.CSVReader;

/**
 * CalculatorServlet --- calculator webapp servlet.
 * @author    Dmitry Vinogradov
 */
public class CalculatorServlet extends HttpServlet {
    
    Map<String, Calculator> sessionCalculators;

    public CalculatorServlet() {

        sessionCalculators = new HashMap<String, Calculator>();
    }

    /**
     * Clear calculator variables and return calculator page.
     * @param request HttpServletRequest containing request params
     * @param request HttpServletResponse containing response
     * @return void.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String sessionId = request.getRequestedSessionId();
        HttpSession session = request.getSession();

        Object isSessionAuth = "";
        if (session.getAttribute("auth") != null) {
            isSessionAuth = session.getAttribute("auth");
        }
        if (isSessionAuth.equals(true)) {
            if (sessionId != null && sessionCalculators.containsKey(sessionId)) {
                Calculator calc = sessionCalculators.get(sessionId);
                calc.clearVars();
                processRequestGetPage(request, response, "index.jsp");
            } else {
                processRequestGetPage(request, response, "auth.jsp");
            }
        } else {
            processRequestGetPage(request, response, "auth.jsp");
        }
    }


    /**
     * Process user request (POST) and return calculator page.
     * @param request HttpServletRequest containing request params
     * @param response HttpServletResponse containing response
     * @return void.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String sessionId = request.getRequestedSessionId();
        String action = "";
        if (request.getParameter("action") != null) {
            action = request.getParameter("action");
        }
        if (action.equals("auth")) {
            processRequestAuth(request, response);
        } else {
            HttpSession session = request.getSession();
            Object isSessionAuth = "";
            if (session.getAttribute("auth") != null) {
                isSessionAuth = session.getAttribute("auth");
            }
            if (isSessionAuth.equals(true)) {
                Calculator calc;
                if (sessionCalculators.containsKey(sessionId)) {
                    calc = sessionCalculators.get(sessionId);
                } else {
                    calc = new Calculator();
                    sessionCalculators.put(sessionId, calc);
                }
                calc.setCurrentParametrs(request);
                calc.changeScreen();
                processRequestPostCalc(request, response, calc);
            } else {
                response.setHeader("error", "Authorization error");
                processRequestGetPage(request, response, "auth.jsp");
            }
        }
    }
    /**
     * Check authorization data and return response.
     * @param request HttpServletRequest containing request params
     * @param response HttpServletResponse containing response
     * @return void
     */
    protected void processRequestAuth(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        if (isUserExist(request)) {
            HttpSession session = request.getSession();
            session.setAttribute("auth", true);
            processRequestGetPage(request, response, "index.jsp");
        } else {
            response.setHeader("error", "invalid data");
            processRequestGetPage(request, response, "auth.jsp");
        }
    }

    /**
     * Check existing of user by request info..
     * @param request HttpServletRequest containing request params
     * @return boolean
     */
    protected boolean isUserExist(HttpServletRequest request)
            throws ServletException, IOException {
        String userName = request.getParameter("username");
        String password = request.getParameter("password");

        String filePath = getServletContext().getRealPath("/") + "users.csv";
        if (new File(filePath).exists()) {
            CSVReader reader = new CSVReader(new FileReader(filePath), ';' , '"' , 1);
            String[] nextLine;

            while ((nextLine = reader.readNext()) != null) {
                if (userName.equals(nextLine[0]) && password.equals(nextLine[1])) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Process user request GET and print html.
     * @param request HttpServletRequest containing request params
     * @param response HttpServletResponse containing response
     * @param page String containing page name
     * @return void.
     */
    protected void processRequestGetPage(HttpServletRequest request, HttpServletResponse response, String page)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        RequestDispatcher dispatcher = request.getServletContext().getRequestDispatcher("/WEB-INF/" + page);
        dispatcher.forward(request, response);
    }

    /**
     * Process user request and print html.
     * @param request HttpServletRequest containing request params
     * @param request HttpServletResponse containing response
     * @return void.
     */
    protected void processRequestPostCalc(HttpServletRequest request, HttpServletResponse response, Calculator calc)
            throws ServletException, IOException {
        Map<String, String> options = new LinkedHashMap<>();
        options.put("screen", calc.getScreenAction());
        options.put("result", String.valueOf(calc.getResult()));
        String json = new Gson().toJson(options);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
    }
}