import java.util.Collection;
import java.util.HashMap;

public class IncidenteServiceMapImp implements IncidenteService{

    private HashMap<Integer,Incidente> incidenteMap;

    public IncidenteServiceMapImp(){
        incidenteMap=new HashMap<Integer, Incidente>();
    }

    @Override
    public void addIncidente(Incidente incidente) {
        incidenteMap.put(incidente.getId(),incidente);
    }

    @Override
    public Collection<Incidente> getIncidentes() {
        return incidenteMap.values();
    }

    @Override
    public Incidente editIncidente(int id, String descripcion, Estado estado) {
            Incidente incidenteEditar = incidenteMap.get(id);
        System.out.println(descripcion);
        System.out.println(incidenteEditar.getDescripcion());
        //A partir de ahora modifico
        incidenteEditar.setDescripcion(descripcion);
        if (estado==Estado.RESUELTO)
        {
            incidenteEditar.setEstado(estado);
        }
        return incidenteEditar;
    }

    @Override
    public boolean existIncidente(int id) {
        return incidenteMap.containsKey(id);
    }
}
