package com.davidmcasas.simplenotepad;

import android.content.Context;
import android.util.Log;

import org.neodatis.odb.ODB;
import org.neodatis.odb.ODBFactory;
import org.neodatis.odb.Objects;
import org.neodatis.odb.core.query.IQuery;
import org.neodatis.odb.core.query.criteria.Where;
import org.neodatis.odb.impl.core.query.criteria.CriteriaQuery;

import java.io.File;
import java.util.ArrayList;

/**
 *
 * Clase ayudante de Neodatis. Usa patrón tipo SINGLETON, para asegugar que solo exista
 * una instancia de esta clase durante el ciclo de vida de la app, y que dicha instancia
 * pueda ser accedida desde cualquier otra clase mediante el método getInstance();
 *
 * Esta clase facilita operaciones CRUD.
 *
 * Se debe inicializar esta clase desde el Activity principal usando "getInstance(this)";
 * para asegurar que reciba como Context dicho Activity.
 *
 * @author davidmcasas
 * @version 2021.28.01
 *
 */
public class NeodatisHelper {

    /**
     * Version de la base de datos,
     * cambiar este valor borrará la base de datos.
     */
    final long VERSION = 1L;

    /**
     * Instancia del ayudante.
     */
    private static NeodatisHelper instance;

    /**
     * Directorio donde se almacenará el archivo de la base de datos.
     */
    private String path;

    /**
     * Constructor con Context.
     *
     * Para asignar el path, se usa el método getDir() de Context, lo cual
     * devuelve el directorio que Android tiene reservado para los archivos de datos
     * de esta app. Si no se hiciera así se producirían errores de lectura y escritura, ya que la
     * app no tiene permisos para escribir fuera de dicho directorio.
     *
     * @param context Es aconsejable que sea el Activity principal.
     */
    public NeodatisHelper(Context context) {

        this.path = context.getDir("data", Context.MODE_PRIVATE).getAbsolutePath()+"/data.odb";
        initialize();
    }

    /**
     * Método para obtener la instancia de NeodatisHelper.
     * Si no existe aún, la crea con el Context aportado.
     *
     * @param context Es aconsejable que sea el Activity principal.
     * @return instancia de NeodatisHelper
     */
    public static synchronized NeodatisHelper getInstance(Context context) {

        if (instance == null) {
            instance = new NeodatisHelper(context);
        }
        return instance;
    }

    /**
     * Método para obtener la instancia de NeodatisHelper sin aportar Context.
     * Si no existe aún, devolverá null.
     *
     * @return instancia de NeodatisHelper, puede ser null
     */
    public static synchronized NeodatisHelper getInstance() {
        return instance;
    }

    /**
     * Clase anidada para controlar la version de la base de datos.
     * Funciona como un wrapper de long de solo lectura.
     */
    private static class DatabaseVersion {
        private long version;
        public DatabaseVersion(long version) { this.version = version; }
        public long getVersion() { return this.version; }
    }

    /**
     * Método encargado de inicializar la base de datos
     * Si la base de datos no existe, se crea y se añade un objeto DatabaseVersion.
     * Si ya existía, recoge su objeto DatabaseVersion y lo compara con la versión actual,
     * y si son versiones diferentes, borra la base de datos y la crea de nuevo.
     */
    private void initialize() {

        ODB odb = ODBFactory.open(path);
        Objects<DatabaseVersion> versionObjects = odb.getObjects(DatabaseVersion.class);

        if (versionObjects.size() == 0) {
            odb.store(new DatabaseVersion(VERSION));
        } else if (versionObjects.getFirst().getVersion() != VERSION) {
            odb.delete(versionObjects.getFirst());
            odb.close();
            File f = new File(path);
            if (f.exists()) {
                if (f.delete()) {
                    odb = ODBFactory.open(path);
                    odb.store(new DatabaseVersion(VERSION));
                }
            }
        }
        //versionObjects = odb.getObjects(DatabaseVersion.class);
        //Log.d("PRUEBAS - Version",""+versionObjects.getFirst().getVersion());
        odb.close();
    }

    /*
      De aquí hacia arriba, métodos que pueden ser comunes para cualquier app que use Neodatis
      De aquí hacia abajo, métodos específicos para esta app.
    */

    /**
     * Guarda un objeto Nota en la base de datos
     * @param nota Nota a guardar
     */
    public void guardarNota(Nota nota) {
        ODB odb = ODBFactory.open(path);
        odb.store(nota);
        odb.close();
    }

    /**
     * Guarda un objeto Categoria en la base de datos
     * @param categoria Categoria a guardar
     */
    public void guardarCategoria(Categoria categoria) {
        ODB odb = ODBFactory.open(path);
        odb.store(categoria);
        odb.close();
    }

    /**
     * Intenta eliminar un objeto Nota, si existe en la base de datos.
     * @param nota Nota a eliminar
     */
    public void borrarNota(Nota nota) {
        ODB odb = ODBFactory.open(path);
        try {
            odb.delete(nota);
        } catch (Exception ignored) {}

        odb.close();
    }

    /**
     * Intenta eliminar un objeto Categoria, si existe en la base de datos.
     * Primero pondrá a null la categoría de todos los objetos Nota que pertenezcan a ella.
     * @param categoria Categoria a eliminar
     */
    public void borrarCategoria(Categoria categoria) {

        ODB odb = ODBFactory.open(path);

        try {

            IQuery query = new CriteriaQuery(Nota.class, Where.equal("categoria", odb.getObjectId(categoria)));
            Objects<Nota> notas = odb.getObjects(query);

            for (Nota nota : notas) {
                nota.setCategoria(null);
                odb.store(nota);
            }
            odb.commit();
            odb.delete(categoria);

        } catch (Exception ignored) {}

        odb.close();
    }

    /**
     * Devuelve una lista de todos los objetos Categoria
     * @return ArrayList de objetos Categoria
     */
    public ArrayList<Categoria> getCategorias() {

        ODB odb = ODBFactory.open(path);
        Objects<Categoria> categoriasObjects = odb.getObjects(Categoria.class);
        ArrayList<Categoria> categorias = new ArrayList<>(categoriasObjects);
        odb.close();
        return categorias;
    }

    /**
     * Devuelve una lista de todos los objetos Nota
     * @return ArrayList de objetos Nota
     */
    public ArrayList<Nota> getNotas() {

        ODB odb = ODBFactory.open(path);
        Objects<Nota> notasObjects = odb.getObjects(Nota.class);
        ArrayList<Nota> notas = new ArrayList<>(notasObjects);
        odb.close();
        return notas;
    }

    /**
     * Devuelve una lista de todos los objetos Nota cuya Categoria sea la indicada
     * @param categoria Categoria a filtrar
     * @return ArrayList de objetos Nota
     */
    public ArrayList<Nota> getNotas(Categoria categoria) {

        if (categoria == null) {
            return getNotas();
        }

        ArrayList<Nota> notas = new ArrayList<>();
        ODB odb = ODBFactory.open(path);

        try {
            IQuery query = new CriteriaQuery(Nota.class, Where.equal("categoria", odb.getObjectId(categoria)));
            Objects<Nota> notasObjects = odb.getObjects(query);
            notas.addAll(notasObjects);
        } catch (Exception ignored) {}

        odb.close();
        return notas;
    }
}
