import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.util.List;

public class CsvValidatorProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {

        String body = exchange.getIn().getBody(String.class);

        String[] lines = body.split("\n");

        if (lines.length < 2) {
            throw new Exception("Archivo sin datos");
        }

        // VALIDAR HEADER
        String header = lines[0].trim();
        if (!header.equals("patient_id,full_name,appointment_date,insurance_code")) {
            throw new Exception("Encabezado incorrecto");
        }

        // VALIDAR FILAS
        for (int i = 1; i < lines.length; i++) {

            String line = lines[i].trim();

            if (line.isEmpty()) continue;

            String[] fields = line.split(",");

            if (fields.length != 4) {
                throw new Exception("Fila con columnas incorrectas en línea " + (i+1));
            }

            String patientId = fields[0].trim();
            String fullName = fields[1].trim();
            String date = fields[2].trim();
            String insurance = fields[3].trim();

            // campos vacíos
            if (patientId.isEmpty() || fullName.isEmpty() || date.isEmpty() || insurance.isEmpty()) {
                throw new Exception("Campos vacíos en línea " + (i+1));
            }

            // validar fecha formato YYYY-MM-DD
            if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
                throw new Exception("Fecha inválida en línea " + (i+1));
            }

            // validar insurance_code
            if (!(insurance.equals("IESS") || insurance.equals("PRIVADO") || insurance.equals("NINGUNO"))) {
                throw new Exception("Insurance inválido en línea " + (i+1));
            }
        }
    }
}