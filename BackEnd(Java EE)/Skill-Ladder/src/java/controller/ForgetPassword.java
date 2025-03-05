
package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Mail;

@WebServlet(name = "ForgetPassword", urlPatterns = {"/ForgetPassword"})
public class ForgetPassword extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
       String code = request.getParameter("code");
       String email = request.getParameter("email");
        System.out.println(code);
        System.out.println(email);
        
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        Thread sendMailThread = new Thread() {

            @Override
            public void run() {
                String emailContent = "<div style=\"font-family: Arial, sans-serif; max-width: 500px; margin: auto; padding: 20px; border-radius: 10px; background: #f9f9f9; border: 1px solid #ddd;\">"
                        + "<h2 style=\"color: #333; text-align: center;\">Skill Ladder Password Reset</h2>"
                        + "<p style=\"color: #555; font-size: 16px; text-align: center;\">Use the verification code below to reset your password:</p>"
                        + "<div style=\"font-size: 24px; font-weight: bold; color: #fff; background: #007bff; padding: 10px; text-align: center; border-radius: 5px;\">"
                        + code + "</div>"
                        + "<p style=\"text-align: center; font-size: 14px; color: #999;\">If you did not request this, please ignore this email.</p>"
                        + "</div>";

                Mail.sendMail(email, "Skill Ladder Password Reset Code", emailContent);
            }
        };
        sendMailThread.start();
        response.setContentType("application/json");
        response.getWriter().write("success");

    }

}
