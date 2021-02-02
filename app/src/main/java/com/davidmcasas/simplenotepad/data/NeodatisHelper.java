package com.davidmcasas.simplenotepad.data;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.davidmcasas.simplenotepad.AppWidget;
import com.davidmcasas.simplenotepad.WidgetLink;

import org.neodatis.odb.ODB;
import org.neodatis.odb.ODBFactory;
import org.neodatis.odb.Objects;
import org.neodatis.odb.core.query.IQuery;
import org.neodatis.odb.core.query.criteria.Where;
import org.neodatis.odb.core.query.nq.SimpleNativeQuery;
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
    final long VERSION = 3L;

    /**
     * Instancia del ayudante.
     */
    private static NeodatisHelper instance;

    /**
     * Directorio donde se almacenará el archivo de la base de datos.
     */
    private String path;

    /**
     * Instancia del gestor de Neodatis.
     */
    private ODB odb;

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
        this.odb = ODBFactory.open(path);
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

        Objects<DatabaseVersion> versionObjects = odb.getObjects(DatabaseVersion.class);

        if (versionObjects.size() == 0) {
            odb.store(new DatabaseVersion(VERSION));
        } else if (versionObjects.getFirst().getVersion() != VERSION) {
            odb.delete(versionObjects.getFirst());
            File f = new File(path);
            if (f.exists()) {
                odb.close();
                if (f.delete()) {
                    odb = ODBFactory.open(path);
                    odb.store(new DatabaseVersion(VERSION));
                }
            }
        }
    }

    /**
     * Cierra la base de datos.
     */
    public void terminate() {
        try {
            this.odb.close();
        } catch (Exception e) {

        }
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
        odb.store(nota);
        odb.commit();
    }

    /**
     * Guarda un objeto Categoria en la base de datos
     * @param categoria Categoria a guardar
     */
    public void guardarCategoria(Categoria categoria) {
        odb.store(categoria);
        odb.commit();
    }

    /**
     * Intenta eliminar un objeto Nota, si existe en la base de datos.
     * @param nota Nota a eliminar
     */
    public void borrarNota(Nota nota) {
        try {
            IQuery query = new CriteriaQuery(WidgetLink.class, Where.equal("nota", odb.getObjectId(nota)));
            Objects<WidgetLink> links = odb.getObjects(query);
            for (WidgetLink link : links) {
                odb.delete(link);
                odb.commit();
            }
            odb.delete(nota);
            odb.commit();
        } catch (Exception ignored) {}
    }

    /**
     * Intenta eliminar un objeto Categoria, si existe en la base de datos.
     * Primero pondrá a null la categoría de todos los objetos Nota que pertenezcan a ella.
     * @param categoria Categoria a eliminar
     */
    public void borrarCategoria(Categoria categoria) {

        try {

            IQuery query = new CriteriaQuery(Nota.class, Where.equal("categoria", odb.getObjectId(categoria)));
            Objects<Nota> notas = odb.getObjects(query);

            for (Nota nota : notas) {
                nota.setCategoria(null);
                odb.store(nota);
                odb.commit();
            }
            odb.delete(categoria);
            odb.commit();

        } catch (Exception e) {e.printStackTrace();}
    }

    /**
     * Devuelve una lista de todos los objetos Categoria
     * @return ArrayList de objetos Categoria
     */
    public ArrayList<Categoria> getCategorias() {

        Objects<Categoria> categoriasObjects = odb.getObjects(Categoria.class);
        return new ArrayList<>(categoriasObjects);
    }

    /**
     * Devuelve una lista de todos los objetos Nota
     * @return ArrayList de objetos Nota
     */
    public ArrayList<Nota> getNotas() {

        IQuery query = new CriteriaQuery(Nota.class).orderByDesc("fecha");
        Objects<Nota> notasObjects = odb.getObjects(query);
        return new ArrayList<>(notasObjects);
    }

    /**
     * Devuelve una lista de todos los objetos Nota cuya Categoria sea la indicada
     * @param categoria Categoria a filtrar. Si es null, se recogeran todas las notas.
     * @param onlyNullCategory Si es true, se ingorará la categoría, y se recogerán solo las notas con categoría null
     * @return ArrayList de objetos Nota
     */
    public ArrayList<Nota> getNotas(Categoria categoria, boolean onlyNullCategory) {

        ArrayList<Nota> notas = new ArrayList<>();

        if (onlyNullCategory) {
            IQuery query = new CriteriaQuery(Nota.class, Where.isNull("categoria")).orderByDesc("fecha");
            Objects<Nota> notasObjects = odb.getObjects(query);
            notas.addAll(notasObjects);
            return notas;
        }

        if (categoria == null) {
            return getNotas();
        }

        try {
            IQuery query = new CriteriaQuery(Nota.class, Where.equal("categoria", odb.getObjectId(categoria))).orderByDesc("fecha");
            Objects<Nota> notasObjects = odb.getObjects(query);
            notas.addAll(notasObjects);
        } catch (Exception ignored) {}

        return notas;
    }

    //--

    public void guardarLink(WidgetLink link) {
        odb.store(link);
        odb.commit();
    }

    public Nota leerLinkId(int id) {
        IQuery query = new CriteriaQuery(WidgetLink.class, Where.equal("id", id));
        Objects<WidgetLink> links = odb.getObjects(query);
        for (WidgetLink link : links) {
            return link.nota;
        }
        return null;
    }

    public void borrarLink(int id) {
        IQuery query = new CriteriaQuery(WidgetLink.class, Where.equal("id", id));
        Objects<WidgetLink> links = odb.getObjects(query);
        for (WidgetLink link : links) {
            odb.delete(link);
            odb.commit();
        }
    }

}
