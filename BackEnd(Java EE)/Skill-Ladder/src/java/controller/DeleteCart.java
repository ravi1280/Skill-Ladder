
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import entity.Cart;
import java.io.IOException;
import java.io.PrintWriter;
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

@WebServlet(name = "DeleteCart", urlPatterns = {"/DeleteCart"})
public class DeleteCart extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();

        // Parse JSON request
        JsonObject jsonObject = gson.fromJson(request.getReader(), JsonObject.class);
        String userId = jsonObject.get("userId").getAsString();
        String lessonId = jsonObject.get("lessonId").getAsString();

        System.out.println("Deleting cart item: UserID=" + userId + ", LessonID=" + lessonId);

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        JsonObject responseObject = new JsonObject();

        try {
            transaction = session.beginTransaction();

            // Fetch the cart item
            Criteria criteria = session.createCriteria(Cart.class);
            criteria.add(Restrictions.eq("userId", userId));
            criteria.add(Restrictions.eq("lessonId", lessonId));

            Cart cartItem = (Cart) criteria.uniqueResult();

            if (cartItem != null) {
                // Delete the cart item
                session.delete(cartItem);
                transaction.commit();

                response.getWriter().write("Cart item deleted successfully !");
            } else {
                response.getWriter().write("Cart item not found !");
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            response.getWriter().write("Failed to delete cart item: " + e.getMessage());
        } finally {
            session.close();
        }
    }

}
