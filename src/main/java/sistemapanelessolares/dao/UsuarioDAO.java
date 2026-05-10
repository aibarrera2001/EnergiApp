package sistemapanelessolares.dao;

import sistemapanelessolares.dominio.Usuario;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO que persiste usuarios en usuarios.txt
 * Formato de cada línea: id,nombre,apellido,correo,contrasena
 */
public class UsuarioDAO {

    private static final String ARCHIVO = "usuarios.txt";

    public void guardar(Usuario usuario) {
        List<Usuario> todos = listarTodos();
        // Asignar ID: el mayor existente + 1
        int nuevoId = todos.stream().mapToInt(Usuario::getIdUsuario).max().orElse(0) + 1;
        usuario.setIdUsuario(nuevoId);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO, true))) {
            bw.write(serializar(usuario));
            bw.newLine();
        } catch (IOException e) {
            System.err.println("Error al guardar usuario: " + e.getMessage());
        }
    }

    public Usuario buscarPorCorreo(String correo) {
        for (Usuario u : listarTodos()) {
            if (u.getCorreo().equalsIgnoreCase(correo)) return u;
        }
        return null;
    }

    public Usuario buscarPorId(int id) {
        for (Usuario u : listarTodos()) {
            if (u.getIdUsuario() == id) return u;
        }
        return null;
    }

    public List<Usuario> listarTodos() {
        List<Usuario> lista = new ArrayList<>();
        File archivo = new File(ARCHIVO);
        if (!archivo.exists()) return lista;

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (!linea.isBlank()) lista.add(deserializar(linea));
            }
        } catch (IOException e) {
            System.err.println("Error al leer usuarios: " + e.getMessage());
        }
        return lista;
    }

    public boolean actualizar(Usuario usuarioActualizado) {
        List<Usuario> todos = listarTodos();
        boolean encontrado = false;
        for (int i = 0; i < todos.size(); i++) {
            if (todos.get(i).getIdUsuario() == usuarioActualizado.getIdUsuario()) {
                todos.set(i, usuarioActualizado);
                encontrado = true;
                break;
            }
        }
        if (encontrado) reescribirArchivo(todos, ARCHIVO);
        return encontrado;
    }

    public boolean eliminar(int id) {
        List<Usuario> todos = listarTodos();
        boolean eliminado = todos.removeIf(u -> u.getIdUsuario() == id);
        if (eliminado) reescribirArchivo(todos, ARCHIVO);
        return eliminado;
    }

    // id,nombre,apellido,correo,contrasena
    private String serializar(Usuario u) {
        return u.getIdUsuario() + "," + u.getNombre() + "," + u.getApellido() + ","
             + u.getCorreo() + "," + u.getContrasena();
    }

    private Usuario deserializar(String linea) {
        String[] partes = linea.split(",");
        return new Usuario(
            Integer.parseInt(partes[0]),
            partes[1], partes[2], partes[3], partes[4]
        );
    }

    static void reescribirArchivo(List<?> lista, String ruta) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ruta, false))) {
            for (Object obj : lista) {
                bw.write(obj.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error al reescribir archivo: " + e.getMessage());
        }
    }
}