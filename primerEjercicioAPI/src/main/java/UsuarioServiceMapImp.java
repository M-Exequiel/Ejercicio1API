import java.util.Collection;
import java.util.HashMap;

public class UsuarioServiceMapImp implements UsuarioService {

    private HashMap<Integer,Usuario> usuarioMap;

    public UsuarioServiceMapImp(){
        usuarioMap=new HashMap<Integer, Usuario>();
    }

    @Override
    public void addUsuario(Usuario usuario) {
        usuarioMap.put(usuario.getId(),usuario);

    }

    @Override
    public Collection<Usuario> getUsuarios() {
        return usuarioMap.values();
    }

    @Override
    public Usuario getUsuario(int id) {
        return  usuarioMap.get(id);
    }

    @Override
    public Usuario editUsuario(Usuario usuario) {
        Usuario usuarioEditar = usuarioMap.get(usuario.getId());
        //A partir de ahora modifico
        usuarioEditar.setNombre(usuario.getNombre());
        usuarioEditar.setApellido(usuario.getApellido());
        return usuarioEditar;
    }

    @Override
    public void deleteUsuario(int id) {
        usuarioMap.remove(id);
    }

    @Override
    public boolean existUsuario(int id) {
        return usuarioMap.containsKey(id);
    }
}
