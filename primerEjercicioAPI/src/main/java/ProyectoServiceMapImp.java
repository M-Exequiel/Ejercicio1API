import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class ProyectoServiceMapImp implements ProyectoService{

    private HashMap<Integer,Proyecto> proyectoMap;

    public ProyectoServiceMapImp(){
        proyectoMap=new HashMap<Integer, Proyecto>();
    }

    @Override
    public void addProyecto(Proyecto proyecto) {
        proyectoMap.put(proyecto.getId(),proyecto);
    }

    @Override
    public Collection<Proyecto> getProyectos() {
        return proyectoMap.values();
    }

    @Override
    public Proyecto getProyecto(int id) {
        return proyectoMap.get(id);
    }

    @Override
    public Proyecto editProyecto(Proyecto proyecto) {
            Proyecto proyectoEditar = proyectoMap.get(proyecto.getId());
            //A partir de ahora modifico
            proyectoEditar.setTitulo(proyecto.getTitulo());
            proyectoEditar.setPropietario(proyecto.getPropietario());
            return proyectoEditar;
    }

    @Override
    public void deleteProyecto(int id) {
        proyectoMap.remove(id);

    }

    @Override
    public boolean existProyecto(int id) {
        return proyectoMap.containsKey(id);
    }
}
