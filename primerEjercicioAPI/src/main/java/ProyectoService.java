import java.util.Collection;

public interface ProyectoService {

    public void addProyecto(Proyecto proyecto);
    public Collection<Proyecto> getProyectos();
    public Proyecto getProyecto(int id);
    public Proyecto editProyecto(Proyecto proyecto);
    public void deleteProyecto(int id);
    public boolean existProyecto(int id);
}
