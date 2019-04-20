import java.util.Collection;

public interface IncidenteService {

    public void addIncidente(Incidente incidente);
    public Collection<Incidente> getIncidentes();
    public Incidente editIncidente(int id, String descripcion, Estado estado);
    public boolean existIncidente(int id);
}
