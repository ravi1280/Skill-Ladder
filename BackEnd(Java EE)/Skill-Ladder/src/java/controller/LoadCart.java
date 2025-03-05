
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import entity.Cart;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "LoadCart", urlPatterns = {"/LoadCart"})
public class LoadCart extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        String userId = request.getParameter("id");
        System.out.println(userId);
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        Gson gson = new Gson();
       JsonObject responseObject = new JsonObject();
        
        try {
            
            Criteria criteria1 = session.createCriteria(Cart.class);
            criteria1.add(Restrictions.eq("userId", userId));
            
            if (!criteria1.list().isEmpty()) {
                List<Cart> cartLessonList = criteria1.list();
                System.out.println(cartLessonList);
                responseObject.add("cartItem", gson.toJsonTree(cartLessonList));
                
            } else {
                responseObject.addProperty("Error", "Empty Cart !");
                System.out.println("empty");
                
            }
            
        } catch (Exception e) {
            responseObject.addProperty("error", e.getMessage());
        }
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(responseObject));
        
        
       
    }
    
}
