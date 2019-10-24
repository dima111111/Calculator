import java.io.*;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Calculator extends HttpServlet {

    private static String currentAction = "";
    private static String actionString = "";
    private static double firstNumber = 0;
    private static double secondNumber = 0;
    private static double result = 0;
    private static boolean isResultExist = true;
    private static boolean isAction = false;

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
        str.append("                <td colspan=\"4\"><input class=\"screen\" type=\"text\" name='actionString' value=\""+ actionString + "\"></td>\n");
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


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        clearVars();
        processRequest(request, response);
    }

    private void clearVars(){
        actionString = "";
        result = 0;
        currentAction = "";
        firstNumber = 0;
        secondNumber = 0;
        isResultExist = false;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        setCurrentParametrs(request);
        changeActionString(request);
        processRequest(request, response);
    }

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

    private void changeActionString(HttpServletRequest request){
        if (isAction) {
            switch(currentAction)
            {
                case "C":
                    clearVars();
                    break;
                case "=":
                    actionString = "";
                    break;
                default:
                    if (isResultExist) {
                        firstNumber = result;
                        actionString = firstNumber + currentAction;
                        secondNumber = 0;
                        isResultExist = false;
                    } else {
                        actionString += currentAction;
                    }
                    break;
            }
        } else {
            if (actionString.equals("") || secondNumber == 0.0)
            {
                result = firstNumber;
            }
            switch(currentAction)
            {
                case "+":
                    result = firstNumber + secondNumber;
                    actionString = firstNumber + "+" + secondNumber;
                    isResultExist = true;
                    break;
                case "-":
                    result = firstNumber - secondNumber;
                    actionString = firstNumber + "-" + secondNumber;
                    isResultExist = true;
                    break;
                case "/":
                    result = firstNumber / secondNumber;
                    actionString = firstNumber + "/" + secondNumber;
                    isResultExist = true;
                    break;
                case "*":
                    result = firstNumber * secondNumber;
                    actionString = firstNumber + "*" + secondNumber;
                    isResultExist = true;
                    break;
            }
        }
    }

}