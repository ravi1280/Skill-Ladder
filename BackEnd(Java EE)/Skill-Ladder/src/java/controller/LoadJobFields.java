
package controller;

import com.google.gson.Gson;
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
import org.hibernate.Transaction;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "LoadJobFields", urlPatterns = {"/LoadJobFields"})
public class LoadJobFields extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        PrintWriter out = response.getWriter();
        Session session = null;
        Transaction transaction = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            Criteria criteria = session.createCriteria(job_field.class);
            criteria.add(Restrictions.eq("status_id.id", 1));

            List<job_field> jobFields = criteria.list();
            transaction.commit();

            Gson gson = new Gson();
            String json = gson.toJson(jobFields);

            session.close();
            response.setContentType("application/json");
            response.getWriter().write(json);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
  


