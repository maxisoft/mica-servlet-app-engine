package myapp;



import com.google.appengine.repackaged.com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import javax.servlet.http.*;

public class ListDocuments extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        File[] listOfFiles = new File(System.getProperty("user.dir")).listFiles();

        if (listOfFiles == null){
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        for (File file : listOfFiles) {
            if (file.isFile()) {
                resp.getWriter().println(file.getName());
            }
        }
        resp.setContentType("text/plain");
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
