public enum Estado {

    ASIGNADO ("Asignado"),
    RESUELTO ("Resuelto");

    private String estado;

    Estado (String estado){
        this.estado=estado;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
