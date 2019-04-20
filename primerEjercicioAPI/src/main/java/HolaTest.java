import static spark.Spark.*;

public class HolaTest {

    public static void main(String[] args) {
        get("/hola", (req,res) -> "Hola Mundo");

        get("/hola/:nombre", (req,res) -> {
            return "Hola " + req.params(":nombre");
        });
    }
}
