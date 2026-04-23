import org.apache.camel.builder.RouteBuilder;

public class FileTransferRoute extends RouteBuilder {


    @Override
    public void configure() {

        // manejo de errores → todo lo inválido va a /error
        onException(Exception.class)
                .handled(true)
                .log("Error detectado: ${exception.message}")
                .to("file:./data/error");

        from("file:./data/input?move=../processing/${file:name}")
                .routeId("file-processing-route")

                .log("Archivo recibido: ${file:name}")

                // VALIDACIÓN REAL
                .process(new CsvValidatorProcessor())

                // SI PASA VALIDACIÓN
                .log("Archivo válido")

                .to("file:./data/output")

                .toD("file:./data/archive?fileName=${file:name}-${date:now:yyyy-MM-dd_HHmmss}")

                .log("Archivo procesado correctamente");
    }

}