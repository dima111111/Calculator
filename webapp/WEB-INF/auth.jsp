<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Authorization</title>
        <script type="text/javascript"><jsp:directive.include file="/js/jquery-2.2.4.js" /></script>
        <script type="text/javascript"><jsp:directive.include file="/js/helper.js" /></script>
        <style><jsp:directive.include file="/css/style.css" /></style>
    </head>
    <body>
        <div class="container">
            <section id="content">
                <form action="" method="post">
                    <h1>Login Form</h1>
                    <%String error;%>
                    <% if ((error = response.getHeader("error")) != null) {%>
                        <p class="error">Error: <%=error%></p>
                    <% } %>
                    <div>
                        <input type="text" placeholder="Username" required="" name="username" />
                    </div>
                    <div>
                        <input type="password" placeholder="Password" required="" name="password" />
                    </div>
                    <div>
                        <input type="hidden" name="action" value="auth" />
                        <input type="submit" value="Log in" />
                        <a href="#">Lost your password?</a>
                        <a href="#">Register</a>
                    </div>
                </form><!-- form -->
            </section><!-- content -->
        </div><!-- container -->
    </body>
</html>