package storage.filemanager;

import com.interactivemesh.jfx.importer.ModelImporter;
import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import javafx.scene.shape.MeshView;

public class MeshImporter {
    private static final String currentDir = System.getProperty("user.dir") + "/src/assets/3Dmodels/";
    private static final ModelImporter importer = new ObjModelImporter();

    private static MeshView[] getNodeArray(String fileName) {
        importer.read(currentDir + fileName);
        return (MeshView[]) importer.getImport();
    }

    public static MeshView[] getTable(){
        return getNodeArray("Table.obj");
    }

    public static MeshView[] getPlayer(String color) {
        return getNodeArray("astronauts/" + color + "Astronaut.obj");
    }

    public static MeshView getRegion() {
        return getNodeArray("Region.obj")[0];
    }

    public static MeshView getHouse() {
        return getNodeArray("House.obj")[0];
    }

    public static MeshView getPirate() {
        return getNodeArray("Pirate.obj")[0];
    }

    public static MeshView getQuestionMark(){
        return getNodeArray("QuestionMark.obj")[0];
    }

    public static MeshView[] getPawn(String color){
        return getNodeArray("pawns/" + color + "Pawn.obj");
    }

    public static MeshView[] getHotel() {
        return getNodeArray("Hotel.obj");
    }

    public static MeshView[] getTest() {
        return getNodeArray("Test.obj");
    }

    public static MeshView getVirus() {
        return getNodeArray("Virus.obj")[0];
    }

    public static MeshView[] getHospital() {
        return getNodeArray("Hospital.obj");
    }

    public static MeshView[] getStartingFlag() {
        return getNodeArray("StartingFlag.obj");
    }
}
