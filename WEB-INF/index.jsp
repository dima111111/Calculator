<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Calculator</title>
        <script src="http://code.jquery.com/jquery-2.2.4.js" type="text/javascript"></script>
        <script type="text/javascript"><jsp:directive.include file="/js/helper.js" /></script>
        <style><jsp:directive.include file="/css/calculator.css" /></style>
    </head>
    <body>
        <h1>Calculator</h1>
        <table class="calculator_table">
            <tr align="center">
                <td colspan="4"><input class="screen" type="text" name="actionString"></td>
            </tr>
            <tr align="center">
                <td colspan="4"><input class="screen" type="text" name="result" readonly></td>
            </tr>
            <tr align="center">
                <td><input class="key" type="submit" value="7" name="num"></td>
                <td><input class="key" type="submit" value="8" name="num"></td>
                <td><input class="key" type="submit" value="9" name="num"></td>
                <td><input class="key" type="submit" value="/" name="action"></td>
            </tr>
            <tr align="center">
                <td><input class="key" type="submit" value="4" name="num"></td>
                <td><input class="key" type="submit" value="5" name="num"></td>
                <td><input class="key" type="submit" value="6" name="num"></td>
                <td><input class="key" type="submit" value="*" name="action"></td>
            </tr>
            <tr align="center">
                <td><input class="key" type="submit" value="1" name="num"></td>
                <td><input class="key" type="submit" value="2" name="num"></td>
                <td><input class="key" type="submit" value="3" name="num"></td>
                <td><input class="key" type="submit" value="-" name="action"></td>
            </tr>
            <tr align="center">
                <td><input class="key" type="submit" value="C" name="action"></td>
                <td><input class="key" type="submit" value="0" name="num"></td>
                <td><input class="key" type="submit" value="=" name="action"></td>
                <td><input class="key" type="submit" value="+" name="action"></td>
            </tr>
        </table>
    </body>
</html>