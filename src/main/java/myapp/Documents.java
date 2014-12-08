package myapp;

import com.google.appengine.repackaged.com.google.common.io.ByteStreams;
import com.google.common.io.Files;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;


public class Documents extends HttpServlet {

    public static final Pattern PATH_PATTERN = Pattern.compile("\\w[\\w\\.]*");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.isEmpty() || pathInfo.equals("/")){
            listFiles(resp);
            return;
        }
        String pathname = pathInfo.substring(1);
        if (!checkFilePath(pathname, resp)){
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        File file = new File(pathname);
        Files.copy(file, resp.getOutputStream());
        resp.setContentType("application/xml");
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        String pathname = pathInfo.substring(1);
        if (!checkFilePath(pathname, resp)){
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        File file = new File(pathname);
        Files.write(ByteStreams.toByteArray(req.getInputStream()), file);
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPut(req, resp);
    }

    private boolean checkFilePath(String pathname, HttpServletResponse resp) {
        return PATH_PATTERN.matcher(pathname).matches();
    }

    private boolean listFiles(HttpServletResponse resp) throws IOException {
        File[] listOfFiles = new File(System.getProperty("user.dir")).listFiles();

        if (listOfFiles == null){
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return false;
        }

        for (File file : listOfFiles) {
            if (file.isFile()) {
                resp.getWriter().println(file.getName());
            }
        }
        resp.setContentType("text/plain");
        resp.setStatus(HttpServletResponse.SC_OK);
        return true;
    }
}
