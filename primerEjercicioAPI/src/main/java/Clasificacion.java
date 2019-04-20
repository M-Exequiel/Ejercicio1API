public enum Clasificacion {

    CRITICA("Critico"),
    NORMAL ("Normal"),
    MENOR ("Menor");

    private String clasificacion;

    Clasificacion (String clasificacion){
        this.clasificacion=clasificacion;
    }

    public String getClasificacion() {
        return clasificacion;
    }

    public void setClasificacion(String clasificacion) {
        this.clasificacion = clasificacion;
    }
}
