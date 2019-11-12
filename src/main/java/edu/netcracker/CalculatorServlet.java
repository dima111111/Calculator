package edu.netcracker;

import java.io.*;
import java.util.Map;
import java.util.LinkedHashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.RequestDispatcher;
import com.google.gson.*;
import edu.netcracker.Calculator;
import java.util.HashMap;

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

        if (sessionId != null && sessionCalculators.containsKey(sessionId)) {
            Calculator calc = sessionCalculators.get(sessionId);
            calc.clearVars();
        }
        
        processRequestGet(request, response);
    }


    /**
     * Process user request (POST) and return calculator page.
     * @param request HttpServletRequest containing request params
     * @param request HttpServletResponse containing response
     * @return void.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String sessionId = request.getRequestedSessionId();
        if (sessionId != null) {
            Calculator calc;
            if (sessionCalculators.containsKey(sessionId)) {
                calc = sessionCalculators.get(sessionId);
            } else {
                calc = new Calculator();
                sessionCalculators.put(sessionId, calc);
            }
            calc.setCurrentParametrs(request);
            calc.changeScreen();
            processRequestPost(request, response, calc);
        }
    }

    /**
     * Process user request GET and print html.
     * @param request HttpServletRequest containing request params
     * @param request HttpServletResponse containing response
     * @return void.
     */
    protected void processRequestGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        RequestDispatcher dispatcher = request.getServletContext().getRequestDispatcher("/WEB-INF/index.jsp");
        dispatcher.forward(request, response);
    }

    /**
     * Process user request and print html.
     * @param request HttpServletRequest containing request params
     * @param request HttpServletResponse containing response
     * @return void.
     */
    protected void processRequestPost(HttpServletRequest request, HttpServletResponse response, Calculator calc)
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