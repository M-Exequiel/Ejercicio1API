import java.util.Collection;
import java.util.function.Predicate;

public class ServiceImpController {

    final UsuarioService usuarioService = new UsuarioServiceMapImp();
    final ProyectoService proyectoService = new ProyectoServiceMapImp();
    final IncidenteService incidenteService = new IncidenteServiceMapImp();

    public boolean verifyUsuarioDelete(int id){
        Boolean esPropietario= proyectoService.getProyectos().stream().anyMatch(p -> p.getPropietario().getId()==id);
        Boolean esResponsable = incidenteService.getIncidentes().stream().anyMatch(i -> i.getResponsable().getId()==id);
        Boolean esReportador = incidenteService.getIncidentes().stream().anyMatch(i -> i.getReportador().getId()==id);

        if (esPropietario | esResponsable | esReportador == true){
            return true;
        }else {
            return false;
        }
    }

    public boolean verifyIncidentesProyecto(int id){
        Boolean tieneIncidentes = incidenteService.getIncidentes().stream().anyMatch(i -> i.getProyecto().getId()==id);
        if (tieneIncidentes==true){
            return true;
        }else{
            return false;
        }
    }

    public boolean verifyUsuarioAlEditarProyecto(Proyecto proyecto){
        if (usuarioService.existUsuario(proyecto.getPropietario().getId())){
            Usuario usuarioAux = usuarioService.getUsuario(proyecto.getPropietario().getId());
            if (proyecto.getPropietario().getNombre().equals(usuarioAux.getNombre()) &
                    proyecto.getPropietario().getApellido().equals(usuarioAux.getApellido())){
                return true;
            }
        }
        return false;
    }
}
