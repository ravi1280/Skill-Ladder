
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

@WebServlet(name = "CheckOutCart", urlPatterns = {"/CheckOutCart"})
public class CheckOutCart extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();

        JsonObject jsonObject = gson.fromJson(request.getReader(), JsonObject.class);
        String userId = jsonObject.get("userId").getAsString();

        System.out.println("CheckOut  cart item: UserID=" + userId);

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        JsonObject responseObject = new JsonObject();

        try {
            transaction = session.beginTransaction();

            Criteria criteria = session.createCriteria(Cart.class);
            criteria.add(Restrictions.eq("userId", userId));
            List<Cart> userLessons = criteria.list();

            if (!userLessons.isEmpty()) {
                for (Cart lesson : userLessons) {
                    session.delete(lesson);
                }
                transaction.commit();
                response.getWriter().write("successfully!");
            } else {
                response.getWriter().write("No purchased lessons found for this user!");
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
