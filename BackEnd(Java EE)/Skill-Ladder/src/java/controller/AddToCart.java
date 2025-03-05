
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import entity.Cart;
import entity.job_field;
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


@WebServlet(name = "AddToCart", urlPatterns = {"/AddToCart"})
public class AddToCart extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(request.getReader(), JsonObject.class);
        
        String userId = jsonObject.get("userId").getAsString();
        String lessonId = jsonObject.get("lessonId").getAsString();
        String lessonName = jsonObject.get("lessonName").getAsString();
        int lessonPrice = jsonObject.get("lessonPrice").getAsInt();
        
        System.out.println(userId);
        System.out.println(lessonId);
        System.out.println(lessonName);
        System.out.println(lessonPrice);
        
          try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction transaction = session.beginTransaction();
            
              Criteria criteria1 = session.createCriteria(Cart.class);
              criteria1.add(Restrictions.eq("userId", userId));
              criteria1.add(Restrictions.eq("lessonId", lessonId));
              criteria1.add(Restrictions.eq("lessonName", lessonName));
              criteria1.add(Restrictions.eq("lessonPrice", lessonPrice));
              
              if (criteria1.list().isEmpty()) {
                  
                  Cart cart = new Cart();
                  cart.setUserId(userId);
                  cart.setLessonId(lessonId);
                  cart.setLessonName(lessonName);
                  cart.setLessonPrice(lessonPrice);
                  
                  session.save(cart);
                  transaction.commit();
                  session.close();
                  response.setContentType("application/json");
                  response.getWriter().write("Cart added successfully!");

              } else {
                  response.setContentType("application/json");
                  response.getWriter().write("Alredy have in Cart !");
                  System.out.println("Alrey Add");

              }
              
        } catch (Exception e) {
            e.printStackTrace();
        }
        
       
    }
        
    }
    
    
 
