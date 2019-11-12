package edu.netcracker;
import javax.servlet.http.HttpServletRequest;

/**
 * Calculator --- calculator class.
 * @author    Dmitry Vinogradov
 */
public class Calculator {

    private String currentAction = ""; // contains current user action (+, -, *, /)
    private String screenAction = ""; // action screen
    private double firstNumber = 0; // contains first number of calculations
    private double secondNumber = 0; // contains second number of calculations
    private double result = 0; // result screen
    private boolean isResultExist = true; // check if action is done
    private boolean isAction = false; // check user request (num or action)

    public String getScreenAction() {
        return screenAction;
    } 

    public double getResult() {
        return result;
    } 

    /**
     * Clear all variables.
     * @return void.
     */
    public void clearVars(){
        screenAction = "";
        result = 0;
        currentAction = "";
        firstNumber = 0;
        secondNumber = 0;
        isResultExist = false;
    }

    /**
     * Setting current action, numbers by request parameters.
     * @param request HttpServletRequest containing request params
     * @return void.
     */
    public void setCurrentParametrs(HttpServletRequest request){
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
    public void changeScreen(){
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