import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;

public class MainApp {

    public static void main(String[] args) throws Exception {

        CamelContext context = new DefaultCamelContext();

        context.addRoutes(new FileTransferRoute());

        context.start();

        System.out.println("Camel corriendo...");

        Thread.sleep(300000);

        context.stop();
    }
}