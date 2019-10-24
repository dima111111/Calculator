import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
     * Return calculator html.
     * @param request HttpServletRequest containing request params
     * @return String.
     */
    protected String getHtml (HttpServletRequest request) {
        StringBuilder str = new StringBuilder();
        str.append("    <head>\n");
        str.append("        <title>Calculator</title>\n");
        str.append("    </head>\n");
        str.append("    <body>\n");
        str.append("        <h1>Calculator</h1>\n");
        str.append("        <table class=\"calculator_table\">\n");
        str.append("            <form method=\"POST\">\n");
        str.append("            <tr align=\"center\">\n");
        str.append("                <td colspan=\"4\"><input class=\"screen\" type=\"text\" name='actionString' value=\""+ screenAction + "\"></td>\n");
        str.append("            </tr>\n");
        str.append("            <tr align=\"center\">\n");
        str.append("                <td colspan=\"4\"><input class=\"screen\" type=\"text\" name='result' value=\""+ result + "\" readonly></td>\n");
        str.append("            </tr>\n");
        str.append("            <tr align=\"center\">\n");
        str.append("                <td><input class=\"key\" type=\"submit\" value=\"7\" name=\"num\"></td>\n");
        str.append("                <td><input class=\"key\" type=\"submit\" value=\"8\" name=\"num\"></td>\n");
        str.append("                <td><input class=\"key\" type=\"submit\" value=\"9\" name=\"num\"></td>\n");
        str.append("                <td><input class=\"key\" type=\"submit\" value=\"/\" name=\"action\"></td>\n");
        str.append("            </tr>\n");
        str.append("            <tr align=\"center\">\n");
        str.append("                <td><input class=\"key\" type=\"submit\" value=\"4\" name=\"num\"></td>\n");
        str.append("                <td><input class=\"key\" type=\"submit\" value=\"5\" name=\"num\"></td>\n");
        str.append("                <td><input class=\"key\" type=\"submit\" value=\"6\" name=\"num\"></td>\n");
        str.append("                <td><input class=\"key\" type=\"submit\" value=\"*\" name=\"action\"></td>\n");
        str.append("            </tr>\n");
        str.append("            <tr align=\"center\">\n");
        str.append("                <td><input class=\"key\" type=\"submit\" value=\"1\" name=\"num\"></td>\n");
        str.append("                <td><input class=\"key\" type=\"submit\" value=\"2\" name=\"num\"></td>\n");
        str.append("                <td><input class=\"key\" type=\"submit\" value=\"3\" name=\"num\"></td>\n");
        str.append("                <td><input class=\"key\" type=\"submit\" value=\"-\" name=\"action\"></td>\n");
        str.append("            </tr>\n");
        str.append("            <tr align=\"center\">\n");
        str.append("                <td><input class=\"key\" type=\"submit\" value=\"C\" name=\"action\"></td>\n");
        str.append("                <td><input class=\"key\" type=\"submit\" value=\"0\" name=\"num\"></td>\n");
        str.append("                <td><input class=\"key\" type=\"submit\" value=\"=\" name=\"action\"></td>\n");
        str.append("                <td><input class=\"key\" type=\"submit\" value=\"+\" name=\"action\"></td>\n");
        str.append("            </tr>\n");
        str.append("            </form>\n");
        str.append("        </table>\n");
        str.append("    </body>");
        return str.toString();
    }

    /**
     * Process user request and print html.
     * @param request HttpServletRequest containing request params
     * @param response HttpServletResponse containing response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String html = getHtml(request);
        PrintWriter out = response.getWriter();
        try {
            out.println(html);
        } finally {
            out.close();
        }
    }

    /**
     * Clear calculator variables and return calculator page.
     * @param request HttpServletRequest containing request params
     * @param request HttpServletResponse containing response
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        clearVars();
        processRequest(request, response);
    }

    /**
     * Clear all variables.
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
     * @param response HttpServletResponse containing response
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        setCurrentParametrs(request);
        changeScreen(request);
        processRequest(request, response);
    }

    /**
     * Setting current action, numbers by request parameters.
     * @param request HttpServletRequest containing request params
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