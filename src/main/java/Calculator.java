import java.io.*;
import java.util.Map;
import java.util.LinkedHashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.RequestDispatcher;
import com.google.gson.*;

/**
 * Calculator --- servlet to implement calculator.
 * @author    Dmitry Vinogradov
 */
public class Calculator extends HttpServlet {

    private static String currentAction = ""; // contains current user action (+, -, *, /)
    private static String screenAction = ""; // action screen
    private static double firstNumber = 0; // contains first number of calculations
    private static double secondNumber = 0; // contains second number of calculations
    private static double result = 0; // result screen
    private static boolean isResultExist = true; // check if action is done
    private static boolean isAction = false; // check user request (num or action)

    /**
     * Redirect servlet request to jsp page.
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
     * Process user ajax request and return json.
     * @param request HttpServletRequest containing request params
     * @param request HttpServletResponse containing response
     * @return void.
     */
    protected void processRequestPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Map<String, String> options = new LinkedHashMap<>();
        options.put("screen", screenAction);
        options.put("result", String.valueOf(result));
        String json = new Gson().toJson(options);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
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
        clearVars();
        processRequestGet(request, response);
    }

    /**
     * Clear all variables.
     * @return void.
     */
    private void clearVars(){
        screenAction = "";
        result = 0;
        currentAction = "";
        firstNumber = 0;
        secondNumber = 0;
        isResultExist = false;
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
        setCurrentParametrs(request);
        changeScreen(request);
        processRequestPost(request, response);
    }

    /**
     * Setting current action, numbers by request parameters.
     * @param request HttpServletRequest containing request params
     * @return void.
     */
    private void setCurrentParametrs(HttpServletRequest request){
        if (request.getParameter("action") != null) {
            currentAction = request.getParameter("action");
            isAction = true;
        }
        if (request.getParameter("num") != null) {
            if (currentAction.equals("")) {
                if (firstNumber == 0.0) {
                    firstNumber = Double.valueOf(request.getParameter("num"));
                } else {
                    firstNumber = Double.valueOf(String.valueOf((int)firstNumber) + request.getParameter("num"));
                }
            } else {
                if (secondNumber == 0.0) {
                    secondNumber = Double.valueOf(request.getParameter("num"));
                } else {
                    secondNumber = Double.valueOf(String.valueOf((int)secondNumber) + request.getParameter("num"));
                }
            }
            isAction = false;
        }
    }

    /**
     * Change action screen and result screen.
     * @param request HttpServletRequest containing request params
     * @return void.
     */
    private void changeScreen(HttpServletRequest request){
        if (isAction) {
            switch(currentAction)
            {
                case "C":
                    clearVars();
                    break;
                case "=":
                    screenAction = "";
                    break;
                default:
                    if (isResultExist) {
                        firstNumber = result;
                        screenAction = firstNumber + currentAction;
                        secondNumber = 0;
                        isResultExist = false;
                    } else {
                        screenAction += currentAction;
                    }
                    break;
            }
        } else {
            if (screenAction.equals("") || secondNumber == 0.0)
            {
                result = firstNumber;
            }
            switch(currentAction)
            {
                case "+":
                    result = firstNumber + secondNumber;
                    screenAction = firstNumber + "+" + secondNumber;
                    isResultExist = true;
                    break;
                case "-":
                    result = firstNumber - secondNumber;
                    screenAction = firstNumber + "-" + secondNumber;
                    isResultExist = true;
                    break;
                case "/":
                    result = firstNumber / secondNumber;
                    screenAction = firstNumber + "/" + secondNumber;
                    isResultExist = true;
                    break;
                case "*":
                    result = firstNumber * secondNumber;
                    screenAction = firstNumber + "*" + secondNumber;
                    isResultExist = true;
                    break;
            }
        }
    }
}