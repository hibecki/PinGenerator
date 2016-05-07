package pingenerator.tvtelecom.com;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Servlet implementation class PatternGetById
 */
@WebServlet("/PatternGetById")
public class PatternGetById extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public PatternGetById() {
        super();
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Logger LOG = Logger.getLogger(PatternTable.class.getName());
        request.setCharacterEncoding(Utils.CharacterEncoding);    
        String patternId = request.getParameter("patternId");
        
		Connection con = null;
		Statement st1 = null;
		String sql1 ="select * from pattern where patternid = " + patternId;
		ResultSet rs1 = null;
		
		String result="failed";
		int PATTERNID = 0;
		String CHANNEL = null;
		String CHANNELCODE = null;
		int DIGIT = 0;
		int PINDIGIT = 0;
		try {
			Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup("java:comp/env/jdbc/PinGen");
			con = ds.getConnection();
			st1 = con.createStatement();
			rs1 = st1.executeQuery(sql1);
			if (rs1.next()) {
				PATTERNID = rs1.getInt("PATTERNID");
				CHANNEL = rs1.getString("CHANNEL");
				CHANNELCODE = rs1.getString("CHANNELCODE");
				DIGIT = rs1.getInt("DIGIT");
				PINDIGIT = rs1.getInt("PINDIGIT");
			}
			result = "succeed";
		} catch(NamingException | SQLException ex) {
			LOG.log(Level.SEVERE, ex.getMessage(), ex);
			result = "failed";
		} finally {
            try {
                if (rs1 != null) {rs1.close();}if (st1 != null) {st1.close();}
                if (con != null) {con.close();}
            } catch (SQLException ex) {
            	LOG.log(Level.WARNING, ex.getMessage(), ex);
            }
		}

		response.setContentType("application/json");
		response.setCharacterEncoding(Utils.CharacterEncoding);
		PrintWriter out = response.getWriter();
		out.print("{\"result\":\""+result+"\",\"PATTERNID\":"+PATTERNID+",\"CHANNEL\":\""+CHANNEL+"\",\"CHANNELCODE\":\""+CHANNELCODE+"\",\"DIGIT\":\""+DIGIT+"\",\"PINDIGIT\":\""+PINDIGIT+"\"}");
		out.flush();
	
//LOG.log(Level.INFO,"{0} {1}",new Object[]{"PatternTable: ",result});
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}