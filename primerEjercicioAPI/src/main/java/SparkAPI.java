import com.google.gson.Gson;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static spark.Spark.*;

public class SparkAPI {

    static ServiceImpController controlador = new ServiceImpController();

    public static void main(String[] args) {

        iniciar();
        port(4567);

        //Usuario

        post("/usuario", (request,response) -> {
            response.type("application/json");
            Usuario usuario = new Gson().fromJson(request.body(),Usuario.class);
            if (!controlador.usuarioService.existUsuario(usuario.getId())){
                controlador.usuarioService.addUsuario(usuario);
                return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS));
            }
            return new Gson().toJson(new StandardResponse(StatusResponse.ERROR, "El usuario ya existe."));

        });

        get("/usuario", (request,response) -> {
            response.type("application/json");
            return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS, new Gson().toJsonTree(
                    controlador.usuarioService.getUsuarios())));
        });

        get("/usuario/:id",(request,response) -> {
            response.type("application/json");
            return new Gson().toJson(new StandardResponse(
                    StatusResponse.SUCCESS,
                    new Gson().toJson(controlador.usuarioService.getUsuario(Integer.parseInt(request.params(":id"))))));
        });

        put("/usuario", (request,response)->{
            response.type("application/json");
            Usuario usuario = new Gson().fromJson(request.body(),Usuario.class);
            Usuario usuarioEditado = controlador.usuarioService.editUsuario(usuario);
            if(usuarioEditado != null){
                return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS, new Gson().toJsonTree(usuarioEditado)));
            }else{
                return new Gson().toJson(new StandardResponse(StatusResponse.ERROR, "Error al editar el usuario"));
            }
        });

        delete("/usuario/:id", (request,response)->{
            response.type("application/json");
            if(controlador.verifyUsuarioDelete(Integer.parseInt(request.params(":id")))==true){
                return new Gson().toJson(
                        new StandardResponse(StatusResponse.ERROR,
                                "El usuario no se puede borrar porque es propietario " +
                                "de un proyecto, responsable o reportador."));
            }else{
                controlador.usuarioService.deleteUsuario(Integer.parseInt(request.params(":id")));
                return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS, "Usuario borrado"));
            }

        });


        //Proyecto

        post("/proyecto", (request,response) -> {
            response.type("application/json");
            Proyecto proyecto = new Gson().fromJson(request.body(),Proyecto.class);
            if (!controlador.proyectoService.existProyecto(proyecto.getId())){
                controlador.proyectoService.addProyecto(proyecto);
                return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS));
            }
            return new Gson().toJson(new StandardResponse(StatusResponse.ERROR,
                    "Ya existe ese proyecto con id=" + proyecto.getId()));

        });

        get("/proyecto", (request,response) -> {
            response.type("application/json");
            if (!request.queryParams().isEmpty()){
                int id= Integer.parseInt(request.queryParams("idPropietario"));
                List<Proyecto> listaProyectos = controlador.proyectoService.getProyectos().stream().filter(
                        p->p.getPropietario().getId()==id).collect(Collectors.toList());
                if (!listaProyectos.isEmpty()){
                    return new Gson().toJson(
                            new StandardResponse(StatusResponse.SUCCESS, new Gson().toJsonTree(listaProyectos)));
                }else{
                    return new Gson().toJson(new StandardResponse(StatusResponse.ERROR,
                            "No hay ningun proyecto para el usuario con id= " + id));
                }
            }else{
                return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS, new Gson().toJsonTree(
                        controlador.proyectoService.getProyectos())));
            }
        });

        get("/proyecto/:id",(request,response) -> {
            response.type("application/json");
            return new Gson().toJson(new StandardResponse(
                    StatusResponse.SUCCESS,
                    new Gson().toJson(controlador.proyectoService.
                            getProyecto(Integer.parseInt(request.params(":id"))))));
        });

        //verificar cuando quiero modificar el ID del proyecto
        put("/proyecto", (request,response)->{
            response.type("application/json");
            Proyecto proyecto = new Gson().fromJson(request.body(),Proyecto.class);
            if (controlador.proyectoService.existProyecto(proyecto.getId())){
                if (controlador.verifyUsuarioAlEditarProyecto(proyecto)==true){
                    Proyecto proyectoEditado = controlador.proyectoService.editProyecto(proyecto);
                    if(proyectoEditado != null){
                        return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS,
                                new Gson().toJsonTree(proyectoEditado)));
                    }else{
                        return new Gson().toJson(new StandardResponse(StatusResponse.ERROR,
                                "Error al editar el proyecto"));
                    }
                }else {
                    return new Gson().toJson(new StandardResponse(StatusResponse.ERROR,
                            "Error al editar el proyecto. El usuario no existe"));
                }
            }
            return new Gson().toJson(new StandardResponse(StatusResponse.ERROR,
                    "No existe el proyecto con id=" + proyecto.getId()));
        });

        delete("/proyecto/:id", (request,response)->{
            response.type("application/json");
            if(controlador.verifyIncidentesProyecto(Integer.parseInt(request.params(":id")))==true){
                return new Gson().toJson(
                        new StandardResponse(StatusResponse.ERROR,
                                "No se puede borrar porque el proyecto tiene incidentes asociados."));
            }else{
                controlador.proyectoService.deleteProyecto(Integer.parseInt(request.params(":id")));
                return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS, "Proyecto borrado"));
            }

        });

        //Incidente

        post("/incidente", (request,response) -> {
            response.type("application/json");
            Incidente incidente = new Gson().fromJson(request.body(),Incidente.class);
            if (!controlador.incidenteService.existIncidente(incidente.getId())){
                controlador.incidenteService.addIncidente(incidente);
                return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS));
            }

            return new Gson().toJson(new StandardResponse(StatusResponse.ERROR, "Ya existe el incidente con id=" + incidente.getId()));
        });

        //get incidente
        //get incidente?idResponsable=X
        //get incidente?idReportador=X
        //get incidente?idProyecto=X
        //get incidente?estado=ASIGNADO
        //get incidente?estado=RESUELTO

        get("/incidente", (request,response) -> {
            response.type("application/json");
            if (!request.queryParams().isEmpty()){
                if (request.queryParams().contains("idResponsable")){
                    int id1 = Integer.parseInt(request.queryParams("idResponsable"));
                    List<Incidente> listaIncidentes1 = controlador.incidenteService.getIncidentes().stream().filter(
                            i -> i.getResponsable().getId() == id1).collect(Collectors.toList());
                    if (!listaIncidentes1.isEmpty()){
                        return new Gson().toJson(
                                new StandardResponse(StatusResponse.SUCCESS, new Gson().toJsonTree(listaIncidentes1)));
                    }else{
                        return new Gson().toJson(new StandardResponse(StatusResponse.ERROR,
                                "No hay ningun incidente con usuario responsable con id= "+ id1));
                    }
                }
                if (request.queryParams().contains("idReportador")){
                    int id2 = Integer.parseInt(request.queryParams("idReportador"));
                    List<Incidente> listaIncidentes2 = controlador.incidenteService.getIncidentes().stream().filter(
                            i -> i.getReportador().getId() == id2).collect(Collectors.toList());
                    if (!listaIncidentes2.isEmpty()){
                        return new Gson().toJson(
                                new StandardResponse(StatusResponse.SUCCESS, new Gson().toJsonTree(listaIncidentes2)));
                    }else{
                        return new Gson().toJson(new StandardResponse(StatusResponse.ERROR,
                                "No hay ningun incidente con usuario reportador con id= "+ id2));
                    }

                }
                if (request.queryParams().contains("estado")){
                    Estado estadoIncidente = Estado.valueOf(request.queryParams("estado"));
                    if (estadoIncidente==Estado.ASIGNADO) {
                        List<Incidente> listaIncidentesAbiertos = controlador.incidenteService.getIncidentes().stream().filter(
                                i -> i.getEstado()==estadoIncidente).collect(Collectors.toList());
                        if (!listaIncidentesAbiertos.isEmpty()){
                            return new Gson().toJson(
                                    new StandardResponse(StatusResponse.SUCCESS, new Gson().toJsonTree(listaIncidentesAbiertos)));
                        }else{
                            return new Gson().toJson(new StandardResponse(StatusResponse.ERROR,
                                    "No hay ningun incidente con estado ASIGNADO"));
                        }
                    }
                    if (estadoIncidente==Estado.RESUELTO) {
                        List<Incidente> listaIncidentesAbiertos = controlador.incidenteService.getIncidentes().stream().filter(
                                i -> i.getEstado()==estadoIncidente).collect(Collectors.toList());
                        if (!listaIncidentesAbiertos.isEmpty()){
                            return new Gson().toJson(
                                    new StandardResponse(StatusResponse.SUCCESS, new Gson().toJsonTree(listaIncidentesAbiertos)));
                        }else{
                            return new Gson().toJson(new StandardResponse(StatusResponse.ERROR,
                                    "No hay ningun incidente con estado RESUELTO"));
                        }
                    }
                }
                if (request.queryParams().contains("idProyecto")){
                    int idProyecto = Integer.parseInt(request.queryParams("idProyecto"));
                    List<Incidente> listaProyectosParaIncidente = controlador.incidenteService.getIncidentes().stream().filter(
                            i -> i.getProyecto().getId() == idProyecto).collect(Collectors.toList());
                    if (!listaProyectosParaIncidente.isEmpty()){
                        return new Gson().toJson(
                                new StandardResponse(StatusResponse.SUCCESS, new Gson().toJsonTree(listaProyectosParaIncidente)));
                    }else{
                        return new Gson().toJson(new StandardResponse(StatusResponse.ERROR,
                                "No hay ningun incidente para el proyecto con id= " + idProyecto));
                    }
                }
        }
            return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS,
                    new Gson().toJsonTree(controlador.incidenteService.getIncidentes())));
        });

        put("/incidente",(request, response) -> {
            response.type("application/json");
            Incidente incidente = new Gson().fromJson(request.body(),Incidente.class);
            Incidente incidenteEditado = controlador.incidenteService.editIncidente(incidente.getId(),incidente.getDescripcion(),incidente.getEstado());
            if (incidenteEditado!=null){
                System.out.println("El incidente se edito");
                return new Gson().toJson(
                        new StandardResponse(StatusResponse.SUCCESS, new Gson().toJsonTree(incidenteEditado)));
            }else{
                System.out.println("El incidente no se pudo editar");
                return new Gson().toJson(
                        new StandardResponse(StatusResponse.ERROR,
                                "Error al editar el incidente. Solo se puede editar " +
                                        "la descripcion o el estado"));
            }
        });
    }

    private static void iniciar(){
        Usuario usuario1 = new Usuario();
        usuario1.setId(1);
        usuario1.setNombre("Rodrigo");
        usuario1.setApellido("Vicente");
        controlador.usuarioService.addUsuario(usuario1);

        Usuario usuario2 = new Usuario();
        usuario2.setId(2);
        usuario2.setNombre("Marcos");
        usuario2.setApellido("Lopez");
        controlador.usuarioService.addUsuario(usuario2);

        Usuario usuario3 = new Usuario();
        usuario3.setId(3);
        usuario3.setNombre("Fabian");
        usuario3.setApellido("Lopez");
        controlador.usuarioService.addUsuario(usuario3);

        Proyecto proyecto1 = new Proyecto();
        proyecto1.setId(1);
        proyecto1.setTitulo("MercadoPago");
        proyecto1.setPropietario(usuario1);
        controlador.proyectoService.addProyecto(proyecto1);

        Proyecto proyecto2 = new Proyecto();
        proyecto2.setId(2);
        proyecto2.setTitulo("MercadoShops");
        proyecto2.setPropietario(usuario2);
        controlador.proyectoService.addProyecto(proyecto2);

        Incidente incidente1 = new Incidente();
        incidente1.setId(1);
        incidente1.setEstado(Estado.ASIGNADO);
        incidente1.setClasificacion(Clasificacion.NORMAL);
        incidente1.setDescripcion("No se generan ventas");
        incidente1.setFechaCreacion(new Date(2019,6,9));
        incidente1.setFechaSolucion(new Date(2019, 8, 12));
        incidente1.setProyecto(proyecto1);
        incidente1.setReportador(usuario3);
        incidente1.setResponsable(usuario2);
        controlador.incidenteService.addIncidente(incidente1);

        Incidente incidente2 = new Incidente();
        incidente2.setId(2);
        incidente2.setEstado(Estado.RESUELTO);
        incidente2.setClasificacion(Clasificacion.MENOR);
        incidente2.setDescripcion("No anda la pagina");
        incidente2.setFechaCreacion(new Date(2019, 2, 8));
        incidente2.setFechaSolucion(new Date(2019, 7, 12));
        incidente2.setProyecto(proyecto2);
        incidente2.setReportador(usuario3);
        incidente2.setResponsable(usuario1);
        controlador.incidenteService.addIncidente(incidente2);
    }

}
