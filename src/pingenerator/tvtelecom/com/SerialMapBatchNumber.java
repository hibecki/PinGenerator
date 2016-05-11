package pingenerator.tvtelecom.com;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/SerialMapBatchNumber")
public class SerialMapBatchNumber extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public SerialMapBatchNumber() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Logger LOG = Logger.getLogger(SerialMapBatchNumber.class.getName());
        request.setCharacterEncoding(Utils.CharacterEncoding);

        String batchNumber = "";
		try {
			Path pathBatchNumber = Paths.get(Utils.PathFileMappingSerialBatchNumber);
			long maxBatch = 0;
			if (Files.exists(pathBatchNumber)) {
				byte[] byteMaxBatch = Files.readAllBytes(pathBatchNumber);
				String stringMaxBatch = new String(byteMaxBatch);
				maxBatch = Long.parseLong(stringMaxBatch);
				if (maxBatch >= 1000000) {
					maxBatch = 1;
					Files.write(pathBatchNumber, Long.toString(maxBatch+1).getBytes(), StandardOpenOption.CREATE);
				}

			} else {
				maxBatch = 1;
				Files.write(pathBatchNumber, Long.toString(maxBatch).getBytes(), StandardOpenOption.CREATE);
			}
			
			String maxBatchFormat = "9" + String.format("%0$" + "6d", 0).replace(' ', '0');
			
			maxBatch = Long.parseLong(maxBatchFormat) + maxBatch;
			
			//batchNumber = "BAT" + Long.toString(maxBatch).substring(1);
			batchNumber = Long.toString(maxBatch).substring(1);
LOG.log(Level.INFO,"SerialMapBatchNumber - batchNumber:{0}",new Object[]{batchNumber});
		} catch(Exception ex) {
LOG.log(Level.SEVERE, ex.getMessage(), ex);
		}
		PrintWriter out = response.getWriter();
		out.print(batchNumber);
		out.flush();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
