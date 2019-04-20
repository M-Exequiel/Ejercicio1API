import com.google.gson.JsonElement;

public class StandardResponse {

    private StatusResponse status;
    private String mensaje;
    private JsonElement data;

    public StandardResponse(){

    }

    public StandardResponse(StatusResponse status){
        this.status=status;
    }

    public StandardResponse(StatusResponse status, String mensaje){
        this.status=status;
        this.mensaje=mensaje;
    }

    public StandardResponse(StatusResponse status, JsonElement data){
        this.status=status;
        this.data=data;
    }

    public StatusResponse getStatus() {
        return status;
    }

    public void setStatus(StatusResponse status) {
        this.status = status;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public JsonElement getData() {
        return data;
    }

    public void setData(JsonElement data) {
        this.data = data;
    }
}
